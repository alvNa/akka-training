package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.datio.akkatraining.Json.{BeerRequest, ClientInfo, HttpResponse, PickRequest}
import com.datio.akkatraining.config.Configuration
import akka.http.scaladsl.model.StatusCodes._

class ZoidBergActor extends Actor
  with ActorLogging with Configuration {

  val bender: ActorRef = context.actorOf(Props[ZoidBergActor],
    "Leela-Actor")

  import scala.collection.JavaConverters._

  def isDefaulter(client: String) =
    getConfig
    .getConfigList(defaulters)
    .asScala.contains(client)

  override def receive: Receive = {
    case req @ BeerRequest(_,_,_) => {
      log.debug("BeerRequest, client: {}, number: {}, trade: {}", req.client, req.number, req.trade)
      bender forward (req, isDefaulter(req.client))
    }
    case req: PickRequest => {
      log.debug("PickRequest to Leela")
      bender forward req
    }
  }
}
