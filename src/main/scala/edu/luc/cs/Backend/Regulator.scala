package edu.luc.cs.Backend;

import java.net.{MalformedURLException, URL}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}

class Regulator {

  private val downloads = ListBuffer[Download]()
  private val asyncDownloader = new AsyncDownloader1

  // method to start download, passing through url and file name
  // if statement checks for either wrong URL type or no URL given
  def start(url: String, file: String): Unit = {

    if (Try(new URL(url)).isFailure || (new URL(url).getHost.isEmpty)) {
      throw new MalformedURLException
    }
    else {
      downloads += asyncDownloader.download(url, file)
    }
  }

  // method to cancel download, passing in Int identifying which download
  // Try used in case download is not present or Int is wrong; Try returns true if cancelled
  // If Try fails, then cancel fails
  def cancel(index: Int): Try[Boolean] = {
    searchList(index) {
      val download = downloads(index)
      val canceled = download.response.cancel(true)
      if (canceled) download.progress.setstatus(Status.Cancelled)
      Try(canceled)
    }
  }

  // method produces list with download data
  // first int is the download index
  // second int is the bytes sent
  // third is the bytes received, which uses Option in case none have been received
  // fourth is the status of the download
  // the for loop continues through the download list and maps each download separately
  def getDownloadList: List[(Int, Int, Option[Int], Status.DownloadStatus)] = (0 until downloads.size).map(getDownload(_).get)toList

  def getDownloadInfo(index: Int): Try[(Int, Int, Option[Int], Status.DownloadStatus)] = {
    searchList(index) {
      val download = downloads(index)
      Try(
        (index, download.progress.getBytesRead, download.progress.getTotalBytes, download.progress.getStatus)
      )
    }
  }

  // method looks through list for user input download index; if not found, error returned
  // if found, searchList works
  def searchList[T](index: Int)(command: => Try[T]) =
    if(index >= 0 && index < downloads.size) command
    else Failure(new IndexOutOfBoundsException)("Index is not valid; download not found")

}
