package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.StatusCodes.{Forbidden, OK, Unauthorized}
import com.datio.akkatraining.config.Logging
import com.datio.akkatraining.json.{BeerRequest, HttpResponse, PickRequest}

import scala.reflect.classTag


abstract class BarTenderActor[A] extends Actor
  with ActorLogging {

  implicit val ev: CheckMetaInfo[A]

  override def receive: Receive = {
    case (req, metainfo: Boolean) if classTag[(A, Boolean)].runtimeClass.isInstance((req, metainfo)) => {
      sender ! ev.getResponse(metainfo, req.asInstanceOf[A])
    }
  }
}


class BenderActor extends BarTenderActor[BeerRequest]
  with ActorLogging with CheckMetaInfoInstances {
  implicit val ev = InstanceBender
}

object BenderActor {
  def props(): Props = Props(classOf[BenderActor])
}

class LeelaActor extends BarTenderActor[PickRequest] with CheckMetaInfoInstances {
  implicit val ev = InstanceLeela
}

object LeelaActor {
  def props(): Props = Props(classOf[LeelaActor])
}

trait CheckMetaInfo[A] {
  def getResponse(metainfo: Boolean, bean: A): Either[HttpResponse, HttpResponse]
}

trait CheckMetaInfoInstances {

  def apply[A](implicit ev: CheckMetaInfo[A]): CheckMetaInfo[A] = ev

  implicit object InstanceBender
    extends CheckMetaInfo[BeerRequest] with Logging {
    override def getResponse(metainfo: Boolean, req: BeerRequest): Either[HttpResponse, HttpResponse] = {
      log.info("client: {}, request to bender", req.client)

      Seq((true, Right(HttpResponse(OK.intValue, Some(s"Thanks! your beer: ${req.trade}")))),
        (false, Left(HttpResponse(Forbidden.intValue, Some(s"Sorry we haven't ${req.trade} beer :)")))))
        .filter{case(condition, _)=> condition == metainfo}.head._2
    }

  }

  implicit object InstanceLeela
    extends CheckMetaInfo[PickRequest] with Logging {
    override def getResponse(metainfo: Boolean, req: PickRequest): Either[HttpResponse, HttpResponse] = {

      log.info(s"Pick up request, client: ${req.client}, proposal: ${req.proposal.getOrElse("")}")

      Seq((true,  Right(HttpResponse(OK.intValue))),
        (false,  Left(HttpResponse(Unauthorized.intValue, Some("keep dreamming!")))))
        .filter{case(condition, _)=> condition == metainfo}.head._2
    }
  }

}


