package entropy.core

/**
 * Created by srichardson on 4/6/16.
 */
object ReactiveSamples extends App {

  import rx.lang.scala.Observable
  import scala.concurrent.duration._

//  Observable.interval(1 second).scan(1l)((x, y) => x + y).zipWithIndex.subscribe(
//    valueIndexTuple => println(s"Outcome of scan ${valueIndexTuple._2}  was ${valueIndexTuple._1}")
//  )

  Observable.interval(1 second).groupBy(x => println(x))



//  Observable.just(1, 2, 3, 4).subscribe(
//    id => println(s"Just $id")
//  )
//
//  Observable.timer(10 seconds).subscribe(
//    id => println("From timer" + id)
//  )
//
//  Observable.interval(1 second).filter(_ % 4 == 0).subscribe(
//    id => println(id)
//  )
  Thread.sleep(100000)




}
