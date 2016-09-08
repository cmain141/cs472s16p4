package edu.luc.cs.Userend

import java.net.{MalformedURLException, URL}
import edu.luc.cs.Regulator
import edu.luc.cs.Status._
import jline.console.ConsoleReader
import scala.util.Try


object Interface extends App {

  // the following RegEx's check for valid URL's
  // developed using regexer.com
  val CancelPattern = """^\s*([cC])\s+([0-9]+)\s*$""".r
  val ListPattern = """^\s*([lL])\s*$""".r
  val URLPattern = """^\s*(http.*)$""".r

  val console = new ConsoleReader
  val downloadManager = new Manager
  val downloadDir = scala.util.Properties.userHome
  val EOL = scala.util.Properties.lineSeparator

  printHelpMessage
  // takes input from user for download URLs
  console.setPrompt("download> ")
  Iterator continually {
    console.readLine()
  } takeWhile {
    isValid
  } foreach { (input: String) =>
    input match {
      case CancelPattern(command, index) => cancel(index.toInt)
      case ListPattern(command) => listAllDownloads(downloadManager.getDownloads)
      case URLPattern(url) =>
        if (Try(new URL(url)).isSuccess) {
          val fileName = url.substring(url.lastIndexOf('/'), url.length)
          try {
            downloadManager.start(url, downloadDir + fileName)
          } catch {
            case e: MalformedURLException => println("Invalid URL!")
          }
        } else
          println("Please inform a valid URL")
      case _ => {
        printHelpMessage
        println("Please inform a valid URL or one of the options.")
      }
    }
  }

  println()

//prompt the user for input and supports

  def cancel(index: Int) {
    val c = downloadManager.cancel(index)
    if (c.isFailure) println(c)
  }

  def printHelpMessage {
    println("\nPossible inputs:")
    println("\tl: List downloads")
    println("\tc <number>: Cancel download")
    println("\tq: Exit\n")
  }

  def listAllDownloads(list: List[(Int, Int, Option[Int], DownloadStatus)]): Unit = {
    val separator = "-" * console.getTerminal.getWidth
    // fileList is metadata of multidimensional array with info for each download
    val fileList = downloader.getDownLoads
    var count = 1
    println("\nDownload list")
    println(separator)
    // trying to build a table report of downloads here...
    println("#SNo.      Status      Bytes Rec'd      Bytes(Total)       %")
    println(separator)
    for(x <- fileList){
      // Using .get because it is a float coming from a Try
      // tutor helped determine this part of separating out list elements of download data
      var percentDownloaded = ((x._2.asInstanceOf[Float]/x._3.get)*100)
      println(count + "         " + x._4 + "          " x._2 + "           " x._3.get + "           " + f"$percentDownload%1.2f")
      count += 1
    }

  }

//  def isValid(s: String): Boolean = Option(s).isDefined && !(s.equals("q"))


}

