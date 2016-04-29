package com.sjr.entropy.core

/**
 * Created by srichardson on 4/29/16.
 */
sealed trait Role {
  def playMove(game: EntropyGame, move: EntropyMove): MoveResult

  def alternate: Role
}

object Order extends Role {
  override def alternate: Role = Chaos

  override def playMove(game: EntropyGame, move: EntropyMove): MoveResult = move match {
    case PassMove => game.skipOrderMove(); ValidMoveResult
    case orderMove: OrderMove => game.playOrderMove(orderMove)
    case _ => IllegalMoveResult("Unsupported move")
  }
}

object Chaos extends Role {
  override def alternate: Role = Order

  override def playMove(game: EntropyGame, move: EntropyMove): MoveResult = move match {
    case chaosMove: ChaosMove => game.playChaosMove(chaosMove)
    case _ => IllegalMoveResult("Unsupported move")
  }
}
