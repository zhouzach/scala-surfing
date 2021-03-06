package org.rabbit.functions.forexp

object EitherFor extends App{
  def positive(i: Int): Either[String,Int] =
    if (i > 0) Right(i) else Left(s"nonpositive number $i")

  val sum = for {
    i1 <- positive(5).right
    i2 <- positive(-1 * i1).right
    i3 <- positive(25 * i2).right
    i4 <- positive(-2 * i3).right
  } yield i1 + i2 + i3 + i4

  println(sum)
}
