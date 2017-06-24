package com.datio.akkatraining.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes.{Forbidden, OK, Unauthorized}
import com.datio.akkatraining.api.CheckMetaInfo
import com.datio.akkatraining.config.Logging
import com.datio.akkatraining.json.{BeerRequest, HttpResponse, PickRequest}

import scala.reflect.classTag
import com.datio.akkatraining.api.CheckMetaInfoSyntax._
abstract class BarTenderActor[A: CheckMetaInfo] extends Actor
  with ActorLogging {


  override def receive: Receive = {
    case (req, metainfo: Boolean) if classTag[(A, Boolean)].runtimeClass.isInstance((req, metainfo)) => {
      sender ! getResponse(metainfo, req.asInstanceOf[A])
    }
  }
}


class BenderActor extends BarTenderActor[BeerRequest]

object BenderActor {
  def props(): Props = Props(classOf[BenderActor])
}


class LeelaActor extends BarTenderActor[PickRequest]

object LeelaActor {
  def props(): Props = Props(classOf[LeelaActor])
}


