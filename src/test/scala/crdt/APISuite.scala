package crdt

import org.scalatest.FunSuite
import API._

class APISuite extends FunSuite {
  test("parse splits the input in head and tail") {
    val parsed = parse("cmd arg1 arg2")
    assert(("cmd", List("arg1", "arg2")) == parsed)
  }

  test("empty arglist") {
    assert(("cmd", Nil) == parse("cmd"))
  }

  test("empty input") {
    assert(("", Nil) == parse(""))
  }

  val pipe = parse _ andThen handle _

  test(":h yields helptext") {
    assert(helpText == pipe(":h"))
  }
}
