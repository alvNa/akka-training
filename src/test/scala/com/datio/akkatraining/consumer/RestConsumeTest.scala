package com.datio.akkatraining.consumer
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpMethods.{GET, POST}
import akka.http.scaladsl.model.StatusCodes.{BadRequest, NoContent, NotFound, OK}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import com.datio.akkatraining.config.{Configuration, Logging}
import org.scalatest._

import scala.concurrent.Future

class RestServer(implicit val system: ActorSystem,
                 implicit val materializer: ActorMaterializer,
                 implicit val route: Route) extends Logging {

  val defaultPort = 8080

  def startServer(address: String = "localhost", port: Int = defaultPort): Future[ServerBinding] = {
    val bind = Http().bindAndHandle(route, address, port)
    log.debug("Test service up in: {} , {}", address, port)
    bind
  }

  def stopServer() {
    log.info("Finish test service")
    system.terminate()
  }
}


class RestConsumeTest extends TestKit(ActorSystem("Rest-consumer"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with Configuration
  with RestService
  with SprayJsonSupport {


  implicit val actorSystem = ActorSystem("rest-server")
  implicit val materializer = ActorMaterializer()
  val server = new RestServer()

  override protected def beforeAll() = {
    server.startServer()
  }



  override def afterAll {
    TestKit.shutdownActorSystem(system)
    server.stopServer()
    actorSystem.terminate()
  }

  val probe: TestProbe = TestProbe()
  val clientActor: ActorRef =
    system.actorOf(Props(classOf[RestConsumer], probe.ref), "test-rest-client-actor")
  probe watch clientActor

  val response = "This is a rest client Rest!"
  val host =  "http://localhost:8080"
  "Test Rest Client GET" should {
    "Bad request status, query not valid" in {
      probe.send(clientActor, HttpRequest(GET, uri = s"$host/service/get"))
      probe.expectMsg((OK, response))
    }


    "get with not found" in {
      probe.send(clientActor, HttpRequest(GET, uri = s"$host/service/wrong"))
      probe.expectMsg((NotFound, "The requested resource could not be found."))

    }
    "get just with status BadRequest" in {
      probe.send(clientActor, HttpRequest(GET, uri = s"$host/service/get/bad"))
      probe.expectMsg((BadRequest, ""))
    }
    "get just with status ok" in {
      probe.send(clientActor, HttpRequest(GET, uri = s"$host/service/get/ok"))
      probe.expectMsg((OK, ""))

    }
  }

  "Test Rest Client POST" should {
    "post with ok" in {
      probe.send(clientActor, HttpRequest(POST, uri = s"$host/service/post"))
      probe.expectMsg((OK, response))
    }
    "post with not found" in {
      probe.send(clientActor, HttpRequest(POST, uri = s"$host/service/post/wrong"))
      probe.expectMsg((NotFound, "The requested resource could not be found."))

    }
    "post just with status no content" in {
      probe.send(clientActor, HttpRequest(POST, uri = s"$host/service/post/nocontent"))
      probe.expectMsg((NoContent, ""))

    }

  }


}


trait RestService {

  import akka.http.scaladsl.server.Directives._

  val resp = HttpResponse(status = StatusCodes.OK,
    entity = HttpEntity(`text/plain(UTF-8)`,
      "This is a rest client Rest!"))
  val target = "cassandra"
  implicit val route: Route =
    path("service" / "get") {
      get {
        complete(resp)
      }
    } ~ path("service" / "get" / "bad") {
      get {
        complete(HttpResponse(BadRequest))
      }
    } ~ path("service" / "get" / "ok") {
      get {
        complete(HttpResponse(OK))
      }

    } ~ path("service" / "post") {
      post {
        complete(resp)
      }
    } ~ path("service" / "post" / "nocontent") {
      post {
        complete(HttpResponse(NoContent))
      }
    }
}
