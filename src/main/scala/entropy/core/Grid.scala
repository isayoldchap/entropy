package com.sjr.entropy.core

import scala.collection.mutable.ArrayBuffer


/**
 * Created by stevenrichardson on 3/29/14.
 */

object Grid extends App {
  def apply[T](sideLength: Int): Grid[T] = Grid(sideLength, sideLength)

  def apply[T](width: Int, height: Int): Grid[T] = new Grid[T](width, height)
}

// should grid be immutable? Each move could produce a new grid - more functional style
class Grid[T](val cols: Int = 5, val rows: Int = 5) {
  type Cell = Op[T]
  type GridSegment = Seq[Cell]
  private val cellCount = rows * cols
  private val emptyCell: Cell = None
  private val cells: ArrayBuffer[Cell] = new ArrayBuffer(cellCount)
  initBoard()

  def allColumnContents: Seq[GridSegment] = (1 to cols).map(columnContents)

  def allRowContents: Seq[GridSegment] = (1 to rows).map(rowContents)

  def initBoard(): Unit = (0 until cellCount).foreach(cells.insert(_, emptyCell))

  def movePiece(source: Point, destination: Point): Cell =
    get(source).map { tile =>
      clear(source)
      put(destination, tile)
      tile
    }

  def clear(): Unit = (0 until cells.size).foreach { cells.update(_, emptyCell) }

  def clear(point: Point): Unit = {
    assertCellWithinBounds(point)
    cells.update(indexOfCell(point), emptyCell)
  }

  def put(point: Point, value: T): Unit = {
    assertCellWithinBounds(point)
    cells.update(indexOfCell(point), Some(value))
  }

  def get(point: Point): Cell = {
    assertCellWithinBounds(point)
    valueAt(point)
  }

  def rowContents(row: Int): GridSegment = {
    assert(rowsRange.contains(row))
    (1 to cols).foldLeft[GridSegment](Seq.empty)((contents, col) => {
      contents :+ valueAt(Point(col, row))
    })
  }

  def columnContents(col: Int): GridSegment = {
    assert(columnRange.contains(col))
    (1 to rows).foldLeft[GridSegment](Seq.empty)((contents, row) => {
      contents :+ valueAt(Point(col, row))
    })
  }

  def isOccupied(point: Point): Boolean = valueAt(point).isDefined

  def isVacantAt(point: Point): Boolean = valueAt(point).isEmpty

  def valueAt(point: Point): Cell = cells(indexOfCell(point))

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

  private def indexOfCell(point: Point): Int = {
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
