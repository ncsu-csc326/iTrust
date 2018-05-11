package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

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
	
	/*
	public void testHCPEditDeactivatedPatient() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Patient Information").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "314159");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
		
		assertTrue(wr.getText().contains("Patient is deactivated.  Cannot edit."));
	}
	
	public void testHCPDeactivatedPatientGroupReport() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Group Report").click();
		assertEquals("iTrust - Generate Group Report", wr.getTitle());
		
		WebForm form = wr.getFormWithID("mainForm");
		wr = form.submit();
		assertEquals("iTrust - Generate Group Report", wr.getTitle());
	}
	
	public void testAdministratorReactivatePatient() throws Exception{
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Activate Patient").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "314159");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/admin/activatePatient.jsp", wr.getURL().toString());
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Successfully Activated"));
	}
	
	public void testPatientDeactivatedLogin() throws Exception{
		// begin at the iTrust home page
		WebConversation wc = new WebConversation();
		WebResponse loginResponse = wc.getResponse(ADDRESS);
		// log in using the given username and password
		WebForm form = loginResponse.getForms()[0];
		form.setParameter("j_username", "314159");
		form.setParameter("j_password", "pw");
		WebResponse homePage = loginResponse.getForms()[0].submit();
		assertTrue(homePage.getText().contains("Failed login attempts"));
	}
	*/
}
