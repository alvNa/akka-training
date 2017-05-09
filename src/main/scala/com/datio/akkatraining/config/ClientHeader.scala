package com.datio.akkatraining.config

import akka.http.scaladsl.model.headers.{ModeledCustomHeader, ModeledCustomHeaderCompanion}

import scala.util.Try

final class ClientHeader(client: String) extends ModeledCustomHeader[ClientHeader] {

  override val companion = ClientHeader

  override def value: String = client

  override def renderInResponses(): Boolean = false

  override def renderInRequests(): Boolean = true
}

object ClientHeader extends ModeledCustomHeaderCompanion[ClientHeader] {
  override val name = "client"

  override def parse(value: String): Try[ClientHeader] = Try(new ClientHeader(value))
}
