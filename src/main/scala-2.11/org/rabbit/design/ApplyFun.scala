package org.rabbit.design

object ApplyFun {
  def apply: ApplyFun = new ApplyFun()


}

class ApplyFun

object ApplyTest extends App {
  val a = ApplyFun
  println(a)
}
