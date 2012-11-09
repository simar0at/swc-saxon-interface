package org.sweble.saxon;

import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.*;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.value.SequenceType;

// swc:configureNS($namespaceKeys as xs:int+, $namespaceNames as xs:string+ $baseConfig as node()+) as node()+
public class ExtensionFunctionParseMediaWikiConfigureNS extends
		ExtensionFunctionDefinition {

	private static final long serialVersionUID = -7758484989537292071L;
	private static final StructuredQName qName =
            new StructuredQName("swc",
                    "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext",
                    "configureNamespace");
	private final static SequenceType[] argumentTypes = new SequenceType[] {
        SequenceType.makeSequenceType(BuiltInAtomicType.INT, StaticProperty.ALLOWS_ONE_OR_MORE), SequenceType.makeSequenceType(BuiltInAtomicType.STRING, StaticProperty.ALLOWS_ONE_OR_MORE), SequenceType.OPTIONAL_DOCUMENT_NODE};
	
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
        return 3;
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
		return new ExtensionFunctionParseMediaWikiConfigureNSCall();
	}	
	
	@Override
	public boolean hasSideEffects() {
		return true;
	}
}
