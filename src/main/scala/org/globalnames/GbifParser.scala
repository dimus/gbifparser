package org.globalnames.gbifparser

import org.gbif.nameparser._
import java.io.{BufferedWriter, FileWriter}
import scala.collection.parallel.ForkJoinTaskSupport
import scala.concurrent.forkjoin.ForkJoinPool
import scala.io.Source
import scala.util.{Failure, Success, Try}

object GbifParser {
  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      Console.err.println("No args found. Type -h for help")
      System.exit(0)
    }

    val argList = args.toList
    type OptionMap = Map[Symbol, String]
    val np = new NameParser(50)

    def nextOption(map: OptionMap, list: List[String]): OptionMap = {
      list match {
        case Nil => map
        case "-input" :: value :: tail =>
          nextOption(map ++ Map('input -> value), tail)
        case "-output" :: value :: tail =>
          nextOption(map ++ Map('output -> value), tail)
        case string :: Nil =>
          nextOption(map ++ Map('name -> string), list.tail)
        case option :: tail =>
          Console.err.println("Unknown option " + option)
          System.exit(1)
          map
      }
    }

    def parse(name: String): String = {
      Try(np.parse(name, null)) match {
        case Failure(_) => s"${name}\tFAILURE\tFAILURE"
        case Success(p) =>
          s"${name}\t${p.canonicalName}\t${p.authorshipComplete}"
      }

    }

    def startFileParse(inputFilePath: String, outputFilePath: String) = {
      Try(Source.fromFile(inputFilePath)) match {
        case Failure(e) => Console.err.println(s"No such file: $inputFilePath")
        case Success(f) =>
          val parallelism = Option(sys.props("parallelism")).map { _.toInt }
            .getOrElse(ForkJoinPool.getCommonPoolParallelism)
          println(s"running with parallelism: $parallelism")
          val parsedNamesCount = new java.util.concurrent.atomic.AtomicInteger()
          val namesInput = f.getLines().toVector.par
          namesInput.tasksupport =
            new ForkJoinTaskSupport(new ForkJoinPool(parallelism))
          val namesParsed = namesInput.map { name ⇒
            val currentParsedCount = parsedNamesCount.incrementAndGet()
            if (currentParsedCount % 10000 == 0) {
              println(s"Parsed $currentParsedCount of ${namesInput.size} lines")
            }
            parse(name.trim)
          }
          val writer = new BufferedWriter(new FileWriter(outputFilePath))
          namesParsed.seq.foreach { name ⇒
            writer.write(name + System.lineSeparator)
          }
          writer.close()
      }
    }

    val options = nextOption(Map(), argList)

    options match {
      case o if o.contains('input) =>
        val input = o('input)
        val output = if (o.contains('output)) o('output) else "output.tsv"
        startFileParse(input, output)
      case o if o.contains('name) =>
        print(parse(o('name)))
    }
    System.exit(0)
  }
}
