package crdt

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration._

case class Command(name: String, args: List[String])

class APIActor extends Actor {
  implicit val timeout = Timeout(1 second)

  def receive = {
    case Command(API.help, _) => sender ! API.helpText
    case Command(cmd, args) => sender ! s"Cmd `$cmd` with args: $args failed"
  }
}

object API {
  val help = ":h"
  val quit = ":q"
  val helpText: String =
    s"""|$help - help
        |$quit - quit""".stripMargin

  def welcome(host: String): String =
    s"""|Welcome to the CRDT sandbox API on $host
        |Try out $help for help""".stripMargin

  def parse(input: String): Command = {
    val parts = input.split(" ").toList
    Command(parts.head, parts.tail)
  }
}

