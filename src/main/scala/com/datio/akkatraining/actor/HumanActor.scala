package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


object HumanActor {
  def props(): Props = Props(classOf[HumanActor])
}

class HumanActor extends Actor with ActorLogging {

  var job: String = "x"

  def getJob: String = job

  def receive: Receive = {
    case x: String =>
      log.info(s"Human receiving message $x")
      job = x
      sender ! "im waiting for you"
    case (d1: ActorRef, d2: ActorRef) =>
      d1 ! job
      d2 ! job
    case _ => log.info("Default message")
  }
}

