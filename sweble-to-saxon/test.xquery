xquery version "1.0";
(:declare default element namespace "http://www.mediawiki.org/xml/export-0.7/";:)
declare namespace mw="http://www.mediawiki.org/xml/export-0.7/";
declare namespace swc="http://sweble.org/doc/site/tooling/sweble/sweble-wikitext";
declare namespace ptk="http://sweble.org/doc/site/tooling/parser-toolkit/ptk-xml-tools";
declare option saxon:output "method=xml";
declare option saxon:output "indent=yes";

let $namespaces := (
for $namespace in /mw:mediawiki/mw:siteinfo/mw:namespaces/mw:namespace
return
  swc:configureNamespace(data($namespace/@key), $namespace/text())
)

let $templates := $namespaces|(
for $page in /mw:mediawiki/mw:page
where $page/mw:ns = 10
return
  swc:storeTemplate($page/mw:title, $page/mw:revision[1]/mw:id, $page/mw:revision/mw:text/text())
)

for $page in /mw:mediawiki/mw:page
let $title := $page/mw:title/text()
let $bla := $templates|swc:parseMediaWiki($page/mw:revision[1]/mw:text)
let $warnings := $bla/ptk:ast//warnings 
where $page/mw:ns != 10 and $title = "جنوب السودان"
return 
(:(
    <title>{$title}</title>,
    <text> {
    for $text in $bla//ptk:t[matches(text(), "\p{IsArabic}+")]/text()
    return
      ($text, <seg/>) }
    </text>
):)
   ( 
   $title,
   $bla,
   string-join($bla//ptk:t/text()|$bla//InternalLink/target[not (../title//ptk:t)], " "), 
   "&#10;"
   ) 
(:   $bla/ptk:ast/CompiledPage/warnings:)
(:   $bla//ptk:t/text():)
