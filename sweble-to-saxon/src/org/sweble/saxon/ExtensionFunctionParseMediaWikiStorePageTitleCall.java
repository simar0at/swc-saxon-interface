package org.sweble.saxon;

import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.EmptyIterator;
import net.sf.saxon.value.IntegerValue;
import net.sf.saxon.value.StringValue;

public class ExtensionFunctionParseMediaWikiStorePageTitleCall extends ExtensionFunctionParseMediaWikiCall {

	private static final long serialVersionUID = 6130119479298672307L;

	/**
	 * see also http://old.nabble.com/problem-returning-a-document-fragment-from-saxon-9.3-integrated-extension-function-td32318492.html
	 */
	
//	private FullPage retrieve(ExpansionFrame expansionFrame,
//				PageTitle pageTitle) {
//		// TODO: implement sth useful!
//		return null;
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SequenceIterator<NodeInfo> call(
			@SuppressWarnings("rawtypes") SequenceIterator<? extends Item>[] args, XPathContext ctx)
					throws XPathException {
		String title;
		int revision;
		try {
			StringValue in = (StringValue) args[0].next();

			if(null == in) {
				return EmptyIterator.getInstance();
			}
			
			IntegerValue inInt = (IntegerValue) args[1].next();
			
			if(null == inInt) {
				return EmptyIterator.getInstance();
			}
			
			revision = inInt.getDecimalValue().intValue();
			title = in.getStringValue();
			
			PageTitle p = PageTitle.make(config, title);
			knownPages.put(p.getTitle(), new FullPage(new PageId(p, revision), ""));
			
		} catch (Exception e) {
			
		}
		return EmptyIterator.getInstance(); 
	}

}
