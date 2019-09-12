<?xml version="1.1" encoding="UTF-8"?>
<!--
Test Case built around:
https://github.com/cfoster/saxon-jing/issues/5
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:output build-tree="false" />
  
  <xsl:template name="xsl:initial-template" as="empty-sequence()">

    <xsl:variable name="unc-path"
      select="resolve-uri('simple-doc.rnc', static-base-uri())"/>

    <xsl:variable name="applier-function"
      select="rng:schema-report($unc-path)" />

    <!-- copy of test-009, but produces the error for the sake of the bug -->
    <xsl:if test="
      not($applier-function instance of function(node()) as element(report))">
      <xsl:sequence select="error(xs:QName('rng:test-unc-paths'),
        'applier function sequence type is not correct')"/>
    </xsl:if>

  </xsl:template>


</xsl:stylesheet>
