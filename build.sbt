name := "foodcoach"

version := "1.0"

lazy val `foodcoach` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(javaJdbc, cache, javaWs, filters)

// Database
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.40"

// HTTP
libraryDependencies += "edu.uci.ics" % "crawler4j" % "4.2"

// Parsers
libraryDependencies += "org.jsoup" % "jsoup" % "1.10.2"
libraryDependencies += "com.univocity" % "univocity-parsers" % "2.3.1"

// Helpers
libraryDependencies += "info.debatty" % "java-string-similarity" % "0.23"

// Visuals
libraryDependencies += "me.tongfei" % "progressbar" % "0.5.3"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

lazy val seed = taskKey[Unit]("Database seeder")
fullRunTask(seed, Compile, "tasks.DatabaseSeeder")

lazy val parse = taskKey[Unit]("Recipe page parser")
fullRunTask(parse, Compile, "tasks.RecipePageParser")
