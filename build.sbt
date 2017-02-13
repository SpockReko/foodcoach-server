name := "foodcoach"

version := "1.0"

lazy val `foodcoach` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(javaJdbc, cache, javaWs)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.40"
libraryDependencies += "org.jsoup" % "jsoup" % "1.10.2"
libraryDependencies += "com.univocity" % "univocity-parsers" % "2.3.1"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

import java.net.URLClassLoader
def registerTask(name: String, taskClass: String, description: String) = {
  val sbtTask = (dependencyClasspath in Runtime) map { (deps) =>
    val depURLs = deps.map(_.data.toURI.toURL).toArray
    val classLoader = new URLClassLoader(depURLs, null)
    val task = classLoader.
      loadClass(taskClass).
      newInstance().
      asInstanceOf[Runnable]
    task.run()
  }
  TaskKey[Unit](name, description) := sbtTask.dependsOn(compile in Compile).value
}
registerTask("generate_db", "tasks.DatabaseGenerator", "Calling DatabaseGenerator from SBT")
