package edu.luc.cs
import edu.luc.cs

import java.net.{MalformedURLException, URL}



import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}


class Manager() {

  private val downloads = ListBuffer[Download]()
  private val asyncDownloader = new AsyncDownloader

  def start(url: String, file: String): Unit = {
    if (Try(new URL(url)).isFailure || (new URL(url).getHost.isEmpty)) {
      throw new MalformedURLException
    }
    else {
      downloads += asyncDownloader.download(url, file)
    }
  }


  def cancel(index: Int): Try[Boolean] = {
    executeOverDownloadsList(index) {
      val download = downloads(index)
      val canceled = download.response.cancel(true)
      if (canceled) download.progress.setstatus(Status.Cancelled)
      Try(canceled)
    }
  }



  def getDownloads: List[(Int, Int, Option[Int], Status.DownloadStatus)] =
    (0 until downloads.size).map(getDownload(_).get).toList

  // number of bytes read and the total number of bytes
  def getDownload(index: Int): Try[(Int, Int, Option[Int], Status.DownloadStatus)] = {
    executeOverDownloadsList(index) {
      val download = downloads(index)
      Try(
        (index,
        download.progress.getBytesRead,
        download.progress.getTotalBytes,
        download.progress.getstatus)
      )
    }
  }

  def executeOverDownloadsList[T](index: Int)(command: => Try[T]) =
    if(index >= 0 && index < downloads.size) command
    else Failure(new IndexOutOfBoundsException("Index is invalid."))

}
