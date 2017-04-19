name := "scala-fun"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  Seq(
//    "com.github.nscala-org.zach.time" %% "nscala-org.zach.time" % "2.4.0",
    "org.apache.hive" % "hive-jdbc" % "1.1.0",
    "org.apache.hadoop" % "hadoop-common" % "2.7.1",
    "com.alibaba" % "druid" % "1.0.28",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.json4s"          %%  "json4s-native" % "3.3.0.RC6",
    "io.spray"            %%  "spray-can"     % "1.3.3",
    "io.spray"            %%  "spray-routing" % "1.3.3",
    "io.spray"            %%  "spray-testkit" % "1.3.3" % "test",
    "org.scalatest"       %%  "scalatest"     % "2.2.1" % "test",
    "com.appadhoc" %% "adhoc-auth" % "1.0"
  )
}

mainClass in Compile := Some("org.zach.web.spray.main.Main")
    