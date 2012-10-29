package org.sweble.saxon;

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
	
	DefaultConfigForDump(String siteName, String wikiUrl, String contentLang, String ownIwPrefix, boolean reportProblems) {
		super(reportProblems ? new WikiConfigurationProblemReporter(): new WikiConfigImpl());
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
