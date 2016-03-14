package com.sjr.entropy.core

import org.scalatest.{FunSpec, Matchers}

import scala.util.Random

/**
 * Created by stevenrichardson on 1/23/15.
 */

class EntropyGameSpec extends FunSpec with Matchers {
  val random = new Random

  describe("An Entropy game") {
    it("Should init to an empty board") {
      val entropyGame = EntropyGame(EntropyStyle.Normal)
      entropyGame.isEmpty shouldBe true
    }

    it("Should enforce legal moves") {
      val entropyGame = EntropyGame(EntropyStyle.Normal)
      val validChaosMove = entropyGame.playMove(ChaosMove(Point(1, 1)))
      validChaosMove shouldBe ValidMoveResult

      val illegalMove = entropyGame.playMove(ChaosMove(Point(1, 1)))
      illegalMove.getClass shouldBe classOf[IllegalMoveResult]

      val validOrderMove = entropyGame.playMove(OrderMove(Point(1, 1), Point(1, 5)))
      validOrderMove shouldBe ValidMoveResult
    }

    it("A normal entropy game should have 25 rounds") {
      val entropyGame = EntropyGame(EntropyStyle.Normal)
      (1 to 25).foreach { turn =>
        entropyGame.gameOver shouldBe false
        entropyGame.currentPiece.foreach { piece =>
          val possibleChaosMoves = entropyGame.legalChaosMoves
          possibleChaosMoves.size > 0 shouldBe true

          val randomChaosMove = possibleChaosMoves(random.nextInt(possibleChaosMoves.size))
          entropyGame.playMove(ChaosMove(randomChaosMove))

          val legalOrderMoves = entropyGame.legalOrderMoves
          if (turn < 25) {
            legalOrderMoves.size > 0 shouldBe true
            val randomOrderMove = legalOrderMoves(random.nextInt(legalOrderMoves.size))
            entropyGame.playMove(randomOrderMove)
          }
        }
      }
      entropyGame.isFull shouldBe true
      entropyGame.gameOver shouldBe true
    }
  }

}
