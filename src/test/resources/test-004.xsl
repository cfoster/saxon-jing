<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:template name="xsl:initial-template" as="empty-sequence()">
    
    <xsl:variable name="input">
      <doc>
        <my-doc>
          <date>2001-01-01</date>
          <text>Hello World</text>
          <unexpected-element>This should not be here</unexpected-element>
        </my-doc>
      </doc>
    </xsl:variable>

    <!-- should throw an error -->
    <xsl:sequence
      select="rng:schema('src/test/resources/simple-doc.rnc')($input)" />
    
  </xsl:template>


</xsl:stylesheet>
