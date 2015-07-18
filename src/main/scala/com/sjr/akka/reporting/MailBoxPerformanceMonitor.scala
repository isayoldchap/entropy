package com.sjr.akka.reporting

import com.sjr.akka.monitoring.{MailBoxStatsCalculator, ActorStatistics, MailboxStatistics}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.actor.Actor
import ExecutionContext.Implicits.global

/**
 *
 * @author srichardson
 *
 *         Date: 4/24/15
 */


class MailBoxPerformanceMonitor extends Actor {

  var receiverToStats = Map[String, Seq[MailboxStatistics]]()

  override def preStart(): Unit = {
    context.system.scheduler.schedule(1 seconds, 30 seconds, self, CompileStatsReport)
    context.system.eventStream.subscribe(self, classOf[ActorStatistics])
    context.system.eventStream.subscribe(self, classOf[MailboxStatistics])
  }

  override def receive: Receive = {
    case CompileStatsReport =>
      receiverToStats.foreach {
        case (key, stats) =>
          println(s"Mailbox $key")
          val summary = MailBoxStatsCalculator.processMailboxEvents(30.seconds, stats)
          println(s"Max Queue Len ${summary.maxQueueLength} arrival rate ${summary.arrivalRate} average wait time ${summary.averageWaitTime} ")
      }
      receiverToStats = Map.empty
    case stats: MailboxStatistics =>
      val receiver = stats.receiver
      if (receiver.contains("Entropy")) {
        val statsList = receiverToStats.getOrElse(receiver, Seq.empty)
        receiverToStats += (receiver -> (statsList :+ stats))
      }
    case stats: ActorStatistics =>
      if (stats.duration > 10) println(s"Stats: $stats")
  }
}

case object CompileStatsReport