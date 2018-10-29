package org.zach.utils

import java.io._
import java.nio.file.{Files, Path, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, _}
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Download {

  val logger = LoggerFactory.getLogger(this.getClass)

  def urlToSource() = {
    scala.io.Source.fromURL("http://baidu.com").mkString
  }

  /**
    * Another approach is to reach out to the shell and use the wget or curl command, like this:
    */
  def byCurl() = {
    // While that also works, I don't like depending on wget or curl
    // when you can do everything you need from within the Scala/Java environment.
    import sys.process._
    "curl http://baidu.com" !!
  }

  def toFile() = {
    import java.io.File
    import java.net.URL

    import sys.process._
    new URL("http://baidu.com") #> new File("Output.html") !!
  }

  def exists(fileName: String) = {
    new File(fileName).exists()
  }

  def fileToByteString(path: Path) = {
    ByteString(Files.readAllBytes(path))
  }

  def byHttp(interface: String = "127.0.0.1", port: Int, baseDir: String)
            (implicit system: ActorSystem, fm: Materializer) = {
    val requestHandler: HttpRequest => Future[HttpResponse] = {
      case HttpRequest(HttpMethods.GET, uri, _, _, _) =>
        val file = baseDir + uri.path.toString
        val path = Paths.get(file)
        val response = exists(file) match {
          case true => // 文件存在
            logger.info(s"$path exists")
            HttpResponse(
              status = StatusCodes.OK,
              entity = HttpEntity(ContentTypes.`application/octet-stream`, Source.single(fileToByteString(path))))
          case false => // 文件不存在
            logger.info(s"$path not exists")
            HttpResponse(
              status = StatusCodes.NotFound,
              entity = HttpEntity(ContentTypes.`application/json`,
                s"""{"result":"error","message":"$path not exists!!!"}"""))
        }
        Future(response)
    }

    Http(system).bind(interface = interface, port = port)
      .to(Sink.foreach(connection => {
        logger.info("浏览器与服务器建立链接")
        connection.handleWithAsyncHandler(requestHandler)
      }))
      .run()
  }

  private val config = ConfigFactory.parseString(
    s"""
       |akka.http.server.remote-address-header = on
    """.stripMargin)
    .withFallback(ConfigFactory.load())

  def fileServerStarter: Unit = {

    implicit val system = ActorSystem("itoa", config)
    implicit val fm = ActorMaterializer()

    byHttp("localhost", 9990, "/Users/Zach/scala-functions/")
  }

  def main(args: Array[String]): Unit = {

    val baseDir = "/Users/Zach/scala-functions/"
    val file = baseDir + "/mave/test.txt"
    val path = Paths.get(file)

    val hasFile = exists(file)
    if (hasFile) println(s"$path exists.") else println(s"$path not exists!")

    println(s"File Server baseDir: $baseDir")
    fileServerStarter
  }

}

object FileName extends App {

  val s = "ps (1).tar"
  val n = s.indexOf("(")
  val m = s.indexOf(")")
  println(n)
  println(m)
  val c = s.charAt(1)
  println(c)

  val s1 = s.substring(0, n)
  val s2 = s.substring(n, m + 1)
  val s3 = s.substring(m + 1)
  println(s1)
  println(s2)
  println(s3)
}