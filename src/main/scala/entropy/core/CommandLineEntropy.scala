package com.sjr.entropy.core

import com.sjr.entropy.core.game._

/**
 * Created by stevenrichardson on 1/24/15.
 */

object CommandLineEntropy extends App {
  val letterMappings = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray.zipWithIndex.map {
    case (letter, index) => (letter.toString, index + 1)
  }.toMap

  val ChaosMoveRegex = """([A-E])([1-5])""".r
  val OrderMoveRegex = """([A-E])([1-5])-([A-E])([1-5])""".r
  val game = EntropyGame(EntropyStyle.Normal)

  game.playRandomMove()
  game.displayBoard

  for (command <- io.Source.stdin.getLines) {
    commandToMove(command) match {
      case Some(move) => game.playMove(move) match {
        case ValidMoveResult =>
          game.displayBoard
          doComputerTurn()
        case IllegalMoveResult(reason) => println(s"Attempted Move $move was illegal")
      }
      case None => println(s"Invalid command $command")
    }

    game.displayBoard
    if (game.gameOver) {
      println(s"Score was ${game.score}")
      game.reset()
    }
  }

  private def commandToMove(command: String): Op[EntropyMove] =
    command.toUpperCase match {
      case "PASS" => Some(PassMove)
      case ChaosMoveRegex(x, y) => Some(ChaosMove(makePoint(x, y)))
      case OrderMoveRegex(x1, y1, x2, y2) => Some(OrderMove(makePoint(x1, y1), makePoint(x2, y2)))
      case _ => None
    }

  private def doComputerTurn() {
    game.nextGameTile.foreach(next => println(s"Next color is $next\n"))
    Thread.sleep(1000)
    game.playRandomMove()
  }

  private def makePoint(x: String, y: String): Point = Point(letterMappings(x), y.toInt)
}
