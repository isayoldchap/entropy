package com.sjr.entropy.core.server

import akka.actor.Actor

/**
 * Created by stevenrichardson on 2/1/15.
 */
class RoomActor extends Actor {
  override def receive: Receive = {
    case _ => // handle leave room
  }
}
