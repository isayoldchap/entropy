package com.sjr.entropy.core.server

import akka.actor.{ActorSystem, Props}
import akka.kernel.Bootable
import com.sjr.akka.reporting.MailBoxPerformanceMonitor
import com.typesafe.config.ConfigFactory

/**
 * Created by srichardson on 4/25/15.
 */

class EntropyServerKernel extends Bootable {

  override def startup(): Unit = { EntropyServerSystem.start() }

  override def shutdown(): Unit = { EntropyServerSystem.stop() }
}

object EntropyServerSystem {
  private var system: ActorSystem = _
  
  def start(): Unit = {
    if (system == null) {
      system = ActorSystem("entropy-system", ConfigFactory.load().getConfig("entropy-server"))
      system.actorOf(Props(new EntropyServer), "Entropyserver")
      system.actorOf(Props(new MailBoxPerformanceMonitor), "Monitor")
    }
  }

  def stop(): Unit = {
    if (system != null) system.awaitTermination()
  }
}
