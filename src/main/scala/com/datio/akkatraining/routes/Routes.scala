package com.datio.akkatraining.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.datio.akkatraining.Json.{HttpResponse => response}
import com.datio.akkatraining.config.Configuration

import scala.concurrent.duration._

trait Routes extends Configuration with GetRoute with PostRoute{

  implicit val timeout: Timeout = Timeout(getKey[Int](timeoutAkka) seconds)

  val routes: Route =
    headerValueByName("client") { client =>
      getRoute(client) ~ postRoute(client)
    }

}
