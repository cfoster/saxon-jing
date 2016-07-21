package net.cfoster.saxonjing;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.EmptySequence;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class SchemaApplierFunction extends SchemaReportApplierFunction
{
  public SchemaApplierFunction(
    XPathContext context,
    Sequence[] initialArguments)
  {
    super(context, initialArguments);
  }

  @Override
  public Sequence call(
    XPathContext context,
    Sequence[] arguments) throws XPathException
  {
    NodeInfo item = (NodeInfo)arguments[0].head();
    Node node = NodeOverNodeInfo.wrap(item);

    validator.validate(new InputSource(node2stream(node)));
    ErrorHandlerImpl eh = validator.getErrorHandler();

    if(eh.fatal != null)
      throw new XPathException(eh.fatal.get(0));
    if(eh.error != null)
      throw new XPathException(eh.error.get(0));

    return EmptySequence.getInstance();
  }
}
