package com.sjr.entropy.core.game

/**
 * Created by stevenrichardson on 3/31/14.
 */

import com.sjr.entropy.core._
import scala.util.Random

object EntropyStyle extends Enumeration {
  val Tiny = Value(3, "Tiny")
  val Normal = Value(5, "Normal")
  val Large = Value(7, "Large")
}

object EntropyGame extends App {
  def apply(tiles: Set[GameTile]): EntropyGame = new EntropyGame(tiles)

  def apply(gameStyle: EntropyStyle.Value): EntropyGame = gameStyle match {
    case EntropyStyle.Tiny => new EntropyGame(TilesForTinyGame)
    case EntropyStyle.Normal => new EntropyGame(TilesForNormalGame)
    case EntropyStyle.Large => new EntropyGame(TilesForLargeGame)
  }
}

class EntropyGame(uniqueGameTiles: Set[GameTile]) {
  assert(uniqueGameTiles.size > 2)

  private var currentRole: Role = Chaos
  private val board = EntropyBoard(uniqueGameTiles.size)
  private val bag = setupBag(uniqueGameTiles)
  private var nextTile: Option[GameTile] = bag.takeNext()
  private val random = new Random

  def rows: Int = board.rows

  def cols: Int = board.columns

  def reset() = {
    bag.clear()
    (1 to uniqueGameTiles.size).foreach(x => uniqueGameTiles.foreach(bag.add))
    bag.shuffle()
    board.clear()
    drawNextTile()
    currentRole = Chaos
  }

  def score: Int = new EntropyScorer().score(board)

  def nextGameTile: Option[GameTile] = nextTile

  def isFull: Boolean = board.isFull

  def isEmpty: Boolean = board.isEmpty

  def playRandomMove(): MoveResult = randomMove.map(playMove).getOrElse(IllegalMoveResult(""))

  def randomMove: Op[EntropyMove] = currentRole.randomMove(this)

  def randomOrderMove: Op[OrderMove] = {
    val allOrderMoves = legalOrderMoves
    allOrderMoves.nonEmpty.toOption.map(_ => allOrderMoves(random.nextInt(allOrderMoves.size)))
  }

  def randomChaosMove: Op[ChaosMove] = {
    val allChaosMoves = legalChaosMoves
    allChaosMoves.nonEmpty.toOption.map(_ => ChaosMove(allChaosMoves(random.nextInt(allChaosMoves.size))))
  }

  def legalOrderMoves: Seq[OrderMove] = if (currentRole == Order) board.allPossibleOrderMoves else Seq.empty

  def legalChaosMoves: Seq[Point] = if (currentRole == Chaos) board.allPossibleChaosMoves else Seq.empty

  def gameOver: Boolean = board.isFull

  def displayBoard: Unit = {
    println(board.header)
    println(board.toString)
  }

  def playMove(move: EntropyMove): MoveResult = currentRole.playMove(this, move)

  private[game] def playPassMove(): MoveResult = {
    drawNextTile()
    switchRole()
    ValidMoveResult
  }

  private[game] def playOrderMove(orderMove: OrderMove): MoveResult = {
    if (legalOrderMoves.contains(orderMove)) {
      board.movePiece(orderMove.source, orderMove.destination)
      drawNextTile()
      switchRole()
      ValidMoveResult
    } else IllegalMoveResult(s"Order Move $orderMove was invalid")
  }

  private[game] def playChaosMove(chaosMove: ChaosMove): MoveResult = {
    if (legalChaosMoves.contains(chaosMove.location)) {
      board.placePiece(chaosMove.location, nextGameTile.get)
      nextTile = None
      switchRole()
      ValidMoveResult
    } else IllegalMoveResult(s"Chaos Move $chaosMove was invalid")
  }

  private def drawNextTile(): Unit = nextTile = bag.takeNext()

  private def switchRole(): Unit = currentRole = currentRole.alternate

  private def setupBag(uniqueGameTiles: Set[GameTile]): Bag[GameTile] = {
    val bag = Bag[GameTile]()
    (1 to uniqueGameTiles.size).foreach(_ => uniqueGameTiles.foreach(bag.add))
    bag.shuffle()
  }
}