package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;


/**
 * Use Case 15
 */
public class MaintainStandardsUseCase extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/*
	 * Authenticate admin 90000000001
	 * Admin Chooses to Edit LOINC Code 
	 * Admin Inputs Code: 10834-7, Component: Fungus Identified, Kind Of Property: Prid, Time Aspect: 24H, System: Body Fluid, Scale: Nominal, Method Type: Scoliosis 
	 * Admin Submits	
	 */
	public void testAddLOINC() throws Exception {
		//gen.admin1();
		//gen.loincs();
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit LOINC Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code", "10834-7");
		form.setParameter("comp", "Fungus Identified");
		form.setParameter("kop", "Prid");
		form.setParameter("time", "24H");
		form.setParameter("system", "Body Fluid");
		form.setParameter("scale", "Nominal");
		form.setParameter("method", "Scoliosis");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editLOINCCodes.jsp"));
		assertTrue(wr.getText().contains("Success: 10834-7 added"));
		assertLogged(TransactionType.LOINC_CODE_ADD, 9000000001L, 0L, "");
	}
	
	/*
	 * Authenticate admin 90000000001
	 * Admin Chooses to Edit LOINC Code 
	 * Admin Inputs Code: 10666-6, Component: Fungus Identified, Kind Of Property: Vol, Time Aspect: Pt, System: Stool, Scale: Nominal, Method Type: Multiple Personality 
	 * Admin Submits	
	 */
	public void testUpdateLOINC() throws Exception {
		//gen.clearAllTables();
		//gen.admin1();
		//gen.loincs();
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit LOINC Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code", "10666-6");
		form.setParameter("comp", "Fungus Identified");
		form.setParameter("kop", "Vol");
		form.setParameter("time", "Pt");
		form.setParameter("system", "Stool");
		form.setParameter("scale", "Nominal");
		form.setParameter("method", "Multiple Personality");
		form.getSubmitButtons()[1].click();
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editLOINCCodes.jsp"));
		assertTrue(wr.getText().contains("Success: 10666-6 updated"));
		assertLogged(TransactionType.LOINC_CODE_EDIT, 9000000001L, 0L, "");
	}
	
	public void testAdminEnterNewICDCode() throws Exception {
		//gen.clearAllTables();
		//gen.standardData();
		
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ICD Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain ICD Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code", "99.3");
		form.setCheckbox("classification", true);
		form.setParameter("description", "Tintinnabulum Bovi Deficiency");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Success: 99.3 - Tintinnabulum Bovi Deficiency added"));
		assertLogged(TransactionType.DIAGNOSIS_CODE_ADD, 9000000001L, 0L, "");
	}
	
	public void testAdminUpdateICDCode() throws Exception {
		//gen.clearAllTables();
		//gen.standardData();
		
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ICD Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain ICD Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code", "99.3");
		form.setCheckbox("classification", true);
		form.setParameter("description", "Tintinnabulum Bovi Deficiency");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Success: 99.3 - Tintinnabulum Bovi Deficiency added"));
		
		wr = wr.getLinkWith("Tintinnabulum Bovi Deficiency").click();
		form = wr.getForms()[0];
		form.setCheckbox("classification", false);
		form.getSubmitButtons()[1].click();
	
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Success: 1 row(s) updated"));
		assertLogged(TransactionType.DIAGNOSIS_CODE_EDIT, 9000000001L, 0L, "");
	}
	
	public void testAdminUpdateICDCode2() throws Exception {
		//gen.clearAllTables();
		//gen.standardData();
		
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ICD Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain ICD Codes", wr.getTitle());
		
		wr = wr.getLinkWith("Tuberculosis of the lung").click();
		WebForm form = wr.getForms()[0];
		form.setParameter("description", "Way too long of a description which is limited to a certain size, how bif can it go I wonder?");
		form.getSubmitButtons()[1].click();
	
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Description: Up to 30 characters, letters, numbers, and a space"));
		assertNotLogged(TransactionType.DIAGNOSIS_CODE_EDIT, 9000000001L, 0L, "");
	}
	
}
