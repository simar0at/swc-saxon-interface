package org.sweble.saxon;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.sweble.wikitext.engine.config.ParserConfigImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;

public class ParserConfigProblemReporter extends ParserConfigImpl implements Serializable {

	private static final long serialVersionUID = 2581699799278441561L;

	ParserConfigProblemReporter(WikiConfigImpl c) {
		super(c);
	}
	
	@Override
	public boolean isInterwikiName(String iwName) {
		boolean res = super.isInterwikiName(iwName);
		if (!res)
			System.err.println("is " + iwName + " an Interwiki Name?");
		return res;
	}
	
	@Override
	public boolean isNamespace(String nsName) {
		boolean res = super.isNamespace(nsName);
//		if (!res)
//			System.err.println("is " + nsName + " a Namespace?");
		return res;
	}


	Set<String> knownMissingXmlEntities = new HashSet<String>();
		
	@Override
	public boolean isValidXmlEntityRef(String name) {
		boolean res = super.isValidXmlEntityRef(name);
		if (!res && !knownMissingXmlEntities.contains(name)) {
			System.err.println("is " + name + " an XML entity?");
			knownMissingXmlEntities.add(name);
		}
		return res;
	}
}
