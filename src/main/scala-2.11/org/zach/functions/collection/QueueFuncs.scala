package org.zach.functions.collection

object QueueFuncs {

}

trait Queue[+T] {
  def head: T

  def tail: Queue[T]

  def enqueue[U >: T](x: U) : Queue[U]
}

object Queue {
  def apply[T](xs: T*): Queue[T] =
    new QueueImpl[T](xs.toList, Nil)

  private class QueueImpl[+T](
                              private[this] var leading: List[T],
                              private[this] var trailing: List[T]
                            ) extends Queue[T] {
    private def mirror() =
      if (leading.isEmpty) {
        while (trailing.nonEmpty) {
          leading = trailing.head :: leading
          trailing = trailing.tail
        } }

    def head: T = {
      mirror()
      leading.head
    }

    def tail: QueueImpl[T] = {
      mirror()
      new QueueImpl(leading.tail, trailing)
    }

    def enqueue[U >: T](x: U) =
      new QueueImpl[U](leading, x :: trailing)
  }

}
