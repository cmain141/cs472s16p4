package edu.luc.cs

import java.net.MalformedURLException

import edu.luc.cs.{Status, Manager}
import org.scalatest.FunSuite

class DownloadTest extends FunSuite {

  def fixtureUrl(): String = {
    "http://mirrors.xmission.com/eclipse/technology/epp/downloads/release/kepler/SR2/eclipse-jee-kepler-SR2-macosx-cocoa-x86_64.tar.gz"
  }
  def fixturePath(): String = {
    scala.util.Properties.userDir + "file.gz"
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
