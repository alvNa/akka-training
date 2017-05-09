package com.datio.akkatraining.Json

case class BeerRequest(client: String,
                       number: Long,
                       trade: Option[String] = Some("Estrella Galicia"))

case class ClientInfo(defaulter: Boolean, reason: Option[String])