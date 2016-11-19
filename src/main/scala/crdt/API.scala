package crdt

object API {
  val help = ":h"
  val quit = ":q"
  val helpText: String =
    s"""|$help - help
        |$quit - quit""".stripMargin

  def welcome(host: String): String =
    s"""|Welcome to the CRDT sandbox API on $host
        |Try out $help for help""".stripMargin

  def parse(input: String): (String, List[String]) = {
    val parts = input.split(" ").toList
    (parts.head, parts.tail)
  }

  def handle(parsedInput: (String, List[String])): String = parsedInput match {
    case (this.help, _) => helpText
    // other commands
    case (cmd, args) => s"Cmd `$cmd` with args: $args failed"
  }
}
