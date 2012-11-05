package org.sweble.saxon;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.sweble.wikitext.engine.ParserFunctionBase;
import org.sweble.wikitext.engine.TagExtensionBase;
import org.sweble.wikitext.engine.config.CompilerConfigImpl;
import org.sweble.wikitext.engine.config.WikiConfigImpl;
import org.sweble.wikitext.engine.config.WikiRuntimeInfoImpl;
import org.sweble.wikitext.engine.nodes.EngineNodeFactoryImpl;

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
