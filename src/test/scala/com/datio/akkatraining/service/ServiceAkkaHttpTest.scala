package com.datio.akkatraining.service

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestActorRef
import com.datio.akkatraining.actor.ZoidBergActor
import com.datio.akkatraining.config.{ClientHeader, Logging}
import com.datio.akkatraining.routes.Routes
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.model.StatusCodes._
import com.datio.akkatraining.Json.HttpResponse
import spray.json.{DefaultJsonProtocol, _}

class ServiceAkkaHttpTest extends FunSuite
  with Matchers
  with ScalatestRouteTest
  with BeforeAndAfterAll
with Routes{

  implicit val zoidBerg: ActorRef =  TestActorRef(Props[ZoidBergActor])

  val client: String = "bender"
  // role, dbType, indexType
//  val payload = PayLoadIndex("daas", "PostgreSQL", "field1-field2", None)
//  val requestEntity = HttpEntity(`application/json`, payload.toJson.compactPrint.getBytes())
import com.datio.akkatraining.Json.HttpResponse._
  test("Insert index in dictionary handled request") {
    Get("/bar/beer/1")
      .addHeader(ClientHeader(client)) ~>
      routes ~> check {
      handled shouldBe true
      status shouldBe OK
      responseEntity shouldEqual HttpEntity(`application/json`,
        HttpResponse(OK.intValue, Some("User fake_user unauthorized")).toJson.prettyPrint)
    }
  }
}
