package com.sjr.entropy.server

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.sjr.entropy.core._
import com.sjr.entropy.core.game._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
 * Created by stevenrichardson on 2/7/15.
 */
class GameActorSpec(system: ActorSystem) extends TestKit(system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A game actor" must {
    "accept entropy moves" in {
      val game = system.actorOf(Props[GameActor])
      game ! ChaosMove(Point(3, 3))
      expectMsg(ValidMoveResult)
      game ! ChaosMove(Point(3, 3))
      expectMsgClass(classOf[IllegalMoveResult])
      game ! OrderMove(Point(3, 3), Point(4, 3))
      expectMsg(ValidMoveResult)
    }
  }
}
