<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="rng"
  version="3.0">
  
  <xsl:output indent="yes" />
  
  <xsl:template match="/">
    
    <!-- load and compile the schema validator (reporter) as a function -->
    <xsl:variable name="report" select="rng:schema-report('report.rnc')" />
    
    <out>
      <!-- validate all documents in data, giving a report instead of
           throwing an error on an invalid document -->
      <xsl:for-each select="collection('data')">
        <file uri="{document-uri(.)}">
          <xsl:sequence select="$report(.)" />          
        </file>
      </xsl:for-each>
    </out>
    
  </xsl:template>
  
</xsl:stylesheet>
