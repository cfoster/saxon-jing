package net.cfoster.saxonjing;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;

public class ValidateRngException extends Exception
{
  protected String errorCode;

  public ValidateRngException(String message, String errorCode)
  {
    super(message);
    this.errorCode = errorCode;
  }

  public ValidateRngException(String message, String errorCode, Throwable e)
  {
    super(message, e);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public XPathException createXPathException(XPathContext context) {
    XPathException ex = new XPathException(getMessage(), getCause());
    ex.setErrorCode(errorCode);
    ex.setXPathContext(context);
    ex.setErrorCodeQName(
      new StructuredQName(
        "rng",
        "http://relaxng.org/ns/structure/1.0",
        errorCode)
    );
    return ex;
  }

}
