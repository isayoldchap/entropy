package com.sjr.entropy.core.game

import com.sjr.entropy.core._

/**
 * Created by srichardson on 4/29/16.
 */
sealed trait Role {
  def playMove(game: EntropyGame, move: EntropyMove): MoveResult = {
    if (move.isValidForRole(this)) move.play(game)
    else IllegalMoveResult(s"Invalid move ${move} for current role $this")
  }

  def alternate: Role
}

object Order extends Role {
  override def alternate: Role = Chaos
}

object Chaos extends Role {
  override def alternate: Role = Order
}
