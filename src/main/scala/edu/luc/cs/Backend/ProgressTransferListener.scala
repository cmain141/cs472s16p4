package edu.luc.cs.Backend

import java.util.concurrent.atomic.AtomicInteger

import com.ning.http.client.FluentCaseInsensitiveStringsMap
import com.ning.http.client.listener.TransferListener

// class that works with code from Laufer
// makes sense of listeners
// applies listeners to bytes, status, and failure
class ProgressTransferListener extends TransferListener {

  var totalBytes: Option[Int] = None
  var bytesRead = new AtomicInteger(0)
  var status = Status.NotStarted

  def getstatus = status
  def getTotalBytes = totalBytes
  def getBytesRead = bytesRead.get()
  def setstatus(downloadstatus: Status.DownloadStatus) = this.status = downloadstatus

  def onRequestHeadersSent(headers: FluentCaseInsensitiveStringsMap): Unit = ()
  def onResponseHeadersSent(headers: FluentCaseInsensitiveStringsMap): Unit = {
    val contentLength = Some(headers.getFirstValue("Length"))
    if (contentLength.isDefined) {
      totalBytes = Some(contentLength.get.toInt)
    }
    status = Status.Running
  }
  def onBytesReceived(buffer: Array[Byte]): Unit = bytesRead.addAndGet(buffer.length)
  def onBytesSent(amount: Long, current: Long, total: Long): Unit = ()
  def onRequestResponseCompleted(): Unit = () {
    println("Download Complete")
    status = Status.Completed
  }

  def onThrowable(throwable: Throwable): Unit = {
    println("Download Failed")
    status = Status.Failed
  }
}
