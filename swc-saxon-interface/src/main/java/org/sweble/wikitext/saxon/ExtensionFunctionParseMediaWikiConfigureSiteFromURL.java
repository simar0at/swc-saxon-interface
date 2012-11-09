package org.sweble.wikitext.saxon;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.*;
import net.sf.saxon.value.SequenceType;

// swc:configureSite($siteName as xs:string, $baseUrl as xs:string, $reportProblems as xs:booblen)
public class ExtensionFunctionParseMediaWikiConfigureSiteFromURL extends
		ExtensionFunctionDefinition {


	private static final long serialVersionUID = -797222386936377909L;
	private static final StructuredQName qName =
            new StructuredQName("swc",
                    "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext",
                    "configureSiteFromURL");
	private final static SequenceType[] argumentTypes = new SequenceType[] {
        SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING, SequenceType.OPTIONAL_BOOLEAN};
	
	@Override
	public SequenceType[] getArgumentTypes() {
		return argumentTypes;
	}

	@Override
	public StructuredQName getFunctionQName() {
		return qName;
	}

    @Override
    public int getMinimumNumberOfArguments() {
        return 2;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 3;
    }
    
	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
        return SequenceType.NODE_SEQUENCE;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return new ExtensionFunctionParseMediaWikiConfigureSiteFromURLCall();
	}	
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}
}
