package com.sjr.entropy.core

import com.sjr.entropy.core.game._

/**
 * Created by stevenrichardson on 1/24/15.
 */
object CommandLineEntropy extends App {
  val letterMappings = Seq("A","B","C","D","E", "F", "G").zipWithIndex.map{
    case (letter, index) => (letter, index + 1)
  }.toMap

  val ChaosMoveRegex = """([A-E])([1-5])""".r
  val OrderMoveRegex = """([A-E])([1-5])-([A-E])([1-5])""".r
  val game = EntropyGame(EntropyStyle.Normal)

  game.playMove(game.randomLegalChaosMove)
  game.displayBoard

  for (command <- io.Source.stdin.getLines) {
    command.toUpperCase match {
      case "PASS" =>
        game.playMove(PassMove)  match {
          case ValidMoveResult =>
            doComputerTurn()
          case IllegalMoveResult(x) =>
            println("Attempted Move was illegal")
        }
      case ChaosMoveRegex(x, y) =>
        println(s"Chaos move was $x$y")
        game.playMove(ChaosMove(makePoint(x,y)))
      case OrderMoveRegex(x1, y1, x2, y2) =>
        println(s"Order move was $x1$y1-$x2$y2")
        game.playMove(OrderMove(makePoint(x1,y1),makePoint(x2,y2))) match {
          case ValidMoveResult =>
            doComputerTurn()
          case IllegalMoveResult(x) =>
            println("Attempted Move was illegal")
        }

      case _ => println("Unrecognized command")
    }
    game.displayBoard
    if ( game.gameOver) {
      println(s"Score was ${game.score}")
      game.reset()
    }
  }

  def doComputerTurn() {
    game.displayBoard
    println("Next color is " + game.currentPiece.get)
    Thread.sleep(1000)
    game.playMove(game.randomLegalChaosMove)
  }

  def makePoint(x:String, y:String): Point = Point(letterMappings(x), y.toInt)

}
