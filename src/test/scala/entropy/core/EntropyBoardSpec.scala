package com.sjr.entropy.core

import org.scalatest.{FunSpec, Matchers}

/**
 * Created by stevenrichardson on 4/7/14.
 */
class EntropyBoardSpec extends FunSpec with Matchers {

  describe( "An entropy board" ){
    it ("should know what moves are legal for order and chaos" ){
      val entropyBoard = EntropyBoard(5)
      entropyBoard.placePiece(Point(A,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(B,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(C,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(D,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(E,1), GameTile.RedTile)
      entropyBoard.allPossibleChaosMoves.size shouldBe 20
      entropyBoard.allPossibleOrderMoves.size shouldBe 20
    }

    it ("A trapped piece should have no moves") {
      val entropyBoard = EntropyBoard(5)
      entropyBoard.placePiece(Point(A,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(B,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(A,2), GameTile.RedTile)
      entropyBoard.allMovesFromSource(Point(A,1)).size shouldBe 0
    }

    it ("A piece trapped on one side can move on only one axis") {
      val entropyBoard = EntropyBoard(5)
      entropyBoard.placePiece(Point(A,1), GameTile.RedTile)
      entropyBoard.placePiece(Point(B,1), GameTile.RedTile)
      entropyBoard.allMovesFromSource(Point(A,1)).size shouldBe 4
    }
  }

}
