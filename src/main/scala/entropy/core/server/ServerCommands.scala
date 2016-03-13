package com.sjr.entropy.core.server

/**
 * Created by stevenrichardson on 2/1/15.
 */

trait ServerRequest

trait ServerResponse

// Game Actions

case class AbortGame(gameId: String)

// User Actions

case class RegisterUser(username: String, email: String)

case class Login(username: String, password: String) extends ServerRequest

case class Logout(username: String) extends ServerRequest

case class UserAuthenticated(player: PlayerInfo, sessionId: String) extends ServerResponse

case object AuthenticationFailed


// Finding/Observing games
case class Seek(gameInfo: GameInfo) extends ServerRequest

case class Join(gameId: String, player: PlayerInfo) extends ServerRequest

case class ActiveSeeks(seeks: Seq[Seek]) extends ServerResponse

case class ActiveGames(seeks: Seq[GameInfo]) extends ServerResponse

case class ObserveGame(gameId: String) extends ServerResponse

// Players

case class Finger(username: String) extends ServerRequest

case object ListPlayers extends ServerRequest // lists all players in the current room

case class ListPlayers(roomId: String) extends ServerRequest

case class PlayerList(players: Seq[PlayerInfo]) extends ServerResponse

// ROOMS - not needed yet. Just one room to begin with.

case object ListRooms extends ServerRequest

case class RoomList(rooms: Seq[RoomInfo]) extends ServerResponse

case class RoomInfo(roomId: String, gamesInProgress: Int, playerCount: Int, capacity: Int)

// Game State/Player Information
case class PlayerInfo(user: String, wins: Int, loss: Int, draws: Int)

case class GameInfo(player1: PlayerInfo, player2: PlayerInfo, boardSize: Int, timeLimitMinutes: Int)

case class GameState(info: GameInfo, boardInAscii: String, round: Int) // player 1 is always choas in round1



// a player can have multiple seeks but only one game in progress

