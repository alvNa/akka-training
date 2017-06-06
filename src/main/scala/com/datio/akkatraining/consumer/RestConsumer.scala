package com.datio.akkatraining.consumer

import akka.actor
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode}

import scala.reflect.classTag

class RestConsumer(newSender: ActorRef) extends Actor with ActorLogging{
  val unmarshallerActor: ActorRef = context.actorOf(Props[UnmarshallRestActor], "UnmarshallRestActor-Actor")
  val requestRestActor: ActorRef = context.actorOf(Props[RequestRestActor], "RequestRest-Actor")

  override def receive: Receive = {
    case request: HttpRequest => {
      log.info("RestConsumer -> request")
      requestRestActor ! request
    }
    case resp: HttpResponse => {
      log.info("RestConsumer  -> unmarshallerActor")
      unmarshallerActor ! resp
    }
    case (status: StatusCode, response: String)
    if classTag[(StatusCode, String)].runtimeClass.isInstance((status, response)) =>{
      log.info("Manager Rest Actor return bean: {}", response)
      newSender ! (status, response)
    }
  }

  override def postStop(): Unit = {
    context stop unmarshallerActor
    context stop requestRestActor
  }
}
