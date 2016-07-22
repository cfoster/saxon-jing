package net.cfoster.saxonjing;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.functions.CallableFunction;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.AnyFunctionType;

public class SchemaFunction extends AbstractSchemaFunction
{
  public SchemaFunction() {
    functionName = "schema";
  }

  @Override
  public ExtensionFunctionCall makeCallExpression() {
    return new ExtensionFunctionCall() {
      @Override
      public Sequence call(
        XPathContext context, Sequence[] arguments) throws XPathException {
        try {
          return new CallableFunction(
            1, new SchemaApplierFunction(context, arguments),
            AnyFunctionType.ANY_FUNCTION
          );
        } catch(ValidateRngException e) {
          throw e.createXPathException(context);
        }
      }
    };
  }

}
