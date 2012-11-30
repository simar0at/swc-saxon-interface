xquery version "1.0";
(:declare default element namespace "http://www.mediawiki.org/xml/export-0.7/";:)
declare namespace mw="http://www.mediawiki.org/xml/export-0.7/";
declare namespace swc="http://sweble.org/doc/site/tooling/sweble/sweble-wikitext";
declare namespace ptk="http://sweble.org/doc/site/tooling/parser-toolkit/ptk-xml-tools";
declare option saxon:output "method=xml";
declare option saxon:output "indent=yes";

declare function local:getIwPrefix($s as xs:string) as xs:string {
    replace(replace($s, "http://", ""), "\..*$", "")
};

let $baseConfig := swc:configureSite("My English Wiki",
"http://localhost/",
"en",
"en",
true())

let $config := swc:configureNamespace((-2, -1, 0, 1), ("Media", "Special", "", "Talk"), $baseConfig)

return
    $config