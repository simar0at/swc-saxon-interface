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

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.*;
import net.sf.saxon.value.SequenceType;

// swc:configureSite($siteName as xs:string, $baseUrl as xs:string, $doWOMLikeProcessing as xs:boolean?, $reportProblems as xs:booblen?)
public class ExtensionFunctionParseMediaWikiConfigureSiteFromURL extends
		ExtensionFunctionDefinition {


	private static final long serialVersionUID = -797222386936377909L;
	private static final StructuredQName qName =
            new StructuredQName("swc",
                    "http://sweble.org/doc/site/tooling/sweble/sweble-wikitext",
                    "configureSiteFromURL");
	private final static SequenceType[] argumentTypes = new SequenceType[] {
        SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING, SequenceType.OPTIONAL_BOOLEAN, SequenceType.OPTIONAL_BOOLEAN};
	
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
        return 4;
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
