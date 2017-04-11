package org.zach.functions.http


import com.appadhoc.auth.model.User

import scalaj.http._
import com.typesafe.config.ConfigFactory
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

case class Users(users: Seq[User])

object HttpFunc extends App{

  val config = ConfigFactory.load()
  val authKey = config.getString("auth.key")

  val queryUrl = "http://auth.soho.com/users"
  val response: HttpResponse[String] = Http(queryUrl)
    .header("Auth-key", authKey)
    .asString
  val bodyLength = response.body.length
  println(s"body length: $bodyLength")
  println(s"response code: ${response.code}")
  println(s"headers: ${response.headers}")
  println(s"cookies: ${response.cookies}")

  implicit val formats = DefaultFormats
  //extract do not support Seq type
//  val users = parse(response.body).extract[Seq[User]]
  val users = parse(response.body).extract[Users].users
  val userSize = users.size
  println(s"users size: $userSize")



}
