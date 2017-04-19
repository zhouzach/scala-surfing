package org.zach.web.spray.paths



import spray.routing.HttpService

trait RoutePath extends HttpService {

  val route = path("index") {
    get {
      complete("hi Spray!")
    }
  }

}
