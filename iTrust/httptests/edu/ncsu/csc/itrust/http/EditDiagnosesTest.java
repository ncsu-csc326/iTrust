package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.enums.TransactionType;

@SuppressWarnings("unused")
public class EditDiagnosesTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testDiagnosesLink() throws Exception {
		// login uap
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Diagnoses URLs").click();
		// choose patient 2
		WebForm Form = wr.getForms()[0];
		Form.setParameter("diagnoses", "715.09");
		Form.submit();
		wr = wc.getCurrentPage();
		Form = wr.getForms()[0];
		Form.setParameter("url", "http://www.wikipedia.org/");
		wr = Form.submit();
		assertEquals("iTrust - Maintain Diagnoses Links", wr.getTitle());
		assertTrue(wr.getText().contains("URL has been successfully updated to http://www.wikipedia.org/"));
		
		wr = wr.getLinkWith("Logout").click(); 
		
		//login as patient
		wc = login("1", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Diagnoses").click(); 
		wr = wr.getLinkWith("http://www.wikipedia.org/").click(); 
		assertEquals("Wikipedia", wr.getTitle());
		
		
	}


	public void testHCPEditDiagnoses() throws Exception {
		// login uap
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Diagnoses URLs").click();
		// choose patient 2
		WebForm Form = wr.getForms()[0];
		Form.setParameter("diagnoses", "11.40");
		Form.submit();
		wr = wc.getCurrentPage();
		Form = wr.getForms()[0];
		Form.setParameter("url", "http://www.google.com/");
		wr = Form.submit();
		assertEquals("iTrust - Maintain Diagnoses Links", wr.getTitle());
		assertTrue(wr.getText().contains("Tuberculosis of the lung's (11.40) URL has been successfully updated to http://www.google.com/"));
		
		
	}
	public void testAdminEditDiagnoses() throws Exception {
		// login uap
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Diagnoses URLs").click();
		// choose patient 2
		WebForm Form = wr.getForms()[0];
		Form.setParameter("diagnoses", "11.40");
		Form.submit();
		wr = wc.getCurrentPage();
		Form = wr.getForms()[0];
		Form.setParameter("url", "http://www.google.com/");
		wr = Form.submit();
		assertEquals("iTrust - Maintain Diagnoses Links", wr.getTitle());	
		assertTrue(wr.getText().contains("Tuberculosis of the lung's (11.40) URL has been successfully updated to http://www.google.com/"));
		
		
	}

}
