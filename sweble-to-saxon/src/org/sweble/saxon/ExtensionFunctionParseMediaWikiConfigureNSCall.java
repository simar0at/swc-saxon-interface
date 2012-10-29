package org.sweble.saxon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.sweble.wikitext.engine.config.NamespaceImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.utils.DefaultConfig;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.EmptyIterator;
import net.sf.saxon.value.IntegerValue;
import net.sf.saxon.value.StringValue;

public class ExtensionFunctionParseMediaWikiConfigureNSCall extends ExtensionFunctionParseMediaWikiCall {


	private static final long serialVersionUID = -6835617630116534748L;

	private List<String> noAliases = Collections.emptyList();

	/**
	 * see also http://old.nabble.com/problem-returning-a-document-fragment-from-saxon-9.3-integrated-extension-function-td32318492.html
	 */


	@SuppressWarnings("unchecked")
	@Override
	public SequenceIterator<NodeInfo> call(
			@SuppressWarnings("rawtypes") SequenceIterator<? extends Item>[] args, XPathContext ctx)
					throws XPathException {
		int keyId;
		String prefix = ""; 

		IntegerValue inInt = (IntegerValue) args[0].next();
		if(null == inInt) {
			return EmptyIterator.getInstance();
		}

		StringValue in = (StringValue) args[1].next();
		if (null == in) {
			prefix = "";
		} else {
			prefix = in.getStringValue().replaceAll(" ", "_");
		}

		keyId = inInt.getDecimalValue().intValue();

		if (config == null) config = new DefaultConfigForDump().getConfig();
		WikiConfigImpl c = (WikiConfigImpl) config;
		NamespaceImpl n;
		if (keyId == 10)
			n = new NamespaceImpl(keyId, prefix, prefix, false, false, Arrays.asList("Template"));
		else if (keyId == 6)
			n = new NamespaceImpl(keyId, prefix, prefix, false, true, Arrays.asList("File", "Image"));
		else if (keyId == 14)
			n = new NamespaceImpl(keyId, prefix, prefix, false, false, Arrays.asList("Category"));
		else if (keyId == 4)
			n = new NamespaceImpl(keyId, prefix, prefix, false, false, Arrays.asList("Wikipedia", "WP"));
		else 
			n = new NamespaceImpl(keyId, prefix, prefix, false, false, noAliases);
		synchronized (config) {
			c.addNamespace(n);
			if (keyId == 0)
				c.setDefaultNamespace(n);
			if (keyId == 10) 
				c.setTemplateNamespace(n);	
		}

		return EmptyIterator.getInstance(); 
	}

}
