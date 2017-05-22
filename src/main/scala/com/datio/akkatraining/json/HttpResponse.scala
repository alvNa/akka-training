package com.datio.akkatraining.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol


case class HttpResponse(status: Int, data: Option[String] = None)
object HttpResponse extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val httpResponse = jsonFormat2(HttpResponse.apply)
}