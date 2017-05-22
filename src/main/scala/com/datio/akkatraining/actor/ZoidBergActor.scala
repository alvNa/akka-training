package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.datio.akkatraining.json.{BeerRequest, ClientInfo}
import com.datio.akkatraining.config.Configuration

class ZoidBergActor extends Actor
  with ActorLogging with Configuration {

  val bender: ActorRef = context.actorOf(BenderActor.props(),
    "Bender-Actor")

  def isDefaulter(client: String) =
    getConfig
    .getStringList(defaulters).contains(client)

  override def receive: Receive = {
    case req @ BeerRequest(_,_,_) => {
      log.info("BeerRequest, client: {}, number: {}, trade: {}", req.client, req.number, req.trade)
      bender forward (req, isDefaulter(req.client))
    }
  }
}

object ZoidBergActor {
  def props(): Props = Props(classOf[ZoidBergActor])
  def props(args: Any*): Props = Props(classOf[ZoidBergActor], args)
}
