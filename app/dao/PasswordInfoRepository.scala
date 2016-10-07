package dao

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.api.libs.json._
import play.modules.reactivemongo.json._
import reactivemongo.api._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class PersistentPasswordInfo(loginInfo: LoginInfo, authInfo: PasswordInfo)

class PasswordInfoRepository @Inject()(db: DB) extends DelegableAuthInfoDAO[PasswordInfo] {

  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val persistentPasswordInfoFormat = Json.format[PersistentPasswordInfo]

  def collection: JSONCollection = db.collection[JSONCollection]("password")

  def find(loginInfo: LoginInfo) = {

    val passwordInfo: Future[Option[PersistentPasswordInfo]] = collection
      .find(Json.obj("loginInfo" -> loginInfo))
      .one[PersistentPasswordInfo]

    passwordInfo.flatMap {
      case None =>
        Future.successful(Option.empty[PasswordInfo])
      case Some(persistentPasswordInfo) =>
        Future(Some(persistentPasswordInfo.authInfo))
    }
  }

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    collection.insert(PersistentPasswordInfo(loginInfo, authInfo))
    Future.successful(authInfo)
  }

  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    Future.successful(authInfo)
  }

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    Future.successful(())
  }
}