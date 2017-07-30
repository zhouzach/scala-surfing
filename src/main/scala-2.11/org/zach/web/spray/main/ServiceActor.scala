package org.zach.web.spray.main

import akka.actor.Actor
import org.zach.web.spray.paths.RouteHttpService

class ServiceActor extends Actor with RouteHttpService {

  /**
    * context: trait ActorContext extends ActorRefFactory
    * Scala API: Stores the context for this actor, including self, and sender.
    */
  def actorRefFactory = context

  override def receive = runRoute {
    //log per request
    logRequest("Request", akka.event.Logging.InfoLevel){
      requestContextRoute
    }

  }


}
