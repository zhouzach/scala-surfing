package org.zach.functions.time

import com.github.nscala_time.time.Imports._
import org.joda.time.{DateTime, DateTimeZone}


object TimeFuncs extends  App{

  val monthInt = "2017-01-11T09:49:40.518Z".substring(5, 7).toInt
  val monthInt2 = "2017-11-11T09:49:40.518Z".substring(5, 7).toInt
//  val intSeq = (monthInt to monthInt2).foreach(println)

  val dayDate = DateTime.now().minusDays(1).withTimeAtStartOfDay()
//  println(s"now: ${DateTime.now}")
//  println(s"dayDate: $dayDate")
  val monthDate = DateTime.now().plusMonths(9).getMonthOfYear
//  println(s"monthDate: $monthDate")

  val int2Date = new DateTime(2017, 2, 12, 14, 27)
//  println(s"int2Date: $int2Date")
  val str2Date = new DateTime("2017-02")
//  println(s"str2Date: $str2Date")
  val strForDate = new DateTime("2017-01-11T09:49:40.518Z").toString("yyyy-M")
//  println(strForDate)

  val nowDate = new DateTime("2018-08-10")
  val nowDateStr = nowDate.toString
  val nowDateLong = nowDate.getMillis
//  val from_dt = DateTime.now().minusDays(1).toString().toLocalDate
  val from_dt = DateTime.now().minusDays(1).withZone(DateTimeZone.UTC).toString
  // 2018-06-06 00:00:00.0
  val fromDate = new DateTime(from_dt).toString("yyyy-MM-dd HH:mm:ss.sss")
  val formattedDate =nowDate.toString("MM").toInt
  val nowWeekDay = nowDate.getDayOfWeek
  val nowDay = nowDate.getDayOfMonth
  val nowMonth =nowDate.getMonthOfYear
  val nowYear =nowDate.getYear
  val yearMonth = s"${nowDate.getYear}-${nowDate.getMonthOfYear}"
//  println(s"from_dt: $from_dt")
//  println(s"fromDate: $fromDate")
  println(s"nowDateStr: $fromDate")
//  println(s"nowDateStr: ${nowDateLong}")
//  printlns(s"nowDay: $nowDay")
//  println(s"nowWeekDay: $nowWeekDay")
//  println(s"nowDate: $nowDate")
//  println(s"formatted date: $formattedDate")
//  println(s"nowMonth: $nowMonth")
//    println(s"nowYear: $nowYear")
//  println(s"yearMonth: $yearMonth")

  val now23h =  DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
  val day7utc23h =  DateTime.now().withDayOfMonth(1).plusDays(6).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999)
    .withZone(DateTimeZone.UTC).getMillis / 1000
  val day7now0h = DateTime.now.withDayOfMonth(1).plusDays(6).withTimeAtStartOfDay().withZone(DateTimeZone.UTC).getMillis / 1000
//  println(s"now0h: $day7now0h")
//  println(s"now23h: $day7utc23h")
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

  val hour23 = new DateTime("2018-09-20").withTimeAtStartOfDay()
    .withZone(DateTimeZone.UTC).withZone(DateTimeZone.forOffsetHours(8))
    .toString("yyyy-MM-dd HH:mm:ss.sss")
  println(s"hour23: $hour23")
  val pDate = new DateTime("2018-09-20").minusDays(1).withTimeAtStartOfDay()
    .withZone(DateTimeZone.UTC).withZone(DateTimeZone.forOffsetHours(8))
    .toString("yyyy-MM-dd")
  println(s"pDate: $pDate")

}
