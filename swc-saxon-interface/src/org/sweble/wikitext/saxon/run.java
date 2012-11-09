package org.sweble.wikitext.saxon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

import net.sf.saxon.expr.CollationMap;
import net.sf.saxon.expr.EarlyEvaluationContext;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.QueryResult;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.StringValue;

public class run {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) 
	{ 
		SequenceIterator<NodeInfo> result = null;
		try {
			String thisLine;
			FileInputStream in = new FileInputStream(new File(args[0]));
			//from here is the classic approach - read bytes, characters,
			//lines in Java style
			InputStreamReader inStream = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(inStream);
			StringBuilder sb = new StringBuilder();
			while ((thisLine = br.readLine()) != null) {
				sb.append(thisLine);
			}
//			System.out.println(sb.toString());
			Processor dummyProcessor = new Processor(false);
			XPathContext ctx = new EarlyEvaluationContext(dummyProcessor.getUnderlyingConfiguration(), new CollationMap(dummyProcessor.getUnderlyingConfiguration()));
			ExtensionFunctionCall testCall = new ExtensionFunctionParseMediaWiki().makeCallExpression();
			@SuppressWarnings({ "rawtypes" })
			SequenceIterator<? extends Item>[] arguments = (SequenceIterator<? extends Item>[]) Array.newInstance(SequenceIterator.class, 1);
			arguments[0] = SingletonIterator.makeIterator(StringValue.makeStringValue(sb.toString()));
			try {
				result = (SequenceIterator<NodeInfo>) testCall.call(arguments, ctx);
			} catch (XPathException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br.close();
			in.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.toString());
		}
		try {
			while(true) {
				if (null == result.next()) break;
				System.out.println(QueryResult.serialize(result.current()));
			}
		} catch (XPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

