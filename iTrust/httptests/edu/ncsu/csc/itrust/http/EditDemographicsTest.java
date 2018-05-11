package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class EditDemographicsTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.patient2();
		gen.icd9cmCodes();
	}

	@Override
	protected void tearDown() throws Exception {
		gen.clearAllTables();
	}

	/*
	 * Authenticate UAP:
	 * MID: 8000000009
	 * Password: uappass1
	 * Choose Edit Patient.
	 * Select patient 2 and confirm.
	 * Change Field:
	 * Street address 1: <script>alert('HACK!');</script>
	 * Confirm and approve the selection
	 */
	public void testEditDemographics2() throws Exception {
		// login uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Patient").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
		
		// update street address to be blank
		WebForm form = wr.getForms()[0];
		form.setParameter("streetAddress1", "<script>alert('HACK!');</script>");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Street Address 1: Up to 30 alphanumeric characters, and ."));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 8000000009L, 2L, "");
	}

	/*
	 * Authenticate UAP:
	 * MID: 8000000009
	 * Password: uappass1
	 * Choose Edit Patient.
	 * Select patient 2 and confirm.
	 * Change Field:
	 * Street address 1: 100 New Address
	 * City: New Bern
	 * State: NC
	 * Zip: 28562
	 * Confirm and approve the selection
	 */
	public void testEditDemographics3() throws Exception {
		// login uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Patient").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
		// update street address to be blank
		WebForm form = wr.getForms()[0];
		form.setParameter("streetAddress1", "100 New Address");
		form.setParameter("city", "New Bern");
		form.setParameter("state", "NC");
		form.setParameter("zip", "28562");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 8000000009L, 2L, "");
	}

	/*
	 * Authenticate UAP:
	 * MID: 8000000009
	 * Password: uappass1
	 * Choose Edit Patient.
	 * Select patient 2 and confirm.
	 * Change Field Street address 2 to a blank.
	 * Confirm and approve the selection
	 */
	/*
	 * This test throws exceptions because of date of death, not sure why
	 */
	public void testEditDemographics5() throws Exception {
		// login hcp
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Patient").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
		// update street address to be blank
		WebForm form = wr.getForms()[0];
		form.setParameter("streetAddress2", "");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 8000000009L, 2L, "");
	}

	/*
	 * Authenticate UAP:
	 * MID: 8000000009
	 * Password: uappass1
	 * Choose Edit Patient.
	 * Select patient 2 and confirm.
	 * Change phone to xxx-xxx-xxxx
	 * Confirm and approve the selection
	 */
	/*
	 * This test throws exception based on parsing the date of death....not sure how to fix 
	 * that right now.
	 */
	public void testEditDemographics6() throws Exception {
		// login as uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		// choose Edit Patient
		wr = wr.getLinkWith("Edit Patient").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
		// update phone number with invalid characters
		WebForm form = wr.getForms()[0];
		form.setParameter("phone", "xxx-xxx-xxxx");
		form.setParameter("dateOfDeathStr", "");
		form.getButtons()[2].click();
		WebResponse add = wc.getCurrentPage();
		assertTrue(add.getText().contains("This form has not been validated correctly."));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 8000000009L, 2L, "");
	}
	
	public void testEditDemographics7() throws Exception {
		// login as Patient 1
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		// choose My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		// tests if google button is present
		try {
			wr.getLinkWith("Sync With Google Account").click();
			//if it gets to here then the test failed
			assertTrue(false);
		}catch(NullPointerException e) {
			//if it gets to here then the test passed
			assertTrue(true);
		}
	}
}
