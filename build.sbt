name := "crdt"
version := "0.0.1"
scalaVersion := "2.12.1"

enablePlugins(JavaAppPackaging)

libraryDependencies += "com.typesafe.conductr" % "scala-conductr-bundle-lib_2.12" % "1.5.0"
libraryDependencies += "com.typesafe.akka" % "akka-stream_2.12" % "2.4.14"
libraryDependencies += "com.typesafe.akka" % "akka-cluster_2.12" % "2.4.14"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test"

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
