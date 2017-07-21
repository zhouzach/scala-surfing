package org.zach.functions.string

object StringFuncs extends App {
  def month = 3
  val dbMonth = s"db_$month"
//  println(s"dbMonth: $dbMonth")

  val db = "mongo"
  val dbStr = s"abc_$db"
  //  println(dbStr)

  val javaUUID = java.util.UUID.randomUUID.toString
  val splitUUID = javaUUID.split("-").toString
  val repUUID = javaUUID.replace('-', 't')

  val str2Int = "0010".toInt
//  println(s"str2Int: $str2Int")

  val special = "$"
  println(s"$special ")

}
