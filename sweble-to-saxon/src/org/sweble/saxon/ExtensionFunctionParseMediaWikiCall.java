package org.sweble.saxon;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.xml.SerializationException;
import de.fau.cs.osr.ptk.common.xml.XmlWriter;
import de.fau.cs.osr.utils.NameAbbrevService;

import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionDebugHooks;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.ExpansionVisitor;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.WtEngine;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.lognodes.ResolveParserFunctionLog;
import org.sweble.wikitext.engine.lognodes.ResolveTagExtensionLog;
import org.sweble.wikitext.engine.lognodes.ResolveTransclusionLog;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtText;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.EmptyIterator;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.StringValue;

public class ExtensionFunctionParseMediaWikiCall extends ExtensionFunctionCall {

	private static final long serialVersionUID = 1621372360808681807L;

	/**
	 * see also http://old.nabble.com/problem-returning-a-document-fragment-from-saxon-9.3-integrated-extension-function-td32318492.html
	 */

	protected static Map<String, FullPage> knownPages = Collections.synchronizedMap(new LinkedHashMap<String, FullPage>());
	protected static WikiConfig config;
	protected static boolean reportProblems = false;

	//	@Override
	//	public void copyLocalData(ExtensionFunctionCall destination) {
	//		ExtensionFunctionParseMediaWikiCall dest = (ExtensionFunctionParseMediaWikiCall) destination;
	//	}

	@SuppressWarnings("unchecked")
	@Override
	public SequenceIterator<NodeInfo> call(
			@SuppressWarnings("rawtypes") SequenceIterator<? extends Item>[] args, XPathContext ctx)
					throws XPathException {
		SequenceIterator<NodeInfo> result = null;
		String data = "";
		try {
			StringValue in = (StringValue) args[0].next();
			if(null == in) {
				return EmptyIterator.getInstance();
			}

			data = in.getStringValue();

			StringReader inStream = new StringReader(data);

			if (config == null)
				throw new RuntimeException("You have to set up the configuration first using configureSite, configureNamespace and storeTemplate.");
			WtEngine wtEngine = new WtEngine(config);
			if (reportProblems) {
				wtEngine.setDebugHooks(new ExpansionDebugHooks() {
					
				@Override
				public WtNode afterResolveParserFunction(
						ExpansionVisitor expansionVisitor, WtTemplate n,
						ParserFunctionBase pfn,
						List<? extends WtNode> argsValues, WtNode result,
						ResolveParserFunctionLog log) {
					WtNode newResult = super.afterResolveParserFunction(expansionVisitor, n, pfn, argsValues,
							result, log);
					if (newResult == null)
						System.err.println("Can't resolve " + " as a parser function.");
					return newResult;
				}
				
				@Override
					public WtNode afterResolveTagExtension(
							ExpansionVisitor expansionVisitor,
							WtTagExtension n, String name,
							WtNodeList attributes,
							WtTagExtensionBody wtTagExtensionBody,
							WtNode result, ResolveTagExtensionLog log) {
					WtNode newResult = super.afterResolveTagExtension(expansionVisitor, n, name, attributes, wtTagExtensionBody, result, log);
					if (newResult == null)
						System.err.println("Can't resolve " + name + " as tag extension.");
					return newResult;
					}
				
				@Override
					public WtNode afterResolveTransclusion(
							ExpansionVisitor expansionVisitor, WtTemplate n,
							String target, List<WtTemplateArgument> args,
							WtNode result, ResolveTransclusionLog log) {
					WtNode newResult = super.afterResolveTransclusion(expansionVisitor, n, target, args, result, log);
					if (newResult == null)
						System.err.println("Can't resolve " + target + " as a transclusion.");
					return newResult;
					}
				});
			}
			EngCompiledPage p = wtEngine.postprocess(new PageId(PageTitle.make(config, "input"), 0), data, new ExpansionCallback() {

				@Override
				public FullPage retrieveWikitext(ExpansionFrame arg0, PageTitle arg1)
						throws Exception {
					FullPage ret = knownPages.get(arg1.getTitle()); 
					return ret;
				}

				@Override
				public String fileUrl(PageTitle pageTitle, int width,
						int height) throws Exception {
					// TODO: Used for existence checks. Pretend that it does for now.
					if (pageTitle.getTitle().contains(".")) 
						return "file://" + pageTitle.getTitle();
					return null;
				}
			});
//			Iterator<Warning> iter = p.getWarnings().iterator();
//			while (iter.hasNext())
//			{
//				Warning w = iter.next();
//				System.err.println(w.toString());
//			}

			NameAbbrevService as = new NameAbbrevService(
					new String[]{"de.fau.cs.osr.ptk.common.test", "ptk"},
					new String[]{"de.fau.cs.osr.ptk.common.xml", "ptk"},
					new String[]{"org.sweble.wikitext.lazy.parser", "swc", "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext"},
					new String[]{"org.sweble.wikitext.lazy.preprocessor", "swc"},
					new String[]{"org.sweble.wikitext.lazy.utils", "swc"},
					new String[]{"org.sweble.wikitext.engine", "swc"},
					new String[]{"org.sweble.wikitext.engine.nodes", "swc"},
					new String[]{"org.sweble.wikitext.engine.log", "swc"},
					new String[]{"org.sweble.wikitext.parser.nodes", "swc"});

			DocumentInfo doc = null;
			try {
//				Properties props = System.getProperties();
//				Properties newProps = new Properties(props);
//				newProps.put("javax.xml.transform.TransformerFactory",
//						"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
//				System.setProperties(newProps);
				StringWriter writer = new StringWriter();
				XmlWriter<WtNode> ptkToXmlWriter = new XmlWriter<WtNode>(WtNode.class, WtNodeList.WtNodeListImpl.class, WtText.class);
				ptkToXmlWriter.setCompact(true);
				ptkToXmlWriter.serialize(p, writer, as);
//				System.setProperties(props);

				doc = ctx.getConfiguration().buildDocument(new StreamSource(new StringReader(writer.toString())));
			} catch (SerializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inStream.close();

			// approach one: create NodeInfo List manually 

			//        NodeInfo top = doc.iterateAxis(Axis.CHILD, NodeKindTest.ELEMENT).next(); 
			//		SequenceIterator<NodeInfo> result = top.iterateAxis(Axis.CHILD, NodeKindTest.ELEMENT);
			result = SingletonIterator.makeIterator((NodeInfo)doc);
		} catch (CompilerException e) {
			writeWikiTextOnException(e.getPageTitle().getTitle(), e, e.getWikiText() == "" ? data : e.getWikiText());
		} catch (Exception e) {
			writeWikiTextOnException("", e, data);
		}
		//		return EmptyIterator.getInstance();
		return result; 
	}

	private void writeWikiTextOnException(String title, Exception e, String wikiText) {
		ByteArrayOutputStream exceptionTraceText = new ByteArrayOutputStream();
		PrintStream exceptionTrace = new PrintStream(exceptionTraceText);
		String tempFileName = "Temp file not set!";
		
		e.printStackTrace(exceptionTrace);

		// Create temp file.
		File temp;
		try {
			temp = File.createTempFile(title == "" ? "wikitext" : title + "_", ".wikitext");

			// Delete temp file when program exits.
			// temp.deleteOnExit();

			// Write to temp file
			BufferedWriter tempOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), Charset.forName("UTF-8")));
			tempOut.write(wikiText);
			tempOut.close();
			tempFileName = temp.getAbsolutePath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		throw new RuntimeException("See: " + tempFileName + ". Error while parsing. " + exceptionTraceText.toString());
	}
}
