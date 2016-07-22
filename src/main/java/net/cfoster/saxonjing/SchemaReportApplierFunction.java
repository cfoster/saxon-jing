/**
 * Copyright 2016 Charles Foster
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.text.MessageFormat;
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
    Sequence[] initialArguments) throws ValidateRngException
  {
    this.context = context;
    this.initialArguments = initialArguments;
    inputSource = fetchSchema(); // stacktrace helps with debugging
    validator = compileSchema(inputSource); // stacktrace helps with debugging
  }

  public InputSource fetchSchema() throws ValidateRngException
  {
    try
    {
      String arg1 = initialArguments[0].head().getStringValue();
      return new InputSource(URL(arg1).toExternalForm());
    }
    catch (XPathException e)
    {
      throw new RuntimeException(e); // unlikely to happen.
    }
  }

  public ValidateRng compileSchema(InputSource i) throws ValidateRngException
  {
    return new ValidateRng(i);
  }

  /**
   * Nodes passed to this are validated against cached RNG Schema
  **/
  public Sequence call(
    XPathContext context,
    Sequence[] arguments) throws XPathException
  {
    NodeInfo item = (NodeInfo)arguments[0].head();
    Node node = NodeOverNodeInfo.wrap(item);

    try
    {
      validator.validate(new InputSource(node2stream(node)));
    }
    catch(ValidateRngException e)
    {
      throw new XPathException(e.getMessage(), e.getErrorCode(), context);
    }

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
  public static final URL URL(String value) throws ValidateRngException
  {
    try
    {
      return new URL(value);
    }
    catch(MalformedURLException e)
    {
      try
      {
        return new File(value).toURI().toURL();
      }
      catch (MalformedURLException e1) {
        throw new ValidateRngException(
          MessageFormat.format(
            "Unable to find Schema ''{0}''. {1}",
             value, e.getMessage()), Constants.ERR_RNG_NOT_FOUND, e1
        );
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
