package crdt

import akka.stream._
import akka.stream.scaladsl._
import akka.actor.ActorSystem
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import com.typesafe.conductr.lib.scala.ConnectionContext.Implicits.global
import com.typesafe.conductr.bundlelib.scala.StatusService

object Endpoint extends App {
  implicit val system = ActorSystem("crdt")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val handler = Sink.foreach[Tcp.IncomingConnection] { connection =>
    import connection._
    println(s"New connection from: $remoteAddress")

    val commandHandler = Flow[String]
      .takeWhile(_ != API.quit)
      .map(API.parse _ andThen API.handle _)

    val serverLogic = Flow[ByteString]
      .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 128))
      .map(_.utf8String.trim.toLowerCase)
      .via(commandHandler)
      .merge(Source.single(API welcome localAddress.toString))
      .map(_ + s"\n[$localAddress]> ")
      .map(ByteString(_))

    connection.handleWith(serverLogic)
  }

  val config = ConfigFactory.load()
  val ip = config.getString("crdt.ip")
  val port = config.getInt("crdt.port")
  val connections = Tcp().bind(ip, port)
  val binding = connections.to(handler).run()

  binding
    .map { b => println(s"Server started on ${b.localAddress}") }
    .map { _ => StatusService.signalStartedOrExit() }
    .recover { case e: Throwable =>
      println(s"Server failed to start: ${e.getMessage}")
      system.terminate()
    }
}
