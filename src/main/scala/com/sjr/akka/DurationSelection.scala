package com.sjr.akka

import scala.concurrent.duration._
import akka.actor.Actor

/**
 * Assumed to be used from within in Actor where access to the retry count would be guaranteed thread safe.
 *
 *
 * @author srichardson
 *
 *         Date: 1/22/14
 */
trait DurationSelection {
  this: Actor =>
  private var attempt = 0
  private val attemptToDuration: Map[Range, FiniteDuration] = buildAttemptCountToDuration

  def attemptCount: Int = attempt

  def resetAttemptCount(): Unit = attempt = 0

  protected def nextDuration(): FiniteDuration = {
    val count = attempt
    attempt += 1
    durationForAttemptNumber(count)
  }

  /**
   * Override to return a mapping of integer ranges (representing the number of reconnect attempts)
   * and the duration it should wait before trying again.
   *
   * @return map of integer range to duration
   */
  protected def buildAttemptCountToDuration: Map[Range, FiniteDuration]

  // Simplest case would be having a constant return value which could be accomplished thus:
  //  protected def buildRetryCountToIntervalDuration:Map[Range, FiniteDuration]
  //    = Map(0.until(Int.MaxValue) -> 10.seconds)

  protected def upperBoundedRange(lowerBound: Int) = lowerBound.to(Int.MaxValue)

  protected def range(lowerBound: Int, upperBoundExclusive: Int) = lowerBound.until(upperBoundExclusive)

  private def durationForAttemptNumber(retryCount: Int): FiniteDuration = {
    val matchingRangeToDuration = attemptToDuration.toList.find(rangeToDuration => {
      rangeToDuration._1.contains(retryCount)
    })
    require(matchingRangeToDuration.isDefined, "There should be a duration for all possible retry count values")
    matchingRangeToDuration.get._2
  }
}
