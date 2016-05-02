package edu.luc.cs

import java.io.{File, FileOutputStream}

import com.ning.http.client.AsyncHttpClientConfig.Builder
import com.ning.http.client.listener.{TransferCompletionHandler, TransferListener}
import com.ning.http.client.resumable.ResumableIOExceptionFilter
import com.ning.http.client.{AsyncHttpClient, FluentCaseInsensitiveStringsMap}


class AsyncDownloader {

   val client = new AsyncHttpClient(new Builder()
    .addIOExceptionFilter(new ResumableIOExceptionFilter).build())

  def fileSaver(name: String) = new TransferListener {
    val file = new File(name)
    var stream: Option[FileOutputStream] = None
    def onRequestHeadersSent(headers: FluentCaseInsensitiveStringsMap): Unit = ()
    def onResponseHeadersReceived(headers: FluentCaseInsensitiveStringsMap): Unit =
      stream = Some(new FileOutputStream(file))
    def onBytesReceived(buffer: Array[Byte]): Unit =
      stream.get.write(buffer)
    def onBytesSent(amount: Long, current: Long, total: Long): Unit = ()
    def onRequestResponseCompleted(): Unit = stream.get.close()
    def onThrowable(t: Throwable): Unit = stream.get.close()
  }



  def download(url: String, local: String): Download = {
    val t = new TransferCompletionHandler
    t.addTransferListener(fileSaver(local))
    val tR = new ProgressTransferListener
    t.addTransferListener(tR)
    val fR = client.prepareGet(url).execute(t)
    new Download(fR, tR)
  }

}
/* using this example
///////////////////////////////////////////////////////////////////////////////////////

// long download: https://downloads.gradle.org/distributions/gradle-2.3-src.zip
// short download: https://repo1.maven.org/maven2/com/ning/async-http-client/1.9.20/async-http-client-1.9.20.jar

// dependencies: see build.sbt

// usage: val f = download(url, localFileName)
// to cancel (if desired): f.cancel(true)

import com.ning.http.client._
import com.ning.http.client.listener._
import java.io.{ File, FileOutputStream }

def fileSaver(name: String) = new TransferListener {
  val file = new File(name)
  var stream: Option[FileOutputStream] = None
  def onRequestHeadersSent(headers: FluentCaseInsensitiveStringsMap): Unit = ()
  def onResponseHeadersReceived(headers: FluentCaseInsensitiveStringsMap): Unit =
  stream = Some(new FileOutputStream(file))
  def onBytesReceived(buffer: Array[Byte]): Unit =
  stream.get.write(buffer)
  def onBytesSent(amount: Long, current: Long, total: Long): Unit = ()
  def onRequestResponseCompleted(): Unit = stream.get.close()
  def onThrowable(t: Throwable): Unit = stream.get.close()
}

  val progressReporter = new TransferListener {
  def onRequestHeadersSent(headers: FluentCaseInsensitiveStringsMap): Unit = ()
  def onResponseHeadersReceived(headers: FluentCaseInsensitiveStringsMap): Unit = print("+")
  def onBytesReceived(buffer: Array[Byte]): Unit = print("*")
  def onBytesSent(amount: Long, current: Long, total: Long): Unit = ()
  def onRequestResponseCompleted(): Unit = println("!")
  def onThrowable(t: Throwable): Unit = { }
}

  val client = new AsyncHttpClient

  def download(url: String, local: String): ListenableFuture[Response] = {
  val t = new TransferCompletionHandler
  t.addTransferListener(fileSaver(local))
  t.addTransferListener(progressReporter)
  client.prepareGet(url).execute(t)
}*/