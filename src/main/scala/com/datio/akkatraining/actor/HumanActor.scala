package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.datio.akkatraining.FuturamaApp.{system}

object HumanActor {
  def props(): Props = Props(classOf[HumanActor])
}

class HumanActor extends Actor with ActorLogging {
  val robotActor = system.actorOf(RobotActor.props, "Bender")

  def receive: Receive = {
    case x =>
      log.info(s"Human receiving message $x")
      robotActor forward x
    case _ => log.info("Default message")
  }
}

