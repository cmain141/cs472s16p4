package edu.luc.cs

import java.util.concurrent.atomic.AtomicInteger

import com.ning.http.client.FluentCaseInsensitiveStringsMap
import com.ning.http.client.listener.TransferListener


class ProgressTransferListener extends TransferListener {

     var totalBytes: Option[Int] = None

     val bytesRead = new AtomicInteger(0)

     var status = Status.NotStarted

    def getstatus = status

    def getTotalBytes = totalBytes

    def getBytesRead = bytesRead.get()

    def addBytesRead(current: Int) = bytesRead.addAndGet(current)

    def setstatus(downloadstatus: Status.DownloadStatus) = this.status = downloadstatus

     def onRequestHeadersSent(headers: FluentCaseInsensitiveStringsMap): Unit = ()


     def onResponseHeadersReceived(headers: FluentCaseInsensitiveStringsMap): Unit = {
        val contentLength = Some(headers.getFirstValue("Content-Length"))
        if (contentLength.isDefined) {
            totalBytes = Some(contentLength.get.toInt)
        }
        status = Status.Running
    }

     def onBytesReceived(buffer: Array[Byte]): Unit = bytesRead.addAndGet(buffer.length)

     def onBytesSent(amount: Long, current: Long, total: Long): Unit = ()

     def onRequestResponseCompleted(): Unit = {
        status = Status.Completed
    }

     def onThrowable(throwable: Throwable): Unit = {
        status = Status.Failed
    }

}
