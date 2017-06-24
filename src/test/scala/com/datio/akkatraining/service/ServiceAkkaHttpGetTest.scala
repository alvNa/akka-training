package com.datio.akkatraining.service

import akka.actor.{ActorRef, ActorRefFactory, Props}
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes.{Forbidden, Unauthorized, _}
import akka.http.scaladsl.model.{HttpEntity, Uri}
import akka.http.scaladsl.server.{MissingHeaderRejection, MissingQueryParamRejection}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import com.datio.akkatraining.actor.{BenderActor, LeelaActor, ZoidBergBeerActor, ZoidBergPickActor}
import com.datio.akkatraining.config.ClientHeader
import com.datio.akkatraining.routes.Routes
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ServiceAkkaHttpGetTest extends WordSpec
  with Matchers
  with ScalatestRouteTest
  with BeforeAndAfterAll
  with Routes {

//  override implicit val zoidBergBeer: ActorRef = ???
//  //TestActorRef(ZoidBergBeerActor.props())
//  override implicit val zoidBergPick: ActorRef = ???
//  val securityActor = TestActorRef(ZoidBergPickActor.props((_: ActorRefFactory) => probe.ref))

  //TestActorRef(ZoidBergPickActor.props())
//  val probeLeela = TestProbe()
//  val probeBender = TestProbe()
  val makerLeela: (ActorRefFactory) => ActorRef = (factory: ActorRefFactory) => factory.actorOf(LeelaActor.props(),
    "Leela-Actor")
  val makerBender: (ActorRefFactory) => ActorRef = (factory: ActorRefFactory) => factory.actorOf(BenderActor.props(),
    "Bender-Actor")
  override implicit val zoidBergBeer: ActorRef =
    system.actorOf(ZoidBergBeerActor.props(makerBender))

  override implicit val zoidBergPick: ActorRef =
    system.actorOf(ZoidBergPickActor.props(makerLeela))



  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val client: String = "fry"

  "Test Futurama bar GET" should {
    "forbidden, bender is de" in {
      Get("/bar/beer/1")
        .addHeader(ClientHeader(client)) ~>
        routes ~> check {
        handled shouldBe true
        status shouldBe Forbidden
        responseAs[String] should include("Sorry we haven't")
        responseEntity shouldEqual HttpEntity(`text/plain(UTF-8)`, "Sorry we haven't Estrella Galicia beer :)")
      }
    }
    "bad uri" in {
      Get("/bar/beer/a")
        .addHeader(ClientHeader(client)) ~>
        routes ~> check {
        handled shouldBe false
      }
    }
    "Missing header, rejected" in {
      Get("/bar/beer/2") ~>
        routes ~> check {
        rejection shouldEqual MissingHeaderRejection("client")
      }
    }

    "with trade parameter" in {
      Get(Uri("/bar/beer/1").withQuery(Uri.Query("trademark" -> "Mahou")))
        .addHeader(ClientHeader("bender")) ~>
        routes ~> check {
        handled shouldBe true
        status shouldBe OK
        responseAs[String] should include("Thanks! your beer")
      }

    }

  }


}
