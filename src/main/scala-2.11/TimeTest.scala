import java.net.URLEncoder

import org.joda.time.{DateTime, DateTimeZone}
import com.github.nscala_time.time.Imports._

import scala.collection.mutable


object TimeTest extends  App{

  val monthInt = "2017-01-11T09:49:40.518Z".substring(5, 7).toInt
  val monthInt2 = "2017-11-11T09:49:40.518Z".substring(5, 7).toInt
//  val intSeq = (monthInt to monthInt2).foreach(println)

  val monthDate = DateTime.now().plusMonths(9).getMonthOfYear()
//  println(s"monthDate: $monthDate")

  val nowDate = new DateTime()
  val formattedDate =nowDate.toString("MM").toInt
  val nowMonth =nowDate.getMonthOfYear
//  println(s"nowDate: $nowDate")
//  println(s"formatted date: $formattedDate")
//  println(s"nowMonth: $nowMonth")

  val now23h =  DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
  val utc23h =  DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).withZone(DateTimeZone.UTC)
//  val t2=  DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).withZone(DateTimeZone.forOffsetHours(9))
//  val t3=  DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)

//  val firstDay = DateTime.now().withTimeAtStartOfDay()
//  val firstDay = DateTime.now().withDayOfMonth(1).toString("yyyy-MM-dd")
  val firstDay = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay()
  val ffirstDay = DateTime.now().dayOfMonth().withMinimumValue().withTimeAtStartOfDay()
//  println(s"firstDay: $firstDay")
//  println(s"ffirstDay: $ffirstDay")
  val lastDay = DateTime.now().dayOfMonth().withMaximumValue().plusDays(1).withTimeAtStartOfDay()
//  println(s"lastDay: $lastDay")


  val month = monthlyTime(DateTime.now())
  def monthlyTime(t: DateTime) = t.withZone(DateTimeZone.forOffsetHours(8))
    .day(1).hour(0).minute(0).second(0).millis(0).withZone(DateTimeZone.UTC).toString

  val lastMonth = monthlyTime {
    DateTime.now().minusMonths(1)
  }

  val llastMonth = monthlyTime {
    DateTime.now().minusMonths(2)
  }
//  println(s"month: $month")
//  println(s"lastmonth: $lastMonth")
//  println(s"llastmonth: $llastMonth")


  //  println(timeRange("2016-09-26T07:35:30.887Z","2016-10-26T07:35:30.887Z"))
  def time(t: DateTime) = t.withZone(DateTimeZone.forOffsetHours(8))
    .hour(0).minute(0).second(0).millis(0).withZone(DateTimeZone.UTC).toString

  def timeRange(from: String, to: String): Seq[String] = {
    val maxRange = 2000
    try {
      val startTime = DateTime.parse(from).minute(0).second(0).millis(0)
      val endTime = DateTime.parse(to).minute(0).second(0).millis(0)
      val millis = (startTime to endTime).millis
      val hours = ((millis + 1) / 3600000).toInt
      (0 to hours).map(h => time(startTime + h.hour)).distinct.take(maxRange)
    } catch {
      case e: IllegalArgumentException => Seq(time(DateTime.now()))
    }
  }

}
