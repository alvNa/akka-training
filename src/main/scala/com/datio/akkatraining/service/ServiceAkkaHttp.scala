package com.datio.akkatraining.service

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.datio.akkatraining.actor.{ZoidBergBeerActor, ZoidBergPickActor}
import com.datio.akkatraining.config.{Configuration, Logging}
import com.datio.akkatraining.routes.Routes

import scala.concurrent.ExecutionContext

object ServiceAkkaHttp extends scala.App
  with Configuration
  with Logging
  with Routes {

  implicit val system = ActorSystem("Futurama-Service")
  implicit val materializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  implicit val zoidBergPick: ActorRef = system.actorOf(ZoidBergPickActor.props(),
    "ZoidBerg-Pick-Actor")

  implicit val zoidBergBeer: ActorRef = system.actorOf(ZoidBergBeerActor.props(),
    "ZoidBerg-Beer-Actor")


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
