package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.datio.akkatraining.config.Configuration

class RouterActor extends Actor
  with ActorLogging with Configuration {

  val resizer = DefaultResizer(lowerBound = getKey[Int](akkaLowerBound),
    upperBound = getKey[Int](akkaUpperBound),
    messagesPerResize = getKey[Int](akkaMessagePerResize),
      backoffThreshold = getConfig.getDouble(akkaBackoffThreshold),
        rampupRate = getConfig.getDouble(akkaBackoffThreshold)
  )

  val rrPool = RoundRobinPool(getKey[Int](akkaPoolActors), Some(resizer))
  val routerCass =
    context.actorOf(rrPool.props(
      PoolActor.props()), s"pool-actor")


  override def receive: Receive = {
    case text: String => {
      log.debug(s"Received message '$text' in actor ${self.path.name}")
      routerCass.tell(text, sender)
    }
  }
}

object RouterActor {
  def props(): Props = Props(classOf[RouterActor])
}
