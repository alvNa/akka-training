package com.datio.akkatraining.json

case class BeerRequest(client: String,
                       number: Long,
                       trade: String)

case class ClientInfo(defaulter: Boolean, reason: String)