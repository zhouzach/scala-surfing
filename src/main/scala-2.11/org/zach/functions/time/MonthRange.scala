package org.zach.functions.time

import com.github.nscala_time.time.Imports._
import org.joda.time.{DateTime, DateTimeZone}

object MonthRange extends App {
  def time(t: DateTime) = t.withZone(DateTimeZone.forOffsetHours(8))
    .dayOfMonth().withMinimumValue().plusDays(23)
    .hour(0).minute(0).second(0).millis(0)
    .withZone(DateTimeZone.UTC)
    .toString

  def timeRange(from: String, to: String): Seq[String] = {
    val maxRange = 2000
    try {
      val startTime = DateTime.parse(from).minute(0).second(0).millis(0)
      val endTime = DateTime.parse(to).minute(0).second(0).millis(0)
      val millis = (startTime to endTime).millis
      val hours = ((millis + 1) / 3600000).toInt
      (0 to hours).map(h => time(startTime + h.hour)).distinct.take(maxRange)
        .map(month => month.substring(0, 7))
        .map(month => new DateTime(month).toString("yyyy-M"))
    } catch {
      case e: IllegalArgumentException => Seq(time(DateTime.now()))
    }
  }
  val monthRange = timeRange("2016-09-26T07:35:30.887Z","2016-12-27T07:35:30.887Z")
  monthRange.foreach(println)


}
