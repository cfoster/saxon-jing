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

import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.functions.CallableFunction;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.FunctionItemType;
import net.sf.saxon.type.SpecificFunctionType;
import net.sf.saxon.value.SequenceType;

public class SchemaFunction extends AbstractSchemaFunction
{
  private final FunctionItemType functionTest;

  public SchemaFunction() {
    functionName = "schema";

    functionTest = new SpecificFunctionType(
      new SequenceType[] { SequenceType.SINGLE_NODE },
      SequenceType.EMPTY_SEQUENCE
    );
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
            functionTest
          );
        } catch(ValidateRngException e) {
          throw e.createXPathException(context);
        }
      }
    };
  }

  @Override
  public SequenceType getResultType(SequenceType[] sequenceTypes) {
    return SequenceType.makeSequenceType(
      functionTest,
      StaticProperty.EXACTLY_ONE);
  }

}
