package com.datio.akkatraining.service

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes.{Forbidden, Unauthorized, _}
import akka.http.scaladsl.model.{HttpEntity, Uri}
import akka.http.scaladsl.server.{MissingHeaderRejection, MissingQueryParamRejection}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestActorRef, TestKit}
import com.datio.akkatraining.actor.{LeelaActor, ZoidBergActor}
import com.datio.akkatraining.config.ClientHeader
import com.datio.akkatraining.routes.Routes
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ServiceAkkaHttpGetTest extends WordSpec
  with Matchers
  with ScalatestRouteTest
  with BeforeAndAfterAll
  with Routes {

  override implicit val zoidBerg: ActorRef = TestActorRef(Props[ZoidBergActor])
  override implicit val leela: ActorRef = TestActorRef(Props[LeelaActor])

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
