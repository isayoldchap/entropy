package com.sjr.entropy.core.game

/**
 * Created by stevenrichardson on 3/31/14.
 */

import com.sjr.entropy.core._

import scala.util.Random

object EntropyStyle extends Enumeration {
  val Normal = Value(5, "Normal")
  val Large = Value(7, "Large")
}

object EntropyGame extends App {
  def apply(tiles: Set[GameTile]): EntropyGame = new EntropyGame(tiles)

  def apply(gameStyle: EntropyStyle.Value): EntropyGame = gameStyle match {
    case EntropyStyle.Normal => new EntropyGame(TilesForNormalGame)
    case EntropyStyle.Large => new EntropyGame(TilesForLargeGame)
  }
}

class EntropyGame(uniqueGameTiles: Set[GameTile]) {
  assert(uniqueGameTiles.size > 3)

  val random = new Random
  val board = EntropyBoard(uniqueGameTiles.size)
  val bag = setupBag(uniqueGameTiles)

  private var currentRole: Role = Chaos
  private var nextTile: Option[GameTile] = bag.takeNext()

  def reset() = {
    bag.clear()
    (1 to uniqueGameTiles.size).foreach(x => {uniqueGameTiles.foreach(bag.add)})
    bag.shuffle()
    board.clear()
    drawNextTile()
    currentRole = Chaos
  }

  def score: Int = new EntropyScorer().score(board)

  def currentPiece:Option[GameTile] = nextTile

  def isFull: Boolean = board.isFull

  def isEmpty: Boolean = board.isEmpty

  def playRandomMove(): MoveResult = playMove(randomMove)

  def randomMove: EntropyMove = currentRole.randomMove(this)

  def randomOrderMove: OrderMove = legalOrderMoves(random.nextInt(legalOrderMoves.size))

  def randomChaosMove: ChaosMove = ChaosMove(legalChaosMoves(random.nextInt(legalChaosMoves.size)))

  def legalOrderMoves: Seq[OrderMove] = if (currentRole == Order) board.allPossibleOrderMoves else Seq.empty

  def legalChaosMoves: Seq[Point] = if (currentRole == Chaos) board.allPossibleChaosMoves else Seq.empty

  def gameOver: Boolean = board.isFull

  def displayBoard: Unit = {
    println("ABCDE")
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
    } else IllegalMoveResult(s"Order Move ${orderMove} was invalid")
  }

  private[game] def playChaosMove(chaosMove: ChaosMove): MoveResult = {
    if (legalChaosMoves.contains(chaosMove.location)) {
      board.placePiece(chaosMove.location, currentPiece.get)
      nextTile = None
      switchRole()
      ValidMoveResult
    } else IllegalMoveResult(s"Chaos Move ${chaosMove} was invalid")

  }

  private def drawNextTile(): Unit = nextTile = bag.takeNext()

  private def switchRole(): Unit = currentRole = currentRole.alternate

  private def setupBag(uniqueGameTiles: Set[GameTile]): Bag[GameTile] = {
    val bag = Bag[GameTile]()
    (1 to uniqueGameTiles.size).foreach(x => {
      uniqueGameTiles.foreach(bag.add)
    })
    bag.shuffle()
  }
}