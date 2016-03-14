package com.sjr.entropy.core

import org.scalatest.{FunSpec, Matchers}

/**
 * Created by stevenrichardson on 3/31/14.
 */
class BagSpec extends FunSpec with Matchers{
    describe("A bag") {
      it ("should dispense items until it is empty") {
        val bag = new Bag((1 to 5).toSeq)
        (1 to 5).foreach(x => bag.takeNext.isDefined should be(true))
      }

      it ("should dispense items in a different order than they went in") {
        val bag = new Bag((1 to 50).toSeq)
        val obtainedItems = (1 to 50).map(x => bag.takeNext.get).foldLeft(Seq.empty[Int])((items, item) => items :+ item)
        (1 to 50).sameElements(obtainedItems) should be(false)
      }
    }
}
