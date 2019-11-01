import sbtassembly.MergeStrategy._

name := "aerospike-spark"

version := "1.1.7"

organization := "com.aerospike"

crossScalaVersions := Seq("2.10.6", "2.12.8")

scalaVersion := "2.12.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

parallelExecution in test := false

fork in test := true

libraryDependencies ++= Seq(
  "org.apache.spark"           %% "spark-core"            % "2.4.3" % Provided,
  "org.apache.spark"           %% "spark-sql"             % "2.4.3" % Provided,
  "com.aerospike"              % "aerospike-query-engine" % "4.4.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.scalatest" %% "scalatest" % "3.0.7" % Test
)

resolvers ++= Seq("Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository")

cancelable in Global := true

assemblyMergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x) =>
    MergeStrategy.concat
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
    MergeStrategy.rename
  case PathList("META-INF", "maven","com.aerospike","aerospike-client", "pom.properties") =>
    MergeStrategy.discard
  case PathList("META-INF", "maven","com.aerospike","aerospike-client", "pom.xml") =>
    MergeStrategy.discard
  case PathList("META-INF", xs @ _*) =>
    xs.map(_.toLowerCase) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case ps @ (x :: _) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: _ =>
        MergeStrategy.discard
      case "services" :: _ =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.deduplicate
    }
  case _ => MergeStrategy.first
}
