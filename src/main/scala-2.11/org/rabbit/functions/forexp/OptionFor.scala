package org.rabbit.functions.forexp


object OptionFor extends App {
  def positive(i: Int): Option[Int] =
    if (i > 0) Some(i) else None

  def printNum = {
    println("hh")
    Some(2)
  }
  val sum = for {
    i1 <- positive(5)
    i2 <- positive(-1 * i1)
    i3 <- positive(25 * i2)
  _ <- printNum
    i4 <- positive(-2 * i3)
  } yield i1 + i2 + i3 + i4

  println(sum)

}
