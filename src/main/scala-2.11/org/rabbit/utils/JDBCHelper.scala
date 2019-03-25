package org.rabbit.utils

object JDBCHelper extends App {

  val conn = C3P0Tools.getConnection()
  println(s"c3p0 get connection: $conn")

  val segmentId = 1
  val user_count = 101
  println(s"segmentId: $segmentId")
  println(s"user_count: $user_count")

  val sql = s"update t_dashboard set user_count='$user_count' where id=$segmentId";
  C3P0Tools.execUpdate(conn, sql)
  C3P0Tools.close(conn.get)
}
