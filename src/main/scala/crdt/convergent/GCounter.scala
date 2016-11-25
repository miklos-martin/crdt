package crdt.convergent

case class GCounter(private val state: Map[String, Int] = Map.empty[String, Int]) extends CRDT.Counter {
  type Self = GCounter

  def value = state.values.sum

  def increment(value: Int = 1)(implicit key: String): GCounter = {
    require(value > 0, "A GCounter can only be incremented with values greater than 0")
    if (state.contains(key)) GCounter(state + (key -> (state(key) + value)))
    else GCounter(state + (key -> value))
  }

  def merge(that: GCounter): GCounter =
    (state.keySet ++ that.state.keySet).foldLeft(GCounter()) { (acc, key) =>
      acc.increment(Math.max(state.getOrElse(key, 0), that.state.getOrElse(key, 0)))(key)
    }
}
