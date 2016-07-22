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
    Sequence[] initialArguments) throws ValidateRngException
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

    try {
      validator.validate(new InputSource(node2stream(node)));
    } catch(ValidateRngException e) {
      throw new XPathException(e.getMessage(), e.getErrorCode(), context);
    }

    ErrorHandlerImpl eh = validator.getErrorHandler();

    String message = null;
    if(eh.fatal != null) message = eh.fatal.get(0).getMessage();
    else if(eh.error != null) message = eh.error.get(0).getMessage();

    if(message != null) {
      throw new ValidateRngException(message, Constants.ERR_INVALID)
        .createXPathException(context);
    }

    return EmptySequence.getInstance();
  }
}
