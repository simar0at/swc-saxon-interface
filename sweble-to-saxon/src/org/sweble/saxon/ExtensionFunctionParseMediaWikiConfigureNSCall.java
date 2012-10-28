package org.sweble.saxon;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.config.Interwiki;
import org.sweble.wikitext.engine.config.MagicWord;
import org.sweble.wikitext.engine.config.Namespace;
import org.sweble.wikitext.engine.ext.UnimplementedParserFunction;
import org.sweble.wikitext.engine.utils.AdaptedInterwiki;
import org.sweble.wikitext.engine.utils.AdaptedMagicWord;
import org.sweble.wikitext.engine.utils.AdaptedSimpleWikiConfiguration;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.postprocessor.ScopeType;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.EmptyIterator;
import net.sf.saxon.value.IntegerValue;
import net.sf.saxon.value.StringValue;

public class ExtensionFunctionParseMediaWikiConfigureNSCall extends ExtensionFunctionParseMediaWikiCall {

	
	private static final long serialVersionUID = -6835617630116534748L;
	private List<String> noAliases = Collections.emptyList();

	private class WikiConfigurationImpl extends SimpleWikiConfiguration {
		
		
		public WikiConfigurationImpl() {
		    super();
			String xmlConfig = "/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml";
			AdaptedSimpleWikiConfiguration swc = null;
				InputStream in = getClass().getResourceAsStream(xmlConfig);
				if (in == null)
					throw new RuntimeException("Default XML config not found at classpath:" + xmlConfig);
			try	{
				JAXBContext context = JAXBContext.newInstance(
						AdaptedSimpleWikiConfiguration.class);
				
				Unmarshaller m = context.createUnmarshaller();
				
				swc = (AdaptedSimpleWikiConfiguration) m.unmarshal(in);
				
				for (AdaptedInterwiki iw : swc.getInterwikiLinks().getInterwiki())
				{
					addInterwiki(new Interwiki(
							iw.getPrefix(),
							iw.getUrl(),
							iw.isLocal(),
							iw.isTrans()));
				}
				
				for (AdaptedMagicWord mw : swc.getMagicWords().getMagicWord())
				{
					addMagicWord(new MagicWord(
							mw.getName(),
							mw.isCaseSensitive(),
							mw.getAliases().getAlias()));
				}
				
			} catch (Exception e) {
				
			}
			setDefaultNamespace(new Namespace(0, "", "", false, false, noAliases));
			setTemplateNamespace(new Namespace(10, "Template", "Template", false, false, noAliases));
		}

		private static final long serialVersionUID = 1L;
		
		@Override
		public boolean isLocalInterwikiName(String iwName) {
			// TODO Do sth. meaningful ...
			return iwName.equals("arz");
		}
		
		@Override
		public boolean isNamespace(String nsName) {
			// TODO Auto-generated method stub
			return super.isNamespace(nsName);
		}
		
//		Only used for fetching sth. from another wiki
//		@Override
//		public Interwiki getInterwiki(String prefix) {
//			System.err.println("want Interwiki " + prefix);
//			return super.getInterwiki(prefix);
//		}
		
		@Override
		public MagicWord getMagicWord(String name) {
			System.err.println("want magic word " + name);
			return super.getMagicWord(name);
		}
		
		Set<String> knownMissingParserFunctions = new HashSet<String>();
		
		@Override
		public ParserFunctionBase getParserFunction(String name) {
			ParserFunctionBase result = super.getParserFunction(name);
			if (result instanceof UnimplementedParserFunction && knownMissingParserFunctions.add(name))
				System.err.println("want parser function " + name);
			return result;
		}

		Set<String> knownMissingXmlElementType = new HashSet<String>();

		@Override
		public ScopeType getXmlElementType(String name) {
			ScopeType result = super.getXmlElementType(name);
			if (knownMissingXmlElementType.add(name))
				System.err.println("want xml element type " + name);
			return result;
		}
		
		Set<String> knownMissingTags = new HashSet<String>();
		
		@Override
		public TagExtensionBase getTagExtension(String name) {
			TagExtensionBase result = super.getTagExtension(name);
			if (result == null && knownMissingTags.add(name))
				System.err.println("want tag extension " + name);
			return result;
		}
		
	}
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
		try {
			IntegerValue inInt = (IntegerValue) args[0].next();
			if(null == inInt) {
				return EmptyIterator.getInstance();
			}
			
			StringValue in = (StringValue) args[1].next();
			if(null == in) {
				return EmptyIterator.getInstance();
			}
			
			keyId = inInt.getDecimalValue().intValue();
			prefix = in.getStringValue();

			if (config == null) config = new WikiConfigurationImpl();
			WikiConfigurationImpl c = (WikiConfigurationImpl) config;
			Namespace n = new Namespace(keyId, prefix, prefix, false, false, noAliases);
			synchronized (config) {
				c.addNamespace(n);
				if (keyId == 0) c.setDefaultNamespace(n);
				if (keyId == 10) c.setTemplateNamespace(n);	
			}

		} catch (Exception e) {
			
		}
		return EmptyIterator.getInstance(); 
	}

}
