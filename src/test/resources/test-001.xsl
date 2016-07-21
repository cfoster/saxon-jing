<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:output build-tree="false"/>

  <xsl:variable name="input-document">
    <doc>
      <my-doc>
        <date>2001-01-01</date>
        <text>Hello World</text>
        <unexpected-element>This should not be here.</unexpected-element>
      </my-doc>
    </doc>
  </xsl:variable>

  <xsl:template name="xsl:initial-template" as="empty-sequence()">

    <xsl:variable name="report" as="element(report)"
      select="
      rng:schema-report('src/test/resources/simple-doc.rnc')($input-document)"/>

    <!-- at time of writing, I can't get xsl:assert to issue an error despite
         the test resulting in false, despite using Saxon EE. -->
    
    <xsl:if test="count($report/fatal) ne 0">
      <xsl:sequence select="error(xs:QName('rng:test-001-a'), 'should have no fatal errors')"/>
    </xsl:if>
    <xsl:if test="count($report/warning) ne 0">
      <xsl:sequence select="error(xs:QName('rng:test-001-b'), 'should have no warning errors')"/>
    </xsl:if>
    <xsl:if test="count($report/error/element()) ne 1">
      <xsl:sequence select="error(xs:QName('rng:test-001-c'), 'should have one error')"/>
    </xsl:if>
  </xsl:template>


</xsl:stylesheet>
