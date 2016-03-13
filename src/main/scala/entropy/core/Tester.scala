package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 3/12/15.
 */

object Tester extends App {
  println(removeDuplicates(Seq[String]("a","a","b","c", "d", "d", "e", "b", "b")))

  def removeDuplicates[A](items: Seq[A]): Seq[A] = items.foldLeft(IndexedSeq.empty[A])((all, each) =>
    if (all.lastOption.exists(_ == each)) all else all :+ each)
}
