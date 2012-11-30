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

import static org.sweble.wikitext.saxon.XmlConstants.AST_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.ATTR_ARRAY_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.ATTR_LOCATION_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.ATTR_NAME_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.ATTR_NULL_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.ATTR_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.LIST_QNAME;
import static org.sweble.wikitext.saxon.XmlConstants.NULL_QNAME;
import static de.fau.cs.osr.ptk.common.xml.XmlConstants.PTK_NS;
import static org.sweble.wikitext.saxon.XmlConstants.TEXT_QNAME;
import static de.fau.cs.osr.ptk.common.xml.XmlConstants.typeNameToTagName;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.AstNodeListImpl;
import de.fau.cs.osr.ptk.common.ast.AstText;
import de.fau.cs.osr.ptk.common.xml.SerializationException;
import de.fau.cs.osr.utils.NameAbbrevService;
import de.fau.cs.osr.utils.ReflectionUtils;
import de.fau.cs.osr.utils.ReflectionUtils.ArrayInfo;

public class XmlWriter<T extends AstNode<T>>
{
	/*
	private static final int MAX_ENTRIES = 128;
	*/
	
	// =========================================================================
	
	private final AttributesImpl atts = new AttributesImpl();
	
	/*
	private final Map<Class<?>, Marshaller> marshallerCache =
			new LinkedHashMap<Class<?>, Marshaller>()
			{
				private static final long serialVersionUID = 1L;
				
				protected boolean removeEldestEntry(
						Map.Entry<Class<?>, Marshaller> eldest)
				{
					return size() > MAX_ENTRIES;
				}
			};
	*/
	
	private boolean compact = false;
	
	private Result result;
	
	private NameAbbrevService abbrevService;
	
	private TransformerHandler th;
	
	private ByteArrayOutputStream baos;
	
	private ObjectOutputStream oos;
	
	private Base64 b64;
	
	private final Class<? extends T> nodeClass;
	
	private final Class<? extends T> listClass;
	
	private final Class<? extends T> textClass;
	
	// =========================================================================
	/*
	public static String write(T node) throws SerializationException
	{
		StringWriter writer = new StringWriter();
		new XmlWriter().serialize(node, writer);
		return writer.toString();
	}
	
	public static String write(
			T node,
			NameAbbrevService abbrevService) throws SerializationException
	{
		StringWriter writer = new StringWriter();
		new XmlWriter().serialize(node, writer, abbrevService);
		return writer.toString();
	}
	
	public static Writer write(T node, Writer writer) throws SerializationException
	{
		new XmlWriter().serialize(node, writer);
		return writer;
	}
	
	public static Writer write(
			T node,
			Writer writer,
			NameAbbrevService abbrevService) throws SerializationException
	{
		new XmlWriter().serialize(node, writer, abbrevService);
		return writer;
	}
	*/
	// =========================================================================
	
	public XmlWriter(
			Class<? extends T> nodeClass,
			Class<? extends T> listClass,
			Class<? extends T> textClass)
	{
		this.nodeClass = nodeClass;
		this.listClass = listClass;
		this.textClass = textClass;
		
		if (!nodeClass.isAssignableFrom(listClass))
			throw new IllegalArgumentException("An instance of listClass must be assignable to nodeClass");
		if (!nodeClass.isAssignableFrom(textClass))
			throw new IllegalArgumentException("An instance of textClass must be assignable to nodeClass");
		
		if (!AstNodeListImpl.class.isAssignableFrom(listClass))
			throw new IllegalArgumentException("An instance of listClass must be assignable to type " + AstNodeListImpl.class.getName());
		if (!AstText.class.isAssignableFrom(textClass))
			throw new IllegalArgumentException("An instance of textClass must be assignable to type " + AstText.class.getName());
	}
	
	// =========================================================================
	/**
	 * If set to true the output is not indented or otherwise beautified.
	 * 
	 * @param compact
	 *            Whether to compact the output.
	 */
	public void setCompact(boolean compact)
	{
		this.compact = compact;
	}
	
	public void serialize(T node, Writer writer) throws SerializationException
	{
		serialize(node, writer, new NameAbbrevService());
	}
	
	public void serialize(T node, Writer writer, NameAbbrevService abbrevService) throws SerializationException
	{
		serialize(node, new StreamResult(writer), abbrevService);
	}	
	
