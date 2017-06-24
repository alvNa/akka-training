package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorRefFactory, Props}
import com.datio.akkatraining.api.{ValidClient, ValidClientInstances}
import com.datio.akkatraining.json.{BeerRequest, PickRequest}
import com.datio.akkatraining.api.ValidClientSyntax._

import scala.reflect.{ClassTag, classTag}

abstract class DirectorActor[A: ClassTag: ValidClient](factory: ActorRefFactory => ActorRef) extends Actor
  with ActorLogging {

  implicit val nextActor: ActorRef = factory(context)

  override def receive: Receive = {
    case req if classTag[A].runtimeClass.isInstance(req) => {
      nextActor forward (req,
        isValidClient(req.asInstanceOf[A]))
    }
  }
}

class ZoidBergBeerActor(factory: ActorRefFactory => ActorRef)
  extends DirectorActor[BeerRequest](factory)

object ZoidBergBeerActor {
  def props(factory: (ActorRefFactory) => ActorRef): Props = Props(classOf[ZoidBergBeerActor],
    factory)
}

class ZoidBergPickActor(factory: ActorRefFactory => ActorRef)
  extends DirectorActor[PickRequest](factory)

object ZoidBergPickActor {
  def props(factory: (ActorRefFactory) => ActorRef): Props = Props(classOf[ZoidBergPickActor],
    factory)
}