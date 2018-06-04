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
    "curl http://baidu.com" !!
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
