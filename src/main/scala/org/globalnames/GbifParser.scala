package org.globalnames.gbifparser

import org.gbif.nameparser._
import java.io.{File, PrintWriter}
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
        case Failure(_) => s"${name}\tFAILURE\tFAILURE\n"
        case Success(p) =>
          s"${name}\t${p.canonicalName}\t${p.authorshipComplete}\n"
      }

    }

    def startFileParse(input: String, output: String) = {
      val writer = new PrintWriter(new File(output))
      Try(Source.fromFile(input)) match {
        case Failure(e) => Console.err.println(s"No such file: $input")
        case Success(f) =>
          f.getLines().zipWithIndex.foreach {
            case (line, i) =>
              if ((i + 1) % 10000 == 0) println(s"Parsed ${i + 1} lines")
              writer.write(parse(line.trim))
          }
      }
      writer.close()
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
