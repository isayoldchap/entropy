package com.sjr.akka.monitoring

/**
 *
 * @author srichardson
 *
 *         Date: 4/24/15
 */

import scala.concurrent.duration.Duration


object MailBoxStatsCalculator {

  def getSummaries(
                    startTimePeriod: Long, period: Duration,
                    groupedMailbox: Map[String, List[MailboxStatistics]],
                    groupedActor: Map[String, List[ActorStatistics]]): Seq[ActorUtilizationSummary] = {

    val mailSummaries = groupedMailbox.map {
      case (actorId, listEvents) => {
        val mailStat = processMailboxEvents(period, listEvents)
        //check if we have actor data too
        val actorStat = groupedActor.get(actorId).map(
          processActorEvents(startTimePeriod, period, _))

        ActorUtilizationSummary(
          actorId = actorId,
          entryTime = startTimePeriod,
          exitTime = startTimePeriod + period.toMillis,
          maxQueueLength = mailStat.maxQueueLength,
          arrivalRate = mailStat.arrivalRate,
          averageWaitTime = mailStat.averageWaitTime,
          utilization = actorStat.map(_.utilization).getOrElse(0),
          averageServiceTime = actorStat.map(_.averageServiceTime)
            .getOrElse(0))
      }
    }.toSeq
    val mailboxKeys = groupedMailbox.keySet
    val onlyActorData = groupedActor.filterNot {
      case (key, list) => mailboxKeys.contains(key)
    }
    val actorSummaries = onlyActorData.map {
      case (actorId, listEvents) => {
        val actorStat = processActorEvents(
          startTimePeriod,
          period,
          listEvents)
        ActorUtilizationSummary(
          actorId = actorId,
          entryTime = startTimePeriod,
          exitTime = startTimePeriod + period.toMillis,
          maxQueueLength = 0,
          arrivalRate = 0,
          averageWaitTime = 0,
          utilization = actorStat.utilization,
          averageServiceTime = actorStat.averageServiceTime)
      }
    }
    mailSummaries ++ actorSummaries
  }

  def processActorEvents(startTimePeriod: Long,
                         period: Duration,
                         events: Seq[ActorStatistics]): ActorSummary = {

    val nrEvents = events.size
    if (nrEvents == 0 || period.toMillis == 0) {
      ActorSummary(0, 0)
    } else {
      val endTimePeriod = startTimePeriod + period.toMillis

      val (nrProcess, totalProcessTime) = events.foldLeft(0, 0L) {
        case ((nr, total), event) => {
          if (event.entryTime < startTimePeriod) {
            (nr, total)
          } else {
            (nr + 1, total + (event.exitTime - event.entryTime))
          }
        }
      }
      val totalUtilizationTime = events.foldLeft(0L) {
        case (total, event) => {
          val entryTime = Math.max(event.entryTime, startTimePeriod)
          val exitTime = Math.min(event.exitTime, endTimePeriod)
          total + (exitTime - entryTime)
        }
      }
      val utilization = (totalUtilizationTime * 100) / period.toMillis
      ActorSummary(utilization = utilization,
        averageServiceTime =
          if (nrProcess != 0)
            (totalProcessTime / nrProcess)
          else 0)
    }
  }

  def processMailboxEvents(period: Duration,
                           events: Seq[MailboxStatistics]): MailboxSummary = {

    val nrEvents = events.size
    if (nrEvents == 0 || period.toSeconds == 0) {
      MailboxSummary(0, 0, 0)
    } else {
      val (maxQueue, totalWaitTime) = events.foldLeft((0, 0L)) {
        case ((max, total), event) => {
          val newMax = Math.max(max, event.queueSize)
          val newTotal = total + (event.exitTime - event.entryTime)
          (newMax, newTotal)
        }
      }
      println(s"Total wait time $totalWaitTime")
      println(s"Total events $nrEvents")
      MailboxSummary(maxQueueLength = maxQueue,
        arrivalRate = nrEvents / period.toSeconds,
        averageWaitTime = totalWaitTime / nrEvents)
    }
  }
}
