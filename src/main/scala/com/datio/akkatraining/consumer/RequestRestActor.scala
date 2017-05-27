package com.datio.akkatraining.consumer

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}

/**
  * Actor to do Rest single request to get HttpResponse.
  */
class RequestRestActor extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  implicit val materializer: ActorMaterializer =
    ActorMaterializer(ActorMaterializerSettings(context.system))
  val http = Http(context.system)

  def receive: Receive = {
    case request@HttpRequest(_, _, _, _, _) => {
      log.info("HttpResponse RequestRestActor -> RestConsumer")
      http.singleRequest(request).pipeTo(sender)
    }
  }
}