	public void serialize(
			T node,
			Result result,
			NameAbbrevService abbrevService) throws SerializationException
	{
		this.result = result;
		
		this.abbrevService = abbrevService;
		
		try
		{
			before();
			dispatch(node);
			after();
		}
		catch (TransformerConfigurationException e)
		{
			throw new SerializationException(e);
		}
		catch (SAXException e)
		{
			throw new SerializationException(e);
		}
		catch (JAXBException e)
		{
			throw new SerializationException(e);
		}
		catch (IOException e)
		{
			throw new SerializationException(e);
		}
		finally
		{
			if (oos != null)
				try
				{
					oos.close();
				}
				catch (IOException e)
				{
				}
		}
	}
	
	// =========================================================================
	
	private void before() throws TransformerConfigurationException, SAXException
	{
		SAXTransformerFactory tf =
				(SAXTransformerFactory) SAXTransformerFactory.newInstance();
		
		if (!compact)
			// This is different for every TransformerFactory
			if (tf.getClass().getName().contains("org.apache.xalan"))
				tf.setAttribute("indent-number", new Integer(2));
		
		th = tf.newTransformerHandler();
		
		Transformer t = th.getTransformer();
		t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		
		if (!compact)
		{
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			// This is different for every transformer.
			// The following works for (com.sun.)org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
			if (t.getClass().getName().contains("org.apache.xalan"))
			{
				t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			}
			
			if (t.getClass().getName().contains("net.sf.saxon")) {
				try {
				  t.setOutputProperty("{http://saxon.sf.net/}indent-spaces", "2");
				} catch (RuntimeException e) {
					// this is "output customization" which is unavailable in saxon HE
				}
			}	
		}
		
		th.setResult(result);
		
		th.startDocument();
		
		th.startPrefixMapping("ptk", PTK_NS);
		startElement(AST_QNAME);
		atts.clear();
	}
	
	private void after() throws SAXException
	{
		endElement(AST_QNAME);
		th.endDocument();
	}
	
	// =========================================================================
	
	private void dispatch(T n) throws SAXException, JAXBException, IOException
	{
		if (listClass.isInstance(n))
		{
			visit((AstNodeListImpl<T>) n);
		}
		else if (textClass.isInstance(n))
		{
			visit((AstText<T>) n);
		}
		else if (n == null)
		{
			startElement(NULL_QNAME);
			endElement(NULL_QNAME);
		}
		else
		{
			visit(n);
		}
	}
	
	private void iterate(T n) throws SAXException, JAXBException, IOException
	{
		for (T c : n)
			dispatch(c);
	}
	
	private void visit(T n) throws SAXException, JAXBException, IOException
	{
		String typeName = abbrevService.abbrev(n.getClass());
		
		String tagName = typeNameToTagName(typeName);
		
		if (n.getNativeLocation() != null)
			addAttribute(ATTR_LOCATION_QNAME, n.getNativeLocation().toString());
		startElement(tagName);
		atts.clear();
		{
			for (Entry<String, Object> e : n.getAttributes().entrySet())
				writeAttribute(e.getKey(), e.getValue());
			
			for (AstNodePropertyIterator i = n.propertyIterator(); i.next();)
				writeProperty(i.getName(), i.getValue());
			
			if (n.isList())
			{
				iterate(n);
			}
			else
			{
				for (int i = 0; i < n.getChildNames().length; ++i)
				{
					startElement(n.getChildNames()[i]);
					{
						dispatch(n.get(i));
					}
					endElement(n.getChildNames()[i]);
				}
			}
		}
		endElement(tagName);
	}
	
	private void visit(AstText<T> n) throws SAXException
	{
		if (n.getNativeLocation() != null)
			addAttribute(ATTR_LOCATION_QNAME, n.getNativeLocation().toString());
		startElement(TEXT_QNAME);
		atts.clear();
		{
			String s = n.getContent();
			th.characters(s.toCharArray(), 0, s.length());
		}
		endElement(TEXT_QNAME);
	}
	
