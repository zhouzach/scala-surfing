package org.zach.util

object Download {

  def urlToString() = {
    println(scala.io.Source.fromURL("http://baidu.com").mkString)
  }

  /**
    * Another approach is to reach out to the shell and use the wget or curl command, like this:
   */
  def cmd() = {
    // While that also works, I don't like depending on wget or curl
    // when you can do everything you need from within the Scala/Java environment.
    import sys.process._
//    "curl http://baidu.com" !!
    "rm -rf ps.pdf" !!
  }

  def toFile() = {
    import sys.process._
    import java.net.URL
    import java.io.File
    new URL("http://baidu.com") #> new File("Output.html") !!
  }

  def main(args: Array[String]): Unit = {

    cmd()

    println("*****************")

    urlToString()

    toFile()

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

  val s1 = s.substring(0,n)
  val s2 = s.substring(n,m+1)
  val s3 = s.substring(m+1)
  println(s1)
  println(s2)
  println(s3)
}