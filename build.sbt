import Dependencies._

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "queuegarden",
    libraryDependencies ++= testDependencies ++ mainDependencies,
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-language:higherKinds",
      "-language:postfixOps",
      "-feature",
      "-Xfatal-warnings",
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oSD"),
    run / fork := true
  )
  .enablePlugins(JavaServerAppPackaging)
  .enablePlugins(UniversalPlugin)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
