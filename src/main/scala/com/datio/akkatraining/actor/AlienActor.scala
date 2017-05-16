package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}


object AlienActor {
  def props(): Props = Props(classOf[AlienActor])
}

class AlienActor extends Actor with ActorLogging {
  def receive: Receive = {
    case x => log.info(s"Alien receiving message $x")
    case _ => log.info("Default message")
  }
}
