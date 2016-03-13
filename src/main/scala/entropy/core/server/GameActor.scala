package com.sjr.entropy.core.server

import akka.actor.Actor
import com.sjr.entropy.core.{EntropyGame, EntropyMove, EntropyStyle}

/**
 * Created by stevenrichardson on 2/7/15.
 */

class GameActor extends Actor {

  var game = EntropyGame(EntropyStyle.Normal)

  override def receive: Receive = {
    case move: EntropyMove => sender() ! game.playMove(move)
    case x: Any =>
  }
}
