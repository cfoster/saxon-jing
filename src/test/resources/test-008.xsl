<?xml version="1.1" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:output build-tree="false" />
  
  <xsl:template name="xsl:initial-template" as="empty-sequence()">

    <xsl:variable name="applier-function"
      select="rng:schema('src/test/resources/simple-doc.rnc')" />
    
    <xsl:if test="
      not($applier-function instance of function(node()) as empty-sequence())">
      <xsl:sequence select="error(xs:QName('rng:test-008'),
        'applier function sequence type is not correct')"/>
    </xsl:if>

  </xsl:template>


</xsl:stylesheet>
