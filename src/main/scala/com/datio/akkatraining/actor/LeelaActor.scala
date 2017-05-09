package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.datio.akkatraining.Json.{BeerRequest, ClientInfo, HttpResponse, PickRequest}
import com.datio.akkatraining.config.Configuration
import akka.http.scaladsl.model.StatusCodes._

class LeelaActor extends Actor
  with ActorLogging with Configuration {

  override def receive: Receive = {
    case (req @ BeerRequest(_,_,_), metaInfo @ ClientInfo(_,_)) => {
     // log.info("BeerRequest, client: {}, number: {}, trade: {}", resp.response, resp.reason)
      if(metaInfo.defaulter){
        sender ! HttpResponse(OK.intValue, Some(s"Thanks! your beer: ${req.trade.get}"))
      }else{
        sender ! HttpResponse(Forbidden.intValue, Some(s"Sorry we haven't ${req.trade.get} beer :)"))
      }
    }
    case req: PickRequest => {
      sender ! "Mario Casas".equals(req.client)
    }
  }

}
