package com.datio.akkatraining.actor

import com.datio.akkatraining.json.{BeerRequest, PickRequest}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ValidClientTest extends WordSpec
with Matchers
with BeforeAndAfterAll{

  val fakeBeerValid = BeerRequest("manolo", 12, "Cruzcampo")
  val fakeBeerInvalid = BeerRequest("fry", 12, "Mahou")

  import com.datio.akkatraining.actor.ValidClientSyntax._

  "Test Futurama BeerRequest" should {
    "invalid" in {
      isValidClient(fakeBeerInvalid) shouldEqual false
    }
    "valid" in {
      isValidClient(fakeBeerValid) shouldEqual true
    }
  }

  val fakePickInvalid = PickRequest("Cristina Ronaldo")
  val fakePickValid = PickRequest("Mario Casas", Some("Hola"))
  "Test Futurama PickRequest" should {
    "invalid" in {
      isValidClient(fakePickInvalid) shouldEqual false
    }
    "valid" in {
      isValidClient(fakePickValid) shouldEqual true
    }
  }
}
