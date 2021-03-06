/**
 * Copyright 2012 Omar Siam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.saxon;

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
