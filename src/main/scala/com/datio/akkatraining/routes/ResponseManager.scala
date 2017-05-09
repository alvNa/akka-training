package com.datio.akkatraining.routes

import akka.http.scaladsl.model.StatusCodes.{BadRequest, NoContent, NotFound}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{Route, StandardRoute}
import com.datio.akkatraining.Json.HttpResponse

object ResponseManager extends ResponseManager

trait ResponseManager {
  def processResponse(response: Either[HttpResponse, HttpResponse]): Route = {
    response match {
      case x if x.isLeft => processLeft(response)
      case x if x.isRight => processRight(response)
      case _ => complete(BadRequest, "Error: Response control not implemented")
    }


  }

  def processLeft(response: Either[HttpResponse, HttpResponse]): Route =
    response.left.get match {
      case bean: HttpResponse => {
        bean.status match {
          case NoContent.intValue => complete(bean.status, "")
          case NotFound.intValue => complete(bean.status, "")
          case _ => complete(bean.status, bean.data.getOrElse(""))
        }
      }
    }


  def processRight(response: Either[HttpResponse, HttpResponse]): Route =
    response.right.get match {
      case bean: HttpResponse
      => complete(bean.status, bean.data.getOrElse(""))

    }
}