package com.sjr.entropy

/**
 * Created by stevenrichardson on 1/18/15.
 */
package object core {
  val A = 1
  val B = 2
  val C = 3
  val D = 4
  val E = 5
  val F = 6
  val G = 7

  type Move = (Point, Point) //from, to

  implicit def extendedString(s: String) = new StringExt(s)

  sealed trait MoveResult

  object LegalMove extends MoveResult

  case class IllegalMove(description: String) extends MoveResult

  sealed trait EntropyMove

  case class OrderMove(source: Point, destination: Point) extends EntropyMove

  case object PassMove extends EntropyMove

  case class ChaosMove(placement: Point) extends EntropyMove

  trait Role

  object Order extends Role

  object Chaos extends Role


  import com.sjr.entropy.core.GameTile._

  val TilesForNormalGame: Set[GameTile] = Set(RedTile, GreenTile, OrangeTile, BlueTile, WhiteTile)
  val TilesForLargeGame: Set[GameTile]  = TilesForNormalGame ++ Set(SilverTile, YellowTile)

}
