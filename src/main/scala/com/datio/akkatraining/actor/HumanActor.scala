package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.datio.akkatraining.FuturamaApp.{system}
import akka.pattern.ask

object HumanActor {
  def props(): Props = Props(classOf[HumanActor])
}

class HumanActor extends Actor with ActorLogging {
  val robotActor = system.actorOf(RobotActor.props, "Bender")

  def receive: Receive = {
    case x =>
      log.info(s"Human receiving message $x")
      val future = robotActor ? "Bite my shiny metal ass"

      future.onSuccess{
        case x: String => log.info(s"my robot answered with ${x}")
      }


    case _ => log.info("Default message")
  }
}

