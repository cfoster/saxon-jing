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

  @Override
  public SequenceType getResultType(SequenceType[] sequenceTypes) {
    return SequenceType.OPTIONAL_FUNCTION_ITEM;
  }
}
