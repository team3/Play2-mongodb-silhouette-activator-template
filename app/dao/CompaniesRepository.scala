package dao

import javax.inject.Inject

import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{DB, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CompaniesRepository {
  def find(term: String): Future[List[JsObject]]

  def save(document: BSONDocument): Future[WriteResult]
}

class MongoDbCompaniesRepository @Inject()(db: DB) extends CompaniesRepository {

  import play.modules.reactivemongo.json._

  protected val collection = db.collection[JSONCollection]("companies")

  override def find(term: String): Future[List[JsObject]] = {
    val query = if (term == "") Json.obj() else Json.obj("$text" -> Json.obj("$search" -> term))
    collection.find(query)
      .cursor[JsObject](ReadPreference.Primary)
      .collect[List]()
  }

  override def save(document: BSONDocument): Future[WriteResult] = {
    collection.update(BSONDocument("_id" -> document.get("_id").getOrElse(BSONObjectID.generate)), document, upsert = true)
  }
}
