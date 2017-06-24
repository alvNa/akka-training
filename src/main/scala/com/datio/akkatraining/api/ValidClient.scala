package com.datio.akkatraining.api

import com.datio.akkatraining.config.Configuration
import com.datio.akkatraining.json.{BeerRequest, PickRequest}

/**
  * Type class
  *
  * @tparam A
  */
trait ValidClient[A] {
  def isValidClient(client: A): Boolean
}

/**
  * Instances.
  */
trait ValidClientInstances {

  def apply[A](implicit ev: ValidClient[A]): ValidClient[A] = ev

  implicit object InstanceBeerRequest
    extends ValidClient[BeerRequest] with Configuration {
    override def isValidClient(client: BeerRequest): Boolean =
      !getConfig
        .getStringList(defaulters).contains(client.client)

  }

  implicit object InstancePickRequest
    extends ValidClient[PickRequest] with Configuration {
    override def isValidClient(req: PickRequest): Boolean =
      "Mario Casas".equals(req.client)

  }

}

/**
  * Syntax
  */

object ValidClientSyntax extends ValidClientInstances {
  def isValidClient[A](client: A)(implicit ev: ValidClient[A]): Boolean =
    ev.isValidClient(client)
}


