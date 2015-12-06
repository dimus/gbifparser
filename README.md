GBIF Parser
-----------

[![DOI](https://zenodo.org/badge/19435/dimus/gbifparser.svg)](https://zenodo.org/badge/latestdoi/19435/dimus/gbifparser)

This is a thin wrapper for GBIF Scientific Name Parser

Prerequisites
-------------

Working sbt

Usage
-----

Compile a 'fat jar' from the project root:

```
sbt assembly
```

Parse one name

```
java -jar target/scala-2.11/gbif-parser-assembly-0.1.0-SNAPSHOT.jar "Sogaella debeckeri meridionalis Leleup 1977"
```

Parse names from a file (one name per line)

```
java -jar target/scala-2.11/gbif-parser-assembly-0.1.0-SNAPSHOT.jar -input names.txt
```


