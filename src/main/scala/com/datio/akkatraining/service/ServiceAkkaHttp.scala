package com.datio.akkatraining.service

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.datio.akkatraining.actor.{BenderActor, LeelaActor, ZoidBergBeerActor, ZoidBergPickActor}
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

  val makerLeela: (ActorRefFactory) => ActorRef = (factory: ActorRefFactory) => factory.actorOf(LeelaActor.props(),
    "Leela-Actor")
  val makerBender: (ActorRefFactory) => ActorRef = (factory: ActorRefFactory) => factory.actorOf(BenderActor.props(),
    "Bender-Actor")

  implicit val zoidBergPick: ActorRef = system.actorOf(ZoidBergPickActor.props(makerLeela),
    "ZoidBerg-Pick-Actor")

  implicit val zoidBergBeer: ActorRef = system.actorOf(ZoidBergBeerActor.props(makerBender),
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

  log.debug(s"Server Running on localhost:$port")
}
