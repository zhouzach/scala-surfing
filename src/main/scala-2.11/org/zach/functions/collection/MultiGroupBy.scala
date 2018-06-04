package org.zach.functions.collection

object MultiGroupBy extends App {

  val mapSeq = Seq(
    Map("time" -> 2, "instance" -> "root", "host" -> 22, "v" -> 101),
    Map("time" -> 2, "instance" -> "root", "host" -> 22, "v" -> 103),
    Map("time" -> 2, "instance" -> "dev", "host" -> 22, "v" -> 103),
    Map("time" -> 1, "instance" -> "home", "host" -> 22, "v" -> 100),
    Map("time" -> 3, "instance" -> "home", "host" -> 22, "v" -> 100),
    Map("time" -> 3, "instance" -> "home", "host" -> 23, "v" -> 102))

  val groupedSeq = mapSeq
    .groupBy(_.get("instance"))
    .map { instanceGroup =>
      val instanceSeq = instanceGroup._2.groupBy(_.get("time")).toSeq

      val hostSeq = instanceSeq
        .map { timeGroup =>
          val time = timeGroup._1
          (time, timeGroup._2.map { hostSeq =>

            (hostSeq.get("v"), hostSeq.get("host"))
          })

        }

      (instanceGroup._1, hostSeq)
    }
    .toSeq

  groupedSeq.foreach(println(_))
  //  System.exit(0)
}
