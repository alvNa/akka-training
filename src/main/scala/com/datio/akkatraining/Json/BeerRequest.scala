package com.datio.akkatraining.Json

case class BeerRequest(client: String,
                       number: Long,
                       trade: String)

case class ClientInfo(defaulter: Boolean, reason: String)