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

import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

public abstract class AbstractSchemaFunction extends ExtensionFunctionDefinition
{
  protected String functionName;

  @Override
  public StructuredQName getFunctionQName() {
    return new StructuredQName(
      "rng",
      "http://relaxng.org/ns/structure/1.0",
      functionName);
  }

  @Override
  public SequenceType[] getArgumentTypes() {
    SequenceType[] argTypes = new SequenceType[1];
    argTypes[0] = SequenceType.SINGLE_ITEM;
    return argTypes;
  }

}
