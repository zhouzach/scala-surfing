package org.zach.concurrency

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by zach on 2017/7/29.
  */

case object StartMessage
case object StopMessage
case object PingMessage
case object PongMessage


object PingPongTest extends App {

  val system = ActorSystem("pingPongSystem")

  val pongActor = system.actorOf(Props[Pong], name = "pongActor")
  val pingActor = system.actorOf(Props(new Ping(pongActor)), name = "pingActor")

  pingActor ! StartMessage

  system.terminate()

}

class Ping(pong: ActorRef) extends Actor {

  var count = 0

  def incrementAndPrint = {
    count = count + 1
    print("\nPing ")
  }
  override def receive: Receive = {
    case StartMessage =>
      incrementAndPrint
      pong ! StartMessage

    case PongMessage =>
      if (count < 10) {
        incrementAndPrint
        sender() ! PingMessage
      } else {
        print(s"\nn = ${count}, Ping Stop, ")
        sender() ! StopMessage
        context.stop(self)
      }

    case _ => println("cannot handle the message")
  }
}

class Pong extends Actor {
  override def receive: Receive = {
    case StartMessage =>
      print("Pong")
      sender() ! PongMessage

    case PingMessage =>
      print("Pong")
      sender() ! PongMessage

    case StopMessage =>
      println("Pong Stop!")
      context.stop(self)

    case _ => println("cannot handle the message")
  }
}
