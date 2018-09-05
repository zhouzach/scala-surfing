package org.zach.inheriting

/**
  * Created by zach on 2017/12/3.
  */
object PrivateTest {

  val s=Seq()
  val m = Map()
  val cache = collection.mutable.Map[String, String]()

}


class Rocket {
  import Rocket.fuel
  private def canGoHomeAgain = fuel > 20
}
object Rocket {
  private def fuel = 10
  def chooseStrategy(rocket: Rocket) = {
    if (rocket.canGoHomeAgain)
      goHome()
    else
      pickAStar()
  }
  def goHome() = {}
  def pickAStar() = {}
}

