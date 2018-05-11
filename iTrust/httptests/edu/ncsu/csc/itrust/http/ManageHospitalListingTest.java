package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.enums.TransactionType;

@SuppressWarnings("unused")
public class ManageHospitalListingTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testCreateHospital() throws Exception{
		WebConversation wc = login("9000000001", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		wr = wr.getLinkWith("Manage Hospital Listing").click();
		assertEquals("iTrust - Maintain Hospital Listing and Assignments", wr.getTitle());
		
		// Fill in the form
		WebForm form = wr.getForms()[0];
		form.setParameter("hospitalID", "777");
		form.setParameter("hospitalName", "Pokemon Center");
		form.setParameter("hospitalAddress", "123 Centenial Parkway");
		form.setParameter("hospitalCity", "Raleigh");
		form.setParameter("hospitalState", "NC");
		form.setParameter("hospitalZip", "27607");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		// Ensure that the form submitted and validated successfully
		/*assertTrue(wr.getText().contains("Success"));
		
		// Make sure the new hospital information is present
		assertTrue(wr.getText().contains("777"));
		assertTrue(wr.getText().contains("Pokemon Center"));
		assertTrue(wr.getText().contains("123 Centenial Parkway"));
		assertTrue(wr.getText().contains("Raleigh"));
		assertTrue(wr.getText().contains("NC"));
		assertTrue(wr.getText().contains("27607"));*/
	}
	
	public void testUpdateHospital() throws Exception{
		WebConversation wc = login("9000000001", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		wr = wr.getLinkWith("Manage Hospital Listing").click();
		assertEquals("iTrust - Maintain Hospital Listing and Assignments", wr.getTitle());
		
		// Fill in the form, but change the name of the hospital
		WebForm form = wr.getForms()[0];
		form.setParameter("hospitalID", "5");
		form.setParameter("hospitalName", "Facebook Insane Asylum");
		form.setParameter("hospitalAddress", "2 Yarborough Drive");
		form.setParameter("hospitalCity", "Raleigh");
		form.setParameter("hospitalState", "NC");
		form.setParameter("hospitalZip", "27607");
		form.getSubmitButtons()[1].click();
		
		/*wr = wc.getCurrentPage();
		
		// Ensure that the form submitted and validated successfully
		assertTrue(wr.getText().contains("Success"));
		
		// Make sure the new hospital information is present
		assertTrue(wr.getText().contains("5"));
		assertTrue(wr.getText().contains("Facebook Insane Asylum"));
		assertTrue(wr.getText().contains("2 Yarborough Drive"));
		assertTrue(wr.getText().contains("Raleigh"));
		assertTrue(wr.getText().contains("NC"));
		assertTrue(wr.getText().contains("27607"));
		
		assertFalse(wr.getText().contains("Facebook Rehab Center"));*/
	}
	
	public void testUpdateWithoutID() throws Exception{
		WebConversation wc = login("9000000001", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		wr = wr.getLinkWith("Manage Hospital Listing").click();
		assertEquals("iTrust - Maintain Hospital Listing and Assignments", wr.getTitle());
		
		// Fill in the form, but change the name of the hospital
		WebForm form = wr.getForms()[0];
		form.setParameter("hospitalName", "Facebook Insane Asylum");
		form.setParameter("hospitalAddress", "2 Yarborough Drive");
		form.setParameter("hospitalCity", "Raleigh");
		form.setParameter("hospitalState", "NC");
		form.setParameter("hospitalZip", "27607");
		form.getSubmitButtons()[1].click();
		
		wr = wc.getCurrentPage();
		
		// Ensure that the form failed validation
		//assertTrue(wr.getText().contains("This form has not been validated correctly"));
	}
	
	public void testNameTooLong() throws Exception{
		WebConversation wc = login("9000000001", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		wr = wr.getLinkWith("Manage Hospital Listing").click();
		assertEquals("iTrust - Maintain Hospital Listing and Assignments", wr.getTitle());
		
		// Fill in the form with a name longer than 30 characters
		WebForm form = wr.getForms()[0];
		form.setParameter("hospitalID", "777");
		form.setParameter("hospitalName", "ABCABCABCABCABCABCABCABCABCABCABC");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		// Ensure that the form failed validation
		//assertTrue(wr.getText().contains("This form has not been validated correctly"));
	}
}
