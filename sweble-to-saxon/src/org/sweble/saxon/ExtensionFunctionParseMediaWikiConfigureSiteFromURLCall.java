package org.sweble.saxon;

import java.util.ArrayList;
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
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.IntegerValue;
import net.sf.saxon.value.StringValue;

public class ExtensionFunctionParseMediaWikiConfigureSiteFromURLCall 
		extends 
			ExtensionFunctionParseMediaWikiConfigureSiteCall 
{


	private static final long serialVersionUID = -6835617630116534748L;


	/**
	 * see also http://old.nabble.com/problem-returning-a-document-fragment-from-saxon-9.3-integrated-extension-function-td32318492.html
	 */


	@SuppressWarnings("unchecked")
	@Override
	public SequenceIterator<NodeInfo> call(
			@SuppressWarnings("rawtypes") SequenceIterator<? extends Item>[] args, XPathContext ctx)
					throws XPathException {
		boolean reportProblems = false;
		String arg, contentLang, iwPrefix, siteName, wikiUrl;

		if (args.length < 2)
		{
			return EmptyIterator.getInstance();
		}
		
		StringValue in = (StringValue) args[0].next();

		if(null == in) {
			return EmptyIterator.getInstance();
		}

		siteName = in.getStringValue();
		
		in = (StringValue) args[1].next();

		if(null == in) {
			return EmptyIterator.getInstance();
		}
		
		arg = in.getStringValue();
		wikiUrl =  arg.replaceAll("wiki/.+$", "wiki/\\$1");
		contentLang = iwPrefix = arg.replaceAll("http://", "").replaceAll("\\..*$", ""); 
		

		if (args.length == 2) {
			BooleanValue lastOption = (BooleanValue) args[4].next();
			if(null != lastOption) {
				reportProblems = lastOption.getBooleanValue();
			}
		}

		return call(siteName, wikiUrl, contentLang, iwPrefix, reportProblems, ctx);
	}

}
