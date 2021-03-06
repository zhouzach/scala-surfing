package org.rabbit.functions.collection

import scala.collection.mutable

object CollectionFuncs extends App{

  val intSeq = (1 to 3).toSeq

  val Seq(n, m) = Seq(3,2)
  //  println(s"n,m: $n,$m")

  val mapSeq = Seq(Map("a" -> 1, "b" -> "str"))
  val Seq(values)
//  val Seq(aV, bV)
    = mapSeq.map { record =>
    (
      record.getOrElse("a", 0).asInstanceOf[Int],
      record.getOrElse("b", "").asInstanceOf[String]
    )
  }
  println(s"values: $values")

  /** MatchError:*/
//  println(s"aV: $aV, $bV")




  val map1 = mutable.Map[Int, String]()
  map1 += (1 -> "a")
  map1 += (2 -> "b")
  //  map1.foreach(println)

  val immutableMap = Map[String, Int]()
  def addToMap(n: Int, map: Map[String, Int]): Map[String, Int] = {
      map + (n.toString + "_pay" -> n)
  }

  val m1 = (1 to 3).flatMap { n =>
    addToMap(n, immutableMap)
  }.toMap
//  m1.foreach(println)

  val m2= (4 to 6).flatMap { n =>
    addToMap(n, immutableMap)
  }.toMap
//  m2.foreach(println)
//  println("********")

  val res = m1 ++ m2
//  res.foreach(println)

  val mutableMap = scala.collection.mutable.Map[String, Int]()
  def addToMap(map: mutable.Map[String, Int]): Map[String, Int] = {
    (1 to 3).flatMap { n =>
      map += (n.toString -> n)
    }
  }.toMap


  val intMap = addToMap(mutableMap)
//  intMap.foreach(println)


}
