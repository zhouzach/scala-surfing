package org.zach.mytrait

object TraitTest extends App{

  val rect = new Rectangle(new Point(1, 1), new Point(10, 10))
  class A extends AnyRef
  val n=new A().##
  val r = rect.##
  val h = rect.hashCode()
  rect.==()
  println(s"n: $n")
  println(s"r: $r")
  println(s"h: $h")
  println(rect.left)
  class MyQueue extends BasicIntQueue with Doubling
  val queue = new MyQueue
  queue.put(10)
  println(queue.get())

  def m(r: Rectangular)={
    import r._
    println(left)
  }

}
class Point(val x: Int, val y: Int)

trait Rectangular {
  def topLeft: Point
  def bottomRight: Point
  def left = topLeft.x
  def right = bottomRight.x
  def width = right - left
  // and many more geometric methods...
}

class Rectangle(val topLeft: Point, val bottomRight: Point)
  extends Rectangular {
  // other methods...
}

abstract class IntQueue {
  def get(): Int
  def put(x: Int)
}

import scala.collection.mutable.ArrayBuffer
class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]
  def get() = buf.remove(0)
  def put(x: Int) = { buf += x }
}
trait Doubling extends IntQueue {
  abstract override def put(x: Int) = { super.put(2 * x) }
}