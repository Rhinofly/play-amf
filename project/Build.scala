import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "play-amf"
  val appVersion = "2.0.1"

  val appDependencies = Seq(
	jdbc,
    anorm,
    "com.exadel.flamingo.flex" % "amf-serializer" % "1.5.0")

  def rhinoflyRepo(version:String) = {
    val repo = if (version endsWith "SNAPSHOT") "snapshot" else "release"
	Some("Rhinofly Internal " + repo.capitalize + " Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-" + repo + "-local")
  }
    
  val main = play.Project(appName, appVersion, appDependencies).settings(
    organization := "nl.rhinofly",
    publishTo <<= version(rhinoflyRepo),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
}
