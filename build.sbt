lazy val commonSettings = Seq(
  libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5",
  libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.4",
  organization := "com.upax-research",
  version := "1.1.0",
  scalacOptions := Seq("-feature"),
  scalaVersion := "2.12.1")

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(name := "RhoClient")

lazy val demos = (project in file("demos"))
  .settings(commonSettings: _*)
  .settings(name := "Demos")
  .dependsOn(root)
