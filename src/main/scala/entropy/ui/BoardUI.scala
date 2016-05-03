package entropy.ui

import java.awt._
import javax.swing.{JFrame, JPanel}

import com.sjr.entropy.core.Point

/**
 * Created by srichardson on 5/2/16.
 */

class BoardUI(cols: Int, rows: Int) extends JPanel {

  def updateUI(currentState: AnyRef): Unit = {

  }

  override def paintComponent(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    val bounds: Rectangle = super.getBounds
    g2.setColor(new Color(40,40,40))
    g2.fillRect(0, 0, bounds.width, bounds.height)
    g2.setColor(Color.DARK_GRAY)
    val rowHeight = bounds.height / rows.toDouble
    val colWidth = bounds.width / cols.toDouble
    (1 to rows).map(_ * rowHeight).foreach(offset => g2.drawLine(0, offset.toInt, bounds.width, offset.toInt))
    (1 to cols).map(_ * colWidth).foreach(offset => g2.drawLine(offset.toInt, 0, offset.toInt, bounds.height))

    val point1 = Point(1,1)
    val point2 = Point(2,2)
    val point3 = Point(3,3)
    val point4 = Point(3,1)
//    val point5 = Point(2,2)
    val point6 = Point(1,3)
    val points = Seq(point1, point2, point3, point4, point6)
    val colors = Seq(Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.WHITE)

    points.zip(colors).foreach{ case (point, color) =>
      val topCorner = (point.x * colWidth, point.y * rowHeight)
      g2.setColor(color)
      g2.fillOval(topCorner._1.toInt,
        topCorner._2.toInt, colWidth.toInt,
        rowHeight.toInt)
//      g2.setColor(Color.RED)
      //    g2.drawOval(topCorner._1.toInt,
      //      topCorner._2.toInt, colWidth.toInt,
      //      rowHeight.toInt)


      //    g2.fillRoundRect(topCorner._1.toInt,
      //      topCorner._2.toInt, colWidth.toInt,
      //      rowHeight.toInt, (colWidth / 4.0).toInt, (rowHeight/4.0).toInt)
    }
  }
}

object TestBoard extends App {
  val board = new BoardUI(7,7)
  val frame = new JFrame("Entropy version 1")
  frame.setContentPane(board)
  frame.setSize(300, 300)
  frame.setVisible(true)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
}
