package com.datio.akkatraining.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller._
import akka.pattern.ask
import akka.util.Timeout
import com.datio.akkatraining.Json.{BeerRequest, HttpResponse => response}
import com.datio.akkatraining.config.Configuration

import scala.concurrent.duration._
import scala.reflect._
import scala.util.{Failure, Success}

trait Routes extends Configuration {

  implicit val zoidBerg: ActorRef
  implicit val timeout: Timeout = Timeout(getKey[Int](timeoutAkka) seconds)
  import com.datio.akkatraining.routes.ResponseManager._

  val routes: Route =
    get {
    headerValueByName("client") { client =>
      path("bar" / "beer" / LongNumber) { beers =>
        parameters('trademark.?) { trade => {
          val future =
            zoidBerg ? BeerRequest(client, beers, trade)

              onComplete(future) {
                case Success(value) =>
                  value match {
                    case result if
                    classTag[Either[response, response]]
                      .runtimeClass
                      .isInstance(result) =>
                        processResponse(result.asInstanceOf[Either[response, response]])

                    case _ => complete(HttpResponse(BadRequest))
                  }
                case Failure(ex) =>
                  complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
              }
        }

        }
      }
    }
  }
}
