package org.rabbit.utils

object Commands {

  import sys.process._

  def toFile() = {
    import java.io.File
    import java.net.URL

    import sys.process._
    new URL("http://baidu.com") #> new File("Output.html") !!
  }


  def urlToSource() = {
    scala.io.Source.fromURL("http://baidu.com").mkString
  }

  def curl(url: String) = {
    // While that also works, I don't like depending on wget or curl
    // when you can do everything you need from within the Scala/Java environment.
    s"curl $url" !!
  }

  def rm(file: String) = {
    println(s"deleting $file")
    s"rm -rf $file" !!
  }

  def main(args: Array[String]): Unit = {

//    rm("bd.pdf")
    curl("http://baidu.com")



  }

}
