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

public class ExtensionFunctionParseMediaWikiConfigureSiteCall extends ExtensionFunctionParseMediaWikiCall {


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
		List<String> argsAsStrings = new ArrayList<String>(4);
		String arg;

		StringValue in = null;
		for (int i = 0; i < 4; i++) {
			in = (StringValue) args[i].next();
			if (null == in) {
				arg = "";
			} else {
				arg = in.getStringValue();
			}
			argsAsStrings.add(arg);
			in = null;
		}

		if (args.length == 5) {
			BooleanValue lastOption = (BooleanValue) args[4].next();
			if(null != lastOption) {
				reportProblems = lastOption.getBooleanValue();
			}
		}

		if (config == null) config = new DefaultConfigForDump(argsAsStrings.get(0), argsAsStrings.get(1), argsAsStrings.get(2), argsAsStrings.get(3), reportProblems).getConfig();

		return EmptyIterator.getInstance(); 
	}

}
