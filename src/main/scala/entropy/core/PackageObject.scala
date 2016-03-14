package com.sjr.entropy.core

import scala.concurrent.Future

/**
 * This software copyright FlexTrade, Inc.
 *
 * @author srichardson
 *
 *         Date: 3/13/16
 */

trait PackageObject {
  val A = 1
  val B = 2
  val C = 3
  val D = 4
  val E = 5
  val F = 6
  val G = 7

  sealed trait Role

  type Op[T] = Option[T]

  type Fu[T] = Future[T]

  object Order extends Role

  object Chaos extends Role

  type Move = (Point, Point) //from, to

  implicit def extendedString(s: String) = new StringExt(s)

  sealed trait MoveResult

  object ValidMoveResult extends MoveResult

  case class IllegalMoveResult(description: String) extends MoveResult

  sealed trait EntropyMove

  case class OrderMove(source: Point, destination: Point) extends EntropyMove

  case object PassMove extends EntropyMove

  case class ChaosMove(placement: Point) extends EntropyMove


  import com.sjr.entropy.core.GameTile._

  val TilesForNormalGame: Set[GameTile] = Set(RedTile, GreenTile, OrangeTile, BlueTile, WhiteTile)
  val TilesForLargeGame: Set[GameTile]  = TilesForNormalGame ++ Set(SilverTile, YellowTile)
}
