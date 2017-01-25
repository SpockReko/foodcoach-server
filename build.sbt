name := "foodcoach"

version := "1.0"

lazy val `foodcoach` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(javaJdbc, cache, javaWs)

libraryDependencies += "org.jsoup" % "jsoup" % "1.10.2"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
