<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="rng"
  version="3.0">
  
  <xsl:template match="/">
    
    <!-- load and compile the schema validator as a function -->
    <xsl:variable name="schema" select="rng:schema('talks.rnc')" />
    
    <xsl:variable name="invalid-conference">
      <conference>
        
        <day name="Wednesday, December 1st" id="1">
          <talks>
            
            <talk>
              <title>JSON is great for Markup!</title>
              <abstract>
                <p>Yeah, well, you know. JSON!</p>
              </abstract>
              <ninjas>John Smith</ninjas>
            </talk>
          </talks>
        </day>
      </conference>
    </xsl:variable>
    
    <out>
      <!-- validate an XML node -->
      <xsl:sequence select="$schema($invalid-conference)" />
      Success ???
    </out>
    
  </xsl:template>
  
</xsl:stylesheet>
