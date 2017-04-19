package org.zach.web.spray.main

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.slf4j.LoggerFactory
import spray.can.Http

import scala.concurrent.duration._

object Main extends App {
  val logger = LoggerFactory.getLogger("org.zach.web.spray.main.Main")

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[ServiceActor], "my-web-server")

  implicit val timeout = Timeout(5.seconds)

  val host = "0.0.0.0"
  val port = system.settings.config.getInt("server.port")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = host, port = port)

  logger.info("Server started at {}:{}", "0.0.0.0", port)

}
