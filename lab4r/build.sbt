ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "lab4r"
  )

val CatsVersion = "3.6.3"
val SttpClient4Version = "4.0.0-M10"
val Log4CatsVersion = "2.2.0"

libraryDependencies ++= Seq(
  "com.bot4s"     %% "telegram-core"       % "7.0.0",
  "org.typelevel" %% "cats-effect"         % CatsVersion,
  "org.typelevel" %% "log4cats-slf4j"      % Log4CatsVersion,
  "com.softwaremill.sttp.client4" %% "async-http-client-backend-cats" % SttpClient4Version
)

libraryDependencies ++= Seq(
  "com.davegurnell" %% "unindent"       % "1.7.0",
  "eu.timepit"      %% "refined"        % "0.9.28"
)
