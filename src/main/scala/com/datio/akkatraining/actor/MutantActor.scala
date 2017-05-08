package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}


object MutantActor {
  def props(): Props = Props(classOf[HumanActor])
}

class MutantActor extends Actor with ActorLogging {
  def receive: Receive = {
    case x => log.info(s"Mutant receiving message $x")
    case _ => log.info("Default message")
  }
}