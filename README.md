GBIF Parser
-----------

[![DOI][doisvg]][doi]

This is a thin wrapper for [GBIF Scientific Name Parser][gbifparser]

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
#to run on all available CPU threads
java -jar target/scala-2.11/gbif-parser-assembly-0.1.0-SNAPSHOT.jar -input names.txt

#to run 2 CPU threads
java -jar -Dparallelism=2 target/scala-2.11/gbif-parser-assembly-0.1.0-SNAPSHOT.jar -input names.txt
```

[doisvg]: https://zenodo.org/badge/19435/dimus/gbifparser.svg
[doi]: https://zenodo.org/badge/latestdoi/19435/dimus/gbifparser
[gbifparser]: https://github.com/gbif/name-parser
