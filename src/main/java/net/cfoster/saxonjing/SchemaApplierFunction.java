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

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.query.QueryResult;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.EmptySequence;
import org.xml.sax.InputSource;

import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

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

    StringWriter sw = new StringWriter();
    QueryResult.serialize(item, new StreamResult(sw), EMPTY_PROPERTIES);

    try {
      validator.validate(new InputSource(new StringReader(sw.toString())));
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
