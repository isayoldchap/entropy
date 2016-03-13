package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 4/4/14.
 */

case class Point(x: Int, y: Int) {
  def withinBounds(grid: Grid[_]) = grid.containsCell(this)

  def applyDirection(direction: Direction) = Point(x + direction.deltaX, y + direction.deltaY)

}

case class Direction(deltaX: Int, deltaY: Int)

object Directions {
  val North = Direction(0, -1)
  val South = Direction(0, 1)
  val East = Direction(1, 0)
  val West = Direction(-1, 0)

  def orthogonal: Seq[Direction] = Seq(North, East, South, West)
}
