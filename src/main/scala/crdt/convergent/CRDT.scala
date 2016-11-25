package crdt.convergent

trait CRDT[T] {
  type Self <: CRDT[T]
  def value: T
  def merge(other: Self): Self
}

object CRDT {
  trait Counter extends CRDT[Int] {}
}
