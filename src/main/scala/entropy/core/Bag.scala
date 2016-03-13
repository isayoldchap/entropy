package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 3/31/14.
 */

object Bag {
  def apply[T](initialContents: Seq[T] = Seq.empty) = new Bag[T](initialContents)
}

class Bag[T](initialContents: Seq[T]) {
  import scala.util._

  private var contents = shuffle(initialContents)

  override def toString: String = contents.toString

  def clear(): Unit = contents = Seq.empty

  def takeNext(): Option[T] = synchronized {
    val item = contents.lastOption
    if (item.isDefined) contents = contents.dropRight(1)
    item
  }

  def add(item: T): T = synchronized {
    contents = contents :+ item
    item
  }

  def shuffle(): Bag[T] = synchronized {
    contents = shuffle(contents)
    this
  }

  private def shuffle(items: Seq[T]): Seq[T] = Random.shuffle(items)

}
