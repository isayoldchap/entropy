package com.sjr.entropy.core

import com.sjr.entropy.core.game._
import entropy.core.NotationUtils

/**
 * Created by stevenrichardson on 1/24/15.
 */

object CommandLineEntropy extends App {
  private val game = initGame

  game.nextGameTile.foreach(next => println(s"Next tile is $next\n"))
  for (command <- io.Source.stdin.getLines) {
    NotationUtils.commandToMove(command) match {
      case Some(move) => game.playMove(move) match {
        case ValidMoveResult =>
          game.displayBoard
          doComputerTurn()
        case IllegalMoveResult(reason) => println(s"Attempted Move $move was illegal")
      }
      case None => println(s"Invalid command $command")
    }

    game.displayBoard
    game.nextGameTile.foreach(next => println(s"Next tile is $next\n"))
    if (game.gameOver) {
      println(s"Score was ${game.score}")
      game.reset()
      game.displayBoard
    }
  }

  private def initGame: EntropyGame = {
    val game = EntropyGame(EntropyStyle.Tiny)
//    game.playRandomMove()
    game.displayBoard
    game
  }

  private def doComputerTurn() {
    game.nextGameTile.foreach(next => println(s"Next tile is $next\n"))
    Thread.sleep(1000)
    game.playRandomMove()
  }
}
