package com.datio.akkatraining

import akka.actor._
import akka.event.Logging
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.datio.akkatraining.actor._

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

  val humanActor = system.actorOf(Props[HumanActor], "Professor_Farnsworth")
  val robotActor = system.actorOf(Props[RobotActor], "Bender")

  //Actor's first call with a message
  humanActor ? "Good news everyone!"
  robotActor ? "Bite my shiny metal ass"

  system.terminate()
}