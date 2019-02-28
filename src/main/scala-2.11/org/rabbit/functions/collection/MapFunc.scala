package org.rabbit.functions.collection

/**
  * although immutable map have +, ++, ++: and - , -- methods, it itself can not insert or remove element
  */
object MapFunc extends App {
  val immutableMap = Map.empty[String, Any]


  val x = immutableMap + ("a" -> 0) + ("aa" -> 2)

  val b = immutableMap ++ Map("b" -> 1)
  val c = immutableMap ++: Map("c" -> 2)

  x foreach { case (key, value) => println(key + " --> " + value) }
  x - "a"
  val aa = x - ("a", "bb")
  val bb = x -- Seq("a", "c")
  x foreach { case (key, value) => println(key + " --> " + value) }
  println("aa: ")
  aa foreach { case (key, value) => println(key + " --> " + value) }
  println("bb: ")
  bb foreach { case (key, value) => println(key + " --> " + value) }

  b foreach { case (key, value) => println(key + " --> " + value) }
  c foreach { case (key, value) => println(key + " --> " + value) }

  for ((key, value) <- immutableMap) {
    println(key + ", " + value)
  }

}

object MutableMapFunc extends App {

  import scala.collection.mutable.Map

  val map = Map.empty[String, Any]

//  val m1 = map + (("a", 1), ("b", 2))
//  val m2 = m1 + ""
//  val m3 = "" + m1
  val m4 = map ++ Map("c" -> 3)
  val m5 = map ++: Map("e" -> 5)
  map ++= Map("f" -> 6)
  map += ("g" -> 7)

  map -= ("g","f")



//  m6 foreach { case (key, value) => println(key + " --> " + value) }
//  println(m2)
//  println(m3)


  println("map:")
  for ((key, value) <- map) {
    println(key + ", " + value)
  }

}

object MapUtil {
  val map = Map("a" -> 1, "b" -> "str")

  def print() = {
    "" + map
  }

  def printByForeach() = {
    map foreach { case (key, value) => println(key + " --> " + value) }
  }

  def printByFor() = {
    for ((key, value) <- map) {
      println(key + ", " + value)
    }
  }

}