package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}


object HumanActor {
  def props(): Props = Props(classOf[HumanActor])
}

class HumanActor extends Actor with ActorLogging {
  def receive: Receive = {
    case x => log.info(s"Human receiving message $x")
    case _ => log.info("Default message")
  }
}

