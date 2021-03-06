name := "scala-surfing"

version := "1.0"

scalaVersion := "2.11.8"


resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "io.github.cloudify" %% "spdf" % "1.4.0",
  "joda-time" % "joda-time" % "2.9.9",
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  "org.apache.hive" % "hive-jdbc" % "1.1.0",
  "org.apache.hadoop" % "hadoop-common" % "2.7.1",
  "com.alibaba" % "druid" % "1.0.28",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "org.json4s" %% "json4s-native" % "3.3.0.RC6",
  "io.spray" %% "spray-can" % "1.3.3",
  "io.spray" %% "spray-routing" % "1.3.3",
  "io.spray" %% "spray-testkit" % "1.3.3" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  //    "com.appadhoc" %% "adhoc-auth" % "1.0",

  //ClassNotFoundException: akka.event.slf4j.Slf4jLoggingFilter
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.5.3",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.5.3",

  // https://mvnrepository.com/artifact/com.typesafe.akka/akka-http
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  // https://mvnrepository.com/artifact/com.typesafe.akka/akka-stream
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",

  "org.apache.poi" % "poi" % "4.0.0",
  "org.apache.poi" % "poi-ooxml" % "4.0.0",
  "org.jfree" % "jfreechart" % "1.5.0",

  // https://mvnrepository.com/artifact/org.mule.mchange/c3p0
  "com.mchange" % "c3p0" % "0.9.5.2",
  "commons-dbutils" % "commons-dbutils" % "1.6",
  "mysql" % "mysql-connector-java" % "5.1.44"



)

mainClass in Compile := Some("org.zach.concurrency.HelloActor")
    