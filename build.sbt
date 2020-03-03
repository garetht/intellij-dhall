import ReleaseTransformations._

intellijPluginName in ThisBuild := "intellij-dhall"
intellijBuild in ThisBuild := "193.4778.7"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion, // performs the initial git checks
  tagRelease,
  pushChanges,
  setNextVersion,
  commitNextVersion,
)

releaseTagComment        := s"[auto-package] Release ${(version in ThisBuild).value}"
releaseCommitMessage     := s"[auto-package] Set release version to ${(version in ThisBuild).value}"
releaseNextCommitMessage := s"[auto-package] Set next development version to ${(version in ThisBuild).value}"

lazy val dhall = project
  .in(file("."))
  .enablePlugins(SbtIdeaPlugin)
  .settings(
    scalaVersion := "2.13.1",
    scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala",
    scalaSource in Test := baseDirectory.value / "src" / "test" / "scala",
    ideBasePackages := Seq("org.intellij.plugins.dhall"),
    unmanagedSourceDirectories in Compile += baseDirectory.value / "gen",
    resourceDirectory in Compile := baseDirectory.value / "resources",

    packageMethod := PackagingMethod.Standalone(targetPath = s"lib/${name.value}-${(version in ThisBuild).value}.jar"),

    patchPluginXml := pluginXmlOptions { xml =>
      xml.version = (version in ThisBuild).value
      xml.sinceBuild = (intellijBuild in ThisBuild).value
    },
    intellijExternalPlugins += IntellijPlugin
      .Id("PsiViewer", Some("193-SNAPSHOT"), None),
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % Test
    )
  )

lazy val runner = createRunnerProject(dhall, "dhall-runner")
