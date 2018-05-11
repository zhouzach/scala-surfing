package org.zach.util

import java.text.{Collator, RuleBasedCollator}
import java.util.Locale


case class Sort() {

  def order4China = {

    // https://blog.csdn.net/u010454030/article/details/79016996
    case class Person(name: String, age: Int) {
      override def toString = {
        "name: " + name + ", age: " + age
      }
    }

    val p1 = Person("中国", 24)
    val p2 = Person("中美", 22)
    val p3 = Person("大连", 15)
    val p4 = Person("a大连", 15)
    val p5 = Person("1最大连", 15)
    val p6 = Person("a小连", 15)
    val persons = Seq(p1, p2, p3, p4, p5, p6)

    implicit object PersonOrdering extends Ordering[Person] {
      override def compare(p1: Person, p2: Person): Int = {
        val instance = Collator.getInstance(Locale.CHINA).asInstanceOf[RuleBasedCollator]
        instance.compare(p1.name, p2.name)
      }
    }
    persons.sorted.foreach(println(_))
  }

}

object Sort extends App {

  Sort.apply().order4China

}
