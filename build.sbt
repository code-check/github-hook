name := """github-hook"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "io.code-check" %% "github-api" % "0.1.1-SNAPSHOT"
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

resolvers ++= Seq(
  "Givery SBT Repository on Github" at "http://givery-technology.github.io/sbt-repo/"
)
