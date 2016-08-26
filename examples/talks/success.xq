xquery version "3.0";

declare namespace rng = "http://relaxng.org/ns/structure/1.0";

(: load and compile the schema validator as a function :)
declare variable $schema := rng:schema('talks.rnc');

<out>{
  (: validate an XML node :)
  $schema(fn:doc('xml-london-2016.xml'))
}
Success!
</out>
