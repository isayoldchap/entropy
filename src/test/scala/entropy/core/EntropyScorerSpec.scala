package com.sjr.entropy.core

import com.sjr.entropy.core.game.EntropyBoard
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by stevenrichardson on 1/23/15.
 */

class EntropyScorerSpec extends FunSpec with Matchers {
  describe("An entropy scorer" ){
    describe("Single pattern scoring") {
      it ("should handle really long palindromes") {
        (new EntropyScorer().scoreSinglePattern("X" * 100000000) > 0) shouldBe true
      }

      it("Should score single patterns based on their length if they are a palindrome") {
        new EntropyScorer().scoreSinglePattern("XOXOY") shouldBe 0
        new EntropyScorer().scoreSinglePattern("XOXOX") shouldBe 5

      }

      it("Should consider two to be the shortest scoring palindrome") {
        new EntropyScorer().scoreSinglePattern("XX") shouldBe 2
        new EntropyScorer().scoreSinglePattern("X") shouldBe 0
      }
    }

    describe("Combination scoring") {
      it("Should find multiple scoring patterns inside a larger pattern") {
        new EntropyScorer().scoreCombination("XXXXX") shouldBe 30
        new EntropyScorer().scoreCombination("XXXX") shouldBe 16
        new EntropyScorer().scoreCombination("XXX") shouldBe 7
        new EntropyScorer().scoreCombination("YXXY") shouldBe 6
        new EntropyScorer().scoreCombination(" XXX ") shouldBe 7

      }
    }

    describe("Board scoring") {
      it("Should score an empty board as zero points") {
        new EntropyScorer().score(new EntropyBoard(new Grid[GameTile]())) shouldBe 0
      }
    }
  }
}
