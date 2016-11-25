package crdt.convergent.database

import akka.actor._
import crdt.convergent._
import CRDT.Counter

object Counters {
  trait CounterRelated

  case class IncrementGCounter(key: String, value: Int) extends CounterRelated
  case class GetValueGCounter(key: String) extends CounterRelated

  def props(implicit nodeName: String): Props = Props(new Counters)
}

import Counters._

class Counters(implicit nodeName: String) extends Actor {

  var counters = Map.empty[String, ActorRef]

  def receive = {
    case inc@IncrementGCounter(key, value) => counterFor(key) forward inc
    case get@GetValueGCounter(key) => counterFor(key) forward get
  }

  def counterFor(key: String): ActorRef =
    if (counters contains key) counters(key)
    else {
      val counter = context.actorOf(GCounterHolder.props, key)
      counters = counters + (key -> counter)
      counter
    }
}

object GCounterHolder {
  def props(implicit nodeName: String) = Props(new GCounterHolder)
}

class GCounterHolder(implicit nodeName: String) extends Actor {
  var counter = GCounter()

  def receive = {
    case IncrementGCounter(_, value) if value > 0 => {
      counter = counter increment value
      sender ! counter.value
    }
    case IncrementGCounter(_, _) => sender ! "Increment value for a GCounter must be greater than 0"

    case GetValueGCounter(_) => sender ! counter.value
  }
}
