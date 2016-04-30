package com.sjr.entropy.core.game

import com.sjr.entropy.core._

/**
 * Created by srichardson on 4/29/16.
 */

//object Move {
//
//  def createOrderMove(source: Point, destination: Point): Move = {
//    Move(Some(source), Some(destination))
//  }
//
//  def createChaosMove(point: Point): Move = Move(source = Some(point), destination = None)
//
//  def createPassMove = Move(None, None)
//}
//
//
//case class Move(source: Op[Point], destination: Op[Point] = None) {
//
//  def isOrderMove: Boolean = source.isDefined && destination.isDefined
//
//  def isPass: Boolean = source.isEmpty && destination.isEmpty
//
//  def isChaosMove: Boolean = source.isDefined && destination.isEmpty
//
//}

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
