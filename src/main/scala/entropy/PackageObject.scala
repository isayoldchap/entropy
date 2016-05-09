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

  implicit def extendedString(s: String) = new StringExt(s)
  implicit def extendedBoolean(b: Boolean) = new BooleanExt(b)



  import com.sjr.entropy.core.GameTile._

  val TilesForTinyGame: Set[GameTile] = Set(RedTile, GreenTile, OrangeTile)
  val TilesForNormalGame: Set[GameTile] = TilesForTinyGame ++ Set(BlueTile, WhiteTile)
  val TilesForLargeGame: Set[GameTile]  = TilesForNormalGame ++ Set(SilverTile, YellowTile)
}
