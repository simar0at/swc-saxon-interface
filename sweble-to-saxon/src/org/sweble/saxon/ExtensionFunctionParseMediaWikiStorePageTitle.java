package org.sweble.saxon;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.*;
import net.sf.saxon.value.SequenceType;

public class ExtensionFunctionParseMediaWikiStorePageTitle extends
		ExtensionFunctionDefinition {

	private static final long serialVersionUID = 935097217990966052L;
	private static final StructuredQName qName =
            new StructuredQName("swc",
                    "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext",
                    "storePageTitle");
	private final static SequenceType[] argumentTypes = new SequenceType[] {
        SequenceType.SINGLE_STRING, SequenceType.SINGLE_INT};
	
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
        return 2;
    }
    
	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
        return SequenceType.EMPTY_SEQUENCE;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return new ExtensionFunctionParseMediaWikiStorePageTitleCall();
	}	
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}
}