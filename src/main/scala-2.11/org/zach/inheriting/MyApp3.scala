package org.zach.inheriting

import java.util.concurrent.ConcurrentHashMap

object MyApp3 extends App {
  val b = new B().pool.get("a")
  println(b)

}

trait A {
  val pool = new ConcurrentHashMap[String, Any]()

  def getValue() = {
    pool.put("a", 1)
    pool
  }

  def set(key: String, value: Any): this.type = {
    pool.put(key, value)
    this
  }
}

class B extends A {
    set("a",2)

  //compile error
//  override val pool = getValue()

//  def put = set("a", 3)

}
