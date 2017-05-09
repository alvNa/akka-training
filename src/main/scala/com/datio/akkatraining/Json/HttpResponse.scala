package com.datio.akkatraining.Json

import spray.json.DefaultJsonProtocol


case class HttpResponse(status: Int, data: Option[String] = None)
object HttpResponse extends DefaultJsonProtocol {
  implicit val httpResponse = jsonFormat2(HttpResponse.apply)
}