package com.datio.akkatraining.service

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes.{Forbidden, _}
import akka.http.scaladsl.model.{HttpEntity, Uri}
import akka.http.scaladsl.server.MissingHeaderRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestActorRef, TestKit}
import com.datio.akkatraining.actor.{LeelaActor, ZoidBergActor}
import com.datio.akkatraining.config.ClientHeader
import com.datio.akkatraining.routes.Routes
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ServiceAkkaHttpPostTest extends WordSpec
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

  "Test Futurama bar POST" should {
    "pickup unauthorized, Bender" in {
      Post(Uri("/bar/beer/leela").withQuery(Uri.Query("proposal" -> "I'm Bender!")))
        .addHeader(ClientHeader("bender")) ~>
        routes ~> check {
        handled shouldBe true
        status shouldBe Unauthorized
        responseEntity shouldEqual HttpEntity(`text/plain(UTF-8)`, "keep dreamming!")
      }
    }
    "pickup ok" in {
      Post(Uri("/bar/beer/leela"))
        .addHeader(ClientHeader("Mario Casas")) ~>
        routes ~> check {
        handled shouldBe true
        status shouldBe OK
      }
    }
  }


}
