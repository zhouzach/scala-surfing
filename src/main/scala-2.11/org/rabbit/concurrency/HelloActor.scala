package org.rabbit.concurrency

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}


class HelloActor(name: String) extends Actor {

  override def receive = {
    case "hello" => println(s"hello from $name")
    case _ => println(s"hah, from $name")
  }

}

object HelloActor extends App {
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "helloactor")

  helloActor ! "hello"
  helloActor ! "nothing"
  helloActor ! PoisonPill

  system.actorSelection("")
  system.terminate()
}
