package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * test DocumentOfficeVisit
 * Use Case 11
 */
public class DocumentOfficeVisitUseCaseTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * Test AddLabProcedure
	 * @throws Exception
	 */
	public void testAddLabProcedure() throws Exception {
		
		// login UAP
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// click 06/10/2007
		wr.getLinkWith("06/10/2007").click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editOfficeVisit.jsp?ovID=955", wr.getURL().toString());
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		//add new lab procedure
		WebForm form = wr.getFormWithID("labProcedureForm");
		form.setParameter("loinc", "10666-6");
		form.setParameter("labTech", "5000000001");
		form.getSubmitButton("addLP").click();
		wr = wc.getCurrentPage();
		
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("information successfully updated"));
	}
	
	
	/**
	 * Test RemoveLabProcedure
	 * @throws Exception
	 */
	public void testRemoveLabProcedure() throws Exception {
		// login UAP
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// click 10/10/2005
		wr.getLinkWith("06/10/2007").click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editOfficeVisit.jsp?ovID=955", wr.getURL().toString());
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		//remove lab procedure
		WebTable wt = wr.getTableStartingWith("[Top]Laboratory Procedures");
		assertFalse(wt.getText().contains("No Laboratory Procedures on record"));
		//click the remove link
		wt = wr.getTableStartingWith("[Top]Laboratory Procedures");
		
		wr = wt.getTableCell(2, 10).getLinkWith("Remove").click();
		
		// confirm delete
		assertEquals("iTrust - Delete Lab Procedure", wr.getTitle());
		WebForm form = wr.getFormWithID("deleteLabProcedureForm");
		form.getButtonWithID("confirmDelete").click();
		wr = wc.getCurrentPage();
		
		assertLogged(TransactionType.OFFICE_VISIT_EDIT, 8000000009L, 2L, "Office visit");
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("information successfully updated"));
		
		// Cannot remove the next lab procedure--it is not in the In_Transit or
		// Received states.
		
		wt = wr.getTableStartingWith("[Top]Laboratory Procedures");
		assertEquals(null, wt.getTableCell(2, 10).getLinkWith("Remove"));
		/*wr = wt.getTableCell(2, 9).getLinkWith("Remove").click();
		assertLogged(TransactionType.OFFICE_VISIT_EDIT, 8000000009L, 2L, "Office visit");
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("information successfully updated"));*/
		
		assertLogged(TransactionType.OFFICE_VISIT_VIEW, 8000000009L, 2L, "Office visit");
		wt = wr.getTableStartingWith("[top]Laboratory Procedures");
		assertFalse(wt.getText().contains("No Laboratory Procedures on record"));
	}


	public void testAddDiagnosisBlank() throws Exception {
		
		// login UAP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click All Patients
		wr = wr.getLinkWith("All Patients").click();
		
		// Select Trend Setter
		wr = wr.getLinkWith("Trend Setter").click();
		
		// Edit visit
		wr = wr.getLinkWith("Aug 30, 2011").click();

		// Submit add diagnosis form
		WebForm diagnosisForm = wr.getFormWithID("diagnosisForm");
		wr = diagnosisForm.submit();
		
		// Should show a validation error
		assertNotNull(wr.getElementWithID("iTrustFooter"));
		assertNotNull(wr.getElementWithID("diagnosisForm"));
	}
	
	public void testAddDiagnosisGood() throws Exception {
		
		// login UAP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click All Patients
		wr = wr.getLinkWith("All Patients").click();
		
		// Select Trend Setter
		wr = wr.getLinkWith("Trend Setter").click();
		
		// Edit visit
		wr = wr.getLinkWith("Aug 30, 2011").click();

		// Submit add diagnosis form for tuberculosis
		WebForm diagnosisForm = wr.getFormWithID("diagnosisForm");
		diagnosisForm.setParameter("ICDCode", "11.40");
		wr = diagnosisForm.submit();
		
		// Should not show any validation errors
		assertNotNull(wr.getElementWithID("iTrustFooter"));
		assertTrue(wr.getText().contains("Diagnosis information successfully updated"));
	}
	

}
