package crdt

import akka.actor._
import crdt.convergent.database._
import Counters._

object API {

  case class Command(name: String, args: List[String])

  val help = ":h"
  val quit = ":q"
  val helpText: String =
    s"""|$help - help
        |$quit - quit
        |
        |Counters
        |========
        |gcinc KEY [VALUE=1] - increment a GCounter
        |gcget KEY - get value of a GCounter
        |
        |""".stripMargin

  def welcome(host: String): String =
    s"""|Welcome to the CRDT sandbox API on $host
        |Try out $help for help""".stripMargin

  def parse(input: String): Command = {
    val parts = input.split(" ").toList
    Command(parts.head, parts.tail)
  }

  def props(implicit nodeName: String): Props = Props(new API)
}

class API(implicit nodeName: String) extends Actor {
  import API._

  val db = context.actorOf(Database.props, "db")

  def receive = {
    case Command(API.help, _) => sender ! API.helpText
    case Command("gcinc", key :: Nil) => db forward IncrementGCounter(key, 1)
    case Command("gcinc", key :: value :: Nil) => db forward IncrementGCounter(key, value.toInt)
    case Command("gcget", key :: Nil) => db forward GetValueGCounter(key)
    case Command(cmd, args) => sender ! s"Cmd `$cmd` with args: $args failed"
  }
}
