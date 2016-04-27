package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 4/4/14.
 */

object EntropyBoard {
  def apply(size: Int): EntropyBoard = new EntropyBoard(Grid[GameTile](size))
}

class EntropyBoard(private val grid: Grid[GameTile]) {

  type EntropySegment = Seq[Op[GameTile]]

  def clear() = grid.clear()

  def allPatterns: Seq[String] = allSegments.map(boardSegmentToPattern)

  def movePiece(source: Point, destination: Point): Unit = {
    if (allPossibleOrderMoves.contains(OrderMove(source, destination))) {
      grid.movePiece(source, destination)
    } else IllegalMoveResult(s"Moving from $source to $destination was not a legal move")
  }

  def placePiece(point: Point, gameTile: GameTile): Unit = grid.put(point, gameTile)

  def isEmpty: Boolean = grid.isEmpty

  def isFull: Boolean = grid.isFull

  def allPossibleChaosMoves: Seq[Point] = vacantSquares

  def allMovesFromSource(source: Point): Seq[OrderMove] = legalMovesFromSource(source)

  def allLegalDestinationsFromSource(source: Point): Seq[Point] = allMovesFromSource(source).map(_.destination)

  def allPossibleOrderMoves: Seq[OrderMove] = {
    grid.occupiedSquares.foldLeft(Seq.empty[OrderMove])((moves, sourcePoint) => {
      moves ++ legalMovesFromSource(sourcePoint)
    })
  }

  override def toString: String = grid.toString

  private def allSegments: Seq[EntropySegment] = grid.allColumnContents ++ grid.allRowContents

  private def boardSegmentToPattern(segment: EntropySegment): String = segment.map(_.getOrElse(" ")).mkString

  private def legalMovesFromSource(source: Point): Seq[OrderMove] = {
    Directions.orthogonal.foldLeft(Seq.empty[OrderMove])((moves, direction) => {
      moves ++ computeAllMovesFromSourceInDirection(source, direction)
    })
  }

  private def computeAllMovesFromSourceInDirection(source: Point, direction: Direction): Seq[OrderMove] =
    legalMovesFromSourceInDirection(source, direction).takeWhile(canBeOccupiedAt).map(destination => OrderMove(source, destination))

  private def legalMovesFromSourceInDirection(source: Point, direction: Direction): Stream[Point] = {
    val nextPoint = source.applyDirection(direction)
    nextPoint #:: legalMovesFromSourceInDirection(nextPoint, direction)
  }

  private def canBeOccupiedAt(point: Point) = point.withinBounds(grid) && grid.isVacantAt(point)

  private def vacantSquares = grid.vacantSquares
}