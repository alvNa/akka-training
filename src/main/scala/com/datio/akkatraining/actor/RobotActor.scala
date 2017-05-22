package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}

import scala.util.Failure


object RobotActor {
  def props(): Props = Props(classOf[RobotActor])
}

class RobotActor extends Actor with ActorLogging {
  def receive: Receive = {
    case x =>
      log.info(s"Robot receiving message $x")
      sender ! "Bite my shiny metal ass"
    case _ =>
      log.info("Default message")
      sender ! Failure(throw new Exception("I don't know what are you saying"))
  }
}
