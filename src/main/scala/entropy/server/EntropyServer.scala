package com.sjr.entropy.server

import akka.actor.Actor
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

/**
 * Created by stevenrichardson on 2/1/15.
 */
class EntropyServer extends Actor {

  override def preStart(): Unit = {
    context.system.scheduler.schedule(0.seconds, 10.millis, self, "hello")
  }

  override def receive: Receive = {
    case x: Any => // do nothing
  }
}
