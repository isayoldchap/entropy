package entropy.core

import com.sjr.entropy.core._
import com.sjr.entropy.core.game.{OrderMove, ChaosMove, PassMove, EntropyMove}

/**
 * Created by srichardson on 5/1/16.
 */

object NotationUtils {
  private val ChaosMoveRegex = """([A-Z])(\d{1,2})""".r
  private val OrderMoveRegex = """([A-Z])(\d{1,2})-([A-Z])(\d{1,2})""".r
  private val Pass = "PASS"

  def commandToMove(command: String): Op[EntropyMove] = command.toUpperCase match {
    case Pass => Some(PassMove)
    case ChaosMoveRegex(x, y) => Some(ChaosMove(makePoint(x, y)))
    case OrderMoveRegex(x1, y1, x2, y2) => Some(OrderMove(makePoint(x1, y1), makePoint(x2, y2)))
    case _ => None
  }

  def makePoint(x: String, y: String): Point = CoordinateUtils.toPoint(x, y)

  def moveToNotation(move: EntropyMove): String = move match {
    case PassMove => Pass
    case OrderMove(source, destination) => ""
    case ChaosMove(destination) => ""
  }
}
