package crdt.convergent.database

import akka.actor._
import Counters._

object Database {
  def props(implicit nodeName: String): Props = Props(new Database)
}

class Database(implicit nodeName: String) extends Actor {
  val counters = context.actorOf(Counters.props, name = "counters")

  def receive = {
    case counterMessage: CounterRelated => counters forward counterMessage
  }
}
