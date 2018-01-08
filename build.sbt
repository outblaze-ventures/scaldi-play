name := "scaldi-play"
organization := "org.scaldi"
version := "0.5.18-SNAPSHOT"

description := "Scaldi-Play - Scaldi integration for Play framework"
homepage := Some(url("http://scaldi.org"))
licenses := Seq("Apache License, ASL Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

scalaVersion := "2.12.3"
crossScalaVersions := Seq("2.11.11", "2.12.3")

scalacOptions ++= Seq("-deprecation", "-feature")
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")
testOptions in Test += Tests.Argument("-oDF")

val playVersion = "2.6.5"
val slickVersion = "3.0.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % playVersion % "provided",
  "com.typesafe.play" %% "play-guice" % playVersion % "provided",
  "org.scaldi" %% "scaldi-jsr330" % "0.5.9",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "com.typesafe.play" %% "play-test" % playVersion % "test",
  "com.typesafe.play" %% "play-slick" % slickVersion % "test",
  "com.typesafe.play" %% "play-slick-evolutions" % slickVersion % "test",
  "com.h2database" % "h2" % "1.4.196" % "test",
  "com.typesafe.play" %% "play-cache" % playVersion % "test" // cache plugin add extra bindings which have some specialties and will be tested automatically
)

git.remoteRepo := "git@github.com:scaldi/scaldi-play.git"


// Site and docs

site.settings
site.includeScaladoc()
ghpages.settings

// Publishing

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := (_ => false)
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
publishTo := Some(
  if (!version.value.trim.endsWith("SNAPSHOT"))
    "Muneris Releases Repository" at "https://team-repository.muneris.io/nexus/repository/thirdparty"
  else
    "Muneris Snapshots Repository" at "https://team-repository.muneris.io/nexus/repository/thirdparty-snapshots"
)

// nice prompt!

shellPrompt in ThisBuild := { state =>
  scala.Console.GREEN + Project.extract(state).currentRef.project + "> " + scala.Console.RESET
}

// Additional meta-info

startYear := Some(2011)
organizationHomepage := Some(url("https://github.com/scaldi"))
developers := Developer("OlegIlyenko", "Oleg Ilyenko", "", url("https://github.com/OlegIlyenko")) :: Nil
scmInfo := Some(ScmInfo(
  browseUrl = url("https://github.com/scaldi/scaldi-play"),
  connection = "scm:git:git@github.com:scaldi/scaldi-play.git"
))