/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
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

import javax.xml.namespace.QName;

public class XmlConstants
{
	public static final String PTK_NS = "http://sweble.org/doc/site/tooling/parser-toolkit/ptk-xml-tools";
	
	// =========================================================================
	
	static final QName AST_QNAME = new QName(PTK_NS, "ast");
	
	static final QName LIST_QNAME = new QName(PTK_NS, "l");
	
	static final QName TEXT_QNAME = new QName(PTK_NS, "t");
	
	static final QName ATTR_QNAME = new QName(PTK_NS, "a");
	
	static final QName NULL_QNAME = new QName(PTK_NS, "null");
	
	// =========================================================================
	
	static final QName TYPE_BYTE = new QName(PTK_NS, "byte");
	
	static final QName TYPE_SHORT = new QName(PTK_NS, "short");
	
	static final QName TYPE_INTEGER = new QName(PTK_NS, "int");
	
	static final QName TYPE_LONG = new QName(PTK_NS, "long");
	
	static final QName TYPE_FLOAT = new QName(PTK_NS, "flt");
	
	static final QName TYPE_DOUBLE = new QName(PTK_NS, "dbl");
	
	static final QName TYPE_BOOLEAN = new QName(PTK_NS, "bool");
	
	static final QName TYPE_CHARACTER = new QName(PTK_NS, "char");
	
	static final QName TYPE_VOID = new QName(PTK_NS, "void");
	
	static final QName TYPE_STRING = new QName(PTK_NS, "str");
	
	// =========================================================================
	
	static final QName ATTR_NAME_QNAME = new QName("name");
	
	static final QName ATTR_LOCATION_QNAME = new QName("location");
	
	static final QName ATTR_ARRAY_QNAME = new QName("array");
	
	static final QName ATTR_NULL_QNAME = new QName("null");
	
	// =========================================================================
	
	public static String typeNameToTagName(final String name)
	{
		return name.replace('$', '-');
	}
	
	public static String tagNameToClassName(final String x)
	{
		return x.replace('-', '$');
	}
}
