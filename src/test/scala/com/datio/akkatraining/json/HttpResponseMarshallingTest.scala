package com.datio.akkatraining.json

import akka.http.scaladsl.model.StatusCodes._
import org.scalatest.{FunSuite, Matchers}
import spray.json._

class HttpResponseMarshallingTest extends FunSuite with Matchers {
  val data = "Some data"
  val labelStatus = "status"
  val labelData = "data"
  val response =
    s"""{
       |"$labelStatus": ${OK.intValue},
       |"$labelData": "$data"
     }""".stripMargin

  test("Marshalling Json") {
    val re = response.parseJson.convertTo[HttpResponse]
    re.status shouldEqual OK.intValue
    re.data shouldEqual Some(data)
  }

  test("Marshalling Json with error") {
    val response: String =
      s"""{
         |"$labelStatus": "a",
         |"$labelData": "$data"
     }""".stripMargin

    val caught = intercept[DeserializationException] {
     response.parseJson.convertTo[HttpResponse]
    }
    caught.msg should include("Expected Int as JsNumber")

  }
}
