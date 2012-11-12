/**
 * Copyright 2012 Omar Siam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.saxon;

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
