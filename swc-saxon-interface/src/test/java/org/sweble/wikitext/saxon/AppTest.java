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

import java.io.File;
import java.net.URL;

import net.sf.saxon.Query;

import de.fau.cs.osr.utils.StringUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private class QueryTester
			extends
				Query
	{
		public void doQuery(URL xQuery)
		{
			doQuery(xQuery, null);
		}
		
		public void doQuery(URL xQuery, URL sourceFile)
		{
			if (sourceFile != null)
				sourceFileName = sourceFile.getFile();
			queryFileName = xQuery.getFile();
			doQuery(new String[] {"-init:org.sweble.wikitext.saxon.AddWikiParserFunction", "-o:" + xQuery.getFile().replaceAll("\\.xquery", "\\.xml")}, "java net.sf.saxon.Query");
		}
	}
	
	private QueryTester tester = new QueryTester();
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    private static final String TESTS_DIR = "org/sweble/wikitext/saxon";
    
    /**
     * 
     */
    public void testConfigureSite()
    {
    	URL xQueryUrl = AppTest.class.getResource("/" + TESTS_DIR + "/testConfigureSite.xquery");
    	String path = StringUtils.decodeUsingDefaultCharset(xQueryUrl.getFile());
		final File file = new File(path);
		try {
			tester.doQuery(xQueryUrl);
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
//    public void testComplete()
//    {    	
//    	URL xQueryUrl = AppTest.class.getResource("/" + TESTS_DIR + "/test.xquery");
//    	URL sourceUrl = AppTest.class.getResource("/" + TESTS_DIR + "/minidump.xml");
//    	String path = StringUtils.decodeUsingDefaultCharset(xQueryUrl.getFile());
//    	final File file = new File(path);
//    	try {
//    		tester.doQuery(xQueryUrl, sourceUrl);
//    	} catch (Exception e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
//    }
}
