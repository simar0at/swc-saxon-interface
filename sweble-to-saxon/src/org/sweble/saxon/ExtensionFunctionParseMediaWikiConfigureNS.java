package org.sweble.saxon;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.*;
import net.sf.saxon.value.SequenceType;

public class ExtensionFunctionParseMediaWikiConfigureNS extends
		ExtensionFunctionDefinition {

	private static final long serialVersionUID = -7758484989537292071L;
	private static final StructuredQName qName =
            new StructuredQName("swc",
                    "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext",
                    "configureNamespace");
	private final static SequenceType[] argumentTypes = new SequenceType[] {
        SequenceType.SINGLE_INT, SequenceType.OPTIONAL_STRING};
	
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
        return SequenceType.NODE_SEQUENCE;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return new ExtensionFunctionParseMediaWikiConfigureNSCall();
	}	
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}
}
