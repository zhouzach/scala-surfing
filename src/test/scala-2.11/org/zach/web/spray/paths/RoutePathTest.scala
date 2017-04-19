package org.zach.web.spray.paths


import akka.actor.ActorRefFactory
import org.scalatest.{FunSpec, Matchers}
import spray.testkit.ScalatestRouteTest


class RoutePathTest extends FunSpec with Matchers with ScalatestRouteTest with RoutePath {

  def actorRefFactory: ActorRefFactory = system

  Get("/index") ~> route ~> check {

    responseAs[String] should equal("hi Spray!")

  }

}
