package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 4/4/14.
 */

object EntropyBoard {
  def apply(size: Int): EntropyBoard = new EntropyBoard(Grid[GameTile](size))
}

class EntropyBoard(private val grid: Grid[GameTile]) {

  def clear() = grid.clear()

  def allPatterns: Seq[String] = {
    val allSegments: Seq[Seq[Option[GameTile]]] = grid.allColumnContents ++ grid.allRowContents
    allSegments.map(boardSegmentToPattern)
  }

  private def boardSegmentToPattern(segment: Seq[Option[GameTile]]): String = segment.map(_.getOrElse(" ")).mkString

  def movePiece(source: Point, destination: Point) = {
    if (allPossibleOrderMoves.contains(OrderMove(source, destination))) {
      grid.movePiece(source, destination)
    } else IllegalMoveResult(s"Moving from $source to $destination was not a legal move")
  }

  def placePiece(point: Point, gameTile: GameTile) = grid.put(point, gameTile)

  def isEmpty: Boolean = grid.isEmpty

  def isFull: Boolean = grid.isFull

  def allPossibleChaosMoves: Seq[Point] = vacantSquares

  def allMovesFromSource(source: Point): Seq[EntropyMove] = legalMovesFromSource(source)

  def allLegalDestinationsFromSource(source: Point): Seq[Point] = allPossibleOrderMoves.map {
    case OrderMove(source, destination) => destination
  }

  def allPossibleOrderMoves: Seq[OrderMove] = {
    grid.occupiedSquares.foldLeft(Seq.empty[OrderMove])((moves, sourcePoint) => {
      moves ++ legalMovesFromSource(sourcePoint)
    })
  }

  override def toString: String = grid.toString

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