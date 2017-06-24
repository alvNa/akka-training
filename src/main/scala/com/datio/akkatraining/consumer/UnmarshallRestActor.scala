package com.datio.akkatraining.consumer

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.pattern.pipe
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.datio.akkatraining.json.{HttpResponse => response}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Unmarshall Actor
  *
  */
class UnmarshallRestActor extends Actor with ActorLogging {

  implicit val materializer: ActorMaterializer =
    ActorMaterializer(ActorMaterializerSettings(context.system))

    override def receive: Receive = {
      case HttpResponse(code, _, entity, _) => {
        log.info("Unmarshal to pipe")
        Unmarshal(entity).to[String].map(x=>(code, x)).pipeTo(sender)
      }
    }
}


