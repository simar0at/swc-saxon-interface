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
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import de.fau.cs.osr.ptk.common.Warning;
import de.fau.cs.osr.ptk.common.xml.SerializationException;
import de.fau.cs.osr.ptk.common.xml.XmlWriter;
import de.fau.cs.osr.utils.NameAbbrevService;

import org.sweble.wikitext.engine.ExpansionCallback;
import org.sweble.wikitext.engine.ExpansionFrame;
import org.sweble.wikitext.engine.FullPage;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngine;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngCompiledPage;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
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
		ByteArrayOutputStream exceptionTraceText = new ByteArrayOutputStream();
		PrintStream exceptionTrace = new PrintStream(exceptionTraceText);
		String tempFileName = "Temp file not set!";
		String data = "";
		try {
			StringValue in = (StringValue) args[0].next();
			if(null == in) {
				return EmptyIterator.getInstance();
			}

			data = in.getStringValue();

			StringReader inStream = new StringReader(data);

			//    	WikiConfigurationInterface config = null;
			//    	Compiler compiler = null;
			//    	try {
			//			config = new SimpleWikiConfiguration("classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
			//			compiler = new Compiler(config);
			//			
			//			Namespace ns = null;
			//			if (namespace != null)
			//				ns = config.getNamespace(namespace);
			//			 
			//			PageTitle title = PageTitle.make(config, page, ns);
			//			FullPage fullPage = retrieve(null, title);
			//			if (fullPage == null)
			//				return null;
			//			
			//			boolean forInclusion = false;
			//			
			//			 compiler.preprocess(
			//					fullPage.getId(),
			//					fullPage.getText(),
			//					forInclusion,
			//					new ExpansionCallbackImpl());
			//		} catch (FileNotFoundException e2) {
			//			// TODO Auto-generated catch block
			//			e2.printStackTrace();
			//		} catch (JAXBException e2) {
			//			// TODO Auto-generated catch block
			//			e2.printStackTrace();
			//		}

			WtEngine wtEngine = new WtEngine(config);
			EngCompiledPage p = wtEngine.postprocess(new PageId(PageTitle.make(config, "target"), 0), data, new ExpansionCallback() {

				@Override
				public FullPage retrieveWikitext(ExpansionFrame arg0, PageTitle arg1)
						throws Exception {
					FullPage ret = knownPages.get(arg1.getTitle()); 
					return ret;
				}

				@Override
				public String fileUrl(PageTitle pageTitle, int width,
						int height) throws Exception {
					// Used for existence checks. Pretend that it does.
					return "";
				}
			});
			Iterator<Warning> iter = p.getWarnings().iterator();
			while (iter.hasNext())
			{
				Warning w = iter.next();
				System.err.println(w.toString());
			}

			NameAbbrevService as = new NameAbbrevService(
					"de.fau.cs.osr.ptk.common.test",
					"de.fau.cs.osr.ptk.common.xml",
					"org.sweble.wikitext.lazy.parser",
					"org.sweble.wikitext.lazy.preprocessor",
					"org.sweble.wikitext.lazy.utils",
					"org.sweble.wikitext.engine",
					"org.sweble.wikitext.engine.nodes",
					"org.sweble.wikitext.engine.log",
					"org.sweble.wikitext.parser.nodes");

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
		} catch (Exception e) {
			e.printStackTrace(exceptionTrace);

			// Create temp file.
			File temp;
			try {
				temp = File.createTempFile("wikitext_", ".txt");

				// Delete temp file when program exits.
				// temp.deleteOnExit();

				// Write to temp file
				BufferedWriter tempOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), Charset.forName("UTF-8")));
				tempOut.write(data);
				tempOut.close();
				tempFileName = temp.getAbsolutePath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			throw new RuntimeException("See: " + tempFileName + ". Error while parsing. " + exceptionTraceText.toString());
		}
		//		return EmptyIterator.getInstance();
		return result; 
	}

}
