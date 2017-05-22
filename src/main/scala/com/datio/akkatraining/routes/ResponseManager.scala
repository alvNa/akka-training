package com.datio.akkatraining.routes

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, NoContent, NotFound}
import akka.http.scaladsl.server.Directives.{complete, onComplete}
import akka.http.scaladsl.server.Route
import com.datio.akkatraining.json.{HttpResponse => response}

import scala.concurrent.Future
import scala.reflect.classTag
import scala.util.{Failure, Success}

object ResponseManager extends ResponseManager

trait ResponseManager {
  def processResponse(response: Either[response, response]): Route = {
    response match {
      case x if x.isLeft => processLeft(response)
      case x if x.isRight => processRight(response)
      case _ => complete(BadRequest, "Error: Response control not implemented")
    }


  }

  def processLeft(response: Either[response, response]): Route =
    response.left.get match {
      case bean: response => {
        bean.status match {
          case NoContent.intValue => complete(bean.status, "")
          case NotFound.intValue => complete(bean.status, "")
          case _ => complete(bean.status, bean.data.getOrElse(""))
        }
      }
    }


  def processRight(response: Either[response, response]): Route =
    response.right.get match {
      case bean: response
      => complete(bean.status, bean.data.getOrElse(""))

    }

  def processFuture(future: Future[Any]) = {
    onComplete(future) {
      case Success(value) =>
        value match {
          case result if
          classTag[Either[response, response]]
            .runtimeClass
            .isInstance(result) =>
            processResponse(result.asInstanceOf[Either[response, response]])

          case _ => complete(HttpResponse(BadRequest))
        }
      case Failure(ex) =>
        complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
    }
  }
}