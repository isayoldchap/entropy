package com.sjr.entropy

import com.sjr.entropy.core._

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

  type Op[T] = Option[T]

  type Fu[T] = Future[T]

//  type Move = (Point, Point) //from, to

  implicit def extendedString(s: String) = new StringExt(s)

  sealed trait MoveResult

  object ValidMoveResult extends MoveResult

  case class IllegalMoveResult(description: String) extends MoveResult

  import com.sjr.entropy.core.GameTile._

  val TilesForTinyGame: Set[GameTile] = Set(RedTile, GreenTile, OrangeTile)
  val TilesForNormalGame: Set[GameTile] = TilesForTinyGame ++ Set(BlueTile, WhiteTile)
  val TilesForLargeGame: Set[GameTile]  = TilesForNormalGame ++ Set(SilverTile, YellowTile)
}
