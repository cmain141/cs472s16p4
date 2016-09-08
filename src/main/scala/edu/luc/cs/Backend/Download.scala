package edu.luc.cs.Backend;

import com.ning.http.client.{ListenableFuture, Response}

class Download(
                val response: ListenableFuture[Response],
                val progress: ProgressTransferListener
              )
