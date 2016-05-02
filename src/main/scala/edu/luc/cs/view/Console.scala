package edu.luc.cs.view

import java.net.{MalformedURLException, URL}

import edu.luc.cs.Manager
import edu.luc.cs.Status._
import jline.console.ConsoleReader

import scala.util.Try

object Console extends App {

  val CancelPattern = """^\s*([cC])\s+([0-9]+)\s*$""".r
  val ListPattern = """^\s*([lL])\s*$""".r
  val URLPattern = """^\s*(http.*)$""".r

  val console = new ConsoleReader
  val downloadManager = new Manager
  val downloadDir = scala.util.Properties.userHome
  val EOL = scala.util.Properties.lineSeparator

  printHelpMessage
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
    println("\nDownload list")
    println(separator)
    println("#SNo.      Status")
    println(separator)
    println()
  }

  def isValid(s: String): Boolean = Option(s).isDefined && !(s.equals("q"))


}

