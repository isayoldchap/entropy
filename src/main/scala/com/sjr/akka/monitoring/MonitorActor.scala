package com.sjr.akka.monitoring

import akka.actor.Actor

/**
 *
 * @author srichardson
 *
 *         Date: 4/24/15
 */

trait MonitorActor extends Actor {

  abstract override def receive = {
    case m: Any => {
      val start = System.currentTimeMillis
      super.receive(m)
      val end = System.currentTimeMillis

      val stat = ActorStatistics(
        self.toString(),
        sender.toString(),
        start,
        end)
      context.system.eventStream.publish(stat)
    }
  }
}
