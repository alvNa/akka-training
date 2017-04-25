package com.datio.akkatraining

import akka.actor._
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.duration._

/**
  * App entrypoint
  */
object FuturamaApp extends App {

  implicit val system = ActorSystem("futurama")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(10 seconds)
  val log = Logging(system, getClass)

  log.info(s">>> ${getClass.getName()} Welcome to the year 3000!!")

  system.terminate()
}