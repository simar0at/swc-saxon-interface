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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.config.WikiConfigImpl;

public class WikiConfigurationProblemReporter extends WikiConfigImpl implements Serializable {
	
	private static final long serialVersionUID = -7320472197068145857L;

	public WikiConfigurationProblemReporter() {
//		this.parserConfig = new ParserConfigProblemReporter(this);
//		this.compilerConfig = new CompilerConfigImpl();
//		this.nodeFactory = new EngineNodeFactoryImpl(this);
//		this.runtimeInfo = new WikiRuntimeInfoImpl(this);
		}
	
	Set<String> knownMissingParserFunctions = new HashSet<String>();
	
	@Override
	public ParserFunctionBase getParserFunction(String name) {
		ParserFunctionBase result = super.getParserFunction(name);
		if (result == null && 
				knownMissingParserFunctions.add(name))
			System.err.println("want parser function " + name);
		return result;
	}
	
	Set<String> knownMissingTags = new HashSet<String>();
	
	@Override
	public TagExtensionBase getTagExtension(String name) {
		TagExtensionBase result = super.getTagExtension(name);
		if (result == null && knownMissingTags.add(name))
			System.err.println("want tag extension " + name);
		return result;
	}
	
}
