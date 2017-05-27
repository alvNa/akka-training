package com.datio.akkatraining.consumer

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class ResponseRest(data: String)

trait JsonSupportResponseRest extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val jsonSupportResponseRest = jsonFormat1(ResponseRest.apply)
}
object JsonSupportResponseRestObject extends JsonSupportResponseRest