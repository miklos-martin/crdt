name := "crdt"
version := "0.0.1"
scalaVersion := "2.11.8"

enablePlugins(JavaAppPackaging)

libraryDependencies += "com.typesafe.conductr" %% "scala-conductr-bundle-lib" % "1.4.12"
libraryDependencies += "com.typesafe.akka" % "akka-stream_2.11" % "2.4.12"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"

import ByteConversions._

BundleKeys.nrOfCpus := 1.0
BundleKeys.memory := 512.MiB
BundleKeys.diskSpace := 5.MB
BundleKeys.roles := Set("crdt")
BundleKeys.system := "crdt"
BundleKeys.endpoints := Map(
  "crdt" -> Endpoint("tcp"),
  "akka-remote" -> Endpoint("tcp")
)
