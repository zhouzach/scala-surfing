package org.rabbit.functions.collection

import java.text.SimpleDateFormat
import java.util.Date

object MultiGroupBy extends App {

  val mapSeq = Seq(
    Map("time" -> 20 * 24 * 60 * 60 * 1000, "instance" -> "root", "host" -> "192.168.1.20", "value" -> 101),
    Map("time" -> 20 * 24 * 60 * 60 * 1000, "instance" -> "root", "host" -> "192.168.1.21", "value" -> 103),
    Map("time" -> 20 * 24 * 60 * 60 * 1000, "instance" -> "dev", "host" -> "192.168.1.24", "value" -> 103),
    Map("time" -> 10 * 24 * 60 * 60 * 1000, "instance" -> "home", "host" -> "192.168.1.22", "value" -> 100),
    Map("time" -> 3 * 24 * 60 * 60 * 1000, "instance" -> "home", "host" -> "192.168.1.22", "value" -> 100),
    Map("time" -> 3 * 24 * 60 * 60 * 1000, "instance" -> "home", "host" -> "192.168.1.23", "value" -> 102))

  val groupedSeq = mapSeq.groupBy(_.getValue("instance", "")).map { case (instance, instanceSeq) =>

    val instanceGroup = instanceSeq.groupBy(_.getTime(default = "")).map { case (time, timeSeq) =>

      val hostValues = timeSeq.map { map =>
        (map.getValue("host", ""), BigDecimal(map.getValue("value", "0")))
      }.toMap

      (time, hostValues)
    }.toSeq.sortBy(_._1)

    (instance, instanceGroup)
  }.toSeq.sortBy(_._1)

  groupedSeq.foreach(println(_))

  //  System.exit(0)


  implicit class Options(opt: Option[Any]) {
    def getValue(default: String) = opt.getOrElse(default) match {
      case null => default
      case any => any.toString
    }
  }

  implicit class MapOps(map: Map[String, Any]) {
    def getValue(key: String, default: String) = map.getOrElse(key, default) match {
      case null => default
      case any => any.toString
    }

    def getTime(time: String = "time", default: String) = map.getOrElse(time, default) match {
      case null => default
      case any => any.toString.toLong.dateFormat("yyyy/MM/dd HH:mm:ss")
    }
  }

  implicit class LongDateFormat(val long: Long) {
    def dateFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String = {
      val dateFormat = new SimpleDateFormat(format)
      val date = new Date(long)
      dateFormat.format(date)
    }
  }

}
