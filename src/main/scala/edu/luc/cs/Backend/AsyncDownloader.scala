package edu.luc.cs.Backend;

import java.io.{File, FileOutStream}

// http://www.ning.com/code/2010/03/introducing-nings-asynchronous-http-client-library/
import com.ning.http.client.AsyncHttpClientConfig.Builder
import com.ning.http.client.listener.{TransferCompleteHandler, TransferListener}
import com.ning.http.client.resumable.ResumableIOExceptionFilter
import com.ning.http.client.{AsyncHttpClient, FluentCaseSensitiveStringsMap}

// The following class is used per Laufer's direction, via https://gist.github.com/klaeufer/e6e3333004d157ad7e06
class AsyncDownloader {

  val client = new AsyncHttpClient(new Builder().addIOExceptionFilter(new ResumableIOExceptionFilter).build())

  def fileSaver(name: String) = new TransferListener {

    val file = new File(name)
    val stream:Option[FileOutputStream] = None

    // Listeners provided by Laufer; listen for download names, byte metadata, and completion.
    def onRequestHeadersSent (headers:FluentCaseInsensitiveStringsMap):Unit = ()
    def onResponseHeadersReceived (headers:FluentCaseInsensitiveStringsMap):
    Unit = stream = Some(new FileOutputStream(file))
    def onBytesReceived (buffer:Array[Byte]):Unit = stream.get.write(buffer)
    def onBytesSent (amount:Long, current:Long, total:Long):Unit = ()
    def onRequestResponseCompleted ():Unit = stream.get.close()
    def onThrowable (t:Throwable):Unit = stream.get.close()
  }

  def download(url: String, local: String): Download = {
    val t = new TransferCompletionHandler
    t.addTransferListener(fileSaver(local))
    val tR = new ProgressTransferListener
    t.addTransferListener(tR)

    if(tR.getBytesRead == 0){println("Download is starting")}
    val fR = client.prepareGet(url).execute(t)

    new Download(fR, tR)
  }

}

