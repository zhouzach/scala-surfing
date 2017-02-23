

object StringTest extends App {

  val db = "mongo"
  val dbStr = s"abc_$db"
  //  println(dbStr)

  val javaUUID = java.util.UUID.randomUUID.toString
  val splitUUID = javaUUID.split("-").toString
  val repUUID = javaUUID.replace('-', 't')

  val str2Int = "0010".toInt
//  println(s"str2Int: $str2Int")

}
