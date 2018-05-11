package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

public class AuditPatientTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.patientDeactivate();
	}
	
	public void testHCPDeactivatePatient() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Audit Patients").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp/auditPage.jsp", wr.getURL().toString());
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.setParameter("understand", "I UNDERSTAND");
		editPatientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Successfully Deactivated"));
	}
	
	public void testHCPDeactivatePatientWrongConfirmation() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Audit Patients").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp/auditPage.jsp", wr.getURL().toString());
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.setParameter("understand", "iunderstand");
		editPatientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("You must type \"I UNDERSTAND\" in the textbox."));
	}
	
	public void testHCPActivatePatient() throws Exception {
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Audit Patients").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "314159");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp/auditPage.jsp", wr.getURL().toString());
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.setParameter("understand", "I UNDERSTAND");
		editPatientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Successfully Activated"));
	}
	
	public void testHCPActivatePatientWrongConfirmation() throws Exception {
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Audit Patients").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "314159");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp/auditPage.jsp", wr.getURL().toString());
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.setParameter("understand", "WOAH");
		editPatientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("You must type \"I UNDERSTAND\" in the textbox."));
	}
}
