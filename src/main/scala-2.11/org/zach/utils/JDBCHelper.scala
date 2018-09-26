package org.zach.utils

import org.apache.commons.dbutils.QueryRunner

object JDBCHelper extends App {
//  val register = "unknown"
//  val id =23
//  val sql = s"update t_customer_region set registerRegion='$register' where id=$id";
//
//
//  C3P0Tools.execUpdate(C3P0Tools.getConnection(), sql)

  val conn = C3P0Tools.getConnection()
  println(s"c3p0 get connection: $conn")

  val segmentId = 1
  val audience_group_user_count = 101
  println(s"segmentId: $segmentId")
  println(s"audience_group_user_count: $audience_group_user_count")

  val sql = s"update t_dashboard_activity_segment set audience_group_user_count='$audience_group_user_count' where id=$segmentId";
  C3P0Tools.execUpdate(conn, sql)
  C3P0Tools.close(conn.get)
}
