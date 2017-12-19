package org.zach.functions.forexp

object WithFilter extends App{
  case class Person(name: String,
                    isMale: Boolean,
                    children: Person*)

  val lara = Person("Lara", false)
  val bob = Person("Bob", true)
  val julie = Person("Julie", false, lara, bob)
  val persons = List(lara, bob, julie)

  persons
    .filter (p => !p.isMale)
    .flatMap (p =>
    p.children map (c => (p.name, c.name)))

  persons withFilter (p => !p.isMale) flatMap (p =>
    p.children map (c => (p.name, c.name)))

}
