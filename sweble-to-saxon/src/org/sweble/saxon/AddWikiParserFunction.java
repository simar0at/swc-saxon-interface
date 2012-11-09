package org.sweble.saxon;

import javax.xml.transform.TransformerException;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.Initializer;

public class AddWikiParserFunction implements Initializer {

	@Override
	public void initialize(Configuration arg0) throws TransformerException {
		arg0.registerExtensionFunction(new ExtensionFunctionParseMediaWiki());
		arg0.registerExtensionFunction(new ExtensionFunctionParseMediaWikiConfigureNS());
		arg0.registerExtensionFunction(new ExtensionFunctionParseMediaWikiStoreTemplate());
		arg0.registerExtensionFunction(new ExtensionFunctionParseMediaWikiConfigureSite());		
		arg0.registerExtensionFunction(new ExtensionFunctionParseMediaWikiConfigureSiteFromURL());		
		arg0.registerExtensionFunction(new ExtensionFunctionParseMediaWikiStorePageTitle());	}

}
