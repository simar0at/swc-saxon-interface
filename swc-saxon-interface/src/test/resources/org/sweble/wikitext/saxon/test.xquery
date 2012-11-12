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

(:let $site := swc:configureSite(/mw:mediawiki/mw:siteinfo/mw:sitename,
replace(/mw:mediawiki/mw:siteinfo/mw:base, "wiki/.+$", "wiki/\$1"),
local:getIwPrefix(/mw:mediawiki/mw:siteinfo/mw:base),
local:getIwPrefix(/mw:mediawiki/mw:siteinfo/mw:base),
true()):)
let $baseConfig := swc:configureSiteFromURL(/mw:mediawiki/mw:siteinfo/mw:sitename,
    /mw:mediawiki/mw:siteinfo/mw:base, true(), true())    

let $config := swc:configureNamespace(data(/mw:mediawiki/mw:siteinfo/mw:namespaces/mw:namespace/@key), /mw:mediawiki/mw:siteinfo/mw:namespaces/mw:namespace, $baseConfig)

let $pageAllExistingTitlesStored := (
for $page in /mw:mediawiki/mw:page
where $page/mw:ns != 10
return
  swc:storePageTitle($page/mw:title, $page/mw:revision[1]/mw:id, $config)
)

let $templatesStored := $pageAllExistingTitlesStored|(
for $page in /mw:mediawiki/mw:page
where $page/mw:ns = 10
return
  swc:storeTemplate($page/mw:title, $page/mw:revision[1]/mw:id, $page/mw:revision/mw:text/text(), $config)
)


for $page in /mw:mediawiki/mw:page
let $title := $page/mw:title/text()
let $parsedPage := $templatesStored|swc:parseMediaWiki($title, $page/mw:revision[1]/mw:text, $config)
let $warnings := $parsedPage/ptk:ast//warnings
let $pageI := $parsedPage/ptk:ast/swc:EngCompiledPage//swc:EngPage
(:= $parsedPage/ptk:ast/swc:EngCompiledPage/child::*[1]/namespace-uri():)
(:where $page/mw:ns != 10 and $title = "جنوب السودان":)
(:where $page/mw:ns != 10 and $title = "النيل":)
(:where $title = "ألبانيا":)
(:where $title = "الامبراطوريه الرومانيه":)
(:where $title = "نايجيريا":)
(:where $title = "7 يناير":)
where $title = "اسرائيل"
return 
(:(
    <title>{$title}</title>,
    <text> {
    for $text in $bla//ptk:t[matches(text(), "\p{IsArabic}+")]/text()
    return
      ($text, <seg/>) }
    </text>
):)
(:   ( 
   $title,
   $bla,
   string-join($bla//ptk:t/text() except $bla//WtXmlAttribute/value/ptk:l/ptk:t/text()|
               $bla//WtInternalLink/target[not (../title//ptk:t)]/WtPageName/content except
               $bla/ptk:ast/EngCompiledPage/page/ptk:l/WtSection[last()]/body/ptk:l/ptk:l[last()]/WtInternalLink/target/WtPageName/content,
               " "), 
   "&#10;"
   ) :)
(:   $bla/ptk:ast/CompiledPage/warnings:)
   $pageI
(: every link which has no text but not the interwiki links at the bottom of the page
$bla//WtInternalLink/target[not(../title//ptk:t)] except $bla/ptk:ast/EngCompiledPage/page/ptk:l/WtSection[last()]/body/ptk:l/ptk:l[last()]/WtInternalLink/target :)

