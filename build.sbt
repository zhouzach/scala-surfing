name := "scala-fun"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  Seq(
    "com.github.nscala-org.zach.time" %% "nscala-org.zach.time" % "2.4.0",
    "org.apache.hive" % "hive-jdbc" % "1.1.0",
    "org.apache.hadoop" % "hadoop-common" % "2.7.1",
    "com.alibaba" % "druid" % "1.0.28",
    "com.appadhoc" %% "adhoc-auth" % "1.0"
  )
}
    