package edu.ncsu.csc.itrust.http;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import junit.framework.TestCase;

public class LoginTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNonNumericLogin(){
		try {
			// begin at the iTrust home page
			WebConversation wc = new WebConversation();
			WebResponse loginResponse = wc.getResponse(iTrustHTTPTest.ADDRESS);
			// log in using the given username and password
			WebForm form = loginResponse.getForms()[0];
			form.setParameter("j_username", "foo");
			form.setParameter("j_password", "1234");
			WebResponse homePage = loginResponse.getForms()[0].submit();
			
			assertFalse(homePage.getText().contains("NumberFormatException"));
		} catch (IOException e) {
			fail("Exception occurred");
		} catch (SAXException e) {
			fail("Exception occurred");
		}
			
	}
}
