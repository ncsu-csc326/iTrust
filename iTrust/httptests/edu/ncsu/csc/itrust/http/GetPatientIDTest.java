package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class GetPatientIDTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testSelectPatientButton() throws Exception
	{
		gen.hcp4();
		gen.hcp5();
		gen.referral_sort_testdata();

		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Patient Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		//click on the "Select Patient" button
		wr.getFormWithID("mainForm").getButtonWithID("selectPatientButton").click();
		wr = wc.getCurrentPage();

		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		assertFalse(wr.getText().contains("HTTP Status 500"));   
		assertFalse(wr.getText().contains("java.lang.NumberFormatException"));

		//click on the "Select Patient" button again
		wr.getFormWithID("mainForm").getButtonWithID("selectPatientButton").click();
		wr = wc.getCurrentPage();

		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		assertFalse(wr.getText().contains("HTTP Status 500"));   
		assertFalse(wr.getText().contains("java.lang.NumberFormatException"));
		assertFalse(wr.getText().contains("Viewing information for <b>null</b>"));
	}
	
}
