name := """discography"""
organization := "uk.co.chriswilding"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

wartremoverErrors in Compile ++= Warts.unsafe
wartremoverExcluded ++= routes.in(Compile).value

val disabledInTestWarts = Seq("NonUnitStatements", "OptionPartial")

scalacOptions in Test ~= {
  val disabledInTestWartsFlags =
    disabledInTestWarts.map(wart => s"-P:wartremover:traverser:org.wartremover.warts.$wart")
  _.filterNot(s => disabledInTestWartsFlags.contains(s))
}

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case manifest if manifest.contains("MANIFEST.MF") =>
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyJarName in assembly := "discography.jar"

test in assembly := {}
