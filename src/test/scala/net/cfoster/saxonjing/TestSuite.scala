package net.cfoster.saxonjing

import java.io.File
import javax.xml.transform.stream.StreamSource

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
    proc.registerExtensionFunction(new SchemaFunction())
    proc.registerExtensionFunction(new SchemaReportFunction())
    proc.newXsltCompiler().compile(
      new StreamSource(new File(PATH, sheet))
    ).load30().callTemplate(null)
  }


}
