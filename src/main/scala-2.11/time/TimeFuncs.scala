package time

import com.github.nscala_time.time.Imports._
import org.joda.time.{DateTime, DateTimeZone}


object TimeFuncs extends  App{

  val monthInt = "2017-01-11T09:49:40.518Z".substring(5, 7).toInt
  val monthInt2 = "2017-11-11T09:49:40.518Z".substring(5, 7).toInt
//  val intSeq = (monthInt to monthInt2).foreach(println)

  val monthDate = DateTime.now().plusMonths(9).getMonthOfYear
//  println(s"monthDate: $monthDate")

  val int2Date = new DateTime(2017, 2, 12, 14, 27)
//  println(s"int2Date: $int2Date")
  val str2Date = new DateTime("2017-02")
//  println(s"str2Date: $str2Date")
  val strForDate = new DateTime("2017-01-11T09:49:40.518Z").toString("yyyy-M")
//  println(strForDate)

  val nowDate = new DateTime()
  val formattedDate =nowDate.toString("MM").toInt
  val nowMonth =nowDate.getMonthOfYear
  val nowYear =nowDate.getYear
  val yearMonth = s"${nowDate.getYear}-${nowDate.getMonthOfYear}"
//  println(s"nowDate: $nowDate")
//  println(s"formatted date: $formattedDate")
//  println(s"nowMonth: $nowMonth")
//    println(s"nowYear: $nowYear")
//  println(s"yearMonth: $yearMonth")

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

  def time2(t: DateTime) = t.withZone(DateTimeZone.forOffsetHours(8))
      .dayOfMonth().withMinimumValue().hour(0).minute(0).second(0).millis(0).withZone(DateTimeZone.UTC).toString

  def time(t: DateTime) = t.withZone(DateTimeZone.forOffsetHours(8))
//      .day(1).
    .hour(0).minute(0).second(0).millis(0).withZone(DateTimeZone.UTC).toString

  def timeRange(from: String, to: String): Seq[String] = {
    val maxRange = 2000
    try {
      val startTime = DateTime.parse(from).minute(0).second(0).millis(0)
      val endTime = DateTime.parse(to).minute(0).second(0).millis(0)
      val millis = (startTime to endTime).millis
      val hours = ((millis + 1) / 3600000).toInt
      val days = ((millis + 1) / 3600000 / 24).toInt
//      println(s"days: $days")
      (0 to hours)
        .map{ h =>
//          println(s"h: $h")
//          println(s"h.hour: ${h.hour}")
          val t = time2(startTime + h.hour)
//          val st = startTime + h.hour
//          println(s"t: $t")
//          println(s"st: $st")
          t
        }
        .distinct
        .take(maxRange)
    } catch {
      case e: IllegalArgumentException => Seq(time(DateTime.now()))
    }
  }
    timeRange("2016-09-26T07:35:30.887Z","2016-12-27T07:35:30.887Z").foreach(println)

}