	private void visit(AstNodeListImpl<T> n) throws SAXException, JAXBException, IOException
	{
		if (n.getNativeLocation() != null)
			addAttribute(ATTR_LOCATION_QNAME, n.getNativeLocation().toString());
		startElement(LIST_QNAME);
		atts.clear();
		{
			@SuppressWarnings("unchecked")
			T node = (T) n;
			iterate(node);
		}
		endElement(LIST_QNAME);
	}
	
	// =========================================================================
	
	private void writeProperty(String name, Object value) throws SAXException, JAXBException, IOException
	{
		if (value == null)
		{
			addAttribute(ATTR_NULL_QNAME, "true");
			startElement(name);
			atts.clear();
			endElement(name);
		}
		else
		{
			marshal(name, value);
		}
	}
	
	private void writeAttribute(String name, Object value) throws SAXException, JAXBException, IOException
	{
		Class<?> type = null;
		Class<?> clazz = null;
		
		addAttribute(ATTR_NAME_QNAME, name);
		
		if (value != null)
		{
			clazz = value.getClass();
			
			ArrayInfo aInfo = ReflectionUtils.arrayDimension(clazz);
			if (aInfo.dim > 0)
			{
				addAttribute(ATTR_ARRAY_QNAME, String.valueOf(aInfo.dim));
				type = aInfo.elementClass;
			}
			else
			{
				type = clazz;
			}
		}
		else
		{
			addAttribute(ATTR_NULL_QNAME, "true");
		}
		
		startElement(ATTR_QNAME);
		atts.clear();
		{
			if (value != null)
			{
				String typeName = abbrevService.abbrev(type);
				
				marshal(typeNameToTagName(typeName), value);
			}
		}
		endElement(ATTR_QNAME);
	}
	
	// =========================================================================
	
	private void marshal(String name, Object obj) throws SAXException, JAXBException, IOException
	{
		Class<?> clazz = obj.getClass();
		if (ReflectionUtils.isExtPrimitive(clazz) || clazz == String.class)
		{
			String value = String.valueOf(obj);
			
			startElement(name);
			th.characters(value.toCharArray(), 0, value.length());
			endElement(name);
		}
		else if (obj instanceof Enum)
		{
			if (!clazz.isEnum())
				clazz = clazz.getEnclosingClass();
			
			if (!clazz.isEnum())
				throw new InternalError();
			
			String value = ((Enum<?>) obj).name();
			
			startElement(name);
			th.characters(value.toCharArray(), 0, value.length());
			endElement(name);
		}
		else if (nodeClass.isAssignableFrom(clazz))
		{
			startElement(name);
			@SuppressWarnings("unchecked")
			T node = (T) obj;
			visit(node);
			endElement(name);
		}
		else
		{
			/*
			if (obj instanceof Enum && !clazz.isEnum())
				clazz = clazz.getEnclosingClass();
			
			Marshaller marshaller = marshallerCache.get(clazz);
			if (marshaller == null)
			{
				marshaller = JAXBContext.newInstance(clazz).createMarshaller();
				marshallerCache.put(clazz, marshaller);
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JAXBElement elem = new JAXBElement(new QName(name), clazz, obj);
			
			marshaller.marshal(elem, th);
			*/
			
			if (baos == null)
			{
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				b64 = new Base64();
			}
			
			oos.writeObject(obj);
			oos.flush();
			
			String value = b64.encodeToString(baos.toByteArray());
			baos.reset();
			
			startElement(name);
			th.characters(value.toCharArray(), 0, value.length());
			endElement(name);
		}
	}
	
	// =========================================================================
	
	private void startElement(String localName) throws SAXException
	{
		th.startElement("", localName, localName, atts);
	}
	
	private void startElement(QName name) throws SAXException
	{
		th.startElement("", name.getLocalPart(), qNameToStr(name), atts);
	}
	
	private void addAttribute(QName name, String value)
	{
		atts.addAttribute("", name.getLocalPart(), qNameToStr(name), "CDATA", value);
	}
	
	private void endElement(String localName) throws SAXException
	{
		th.endElement("", localName, localName);
	}
	
	private void endElement(QName name) throws SAXException
	{
		th.endElement("", name.getLocalPart(), qNameToStr(name));
	}
	
	private String qNameToStr(QName name)
	{
		if (name.getNamespaceURI() == null || name.getNamespaceURI().isEmpty())
			return name.getLocalPart();
		
		return "ptk:" + name.getLocalPart();
	}
}
