name := """exsolnet"""

version := "0.1-PROTOTYPE"

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

jacoco.settings

