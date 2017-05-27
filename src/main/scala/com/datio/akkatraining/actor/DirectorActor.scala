package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorContext, ActorLogging, ActorRef, Props}
import com.datio.akkatraining.json.{BeerRequest, ClientInfo, PickRequest}
import com.datio.akkatraining.config.Configuration

import scala.reflect.{ClassTag, classTag}

abstract class DirectorActor[A: ClassTag] extends Actor
  with ActorLogging{

  implicit val ev: ValidClient[A]
  implicit val nextActor: ActorRef

  override def receive: Receive = {
      case req if classTag[A].runtimeClass.isInstance(req) => {
        nextActor forward (req, ev.isValidClient(req.asInstanceOf[A]))
    }
  }
}

class ZoidBergBeerActor
  extends DirectorActor[BeerRequest]
    with ValidClientInstances {
  implicit val ev = InstanceBeerRequest
  override implicit val nextActor: ActorRef = context.actorOf(BenderActor.props(), "Bender-Actor")
}
class ZoidBergPickActor
  extends DirectorActor[PickRequest]
    with ValidClientInstances {
  implicit val ev = InstancePickRequest
  override implicit val nextActor: ActorRef = context.actorOf(LeelaActor.props(), "Leela-Actor")
}
object ZoidBergBeerActor {
  def props(): Props = Props(classOf[ZoidBergBeerActor])
}
object ZoidBergPickActor {
  def props(): Props = Props(classOf[ZoidBergPickActor])
}

trait ValidClient[A]{
  def isValidClient(client: A): Boolean
}

trait ValidClientInstances {

  def apply[A](implicit ev: ValidClient[A]): ValidClient[A] = ev

  implicit object InstanceBeerRequest
    extends ValidClient[BeerRequest] with Configuration {
    override def isValidClient(client: BeerRequest): Boolean =
      !getConfig
        .getStringList(defaulters).contains(client.client)

  }
  implicit object InstancePickRequest
    extends ValidClient[PickRequest] with Configuration {
    override def isValidClient(req: PickRequest): Boolean =
      "Mario Casas".equals(req.client)

  }
}

object ValidClientSyntax extends ValidClientInstances {
  def isValidClient[A](client: A)(implicit ev: ValidClient[A]): Boolean =
    ev.isValidClient(client)
}



