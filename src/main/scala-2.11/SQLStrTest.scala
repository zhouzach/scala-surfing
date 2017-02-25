import java.net.URLEncoder

import org.joda.time.{DateTime, DateTimeZone}

object SQLStrTest extends App {

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
//    println (sql1)
  //  println(sql2)

}
