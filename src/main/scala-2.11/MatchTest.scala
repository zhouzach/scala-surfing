
object MatchTest extends App {
  var x: Any = Seq(1,2)
  x match {
    case s: String =>
      println("it is string")
    case n: Int =>
      println("it is int")
    case _ =>
      println("it is ohter")
  }

}
