package edu.ncsu.csc.itrust;

import junit.framework.TestCase;

public class HtmlEncoderTest extends TestCase {
	public void testEscapeCharacters() throws Exception {
		assertEquals("&lt;tag&gt;", HtmlEncoder.encode("<tag>"));
		assertEquals("&lt;tag&gt;&lt;/tag&gt;", HtmlEncoder.encode("<tag></tag>"));
		assertEquals("<br />", HtmlEncoder.encode("\n"));
		assertEquals("&lt;&lt;tag&gt;&gt;\"Lots of text!\"&lt;&lt;/tag&gt;&gt;", HtmlEncoder
				.encode("<<tag>>\"Lots of text!\"<</tag>>"));
	}

	public void testOffSite() {
		assertFalse(HtmlEncoder.URLOnSite("http://www.google.com"));
	}

}
