package net.cfoster.saxonjing;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.Callable;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceExtent;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SchemaReportApplierFunction implements Callable
{
  protected final XPathContext context;
  protected final Sequence[] initialArguments;
  protected final InputSource inputSource;
  protected final ValidateRng validator;

  /**
   * Constructor compiles RNG Schema for future use
   */
  public SchemaReportApplierFunction(
    XPathContext context,
    Sequence[] initialArguments)
  {
    this.context = context;
    this.initialArguments = initialArguments;
    inputSource = fetchSchema(); // stacktrace helps with debugging
    validator = compileSchema(inputSource); // stacktrace helps with debugging
  }

  public InputSource fetchSchema()
  {
    String arg1 = null;
    try {
      arg1 = initialArguments[0].head().getStringValue();
    }
    catch (XPathException e) {
      throw new RuntimeException(e);
    }

    return new InputSource(URL(arg1).toExternalForm());
  }

  public ValidateRng compileSchema(InputSource i) {
    return new ValidateRng(i);
  }

  /**
   * Nodes passed to this are validated against cached RNG Schema
  **/
  @Override
  public Sequence call(
    XPathContext context,
    Sequence[] arguments) throws XPathException
  {
    NodeInfo item = (NodeInfo)arguments[0].head();
    Node node = NodeOverNodeInfo.wrap(item);

    validator.validate(new InputSource(node2stream(node)));

    ErrorHandlerImpl eh = validator.getErrorHandler();
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    try
    {
      XMLStreamWriter out =
        XMLOutputFactory.newFactory().createXMLStreamWriter(buffer);

      out.writeStartElement("report");
      serializeErrorList("fatal","message", eh.fatal, out);
      serializeErrorList("error","message", eh.error, out);
      serializeErrorList("warning","message", eh.warning, out);
      out.writeEndElement(); // </report>
      out.flush();
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }

    TreeInfo outDocument = context.getConfiguration().buildDocumentTree(
      new StreamSource(new ByteArrayInputStream(buffer.toByteArray()))
    );

    SequenceIterator iter =
      outDocument.getRootNode().iterateAxis(AxisInfo.CHILD);

    return new SequenceExtent(
      new Item[] { iter.next() }
    );
  }

  protected final void serializeErrorList(
    String parentElementName,
    String childElementName,
    List <SAXParseException> errors,
    XMLStreamWriter out
  ) throws XMLStreamException
  {
    if(errors == null)
      return;

    out.writeStartElement(parentElementName);
    for(SAXParseException spe : errors)
      serializeErrorItem(childElementName, spe, out);
    out.writeEndElement();
  }

  protected final void serializeErrorItem(
    String elementName,
    SAXParseException e,
    XMLStreamWriter out) throws XMLStreamException
  {
    out.writeStartElement(elementName);

    if(e.getColumnNumber() > 0)
      out.writeAttribute("column-number", String.valueOf(e.getColumnNumber()));

    if(e.getLineNumber() > 0)
      out.writeAttribute("line-number", String.valueOf(e.getLineNumber()));

    if(e.getPublicId() != null)
      out.writeAttribute("public-id", e.getPublicId());

    if(e.getSystemId() != null)
      out.writeAttribute("system-id", e.getSystemId());

    if(e.getMessage() != null)
      out.writeCharacters(e.getMessage());

    out.writeEndElement(); // </sax-parse-exception>
  }

  /**
   * Converts a String value into a URL object.
   */
  public static final URL URL(String value)
  {
    try {
      return new URL(value);
    }
    catch(MalformedURLException e) {
      try {
        return new File(value).toURI().toURL();
      }
      catch (MalformedURLException e1) {
        throw new RuntimeException(e);
      }
    }
  }

  static final InputStream node2stream(Node node)
  {
    try {
      ByteArrayOutputStream o = new ByteArrayOutputStream();
      TransformerFactory.newInstance().newTransformer().transform(
        new DOMSource(node),
        new StreamResult(o)
      );
      return new ByteArrayInputStream(o.toByteArray());
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

}
