package com.datio.akkatraining.actor

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.StatusCodes._
import com.datio.akkatraining.Json.{BeerRequest, ClientInfo, HttpResponse}
import com.datio.akkatraining.config.Configuration

class BenderActor extends Actor
  with ActorLogging with Configuration {

  override def receive: Receive = {
    case (req@BeerRequest(_, _, _), metaInfo@ClientInfo(_, _)) => {
      // log.info("BeerRequest, client: {}, number: {}, trade: {}", resp.response, resp.reason)
      if (metaInfo.defaulter) {
        sender ! HttpResponse(OK.intValue, Some(s"Thanks! your beer: ${req.trade.get}"))
      } else {
        sender ! HttpResponse(Forbidden.intValue, Some(s"Sorry we haven't ${req.trade.get} beer :)"))
      }
    }

  }
}
