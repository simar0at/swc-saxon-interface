package org.sweble.wikitext.saxon;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class util {
	public static String getStackTraceAsText(Throwable t) {
		ByteArrayOutputStream exceptionTraceText = new ByteArrayOutputStream();
		PrintStream exceptionTrace = new PrintStream(exceptionTraceText);
		t.printStackTrace(exceptionTrace);
		return exceptionTraceText.toString();		
	}
}
