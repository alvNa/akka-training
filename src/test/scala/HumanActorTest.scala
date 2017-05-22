import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import scala.concurrent.duration._
import com.datio.akkatraining.actor.HumanActor
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by rmulero on 22/05/17.
  */
class HumanActorTest extends TestKit(ActorSystem("testAkkaSystem"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender{

  val humanActor = TestActorRef(Props[HumanActor], "Professor_Farnsworth")

  "A HumanActor" must {
    "respond with a message" in {

      humanActor ! "hey you! make a new job!"

      humanActor.underlyingActor.asInstanceOf[HumanActor].getJob must be
      ("hey you! make a new job!")


    }
  }

  "A TestProbe test" in {
    val probe1 = TestProbe()
    val probe2 = TestProbe()
    val actor = system.actorOf(Props[HumanActor])
    actor ! "A job"
    actor ! ((probe1.ref, probe2.ref))

    probe1.expectMsg(500 millis, "A job")
    probe2.expectMsg(500 millis, "A job")
  }

  "A watch test" in {
    val probe = TestProbe()
    probe watch humanActor
    humanActor ! PoisonPill
    probe.expectTerminated(humanActor)

  }
}
