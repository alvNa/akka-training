package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}


object HumanActor {
  def props(): Props = Props(classOf[HumanActor])
}

class HumanActor extends Actor with ActorLogging {
  def receive: Receive = {
    case _ => log.info("Human receiving message")
  }
}

