package org.rabbit.functions

import org.rabbit.functions.model.UserActiveRecord


object MatchFuncs extends App {
  var x: Any = Seq(1,2)
  x match {
    case s: String =>
      println("it is string")
    case n: Int =>
      println("it is int")
    case _ =>
      println("it is ohter")
  }


  val userActiveRecordSeq =
    Seq(
//    UserActiveRecord("user_id_01", 1000, 1000, 1000, 1000, 1000, 10.0, 10.0, 10.0, 1000, 0.8),
//    UserActiveRecord("user_id_02", 1000, 1000, 1000, 1000, 1000, 10.0, 10.0, 10.0, 1000, 0.8),
//    UserActiveRecord("user_id_03", 1000, 1000, 1000, 1000, 1000, 10.0, 10.0, 10.0, 1000, 0.8)
    )

  userActiveRecordSeq match {
    case Seq(a) =>
      println(s"match the situation of only one element in Seq")
      println(s"a: $a")
    case Seq(a, b) =>
      println(s"match the situation of only two elements in Seq")
      println(s"a: $a, b: $b")
    case a: Seq[UserActiveRecord] =>
      println(s"match the Seq that is Empty or have more than a element")
      a.foreach(println)
    case _ =>
      println(s"other situation")
      None
  }


//  println(userActiveRecordSeq.headOption)
}
