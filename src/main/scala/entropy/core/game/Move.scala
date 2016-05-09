package com.sjr.entropy.core.game

import com.sjr.entropy.core._

/**
 * Created by srichardson on 4/29/16.
 */

sealed trait EntropyMove {
  def isValidForRole(role: Role): Boolean

  def play(game: EntropyGame): MoveResult
}

case class OrderMove(source: Point, destination: Point) extends EntropyMove {

  override def isValidForRole(role: Role): Boolean = role == Order

  override def play(game: EntropyGame): MoveResult = game.playOrderMove(this)
}

case object PassMove extends EntropyMove {

  override def isValidForRole(role: Role): Boolean = role == Order

  override def play(game: EntropyGame): MoveResult =  game.playPassMove()
}

case class ChaosMove(location: Point) extends EntropyMove {

  override def isValidForRole(role: Role): Boolean = role == Chaos

  override def play(game: EntropyGame): MoveResult =  game.playChaosMove(this)
}

sealed trait MoveResult

object ValidMoveResult extends MoveResult

case class IllegalMoveResult(description: String) extends MoveResult
