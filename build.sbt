name := "gbif-parser"
version := "0.1.0"
scalaVersion := "2.11.7"

resolvers += "gbif-all" at "http://repository.gbif.org/content/groups/gbif"

libraryDependencies ++= Seq(
"org.gbif" % "gbif-api" % "0.30",
"org.gbif" % "gbif-common" % "0.20",
"org.gbif" % "name-parser" % "2.10",
"org.slf4j" % "slf4j-api" % "1.7.12",
"commons-io" % "commons-io" % "2.4",
"org.apache.commons" % "commons-lang3" % "3.4",
"com.google.guava" % "guava" % "18.0",
"junit" % "junit" % "4.12" % "test",
"ch.qos.logback" % "logback-classic" % "1.1.3" % "test"
)
