import java.util.UUID

import com.google.inject.AbstractModule
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.test._
import controllers.CompaniesController
import dao.CompaniesRepository
import model.Company.Fields._
import model.User
import modules.JWTEnv
import net.codingwell.scalaguice.ScalaModule
import org.specs2.execute.Results
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BusinessControllerSpec extends PlaySpecification with Mockito with Results {
  "The `save` method" should {
    "return status 401 if no authenticator was found" in new Context {
      new WithApplication(application) {
        val businessObj = Json.obj(Name -> "test", Address -> "abc", Email -> "a@a.com", Phone -> 123)
        val request = FakeRequest().withBody(businessObj)
        val controller = app.injector.instanceOf[CompaniesController]
        val result = controller.save("57f0f0a79e6e4aa2c730de25")(request).run()

        status(result) must equalTo(UNAUTHORIZED)
      }
    }

    "return status 200 if authenticator was found" in new Context {
      new WithApplication(application) {
        val businessObj = Json.obj(Name -> "test", Address -> "abc", Email -> "a@a.com", Phone -> 123)
        val request = FakeRequest().withAuthenticator[JWTEnv](identity.loginInfo).withJsonBody(businessObj)
        val controller = app.injector.instanceOf[CompaniesController]
        val result = controller.save("57f0f0a79e6e4aa2c730de25").apply(request)

        status(result) must equalTo(OK)
      }
    }

    "return status 400 if request is invalid" in new Context {
      new WithApplication(application) {
        val request = FakeRequest().withAuthenticator[JWTEnv](identity.loginInfo).withJsonBody(Json.obj())
        val controller = app.injector.instanceOf[CompaniesController]
        val result:Future[Result] = controller.save("57f0f0a79e6e4aa2c730de25").apply(request)

        status(result) must equalTo(BAD_REQUEST)
      }
    }
  }

  trait Context extends Scope {

    class FakeModule extends AbstractModule with ScalaModule {

      def configure() = {
        bind[Environment[JWTEnv]].toInstance(env)
        bind[CompaniesRepository].toInstance(mock[CompaniesRepository])
      }
    }

    val identity = User(userID = UUID.randomUUID(), name = "name", loginInfo = LoginInfo("test", "test@users.com"))

    implicit val env: Environment[JWTEnv] = new FakeEnvironment[JWTEnv](Seq(identity.loginInfo -> identity))

    lazy val application = new GuiceApplicationBuilder()
      .overrides(new FakeModule)
      .build()
  }

}
