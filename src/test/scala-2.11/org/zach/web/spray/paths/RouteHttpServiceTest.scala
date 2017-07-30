package org.zach.web.spray.paths


import akka.actor.ActorRefFactory
import org.scalatest.{FunSpec, Matchers}
import spray.testkit.ScalatestRouteTest


class RouteHttpServiceTest extends FunSpec with Matchers with ScalatestRouteTest with RouteHttpService {

  def actorRefFactory: ActorRefFactory = system

  Get("/index") ~> requestContextRoute ~> check {

    responseAs[String] should equal("hi Spray!")

  }

}
