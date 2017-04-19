package org.zach.web.spray.main

import akka.actor.Actor
import org.zach.web.spray.paths.RoutePath

class ServiceActor extends Actor with RoutePath {

  def actorRefFactory = context

  override def receive = runRoute {

    //log per request
    logRequest("Request", akka.event.Logging.InfoLevel){

      route

    }

  }


}
