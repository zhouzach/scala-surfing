package org.zach.functions.collection

import org.zach.functions.model.UserActiveRecord

object MaxByFunc extends App{

  val records = Seq(
    UserActiveRecord("uid01", last_mau = 1000L),
    UserActiveRecord("uid02", last_mau = 2000L),
    UserActiveRecord("uid03", last_mau = 3000L)
  )

  val maxMAU = records.maxBy(_.last_mau).last_mau
//  println(s"maxMAU: $maxMAU")

  val emptyRecords = Seq.empty[UserActiveRecord]
  //will throw exception
  val emptyMAU = emptyRecords.maxBy(_.last_mau).last_mau
  println(s"emptyMau: $emptyMAU")

}
