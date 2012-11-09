package org.sweble.wikitext.saxon;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.*;
import net.sf.saxon.value.SequenceType;

//swc:storeTemplate($title as xs:string, $revision as xs:int, $wikiText as xs:string, $config as node()+) as empty()
public class ExtensionFunctionParseMediaWikiStoreTemplate extends
		ExtensionFunctionDefinition {

	private static final long serialVersionUID = 935097217990966052L;
	private static final StructuredQName qName =
            new StructuredQName("swc",
                    "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext",
                    "storeTemplate");
	private final static SequenceType[] argumentTypes = new SequenceType[] {
        SequenceType.SINGLE_STRING, SequenceType.SINGLE_INT, SequenceType.OPTIONAL_STRING, SequenceType.OPTIONAL_DOCUMENT_NODE};
	
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
        return 4;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 4;
    }
    
	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
        return SequenceType.EMPTY_SEQUENCE;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return new ExtensionFunctionParseMediaWikiStoreTemplateCall();
	}	
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}
}
