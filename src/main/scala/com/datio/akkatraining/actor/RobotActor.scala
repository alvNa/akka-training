package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}


object RobotActor {
  def props(): Props = Props(classOf[HumanActor])
}

class RobotActor extends Actor with ActorLogging {
  def receive: Receive = {
    case _ => log.info("Robot receiving message")
  }
}
