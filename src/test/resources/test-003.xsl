<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:template name="xsl:initial-template" as="empty-sequence()">
    
    <xsl:variable name="input">
      <e/>
    </xsl:variable>


    <xsl:variable name="f" as="function(node()) as element(report)"
      select="rng:schema-report('src/test/resources/bad-syntax.rnc')" />
    
    <xsl:sequence select="$f($input)" /> <!-- should throw error -->

  </xsl:template>


</xsl:stylesheet>
