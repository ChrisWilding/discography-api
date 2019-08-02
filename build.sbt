import play.sbt.routes.RoutesKeys

name := """discography"""
organization := "uk.co.chriswilding"
maintainer := "hello@chriswilding.co.uk"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(CodegenPlugin)
  .enablePlugins(PlayScala)

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-explaintypes",
  "-feature",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xcheckinit",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint:adapted-args",
  "-Xlint:by-name-right-associative",
  "-Xlint:constant",
  "-Xlint:delayedinit-select",
  "-Xlint:doc-detached",
  "-Xlint:inaccessible",
  "-Xlint:infer-any",
  "-Xlint:missing-interpolator",
  "-Xlint:nullary-override",
  "-Xlint:nullary-unit",
  "-Xlint:option-implicit",
  "-Xlint:package-object-classes",
  "-Xlint:poly-implicit-overload",
  "-Xlint:private-shadow",
  "-Xlint:stars-align",
  "-Xlint:type-parameter-shadow",
  "-Xlint:unsound-match",
  "-Yno-adapted-args",
  "-Ypartial-unification",
  "-Ywarn-dead-code",
  "-Ywarn-extra-implicit",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-Ywarn-value-discard"
)

autoCompilerPlugins := true
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "4.0.2",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.6",
  "org.sangria-graphql" %% "sangria" % "1.4.2",
  "org.sangria-graphql" %% "sangria-play-json" % "1.0.5",
  "org.sangria-graphql" %% "sangria-relay" % "1.4.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
)

sourceGenerators in Compile += slickCodegen
slickCodegenDatabaseUrl := "jdbc:postgresql://localhost:5432/discography"
slickCodegenDatabaseUser := "discography"
slickCodegenDatabasePassword := "discography"
slickCodegenOutputPackage := "discography.slick"
slickCodegenExcludedTables := Seq("flyway_schema_history")

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

RoutesKeys.routesImport := Seq.empty
