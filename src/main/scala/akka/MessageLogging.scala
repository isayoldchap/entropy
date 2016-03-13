package com.sjr.akka

import akka.actor.SupervisorStrategy.{Decider, Resume, Stop}
import akka.actor._
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Try}

/** Use this trait as a dynamic mix-in. */
trait MessageLogging extends Actor with ActorLogging {
  abstract override def receive: Receive = {
    case message: Any =>
      log.debug("Received message: {}", message)
      super.receive(message)
  }
}

final class ResumingSupervisorStrategyConfigurator extends SupervisorStrategyConfigurator {
  override def create(): SupervisorStrategy = BaseActor.resumingSupervisorStrategy
}

object BaseActor {
  // The default decider in Akka's Actor implementation restarts the failing child actor instead - this one will resume instead
  final val resumingDecider: Decider = {
    case _: ActorInitializationException ⇒ Stop
    case _: ActorKilledException ⇒ Stop
    case _: DeathPactException ⇒ Stop
    case _: Exception => Resume
  }

  final val resumingSupervisorStrategy: SupervisorStrategy = OneForOneStrategy()(resumingDecider)

  def exists(path: String, system: ActorSystem): Boolean = {
    try {
      implicit val timeout = Timeout(5.seconds)
      Await.result(system.actorSelection(path).resolveOne(), timeout.duration)
      true
    }
    catch {
      case e: ActorNotFound => false
    }
  }
}

trait ResumingSupervisorBehavior extends Actor {
  override def supervisorStrategy: SupervisorStrategy = BaseActor.resumingSupervisorStrategy
}

/** If you use this along with MessageLogging you get both incoming & outgoing messages logged. */
trait BaseActor extends Actor with ActorLogging {
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    log.error(reason, message.getOrElse("Empty message").toString)
  }

  protected def reply(message: AnyRef, recipient: ActorRef = sender()): Unit = {
    log.debug("Sending reply {} to {}", message, recipient)
    recipient ! message
  }

  protected def safeAction(f: => Unit) = Try(f) match {
    case Failure(t) => log.error(t, s"Error running $f")
    case _ =>
  }

  protected def unknownMessage(any: Any): Unit = log.error(s"Can't handle message $any from sender ${sender()}")
}
