package modules

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.crypto.{AuthenticatorEncoder, Base64AuthenticatorEncoder}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services._
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.impl.authenticators._
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.util._
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import dao._
import model.User
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api._
import service.{UserService, UserServiceImpl}

trait JWTEnv extends Env {
  type I = User
  type A = JWTAuthenticator
}

class SilhouetteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CompaniesRepository].to[MongoDbCompaniesRepository]
    bind[Silhouette[JWTEnv]].to[SilhouetteProvider[JWTEnv]]
    bind[UserRepository].to[MongoDbUserRepository]
    bind[UserService].to[UserServiceImpl]
    bind[DB].toInstance {
      import com.typesafe.config.ConfigFactory

      import scala.collection.JavaConversions._
      import scala.concurrent.ExecutionContext.Implicits.global

      val config = ConfigFactory.load
      val driver = new MongoDriver
      val connection = driver.connection(
        config.getStringList("mongodb.servers"),
        MongoConnectionOptions(),
        Seq()
      )
      connection.db(config.getString("mongodb.db"))
    }
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordInfoRepository]
    bind[CacheLayer].to[PlayCacheLayer]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[AuthenticatorEncoder].toInstance(new Base64AuthenticatorEncoder)
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
  }

  @Provides def provideEnvironment(
                                    userService: UserService,
                                    authenticatorService: AuthenticatorService[JWTAuthenticator],
                                    eventBus: EventBus): Environment[JWTEnv] = {
    Environment[JWTEnv](userService, authenticatorService, Seq(), eventBus)
  }

  @Provides def provideAuthenticatorService(
                                             authenticatorEncoder: AuthenticatorEncoder,
                                             idGenerator: IDGenerator,
                                             configuration: Configuration,
                                             clock: Clock): AuthenticatorService[JWTAuthenticator] = {
    new JWTAuthenticatorService(new JWTAuthenticatorSettings(sharedSecret = "secret"), None, authenticatorEncoder, idGenerator, clock)
  }

  @Provides
  def provideCredentialsProvider(
                                  authInfoRepository: AuthInfoRepository,
                                  passwordHasher: PasswordHasher): CredentialsProvider = {

    new CredentialsProvider(authInfoRepository, new PasswordHasherRegistry(passwordHasher))
  }

  @Provides def provideAuthInfoRepository(
                                           passwordInfoRepository: DelegableAuthInfoDAO[PasswordInfo]): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoRepository)
  }
}
