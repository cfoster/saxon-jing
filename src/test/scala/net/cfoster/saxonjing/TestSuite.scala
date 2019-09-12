/**
 * Copyright 2016 Charles Foster
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.cfoster.saxonjing

import java.io.File
import javax.xml.transform.stream.StreamSource

import net.sf.saxon.Configuration
import net.sf.saxon.s9api._
import org.scalatest.FunSuite

class TestSuite extends FunSuite
{
  final val PATH: File = new File("src/test/resources")

  test("Produce Report in XSLT")
  {
    xsl("test-001.xsl")
  }

  /**
   * This test requires Saxon EE
   **/
  test("RelaxNG Schema Not there")
  {
    try {
      xsl("test-002.xsl", true)
    } catch {
      case ex : SaxonApiException => {
        assert(ex.getErrorCode.toString ==
          s"rng:${Constants.ERR_RNG_NOT_FOUND}")
        assert(ex.getMessage.startsWith("Unable to find Schema"))
      }
    }
  }

  /**
   * This test requires Saxon EE
   **/
  test("Bad Syntax RelaxNG Schema")
  {
    try {
      xsl("test-003.xsl", true)
    } catch {
      case ex : SaxonApiException => {
        assert(ex.getErrorCode.toString == s"rng:${Constants.ERR_RNG_SYNTAX}")
        assert(ex.getMessage.startsWith("Found Invalid RelaxNG Syntax."))
      }
    }
  }

  test("Instance Document Invalid")
  {
    try {
      xsl("test-004.xsl")
    } catch {
      case ex : SaxonApiException => {
        assert(ex.getErrorCode.toString ==
          s"rng:${Constants.ERR_INVALID}")
      }
    }
  }

  test("Instance Document Valid") {
    xsl("test-005.xsl")
  }

  test("Valid Instance Report Generated")
  {
    xsl("test-006.xsl")
  }

  test("Dealing with XML 1.1 documents")
  {
    xsl("test-007.xsl")
  }

  test("return type of rng:schema function")
  {
    xsl("test-008.xsl", true)
  }

  test("return type of rng:schema-report function")
  {
    xsl("test-009.xsl", true)
  }

  test("UNC Paths")
  {
    xsl("test-unc-paths.xsl", true)
  }

  test("Issue #4")
  {
    net.sf.saxon.Transform.main(
      Array(
        "-init:net.cfoster.saxonjing.JingInitializer",
        "-config:src/test/resources/config-4.xml",
        "-s:src/test/resources/test-009.xsl",
        "-xsl:src/test/resources/test-009.xsl",
        s"-o:${File.createTempFile("temp", System.nanoTime().toString())}.xml")
    )
  }


  test("Test String to URL")
  {
    assert(
      "http://a/b/c.xml" ==
        SchemaReportApplierFunction.URL("http://a/b/c.xml").toExternalForm)
    assert(new File("/a/b/c.xml").toURI.toURL.toExternalForm
      == SchemaReportApplierFunction.URL("/a/b/c.xml").toExternalForm)
    assert(new File("a/b/c.xml").toURI.toURL.toExternalForm
      == SchemaReportApplierFunction.URL("a/b/c.xml").toExternalForm)
  }

  def xsl(sheet : String, ee : Boolean = false): Unit = {
    val proc : Processor = new Processor(ee)
    // proc.setConfigurationProperty()
    proc.registerExtensionFunction(new SchemaFunction())
    proc.registerExtensionFunction(new SchemaReportFunction(proc))
    proc.newXsltCompiler().compile(
      new StreamSource(new File(PATH, sheet))
    ).load30().callTemplate(null)
  }


}
