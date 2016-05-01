package com.sjr.entropy.core

import com.sjr.entropy.core.game.EntropyBoard
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by stevenrichardson on 1/23/15.
 */

class EntropyScorerSpec extends FunSpec with Matchers {

  private def scoreSinglePattern(pattern: String): Int = new EntropyScorer().scoreSinglePattern(pattern)

  private def scoreCombination(pattern: String): Int = new EntropyScorer().scoreCombination(pattern)

  describe("An entropy scorer" ){
    describe("Single pattern scoring") {
      it ("should handle really long palindromes") {
        (scoreSinglePattern("X" * 100000000) > 0) shouldBe true
      }

      it("Should score single patterns based on their length if they are a palindrome") {
        scoreSinglePattern("XOXOY") shouldBe 0
        scoreSinglePattern("XOXOX") shouldBe 5

      }

      it("Should consider two to be the shortest scoring palindrome") {
        scoreSinglePattern("XX") shouldBe 2
        scoreSinglePattern("X") shouldBe 0
      }
    }

    describe("Combination scoring") {
      it("Should find multiple scoring patterns inside a larger pattern") {
        scoreCombination("XXXXX") shouldBe 30
        scoreCombination("XXXX") shouldBe 16
        scoreCombination("XXX") shouldBe 7
        scoreCombination("YXXY") shouldBe 6
        scoreCombination(" XXX ") shouldBe 7

      }
    }

    describe("Board scoring") {
      it("Should score an empty board as zero points") {
        new EntropyScorer().score(new EntropyBoard(new Grid[GameTile]())) shouldBe 0
      }
    }
  }
}
