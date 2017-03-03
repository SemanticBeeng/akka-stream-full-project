organization := "com.gvolpe"

name := """akka-streams-full-project"""

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

val akkaStreamVersion = "2.4.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaStreamVersion,
  "org.scalatest" %% "scalatest" % "3.0.1"
)

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := ".*StreamsApp.*"
