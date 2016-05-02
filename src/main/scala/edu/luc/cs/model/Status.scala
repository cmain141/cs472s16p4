package edu.luc.cs

object Status extends Enumeration {
  type DownloadStatus = Value
  val NotStarted, Running, Completed, Failed, Cancelled = Value
}
