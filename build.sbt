name := "foodcoach"

version := "1.0"

lazy val `foodcoach` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(javaJdbc, cache, javaWs)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.40"
libraryDependencies += "org.jsoup" % "jsoup" % "1.10.2"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
