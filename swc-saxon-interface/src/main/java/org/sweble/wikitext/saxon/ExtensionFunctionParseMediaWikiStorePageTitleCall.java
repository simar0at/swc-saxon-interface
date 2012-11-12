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

import javax.xml.bind.JAXBException;
import javax.xml.transform.dom.DOMSource;

import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfigImpl;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.EmptyIterator;
import net.sf.saxon.value.IntegerValue;
import net.sf.saxon.value.StringValue;

public class ExtensionFunctionParseMediaWikiStorePageTitleCall extends ExtensionFunctionParseMediaWikiCall {

	private static final long serialVersionUID = 6130119479298672307L;
	private WikiConfigImpl config = null;
	private DocumentInfo configDoc = null;

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
			DocumentInfo currentConfigDoc = (DocumentInfo) args[2].next();
			if (configDoc == null || !configDoc.equals(currentConfigDoc))
				try {
					configDoc = currentConfigDoc;
					config = WikiConfigImpl.load(new DOMSource(NodeOverNodeInfo.wrap(configDoc)));
				} catch (JAXBException e) {
					return EmptyIterator.getInstance();
				}
			
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
