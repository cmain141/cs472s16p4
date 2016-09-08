package edu.luc.cs

import java.net.MalformedURLException
import edu.luc.cs.{Status, Regulator}
import org.scalatest.FunSuite

class DownloadTest extends FunSuite {

  def fixtureUrl(): String = {
    "http://luc.edu/media/lucedu/studentdevelopment/orgcharts/LUCDSDOrgChartAugust2016.pdf"
  }

  def fixturePath(): String = {
    scala.util.Properties.userDir + "file.pdf"
  }

  def fixture(): Manager = {
    new Manager
  }

  test("empty url"){
    val manager = fixture()
    intercept[MalformedURLException] {
      manager.start("http://", fixturePath())
    }
  }

  test("invalid url"){
    val manager = fixture()
    intercept[MalformedURLException] {
      manager.start("",fixturePath())
    }
  }

}
