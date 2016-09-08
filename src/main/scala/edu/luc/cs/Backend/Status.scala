package edu.luc.cs.Backend

// enumerator allowing status to be one of many values
object Status extends Enumeration {
  type DownloadStatus = Value
  val NotStarted, Running, Completed, Failed, Cancelled = Value
}
