import java.net.URLEncoder

import org.joda.time.{DateTime, DateTimeZone}
import com.github.nscala_time.time.Imports._


object TestFuns extends  App{

  val payApps =
    s"""
        t_apps as (
          select *
          from mongoDB.apps
          where deleted = false
          and author_id in (
            select user_id
            from mongoDB.pay
            where state = "pay"))"""

  val apps =
    s"""
        t_apps as(
          select *
          from mongoDB.apps
          where deleted = false
          and author_id in (
            select id
            from t_users))"""
  def dailySqlByApps(dateTime: DateTime, apps: String) = {
    val tags = URLEncoder.encode("{ \"tags\" : \"内测\"}", "UTF-8")
    val hour0 = dateTime.withTimeAtStartOfDay().withZone(DateTimeZone.UTC).getMillis / 1000
    val hour23 = dateTime.plusDays(1).withTimeAtStartOfDay().withZone(DateTimeZone.UTC).getMillis / 1000
    val apiDate = dateTime.toString("yyyy-MM-dd")
    s"""
        with
          t_users as (
            select *
            from mongoDB.users
            where operation_info!="$tags"
            and third_party_from=""
            and `role`="Owner"
            and blocked= false
            and actived= true),
          $apps,
          t_experiments as (
            select *
            from mongoDB.experiments
            where app_id in (
              select id
              from t_apps)),
          t_running_experiments as (
            select *
            from t_experiments
            where start_date < $hour0
            and end_date > $hour0),
          t1 as (
            select count(*) as register_user_num
            from t_users
            where created_at >= $hour0
            and created_at <= $hour23),
          t2 as (
            select count(*) as app_num
            from t_apps
            where created_at >= $hour0
            and created_at <= $hour23),
          t3 as (
           select count(distinct group_id) as created_group_num
           from t_experiments
           where created_at > $hour0 * 1000
           and create_at < $hour23 * 1000),
          t4 as (
            select count(distinct group_id) as running_group_num
            from t_running_experiments),
          t5 as (
           select count(distinct author_id) as running_group_user_num
           from t_apps
           where id in (
             select distinct app_id
             from t_running_experiments)),
          t6 as (
            select day, count(*) as api_num
            from logDB.log
            where day = $apiDate
            group by day)
        select *
        from t6 join t1 join t2 join t3 join t4 join t5;
     """

  }

  val tnow = new DateTime()
  val sql1 = dailySqlByApps(tnow, apps)
  val sql2 = dailySqlByApps(tnow, payApps)
//  println (sql1)
//  println(sql2)


  val substr= "2017-01-11T09:49:40.518Z".substring(0,10)
//  println(substr)
  val db = "mongo"
  val str = s"abc_$db"
//  println(str)

  val javaUUID = java.util.UUID.randomUUID.toString
  val splitUUID = javaUUID.split("-").toString
  val repUUID = javaUUID.replace('-', 't')


  val monthDate = DateTime.now().getMonthOfYear()
//  println(s"monthDate: $monthDate")

  val nowDate = new DateTime()
  val formattedDate =nowDate.toString("yyyy-MM")
  println(s"nowDate: $nowDate")
  println(s"formatted date: $formattedDate")

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
//  val lastDay = DateTime.now().dayOfMonth().withMaximumValue().plusDays(1).withTimeAtStartOfDay()
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


  case class People(name: String, age: Option[Int])
  case class Num(id: String, deleted: Option[String])

  var peos = Seq(People("xiao", Some(1))
//    People("hong", Some(2)),
//    People("ming",None)
  )

  val nums = Seq(Num("1",Some("false")),Num("2",Some("true")), Num("3",None),Num("4",Some("true")),Num("5",None))
  val filterNums = nums.map(_.deleted.getOrElse("")).distinct


  val filterMap =Map(1 -> "a", 2 -> "b").filter(m =>List(2,3).contains(m._1))
//  filterMap.foreach(println)


  def remove(p: People, list: List[People]) = list diff List(p)
  val removedPeo = remove(People("xiao",Some(1)),peos.toList)

  val groupedPeo = removedPeo.groupBy(_.name).map(m => m._2.maxBy(_.age))


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
