
import sbt._
import sbt.Default._
import sbt.Keys._

import scalariform.formatter.preferences._
import com.typesafe.sbtscalariform.ScalariformPlugin._

object AWS {
    val scalaVersion = "2.10.0"
    val version = "0.1-SNAPSHOT"
    val playVersion = "2.1-RC2"
    val repository = "AWS" at "http://pellucidanalytics.github.com/aws/repository/"

}

object ApplicationBuild extends Build {

    lazy val projectScalariformSettings = defaultScalariformSettings ++ Seq(
        ScalariformKeys.preferences := FormattingPreferences()
            .setPreference(AlignParameters, true)
            .setPreference(FormatXml, false)
    )

    lazy val commonSettings: Seq[Setting[_]] = Project.defaultSettings ++ projectScalariformSettings ++ Seq(
        organization := "aws",
        scalaVersion := AWS.scalaVersion,
        scalacOptions += "-feature",
        version := AWS.version,
        compileOrder in Compile := CompileOrder.ScalaThenJava,
        compileOrder in Test := CompileOrder.Mixed,
        resolvers ++= Seq(
          "typesafe" at "http://repo.typesafe.com/typesafe/releases",
          "sonatype" at "http://oss.sonatype.org/content/repositories/releases"
        ),
        libraryDependencies ++= Seq(
          "play" %% "play" % AWS.playVersion,
          "play" %% "play-java" % AWS.playVersion,
          "org.specs2" %% "specs2" % "1.12.3" % "test",
          "com.novocode" % "junit-interface" % "0.10-M2" % "test"),
        testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
        //testListeners <<= (target, streams).map((t, s) => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath, s.log)))
    )

    lazy val core = Project("core", file("core"), settings = commonSettings)

    //REVIST THIS AND MAKE IT WORK WITH NEW WAY OF DOING CREDENTIALS
    lazy val s3 = Project("s3", file("s3"), settings = commonSettings).dependsOn(core)

    //REVIST THIS AND MAKE IT WORK WITH NEW WAY OF DOING CREDENTIALS
    //lazy val sqs = Project("sqs", file("sqs"), settings = commonSettings).dependsOn(core)

    lazy val sns = Project("sns", file("sns"), settings = commonSettings).dependsOn(core)

    lazy val dynamodb = Project("dynamodb", file("dynamodb"), settings = commonSettings).dependsOn(core)

    lazy val simpledb = Project("simpledb", file("simpledb"), settings = commonSettings).dependsOn(core)

    //REVIST THIS AND MAKE IT WORK WITH NEW WAY OF DOING CREDENTIALS
    //lazy val ses = Project("ses", file("ses"), settings = commonSettings).dependsOn(core)

    //REVIST THIS AND MAKE IT WORK WITH NEW WAY OF DOING CREDENTIALS
    //lazy val cloudsearch = Project("cloud-search", file("cloudsearch"), settings = commonSettings).dependsOn(core)

    lazy val root = Project("root", file("."), settings = Project.defaultSettings ++ Unidoc.settings).aggregate(
        core, simpledb, sns, dynamodb
    )

}

