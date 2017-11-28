package org.zach.functions.collection

object ListFuncs extends App{
  def isort(xs: List[Int]): List[Int] =
    if (xs.isEmpty) Nil
    else insert(xs.head, isort(xs.tail))
  def insert(x: Int, xs: List[Int]): List[Int] =
    if (xs.isEmpty || x <= xs.head) x :: xs
    else xs.head :: insert(x, xs.tail)
  val l=List(5,4,3,2,1)
  isort(l)
  l.foreach(print(_))

}
