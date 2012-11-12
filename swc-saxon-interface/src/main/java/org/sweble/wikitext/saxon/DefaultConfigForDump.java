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

import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfig;

public class DefaultConfigForDump extends DefaultConfig {

	private String siteName = "My Wiki";
	private String wikiUrl = "http://localhost/";
	private String contentLang = "en";
	private String ownIwPrefix = "en";

	DefaultConfigForDump() {
		super(new WikiConfigImpl());
		DefaultConfig.generate(this);
	}
	
	DefaultConfigForDump(String siteName, String wikiUrl, String contentLang, String ownIwPrefix) {
//		super(reportProblems ? new WikiConfigurationProblemReporter(): new WikiConfigImpl());
		super(new WikiConfigImpl());
		this.siteName = siteName;
		this.wikiUrl = wikiUrl;
		this.contentLang = contentLang;
		this.ownIwPrefix = ownIwPrefix;
		DefaultConfig.generate(this);
	}

	@Override
	public void configureWiki() {
		// --[ Properties of the wiki instance ]--

		c.setSiteName(siteName);

		c.setWikiUrl(wikiUrl);

		c.setContentLang(contentLang);

		c.setIwPrefix(ownIwPrefix);

		super.configureWiki();
	}

	@Override
	protected void addNamespaces() {			
		// rest is set up using this xquery function
	}

	WikiConfigImpl getConfig() {
		return c;
	}

}
