package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.StatusCodes.{Forbidden, OK}
import com.datio.akkatraining.Json.{BeerRequest, ClientInfo, HttpResponse}
import com.datio.akkatraining.config.Configuration

class BenderActor extends Actor
  with ActorLogging with Configuration {
  override def receive: Receive = {

    case (req @ BeerRequest(_,_,_), metaInfo @ ClientInfo(_,_)) => {
      if(!metaInfo.defaulter){
        log.info("client: {}, is not defaulter", req.client)
        sender ! Right(HttpResponse(OK.intValue, Some(s"Thanks! your beer: ${req.trade}")))
      }else{
        log.info("client: {}, is defaulter", req.client)
        sender ! Left(HttpResponse(Forbidden.intValue, Some(s"Sorry we haven't ${req.trade} beer :)")))
      }
    }
  }
}

object BenderActor {
  def props(): Props = Props(classOf[BenderActor])
  def props(args: Any*): Props = Props(classOf[BenderActor], args)
}
