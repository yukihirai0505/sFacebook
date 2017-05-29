import SonatypeKeys._

sonatypeSettings

name := "sFacebook"

version := "0.0.2"

scalaVersion := "2.11.8"

scalacOptions += "-feature"

version := "1.0-SNAPSHOT"

lazy val scalaRequestJson = ProjectRef(file("./git-submodules/scala-request-json"), "scala-request-json")
lazy val root = Project(id = "root", base = file("./"))
  .dependsOn(scalaRequestJson)
  .aggregate(scalaRequestJson)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" % "play-json_2.11" % "2.5.9",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "com.netaporter" %% "scala-uri" % "0.4.16",
  "com.github.tototoshi" %% "play-json-naming" % "1.1.0",
  "org.seleniumhq.selenium" % "selenium-java" % "2.53.1",
  "org.scalatest" % "scalatest_2.11" % "3.0.0"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

organization := "com.yukihirai0505"

organizationName := "com.yukihirai0505"

profileName := "com.yukihirai0505"

organizationHomepage := Some(url("https://yukihirai0505.github.io"))

description := "A Scala library for the Facebook API"

pomExtra :=
  <url>https://github.com/yukihirai0505/sFacebook</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>https://github.com/yukihirai0505/sFacebook/blob/master/LICENSE.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:yukihirai0505/sFacebook.git</url>
    <connection>scm:git:git@github.com:yukihirai0505/sFacebook.git</connection>
  </scm>
  <developers>
    <developer>
      <id>yukihirai0505</id>
      <name>Yuki Hirai</name>
      <url>https://yukihirai0505.github.io/</url>
    </developer>
  </developers>