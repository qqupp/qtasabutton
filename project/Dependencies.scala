import sbt._

object Dependencies {

  lazy val mainDependencies = cats ++ circe ++ http4s ++ logging

  lazy val testDependencies = scalaTest.map(_ % "test")

  lazy val scalaTest = Seq("org.scalatest" %% "scalatest" % "3.2.2")

  val catsVersion = "2.1.1"
  lazy val cats = Seq(
    "cats-core",
    "cats-kernel"
  ).map(m => "org.typelevel" %% m % catsVersion)

  val http4sVersion = "0.21.8"
  lazy val http4s = Seq(
    "org.http4s"      %% "http4s-blaze-server" % http4sVersion,
    "org.http4s"      %% "http4s-blaze-client" % http4sVersion,
    "org.http4s"      %% "http4s-circe"        % http4sVersion,
    "org.http4s"      %% "http4s-dsl"          % http4sVersion,
  )

  val circeVersion = "0.13.0"
  lazy val circe = Seq(
    "io.circe"        %% "circe-generic"       % circeVersion
  )

  val LogbackVersion = "1.2.3"
  lazy val logging = Seq(
    "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
  )

}
