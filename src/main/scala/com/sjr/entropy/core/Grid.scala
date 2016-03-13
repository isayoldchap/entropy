package com.sjr.entropy.core

import scala.collection.mutable.ArrayBuffer


/**
 * Created by stevenrichardson on 3/29/14.
 */

object Grid extends App {
  def apply[T](sideLength: Int): Grid[T] = Grid(sideLength, sideLength)

  def apply[T](width: Int, height: Int): Grid[T] = new Grid[T](width, height)
}

class Grid[T](val cols: Int = 5, val rows: Int = 5) {
  type optionOfT = Option[T]


  private val cells: ArrayBuffer[optionOfT] = new ArrayBuffer(cols * rows)
  initBoard()

  def allColumnContents: Seq[Seq[optionOfT]] = (1 to cols).map(columnContents)

  def allRowContents: Seq[Seq[optionOfT]] = (1 to rows).map(rowContents)

  def initBoard(): Unit = (0 until cols * rows).foreach(cells.insert(_, None))

  def movePiece(source: Point, destination: Point): Option[T] =
    get(source).map { tile =>
      clear(source)
      put(destination, tile)
      tile
    }

  def clear(): Unit = (0 until cells.size).foreach {
    cells.update(_, None)
  }

  def clear(point: Point): Unit = {
    assertCellWithinBounds(point)
    cells.update(cellIndex(point), None)
  }

  def put(point: Point, value: T): Unit = {
    assertCellWithinBounds(point)
    cells.update(cellIndex(point), Some(value))
  }

  def get(point: Point): optionOfT = {
    assertCellWithinBounds(point)
    valueAt(point)
  }

  def rowContents(row: Int): Seq[optionOfT] = {
    assert(rowsRange.contains(row))
    (1 to cols).foldLeft[Seq[optionOfT]](Seq.empty)((contents, col) => {
      contents :+ valueAt(Point(col, row))
    })
  }

  def columnContents(col: Int): Seq[optionOfT] = {
    assert(columnRange.contains(col))
    (1 to rows).foldLeft[Seq[optionOfT]](Seq.empty)((contents, row) => {
      contents :+ valueAt(Point(col, row))
    })
  }

  def isOccupied(point: Point): Boolean = valueAt(point).isDefined

  def isVacantAt(point: Point): Boolean = valueAt(point).isEmpty

  def valueAt(point: Point): optionOfT = cells(cellIndex(point))

  def containsCell(point: Point): Boolean = columnRange.contains(point.x) && rowsRange.contains(point.y)

  def vacantSquares: Seq[Point] = allPoints.filter(isVacantAt)

  def occupiedSquares: Seq[Point] = allPoints.filter(isOccupied)

  def isFull: Boolean = vacantSquares.isEmpty

  def isEmpty: Boolean = cells.forall(_ == None)

  private def allPoints: Seq[Point] = (0 until cols * rows).map(pointAtCellIndex)

  private def columnRange = 1 to cols

  private def rowsRange = 1 to rows

  private def assertCellWithinBounds(point: Point): Unit = assert(containsCell(point))

  private def pointAtCellIndex(cellIndex: Int): Point = {
    val rowIndex = (cellIndex / cols) + 1
    val colIndex = (cellIndex % cols) + 1
    Point(colIndex, rowIndex)
  }

  private def cellIndex(point: Point): Int = {
    val index: Int = (point.x + (point.y - 1) * cols) - 1
    index
  }

  override def toString: String = {
    val boardRepresentation = new StringBuilder
    var index: Int = 1
    (1 to rows).map(rowContents).foreach { row =>
      row.foreach { cell =>
        boardRepresentation.append(cell.map(_.toString).getOrElse("."))
      }
      boardRepresentation.append(s" $index")
      boardRepresentation.append("\n")
      index += 1
    }
    boardRepresentation.toString
  }
}
