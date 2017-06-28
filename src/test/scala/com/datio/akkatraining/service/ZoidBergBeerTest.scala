package com.datio.akkatraining.service

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem, PoisonPill}
import akka.testkit.{TestKit, TestProbe}
import com.datio.akkatraining.actor.ZoidBergBeerActor
import com.datio.akkatraining.json.BeerRequest
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ZoidBergBeerTest extends TestKit(ActorSystem("testAkkaActors"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {


  override def afterAll {
    probe.send(zoidBergBeer, PoisonPill)
    TestKit.shutdownActorSystem(system)
  }

  val probe = TestProbe()
  val zoidBergBeer: ActorRef =
    system.actorOf(ZoidBergBeerActor.props((_: ActorRefFactory) => probe.ref))

  "Send beers to Zoiberg" must {
    "right user" in {
      val beer = BeerRequest("manolo", 2, "Cruzcampo")
      probe watch zoidBergBeer
      probe.send(zoidBergBeer, beer)
      probe.expectMsg((beer, true))
    }

    "wrong user" in {
      val beer = BeerRequest("fry", 1, "Mahou")
      probe watch zoidBergBeer
      probe.send(zoidBergBeer, beer)
      probe.expectMsg((beer, false))

    }
  }
}
