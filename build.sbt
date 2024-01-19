import scala.collection.immutable.Seq

val zioVersion = "2.0.20"
val zioJsonVersion = "0.5.0"
val zioHttpVersion = "3.0.0-RC2"
val scalaCsvVersion = "1.3.10"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "s-project",
    idePackagePrefix := Some("org.project.scala"),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-http" % zioHttpVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion,
      "com.github.tototoshi" %% "scala-csv" % scalaCsvVersion,
      "org.scalatest" %% "scalatest" % "3.2.15" % "test",
      "dev.zio" %% "zio-test" % "2.0.15" % Test,

    )
  )
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
