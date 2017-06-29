package com.datio.akkatraining.service

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{get, parameters, path}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.datio.akkatraining.actor._
import com.datio.akkatraining.config.{Configuration, Logging}
import com.datio.akkatraining.json.BeerRequest
import com.datio.akkatraining.routes.ResponseManager.processFuture
import com.datio.akkatraining.routes.Routes
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Directives._

object ServicePoolAkkaHttp extends scala.App
  with Configuration
  with Logging
  with PoolRoutes{

  implicit val system = ActorSystem("Futurama-Service")
  implicit val materializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  implicit val routerActor: ActorRef = system.actorOf(RouterActor.props(),
    "Router-Actor")


  val host = getKey[String](serviceAddress)
  val port = getKey[Int](servicePort)
  val bindingFuture = Http().bindAndHandle(routes, host, port)

  log.info(s"Opening Service ${system.name} in $host:$port")

  bindingFuture.onFailure {
    case _: Exception =>
      log.error("Failed to bind to {}:{}!", host, port)
      system.terminate()
  }

  log.info(s"Server Running on localhost:$port")

}
trait PoolRoutes extends Configuration{
  implicit val routerActor: ActorRef
  implicit val timeout: Timeout = Timeout(getKey[Int](timeoutAkka) seconds)

  val routes: Route =
    get {
      path("pool") {
        processFuture(routerActor ? "Pool request")
      }
    }
}
