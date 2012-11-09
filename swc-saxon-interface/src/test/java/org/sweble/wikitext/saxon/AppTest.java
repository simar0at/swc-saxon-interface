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
			Query.main(new String[] {"-init:org.sweble.wikitext.saxon.AddWikiParserFunction", "-s:M:\\arzwiki-20120728-pages-articles.xml", "-q:" + xQueryUrl.getFile()});
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
