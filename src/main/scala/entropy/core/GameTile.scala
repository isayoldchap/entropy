package com.sjr.entropy.core

/**
 * Created by stevenrichardson on 3/31/14.
 */

object Color {
  val Red = Color("!Red")
  val Blue = Color("@Blue")
  val Yellow = Color("#Yellow")
  val Green = Color("$Green")
  val White = Color("%White")
  val Orange = Color("^Orange")
  val Silver = Color("&B1Silver")
}

case class Color(description:String)

case class GameTile(color: Color){
  override def toString: String = "" + color.description.head
}

object GameTile {
  val RedTile = GameTile(Color.Red)
  val BlueTile = GameTile(Color.Blue)
  val YellowTile = GameTile(Color.Yellow)
  val GreenTile = GameTile(Color.Green)
  val WhiteTile = GameTile(Color.White)
  val OrangeTile = GameTile(Color.Orange)
  val SilverTile = GameTile(Color.Silver)
}


