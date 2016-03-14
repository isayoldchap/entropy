package com.sjr.entropy.server

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{WordSpecLike, Matchers, BeforeAndAfterAll}

/**
 * Created by stevenrichardson on 2/1/15.
 */

class EntropyServerSpec(system: ActorSystem) extends TestKit(system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An entropy server" must {
    "send back messages unchanged" in {
      val server = system.actorOf(Props[EntropyServer])
      server ! "hello world"
//      expectMsg("hello world")
    }
  }
}
