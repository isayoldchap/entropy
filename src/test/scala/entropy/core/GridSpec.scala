package com.sjr.entropy.core

import org.scalatest.{FunSpec, Matchers}

import scala.util.Try

/**
 * Created by stevenrichardson on 3/29/14.
 */
class GridSpec extends FunSpec with Matchers {

  describe("A Grid") {
    it("Should know its bounds") {
      val grid = Grid[Int](5, 5)
      grid.containsCell(Point(5, 1)) should be(true)
      grid.containsCell(Point(5, 5)) should be(true)

      grid.containsCell(Point(0, 0)) should be(false)
      grid.containsCell(Point(6, 0)) should be(false)
      grid.containsCell(Point(6, 6)) should be(false)
    }

    it("should know if a location is occupied") {
      val grid = Grid[Int](3, 3)
      grid.isOccupied(Point(1, 1)) should be(false)
    }

    it("should know all occupied points") {
      val grid = Grid[Int](5, 1) //one row with 5 columns
      grid.put(Point(1, 1), 99)
      grid.occupiedSquares.size should be(1)
      grid.occupiedSquares.contains(Point(1, 1))
      grid.vacantSquares.size should be(4)
    }

    it("should allow itself to be populated") {
      val grid = Grid[Int](5, 5)
      var x = 1
      for (row <- 1 to grid.rows) {
        for (col <- 1 to grid.cols) {
          grid.put(Point(col, row), x)
          grid.get(Point(col, row)) should be(Some(x))
          x += 1
        }
      }
    }

    it("should reject access when out of bounds") {
      val grid = Grid[Int](5, 5)
      expectFailure(() => grid.get(Point(6, 1)))
      expectFailure(() => grid.get(Point(1, 6)))
    }

    it("should allow access outside of bounds") {
      val grid = Grid[Int](5, 5)
      (1 to 5).foreach(x => expectSuccess(() => grid.get(Point(x, x))))
    }

    it("should be able to provide its columns and rows as sequences") {
      val grid = Grid[Int](5, 5)
      var x = 1
      for (row <- 1 to grid.rows) {
        for (col <- 1 to grid.cols) {
          grid.put(Point(col, row), x)
          x += 1
        }
      }
      grid.rowContents(1).flatten should be((1 to 5).toSeq)
      grid.rowContents(5).flatten should be((21 to 25).toSeq)
      grid.columnContents(1).flatten should be(Seq(1, 6, 11, 16, 21))
      grid.columnContents(5).flatten should be(Seq(5, 10, 15, 20, 25))
    }
  }

  def expectFailure(f: () => Unit): Unit = {
    val result = Try(f())
    result.isFailure should be(true)
  }

  def expectSuccess(f: () => Unit): Unit = {
    val result = Try(f())
    result.isSuccess should be(true)
  }
}
