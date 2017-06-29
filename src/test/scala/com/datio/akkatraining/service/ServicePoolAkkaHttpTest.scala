package com.datio.akkatraining.service

import akka.actor.ActorRef
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestKit
import com.datio.akkatraining.actor._
import com.datio.akkatraining.config.Logging
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ServicePoolAkkaHttpTest extends WordSpec
  with Matchers
  with ScalatestRouteTest
  with BeforeAndAfterAll
  with PoolRoutes
with Logging{


  override implicit val routerActor: ActorRef = system.actorOf(RouterActor.props(),
    "Router-Actor")

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val client: String = "fry"

  "Test pool" should {
    "request 1" in {
      Get("/pool") ~>
        routes ~> check {
        handled shouldBe true
        status shouldBe OK
        responseEntity shouldEqual HttpEntity(`text/plain(UTF-8)`, "Pool request")

      }
    }

    "request 2" in {
      Get("/pool") ~>
        routes ~> check {
        handled shouldBe true
        status shouldBe OK

      }
    }
      "request 3" in {
        Get("/pool") ~>
          routes ~> check {
          handled shouldBe true
          status shouldBe OK
        }
    }
  }

}
