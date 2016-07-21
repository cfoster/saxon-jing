package com.hsbc.saxonjing

import java.io.File
import java.net.URL
import javax.xml.transform.stream.StreamSource

import net.cfoster.saxonjing.{SchemaFunction,
  SchemaReportApplierFunction,
  SchemaReportFunction}
import net.sf.saxon.s9api._
import net.sf.saxon.trans.XPathException
import org.scalatest.FunSuite

class TestSuite extends FunSuite
{
  final val PATH: File = new File("src/test/resources")

  test("Test String to URL") {

    var url: URL = null
    url = SchemaReportApplierFunction.URL("http://a/b/c.xml")
    assert("http://a/b/c.xml" == url.toExternalForm)
    url = SchemaReportApplierFunction.URL("/a/b/c.xml")
    assert(new File("/a/b/c.xml").toURI.toURL.toExternalForm
      == url.toExternalForm)
    url = SchemaReportApplierFunction.URL("a/b/c.xml")
    assert(new File("a/b/c.xml").toURI.toURL.toExternalForm
      == url.toExternalForm)
  }

  test("Successful Schema Report")
  {
    val proc : Processor = new Processor(false);
    proc.registerExtensionFunction(new SchemaReportFunction());
    val comp : XPathCompiler = proc.newXPathCompiler();
    comp.declareNamespace("rng", "http://relaxng.org/ns/structure/1.0");
    // comp.declareVariable(new QName("arg"));
    val exp : XPathExecutable =
      comp.compile("rng:schema-report('src/test/resources/simple-doc.rnc')"+
        "(doc('src/test/resources/simple-doc.xml'))");
    val ev : XPathSelector = exp.load();

    val v : XdmValue = ev.evaluate();
    val result : String = v.toString();

    assert("<report/>" == result)
  }

  test("Successful Schema Validate")
  {
    val proc : Processor = new Processor(false);
    proc.registerExtensionFunction(new SchemaFunction());
    val comp : XPathCompiler = proc.newXPathCompiler();
    comp.declareNamespace("rng", "http://relaxng.org/ns/structure/1.0");

    val exp : XPathExecutable =
      comp.compile("rng:schema('src/test/resources/simple-doc.rnc')"+
        "(doc('src/test/resources/simple-doc.xml'))");
    val ev : XPathSelector = exp.load();

    try {
      val v : XdmValue = ev.evaluate();
      assert(v == XdmEmptySequence.getInstance())
      println(v);
    } catch {
      case e : XPathException => fail("call must not throw error")
    }
    /** NO Exception Thrown **/
  }

  /**
   * This test requires Saxon EE
  **/
  test("Produce Report in XSLT")
  {
    val proc : Processor = new Processor(true);
    proc.registerExtensionFunction(new SchemaReportFunction());
    val exe : XsltExecutable = proc.newXsltCompiler().compile(
      new StreamSource(new File(PATH, "test-001.xsl"))
    )

    val transformer :  Xslt30Transformer = exe.load30()
    transformer.callTemplate(null)
  }

}
