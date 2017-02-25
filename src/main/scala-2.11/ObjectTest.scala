
object ObjectTest extends App{

  case class People(name: String, age: Option[Int])
  case class Num(id: String, deleted: Option[String])

  var peos = Seq(People("xiao", Some(1))
    //    People("hong", Some(2)),
    //    People("ming",None)
  )

  val nums = Seq(Num("1",Some("false")),Num("2",Some("true")), Num("3",None),Num("4",Some("true")),Num("5",None))
  val filterNums = nums.map(_.deleted.getOrElse("")).distinct


  val filterMap =Map(1 -> "a", 2 -> "b").filter(m =>List(2,3).contains(m._1))
  //  filterMap.foreach(println)


  def remove(p: People, list: List[People]) = list diff List(p)
  val removedPeo = remove(People("xiao",Some(1)),peos.toList)

  val groupedPeo = removedPeo.groupBy(_.name).map(m => m._2.maxBy(_.age))

}
