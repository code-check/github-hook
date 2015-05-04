name := """github-hook"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.github.tototoshi" %% "play-json4s-jackson" % "0.3.1",
  "com.github.tototoshi" %% "play-json4s-test-jackson" % "0.3.1" % "test"
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

resolvers ++= Seq(
  "Givery SBT Repository on Github" at "http://givery-technology.github.io/sbt-repo/"
)
