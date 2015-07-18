package com.sjr.akka.monitoring

/**
 * Utilities for performance monitoring.
 *
 * These are modified versions of samples found in the Manning book
 * "Akka in Action".
 *
 * @author srichardson
 *
 *         Date: 4/24/15
 */

import akka.actor.{ ActorRef, ActorSystem }
import akka.dispatch._
import java.util.Queue
import com.typesafe.config.Config
import java.util.concurrent.ConcurrentLinkedQueue
import akka.dispatch.MessageQueue


trait MonitorMailbox extends MessageQueue {
  def system: ActorSystem
  def queue: Queue[MonitorEnvelope]
  def numberOfMessages = queue.size
  def hasMessages = !queue.isEmpty
  def cleanUp(owner: ActorRef, deadLetters: MessageQueue): Unit = {
    if (hasMessages) {
      var envelope = dequeue
      while (envelope ne null) {
        deadLetters.enqueue(owner, envelope)
        envelope = dequeue
      }
    }
  }

  def enqueue(receiver: ActorRef, handle: Envelope): Unit = {
    val env = MonitorEnvelope(queueSize = queue.size() + 1,
      receiver = receiver.toString,
      entryTime = System.currentTimeMillis,
      handle = handle)
    queue add env
  }
  def dequeue(): Envelope = {
    val monitor = queue.poll()
    if (monitor != null) {
      monitor.handle.message match {
        case stat: MailboxStatistics => //skip message
        case _ => {
          val stat = MailboxStatistics(
            queueSize = monitor.queueSize,
            receiver = monitor.receiver,
            sender = monitor.handle.sender.toString,
            entryTime = monitor.entryTime,
            exitTime = System.currentTimeMillis)
          system.eventStream.publish(stat)
        }
      }
      monitor.handle
    } else {
      null
    }
  }
}

class MonitorMailboxType extends akka.dispatch.MailboxType {

  def this(settings: ActorSystem.Settings, config: Config) = this()

  final override def create(owner: Option[ActorRef],
                            system: Option[ActorSystem]): MessageQueue = {
    system match {
      case Some(sys) =>
        new ConcurrentLinkedQueue[MonitorEnvelope]() with MonitorMailbox {
          final def system = sys
          final def queue: Queue[MonitorEnvelope] = this
        }
      case _ =>
        throw new IllegalArgumentException("requires an system")
    }
  }
}


case class MonitorEnvelope(queueSize: Int,
                           receiver: String,
                           entryTime: Long,
                           handle: Envelope)


