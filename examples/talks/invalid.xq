xquery version "3.0";

declare namespace rng = "http://relaxng.org/ns/structure/1.0";

(: load and compile the schema validator as a function :)
declare variable $schema := rng:schema('talks.rnc');

declare variable $invalid-conference :=
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
  </conference>;

<out>{
  (: validate an XML node :)
  $schema($invalid-conference)
}
Success ????
</out>
