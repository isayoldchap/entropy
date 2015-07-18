package com.sjr.akka.monitoring

/**
 * @author srichardson
 *
 *         Date: 4/24/15
 */

case class MailboxStatistics(queueSize: Int,
                             receiver: String,
                             sender: String,
                             entryTime: Long,
                             exitTime: Long)

case class ActorStatistics(receiver: String,
                           sender: String,
                           entryTime: Long,
                           exitTime: Long){

  def duration: Long = exitTime - entryTime
}

case class MailboxSummary(maxQueueLength: Int,
                          arrivalRate: Double,
                          averageWaitTime: Double)

case class ActorSummary(utilization: Double,
                        averageServiceTime: Double)

case class ActorUtilizationSummary(actorId: String,
                                   entryTime: Long,
                                   exitTime: Long,
                                   maxQueueLength: Int,
                                   arrivalRate: Double,
                                   averageWaitTime: Double,
                                   utilization: Double,
                                   averageServiceTime: Double)


