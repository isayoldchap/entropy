package com.sjr.entropy.server

import akka.actor.SupervisorStrategy.Restart
import akka.actor._

import scala.concurrent.duration._
import scala.util.Random

/**
 * Created by srichardson on 4/6/15.
 */
class SupervisingActor extends Actor {

  import scala.concurrent.ExecutionContext.Implicits.global

  override def preStart(): Unit = {
    (1 to 2).foreach{ id =>
      val child = context.actorOf(Props(new ChildActor(id)))
      context.system.scheduler.schedule(1.second, 100.millis, child, "Test")
    }
  }

  override val supervisorStrategy: SupervisorStrategy = AllForOneStrategy(maxNrOfRetries = -1, loggingEnabled = false){
    case x: Exception => Restart
    case _ => Restart
  }

  override def receive: Receive = {
    case  _ =>
  }
}

class ChildActor(id: Int) extends Actor {
  private val random = new Random

  override def receive: Actor.Receive = {
    case x:Any => throw new Exception("Oops")
//      val nextNumber = random.nextInt(20)
//      if (nextNumber == 1) throw new Exception(s"Oh dear from actor $id when next no. was $nextNumber")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
//    super.preRestart(reason, message)
    println(s"PreRestart on actor $id with message: $message on actor with ID $id")
  }

  override def preStart(): Unit = println(s"Pre start: $id")

  override def postStop(): Unit = { println(s"Post stop: $id")}

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    println(s"Post Restart: $reason")
  }
}
