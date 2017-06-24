package com.datio.akkatraining.api

import akka.http.scaladsl.model.StatusCodes.{Forbidden, OK, Unauthorized}
import com.datio.akkatraining.config.Logging
import com.datio.akkatraining.json.{BeerRequest, HttpResponse, PickRequest}

/**
  * Type class
  *
  * @tparam A
  */
trait CheckMetaInfo[A] {
  def getResponse(metainfo: Boolean, bean: A): Either[HttpResponse, HttpResponse]
}

/**
  * Instances
  */
trait CheckMetaInfoInstances {

  def apply[A](implicit ev: CheckMetaInfo[A]): CheckMetaInfo[A] = ev

  implicit val instanceBender = new CheckMetaInfo[BeerRequest] with Logging {
    override def getResponse(metainfo: Boolean, req: BeerRequest): Either[HttpResponse, HttpResponse] = {

      log.info("client: {}, request to bender", req.client)

      Seq((true, Right(HttpResponse(OK.intValue, Some(s"Thanks! your beer: ${req.trade}")))),
        (false, Left(HttpResponse(Forbidden.intValue, Some(s"Sorry we haven't ${req.trade} beer :)")))))
        .filter { case (condition, _) => condition == metainfo }.head._2
    }

  }

  implicit val instanceLeela = new CheckMetaInfo[PickRequest] with Logging {
    override def getResponse(metainfo: Boolean, req: PickRequest): Either[HttpResponse, HttpResponse] = {

      log.info(s"Pick up request, client: ${req.client}, proposal: ${req.proposal.getOrElse("")}")

      Seq((true, Right(HttpResponse(OK.intValue))),
        (false, Left(HttpResponse(Unauthorized.intValue, Some("keep dreamming!")))))
        .filter { case (condition, _) => condition == metainfo }.head._2
    }
  }

}

/**
  * Syntax
  */
object CheckMetaInfoSyntax extends CheckMetaInfoInstances {
  def getResponse[A](metainfo: Boolean, bean: A)(implicit ev: CheckMetaInfo[A]): Either[HttpResponse, HttpResponse] =
    ev.getResponse(metainfo, bean)
}