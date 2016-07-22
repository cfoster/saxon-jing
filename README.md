# Saxon Jing

XPath 3.0 Extension Functions for Saxon.

Enables Saxon to validate XML documents with a RelaxNG Compact Syntax Schema during within XSLT 3.0 or XQuery 3.0 / 3.1.

The following two functions are provided

```
namespace xmlns:rng = "http://relaxng.org/ns/structure/1.0"

rng:schema($uri as xs:string) as function(node()) as empty-sequence()

rng:schema-report($uri as xs:string) as function(node()) as element(report)
```

* The `$uri` parameter can either be a path to a local file or a URI/URL location.
* Each function returns a higher order function, which can then be used to validate any number of XML documents.
* The first function will throw an Error if a given document is not valid according to the RelaxNG Schema.
* The second function generates a report detailing errors, fatal errors and warnings (if any) for a given instance document should not thrown an error.

## Example XSLT code

```xslt
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:rng="http://relaxng.org/ns/structure/1.0"
  exclude-result-prefixes="xs"
  version="3.0">

  <xsl:template name="xsl:initial-template">
    
    <xsl:variable name="input-1">
      <my-instance>
        <e>Hello World</e>
      </my-instance>
    </xsl:variable>
    
    <xsl:variable name="input-2">
      <my-instance>
        <e>Goodbye World</e>
      </my-instance>
    </xsl:variable>

    <xsl:variable
      name="validator"
      as="function(node()) as element(report)"
      select="rng:schema-report('my-schema.rnc')" />

    <xsl:variable
      name="report-generator"
      as="function(node()) as element(report)"
      select="rng:schema-report('my-schema.rnc')" />

	<!-- straight forward validation -->    
    <xsl:sequence select="$validator($input-1)" /> <!-- empty sequence -->
    <xsl:sequence select="$validator($input-2)" /> <!-- empty sequence -->

	<!-- generate report XML elements -->
    <xsl:variable name="report-1" as="element(report)"
      select="$report-generator($input-1)" />

    <xsl:variable name="report-2" as="element(report)"
      select="$report-generator($input-2)" />

  </xsl:template>

</xsl:stylesheet>
```

## Error Codes


| ERROR CODE | MEANING                                                |
|------------|--------------------------------------------------------|
| SXJG0001   | Jing can not be found on Classpath                     |
| SXJG0002   | Syntax Error in Relax NG Schema                        |
| SXJG0004   | Could not find Relax NG Schema                         |
| SXJG0005   | Generic Exception whilst trying to load RelaxNG Schema |
| SXJG0006   | Schema Validation Failed                               |
| SXJG0007   | Unknown Exception                                      |
