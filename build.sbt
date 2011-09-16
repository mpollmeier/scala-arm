resolvers += "junit interface repo" at "https://repository.jboss.org/nexus/content/repositories/scala-tools-releases"

organization := "com.github.jsuereth.scala-arm"

name := "scala-arm"

version := "1.1-SNAPSHOT"

scalaVersion := "2.9.1"

autoCompilerPlugins := true

libraryDependencies <<= (scalaVersion, libraryDependencies) { (ver, deps) =>
    deps :+ compilerPlugin("org.scala-lang.plugins" % "continuations" % ver)
}

scalacOptions += "-P:continuations:enable"

crossScalaVersions := Seq("2.9.1", "2.8.1", "2.8.0")

libraryDependencies += "junit" % "junit" % "4.5" % "test"

libraryDependencies += "com.novocode" % "junit-interface" % "0.7" % "test->default"

libraryDependencies += "org.apache.derby" % "derby" % "10.5.3.0_1" % "test"

// Publish to scala-tools Nexus
publishTo <<= version { (v: String) =>
    val nexus = "http://nexus.scala-tools.org/content/repositories/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "snapshots/") 
    else                             Some("releases"  at nexus + "releases/")
}

publishMavenStyle := true

publishArtifact in Test := false

seq(org.clapper.sbt.lwm.LWM.lwmSettings: _*)

LWM.sourceFiles in LWM.Config <++= baseDirectory(d => (d / "src" / "site" ** "*.md").get)

LWM.targetDirectory in LWM.Config <<= target(_ / "site")

seq(ghpages.settings:_*)

ghpages.gitRemoteRepo := "git@github.com:jsuereth/scala-arm.git"

ghpages.genSite <<= (ghpages.genSite, LWM.translate in LWM.Config) map ((_,_) => ())
