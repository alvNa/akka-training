package com.datio.akkatraining.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives.{get, parameters, path, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller._
import akka.pattern.ask
import akka.util.Timeout
import com.datio.akkatraining.json.BeerRequest
import com.datio.akkatraining.config.Configuration
import com.datio.akkatraining.routes.ResponseManager._

trait GetRoute extends Configuration {

  implicit val zoidBergBeer: ActorRef

  implicit val timeout: Timeout

  def getRoute(client: String): Route =
    get {
      path("bar" / "beer" / IntNumber) { beers =>
        parameters('trademark ? "Estrella Galicia") { trade =>
          processFuture(zoidBergBeer ? BeerRequest(client, beers, trade))
        }
      }
    }
}
