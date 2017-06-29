package com.datio.akkatraining.config

import com.typesafe.config.{Config, ConfigFactory}

trait Configuration {

  val serviceAddress = "akka-training.akka.interface"
  val servicePort = "akka-training.akka.port"
  val timeoutAkka = "akka-training.akka.timeout"
  val defaulters = "akka-training.defaulters"
  val akkaPoolActors = "akka-training.akka.poolActors"
  val akkaLowerBound = "akka-training.akka.lowerBound"
  val akkaUpperBound = "akka-training.akka.upperBound"
  val akkaMessagePerResize = "akka-training.akka.messagesPerResize"
  val akkaBackoffThreshold = "akka-training.akka.backoff-threshold"
  val akkaRampupRate = "akka-training.akka.rampupRate"


  private lazy val config: Config = ConfigFactory.load

  def getConfig: Config = {
    config
  }

  def getOption(key: String): Option[String] = {
    if (config.hasPath(key)) {
      Some(config.getString(key))
    } else {
      None
    }
  }

  def getKey[T](key: String): T = {
    config.getAnyRef(key).asInstanceOf[T]
  }

}
