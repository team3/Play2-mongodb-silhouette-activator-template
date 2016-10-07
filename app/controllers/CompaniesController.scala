package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import dao.MongoDbCompaniesRepository
import model.Company
import modules.JWTEnv
import play.api.i18n.MessagesApi
import play.api.libs.json.{JsError, Json, _}
import play.api.mvc._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CompaniesController @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                                    val messagesApi: MessagesApi,
                                    val companiesRepository: MongoDbCompaniesRepository,
                                    silhouette: Silhouette[JWTEnv]) extends Controller {

  private[CompaniesController] def display(objects: Future[List[JsObject]]) =
    objects
      .map(obj => Ok(obj.map(b => b.values).mkString(",")))
      .recover { case e: Exception => BadRequest(e.getMessage) }

  def search(term: String) = Action.async { implicit request =>
    display(companiesRepository.find(term))
  }

  def all = Action.async { implicit request =>
    display(companiesRepository.find(""))
  }

  def save(id: String) = silhouette.SecuredAction.async { implicit request =>
    import model.Company.Fields._

    request.body.asJson.map { r =>
      r.validate[Company] match {
        case JsSuccess(business, _) =>
          val document = BSONDocument(
            Id -> id,
            Name -> business.name,
            Address -> business.address,
            Email -> business.email,
            Phone -> business.phone
          )

          companiesRepository.save(document)
            .map {
              result => Ok(Json.obj("success" -> result.ok))
            }
            .recover {
              case e: Exception => Ok(Json.obj("error while saving new entry" -> e.getMessage))
            }
        case err@JsError(_) => Future.successful(BadRequest(JsError.toJson(err)))
      }
    }.getOrElse(Future.successful(BadRequest("Invalid request")))
  }
}


