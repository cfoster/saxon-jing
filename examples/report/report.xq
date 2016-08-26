xquery version "3.0";

declare namespace rng = "http://relaxng.org/ns/structure/1.0";

(: load and compile the schema validator (reporter) as a function :)
declare variable $report := rng:schema-report('report.rnc');

<out>{
  (: validate all documents in data, giving a report instead of
       throwing an error on an invalid document :)
  for $x in fn:collection('data')
  return
  <file uri="{document-uri(.)}">
    <xsl:sequence select="$report(.)" />          
  </file>
}</out>
