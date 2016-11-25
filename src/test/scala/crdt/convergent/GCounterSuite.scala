package crdt.convergent

import org.scalatest.FunSuite

class GCounterSuite extends FunSuite {
  test("value defaults to 0") {
    val counter = GCounter();
    assert(counter.value == 0)
  }

  test("can grow") {
    assert(GCounter().increment(1)("node1").value == 1)
  }

  test("can not increment with 0") {
    assertThrows[IllegalArgumentException] {
      GCounter().increment(0)("node1")
    }
  }

  test("can not decrement") {
    assertThrows[IllegalArgumentException] {
      GCounter().increment(-1)("node1")
    }
  }

  test("sums all nodes") {
    val counter = GCounter()
      .increment(1)("node1")
      .increment(2)("node2")

    assert(counter.value == 3)
  }

  test("merge resolves conflicting states by taking the max of each key") {
    val counter1 = GCounter()
      .increment(1)("node1")
      .increment(2)("node2")

    val counter2 = GCounter()
      .increment(3)("node1")
      .increment(1)("node2")
      .increment(1)("node3")

    val resolved = counter1 merge counter2

    assert(resolved.value == 6)
  }
}
