package org.zach.utils

object Commands {

  import sys.process._

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
