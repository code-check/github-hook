name := """github-hook"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  ws,
  cache,
  "com.ning" % "async-http-client" % "1.9.38",
  "joda-time" % "joda-time" % "2.9.3",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "org.json4s" %% "json4s-ext" % "3.3.0",
  "com.github.tototoshi" %% "play-json4s-jackson" % "0.5.0",
  "com.github.tototoshi" %% "play-json4s-test-jackson" % "0.5.0" % "test"
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

routesGenerator := InjectedRoutesGenerator

