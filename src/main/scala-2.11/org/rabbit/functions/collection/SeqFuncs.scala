package org.rabbit.functions.collection

object SeqFuncs {

  def main(args: Array[String]): Unit = {

    find()

  }

  def find() = {
    val s = Seq(1, 2, 3)

    //find返回集合中第一个匹配谓词函数的元素
    val i = s.find(p => p > 1)
    println(i.getOrElse(0))
  }

}
