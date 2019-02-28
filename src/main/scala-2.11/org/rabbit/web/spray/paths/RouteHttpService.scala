package org.rabbit.web.spray.paths



import spray.routing.HttpService

trait RouteHttpService extends HttpService {

  val requestContextRoute = path("index") {
    get {
      complete("hi Spray!")
    }
  }

}
