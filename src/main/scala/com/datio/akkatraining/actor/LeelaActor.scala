package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.StatusCodes._
import com.datio.akkatraining.Json.{HttpResponse, PickRequest}
import com.datio.akkatraining.config.Configuration

class LeelaActor extends Actor
  with ActorLogging with Configuration {

  override def receive: Receive = {
    case req: PickRequest => {
      log.info("Pick up request, client: {}, proposal: {}", req.client, req.proposal)
      if("Mario Casas".equals(req.client)){
        sender ! Right(HttpResponse(OK.intValue))
      }else{
        sender ! Left(HttpResponse(Unauthorized.intValue, Some("keep dreamming!")))
      }

    }
  }

}
object LeelaActor {
  def props(): Props = Props(classOf[LeelaActor])
  def props(args: Any*): Props = Props(classOf[LeelaActor], args)
}

