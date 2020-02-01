intellijPluginName in ThisBuild := "intellij-dhall"
intellijBuild in ThisBuild := "193.4778.7"

lazy val dhall = project
  .in(file("."))
  .enablePlugins(SbtIdeaPlugin)
  .settings(
    scalaVersion := "2.13.1",
    version := "2019.3.0",
    scalaSource in Compile := baseDirectory.value / "src" / "main" / "scala",
    ideBasePackages := Seq("org.intellij.plugins.dhall"),
    unmanagedSourceDirectories in Compile += baseDirectory.value / "gen",
    resourceDirectory in Compile := baseDirectory.value / "resources",
    patchPluginXml := pluginXmlOptions { xml =>
      xml.version = version.value
    },
    intellijExternalPlugins += IntellijPlugin
      .Id("PsiViewer", Some("193-SNAPSHOT"), None)
  )

lazy val runner = createRunnerProject(dhall, "dhall-runner")
