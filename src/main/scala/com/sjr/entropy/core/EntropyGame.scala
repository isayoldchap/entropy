package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 3/31/14.
 */

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
  val board = new EntropyBoard(Grid[GameTile](uniqueGameTiles.size))
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

  def playMove(move: EntropyMove): MoveResult = move match {
    case orderMove: OrderMove if currentRole == Order => playOrderMove(orderMove)
    case PassMove if currentRole == Order => skipOrderMove()
    case chaosMove: ChaosMove if currentRole == Chaos => playChaosMove(chaosMove)
    case _ => IllegalMove(s"It is currently ${currentRole}'s turn")
  }
  def randomLegalChaosMove: ChaosMove = ChaosMove(legalChaosMoves(random.nextInt(legalChaosMoves.size)))

  def legalOrderMoves: Seq[EntropyMove] = if (currentRole == Order) board.allPossibleOrderMoves else Seq.empty

  def legalChaosMoves: Seq[Point] = if (currentRole == Chaos) board.allPossibleChaosMoves else Seq.empty

  def gameOver: Boolean = board.isFull

  def displayBoard: Unit = {
    println("ABCDE")
    println("-----")
    println(board.toString)
  }

  private def skipOrderMove(): MoveResult = {
    drawNextTile()
    switchRole()
    LegalMove
  }

  private def playOrderMove(orderMove: OrderMove): MoveResult = {
    if (legalOrderMoves.contains(orderMove)) {
      board.movePiece(orderMove.source, orderMove.destination)
      drawNextTile()
      switchRole()
      LegalMove
    } else IllegalMove(s"Order Move ${orderMove} was invalid")
  }

  private def playChaosMove(chaosMove: ChaosMove): MoveResult = {
    if (legalChaosMoves.contains(chaosMove.placement)) {
      board.placePiece(chaosMove.placement, currentPiece.get)
      nextTile = None
      switchRole()
      LegalMove
    } else IllegalMove(s"Chaos Move ${chaosMove} was invalid")

  }

  private def drawNextTile(): Unit = nextTile = bag.takeNext()

  private def switchRole(): Unit = currentRole = if (currentRole == Order) Chaos else Order

  private def setupBag(uniqueGameTiles: Set[GameTile]): Bag[GameTile] = {
    val bag = Bag[GameTile]()
    (1 to uniqueGameTiles.size).foreach(x => {
      uniqueGameTiles.foreach(bag.add)
    })
    bag.shuffle()
  }
}