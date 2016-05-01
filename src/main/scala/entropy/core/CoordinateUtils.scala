package entropy.core

import com.sjr.entropy.core.Point
import com.sjr.entropy.core.game.EntropyBoard

/**
 * Created by srichardson on 5/1/16.
 */
object CoordinateUtils {
  private val columnHeadings = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

  def makeHeaderString(board: EntropyBoard) = columnHeadings.take(board.columns)

  def toPoint(x: String, y: String): Point = Point(letterMappings(x), y.toInt)

  private val letterMappings = columnHeadings.toCharArray.zipWithIndex.map {
    case (letter, index) => (letter.toString, index + 1)
  }.toMap

}
