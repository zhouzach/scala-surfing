package org.zach.concurrency

import akka.actor._

/**
  * Created by zach on 2017/7/29.
  */
object DeathWatchTest extends App {
  // create the ActorSystem instance
  val system = ActorSystem("DeathWatchTest") // create the Parent that will create Kenny
  val parent = system.actorOf(Props[Parent], name = "Parent")
  // lookup kenny, then kill it
  val kenny = system.actorSelection("/user/Parent/Kenny")
  kenny ! PoisonPill

  Thread.sleep(5000)
  println("calling system.shutdown")
  system.terminate()

}

class Parent extends Actor {
  // start Kenny as a child, then keep an eye on it
  val kenny = context.actorOf(Props[Kenny], name = "Kenny")
  context.watch(kenny)

  def receive = {
    case Terminated(kenny) => println("OMG, they killed Kenny")
    case _ => println("Parent received a message") }
}

case object Explode
class Kenny extends Actor {
  def receive = {
  case Explode => throw new Exception("Boom!")
  case _ => println("Kenny received a message")
  }

  override def preStart { println("kenny: preStart") }
  override def postStop { println("kenny: postStop") }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("kenny: preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) {
    println("kenny: postRestart")
    super.postRestart(reason)
  }
}
