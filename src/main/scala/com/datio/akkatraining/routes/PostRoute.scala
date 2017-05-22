package com.datio.akkatraining.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives.{parameters, path, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller._
import akka.pattern.ask
import akka.util.Timeout
import com.datio.akkatraining.json.{PickRequest, HttpResponse => response}
import com.datio.akkatraining.routes.ResponseManager._

trait PostRoute {
  implicit val leela: ActorRef
  implicit val timeout: Timeout

  def postRoute(client: String): Route =
    post {
      path("bar" / "beer" / "leela")
      parameters('proposal.?) { proposal =>
        processFuture(leela ? PickRequest(client, proposal))
      }
    }
}
