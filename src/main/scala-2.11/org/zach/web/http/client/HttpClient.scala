package org.zach.web.http.client

import com.appadhoc.auth.model.User
import com.typesafe.config.ConfigFactory
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

import scalaj.http._

case class Users(users: Seq[User])

object HttpClient extends App{


  val config = ConfigFactory.load()
  val authKey = config.getString("auth.key")

  val queryUrl = "http://auth.soho.com/users"
  val response: HttpResponse[String] = Http(queryUrl)
    .header("Auth-key", authKey)
    .asString
  val bodyLength = response.body.length
  println(s"body length: $bodyLength")
  println(s"response code: ${response.code}")
  println("headers: ")
  response.headers.foreach(println)
//  println(s"cookies: ${response.cookies}")

  implicit val formats = DefaultFormats
  //extract do not support Seq type
  //  val users = parse(response.body).extract[Seq[User]]
  val users = parse(response.body).extract[Users].users
  val userSize = users.size
  println(s"users size: $userSize")


}
