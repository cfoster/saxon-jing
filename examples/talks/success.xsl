<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="rng"
  version="3.0">
  
  <xsl:template match="/">
    
    <!-- load and compile the schema validator as a function -->
    <xsl:variable name="schema" select="rng:schema('talks.rnc')" />
    
    <out>
      <!-- validate an XML node -->
      <xsl:sequence select="$schema(doc('xml-london-2016.xml'))" />
      Success!
    </out>
    
  </xsl:template>
  
</xsl:stylesheet>
