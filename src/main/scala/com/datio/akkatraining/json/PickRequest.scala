package com.datio.akkatraining.json

case class PickRequest(client: String, proposal: Option[String] = None)