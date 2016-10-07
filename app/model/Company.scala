package model

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Company(name: String, address: String, email: String, phone: Int)

object Company {

  import Fields._

  def companyWrites(business: Company): JsValue = {
    JsObject(Seq(
      Name -> JsString(business.name),
      Address -> JsString(business.address),
      Email -> JsString(business.email),
      Phone -> JsNumber(business.phone)
    ))
  }

  implicit val companyReads: Reads[Company] = (
    (__ \ "name").read[String] ~
      (__ \ "address").read[String] ~
      (__ \ "email").read[String] ~
      (__ \ "phone").read[Int]
    ) (Company.apply _)


  object Fields {
    val Id = "_id"
    val Name = "name"
    val Address = "address"
    val Email = "email"
    val Phone = "phone"
  }

}