name := """exsolnet"""

version := "0.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  jdbc,
  evolutions
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1208"

libraryDependencies += "org.hamcrest" % "hamcrest-core" % "1.3"
libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19"

jacoco.settings



fork in run := true
