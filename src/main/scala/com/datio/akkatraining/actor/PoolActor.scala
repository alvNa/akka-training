package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.StatusCodes._
import com.datio.akkatraining.json.HttpResponse

class PoolActor extends Actor with ActorLogging {
  log.info(s"Created actor: ${self.path.name}")

  override def receive: Receive = {
    case text: String => {
      log.debug(s"Received message '$text' in actor ${self.path.name}")
      sender ! Right(HttpResponse(OK.intValue, Some(text)))
    }
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    log.info(s"Stop actor ${self.path.name}")

  }
}

object PoolActor {
  def props(): Props = Props(classOf[PoolActor])
}
