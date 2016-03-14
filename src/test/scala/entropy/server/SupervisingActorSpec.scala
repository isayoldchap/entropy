package com.sjr.entropy.core.server

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._

/**
 * Created by srichardson on 4/6/15.
 */

//class SupervisingActorSpec (system: ActorSystem) extends TestKit(system) with ImplicitSender
//with WordSpecLike with Matchers with BeforeAndAfterAll {
//
//  def this() = this(ActorSystem("SupervisingActorSpec"))
//
//  override def afterAll {
//    TestKit.shutdownActorSystem(system)
//  }
//
//  "An entropy server" must {
//    "send back messages unchanged" in {
//      val server = system.actorOf(Props[SupervisingActor])
//      server ! "hello world"
//      expectMsg(30.seconds, "hello world")
//    }
//
//  }
//}
