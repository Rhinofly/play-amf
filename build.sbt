name := "play-amf"

organization := "nl.rhinofly"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.exadel.flamingo.flex" % "amf-serializer" % "1.5.0",
  "com.typesafe.play" %% "play" % "2.3.7" % "provided",
  "com.typesafe.play" %% "play-test" % "2.3.7" % "test",
  "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

publishTo := rhinoflyRepo(version.value)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

def rhinoflyRepo(version: String) = {
  val repo = if (version endsWith "SNAPSHOT") "snapshot" else "release"
  Some("Rhinofly Internal " + repo.capitalize + " Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-" + repo + "-local")
}

releaseSettings
