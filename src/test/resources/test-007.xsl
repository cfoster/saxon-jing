<?xml version="1.1" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:output build-tree="false" />

  <xsl:variable name="input-document">
    <doc>
      <my-doc>
        <date>2001-01-01</date>
        <text>Hello World</text>
      </my-doc>
    </doc>
  </xsl:variable>
  
  <xsl:template name="xsl:initial-template" as="empty-sequence()">

    <xsl:variable name="report" as="element(report)"
      select="
      rng:schema-report('src/test/resources/xml-11.rnc')(doc('xml-11.xml'))"/>
    
    <xsl:if test="count($report/element()) gt 0">
      <xsl:sequence select="error(xs:QName('rng:test-007'),
        'successful report should be empty')"/>
    </xsl:if>

  </xsl:template>


</xsl:stylesheet>
