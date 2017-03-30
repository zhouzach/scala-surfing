package org.zach.functions.time

import com.github.nscala_time.time.Imports._
import org.joda.time.{DateTime, DateTimeZone}

object DayRange extends App {

  def time(t: DateTime) = t.withZone(DateTimeZone.forOffsetHours(8))
    .hour(0).minute(0).second(0).millis(0).withZone(DateTimeZone.UTC).toString

  def timeRange(from: String, to: String): Seq[String] = {
    val maxRange = 2000
    try {
      val startTime = DateTime.parse(from).minute(0).second(0).millis(0)
      val endTime = DateTime.parse(to).minute(0).second(0).millis(0)
      val millis = (startTime to endTime).millis
      val hours = ((millis + 1) / 3600000).toInt
      (0 to hours)
        .map{ h =>

          //          val st = startTime + h.hour
          //          println(s"st: $st")
          /**
            * accumulate startTime by hour,
            * and get many hour DateTime in the Same day,
            * but after org.zach.time() function, get many same days
            */
          val t = time(startTime + h.hour)
          //          println(s"t: $t")
          t
        }
        .distinct
        .take(maxRange)
    } catch {
      case e: IllegalArgumentException => Seq(time(DateTime.now()))
    }
  }
  timeRange("2016-09-26T07:35:30.887Z","2016-11-12T07:35:30.887Z").foreach(println)


}
