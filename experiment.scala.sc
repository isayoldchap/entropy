import rx.lang.scala.Observable
import scala.concurrent.duration._

Observable.interval(1 second).take(4 hours).subscribe(
  id => println(id)
)