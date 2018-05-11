package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.TableRow;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * PatientInstructionsUseCaseTest
 */
public class PatientInstructionsUseCaseTest extends iTrustHTTPTest {

	/**
	 * setUp
	 */
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * testAcceptanceScenario1
	 * @throws Exception
	 */
	public void testAcceptanceScenario1() throws Exception {
		gen.hcp4();

		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/28/2011");
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "Has flu.");
		form.getButtonWithID("update").click();
		
		// Check that it was created
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		// Add instructions 
		form = wr.getFormWithID("patientInstructionsForm");
		form.setParameter("name", "Flu Diet");
		form.setParameter("url", "http://www.webmd.com/cold-and-flu/flu-guide/what-to-eat-when-you-have-the-flu");
		form.setParameter("comment", "Eat a healthy diet to help you get over the flu faster! Take your vitamins and drink lots of fluids");
		form.getButtonWithID("addPatientInstructionsButton").click();
		
		// check updated page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient-Specific Instructions information successfully updated."));
		assertLogged(TransactionType.PATIENT_INSTRUCTIONS_ADD, 9000000004L, 1L, "");

		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Flu Diet"));
	}
	
	/**
	 * testAcceptanceScenario2
	 * @throws Exception
	 */
	public void testAcceptanceScenario2() throws Exception {
		gen.hcp4();
		gen.uc44_acceptance_scenario_2();
		
		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		wr.getLinkWith("1/28/2011").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// Verify instructions are visible
		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Flu Diet"));
		// Click the remove link
		assertEquals(8, tbl.getRowCount());
		assertEquals(4, tbl.getColumnCount());
		wr = tbl.getTableCell(2, 3).getLinkWith("Remove").click();
		
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("Patient-Specific Instructions information successfully updated."));
		// verify the instructions have been deleted
		tbl = wr.getTableWithID("patientInstructionsTable");
		rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("No Patient Instructions on record"));
	}
	
	/**
	 * testAcceptanceScenario3
	 * @throws Exception
	 */
	public void testAcceptanceScenario3() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		gen.uc44_acceptance_scenario_3();
		
		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "5");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		wr.getLinkWith("1/28/2011").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());

		form = wr.getFormWithID("patientInstructionsForm");
		form.setParameter("name", "Infant Milestones - 1 to 6 months.");
		form.setParameter("url", "http://www.babycenter.com/0_milestone-chart-1-to-6-months_1496585.bc");
		form.setParameter("comment", "Watch for Baby Programmer rolling over soon. Make sure to prevent falls from furniture.");
		form.getButtonWithID("addPatientInstructionsButton").click();
		
		// check updated page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient-Specific Instructions information successfully updated."));
		assertLogged(TransactionType.PATIENT_INSTRUCTIONS_ADD, 9000000003L, 5L, "");

		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Infant Milestones - 1 to 6 months."));
		
	}
	
	/**
	 * testAcceptanceScenario4
	 * @throws Exception
	 */
	public void testAcceptanceScenario4() throws Exception {
		gen.hcp4();

		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/20/2011");
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "Wrist Pain.");
		form.getButtonWithID("update").click();
		
		// Check that it was created
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		// Add instructions 
		form = wr.getFormWithID("patientInstructionsForm");
		form.setParameter("name", "Carpal Tunnel Syndrome Exercises");
		form.setParameter("url", "http://www.eatonhand.com/hw/ctexercise.htm");
		form.setParameter("comment", "Remember to try all the exercises. Use the following order: #1, #2 and #6; #3 - #5 are 'optional'. If you have any questions, please let me know!");
		form.getButtonWithID("addPatientInstructionsButton").click();
		
		// check updated page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient-Specific Instructions information successfully updated."));
		assertLogged(TransactionType.PATIENT_INSTRUCTIONS_ADD, 9000000004L, 2L, "");

		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Carpal Tunnel Syndrome Exercises"));
	}
	
	/**
	 * testAcceptanceScenario5
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void testAcceptanceScenario5() throws Exception {
		gen.hcp5();
		gen.uc44_acceptance_scenario_5();
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		// click Patient Specific Instructions
		wr = wr.getLinkWith("Patient Specific Instructions").click();

		assertEquals("iTrust - View Patient-Specific Instructions", wr.getTitle());
		
		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		int heartburnRow = 0;
		int veinRow = 0;
		if (tbl.getTableCell(2, 2).asText().equals("Heartburn Information")) {
			heartburnRow = 2;
		} else if (tbl.getTableCell(3, 2).asText().equals("Heartburn Information")) {
			heartburnRow = 3;
		} else if (tbl.getTableCell(4, 2).asText().equals("Heartburn Information")) {
			heartburnRow = 4;
		} else {
			fail("\"Heartburn Information\" not found in table.");
		}
		if (tbl.getTableCell(2, 2).asText().equals("Glucose Testing Information")) {
			fail("\"Glucose Testing Information\" was found in table.");
		} else if (tbl.getTableCell(3, 2).asText().equals("Glucose Testing Information")) {
			fail("\"Glucose Testing Information\" was found in table.");
		}
		if (tbl.getTableCell(2, 2).asText().equals("Vein Procedure Resource")) {
			veinRow = 2;
		} else if (tbl.getTableCell(3, 2).asText().equals("Vein Procedure Resource")) {
			veinRow = 3;
		} else {
			fail("\"Vein Procedure Resource\" not found in table.");
		}
		// verify information in patient instructions table
		assertEquals("Heartburn Information", tbl.getTableCell(heartburnRow, 2).asText());
		assertEquals("Sarah Soulcrusher", tbl.getTableCell(heartburnRow, 1).asText());
		assertEquals("06/10/2007", tbl.getTableCell(heartburnRow, 0).asText());
		
		assertEquals("Vein Procedure Resource", tbl.getTableCell(veinRow, 2).asText());
		assertEquals("Kelly Doctor", tbl.getTableCell(veinRow, 1).asText());
		assertEquals("06/09/2007", tbl.getTableCell(veinRow, 0).asText());
		// verify link address
		assertEquals("http://www.mayoclinic.com/health/sclerotherapy/MY01302",
				     wr.getLinkWith("Vein Procedure Resource").getURLString());
	}
	
	/**
	 * testLinkToOfficeVisit
	 * @throws Exception
	 */
	public void testLinkToOfficeVisit() throws Exception {
		gen.hcp4();
		gen.uc44_acceptance_scenario_2();
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		// click Patient Specific instructions
		wr = wr.getLinkWith("Patient Specific Instructions").click();

		assertEquals("iTrust - View Patient-Specific Instructions", wr.getTitle());
		
		// Follow link to office visit page
		wr = wr.getLinkWith("1/28/2011").click();
		assertEquals("iTrust - View Office Visit Details", wr.getTitle());
	}
	
	/**
	 * testMissingField
	 * @throws Exception
	 */
	public void testMissingField() throws Exception {
		gen.hcp4();

		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/20/2011");
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "Wrist Pain.");
		form.getButtonWithID("update").click();
		
		// Check that it was created
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		// Add instructions, but with a field missing
		form = wr.getFormWithID("patientInstructionsForm");
		form.setParameter("name", "Carpal Tunnel Syndrome Exercises");
		form.setParameter("url", "http://www.eatonhand.com/hw/ctexercise.htm");
		form.getButtonWithID("addPatientInstructionsButton").click();
		
		// check for error page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Comments: Up to 500 alphanumeric characters, with space, and other punctuation"));
		
		// check that form fields still contain prior values
		form = wr.getFormWithID("patientInstructionsForm");
		assertEquals("Carpal Tunnel Syndrome Exercises", form.getParameterValue("name"));
		assertEquals("http://www.eatonhand.com/hw/ctexercise.htm", form.getParameterValue("url"));
		assertEquals("", form.getParameterValue("comment"));
	}
	
	/**
	 * testTooManyCharacters
	 * @throws Exception
	 */
	public void testTooManyCharacters() throws Exception {
		gen.hcp4();

		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/20/2011");
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "Wrist Pain.");
		form.getButtonWithID("update").click();
		
		// Check that it was created
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		// Add instructions, but with the name is too long
		form = wr.getFormWithID("patientInstructionsForm");
		form.setParameter("comment", "Let me know if you have any questions");
		form.setParameter("url", "http://www.eatonhand.com/hw/ctexercise.htm");
		form.setParameter("name", "This is a very long name. In fact it is too long for Patient Specific Instructions. The name must be less than 100 characters.");
		form.getButtonWithID("addPatientInstructionsButton").click();
		
		// check for error page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Name: Up to 100 alphanumeric characters, with space, and other punctuation"));

		// check that form fields still contain prior values
		form = wr.getFormWithID("patientInstructionsForm");
		assertEquals("This is a very long name. In fact it is too long for Patient Specific Instructions. The name must be less than 100 characters.", form.getParameterValue("name"));
		assertEquals("http://www.eatonhand.com/hw/ctexercise.htm", form.getParameterValue("url"));
		assertEquals("Let me know if you have any questions", form.getParameterValue("comment"));
	}
	
	/**
	 * testModifiedDate
	 * @throws Exception
	 */
	public void testModifiedDate() throws Exception {
		gen.hcp4();
		gen.uc44_acceptance_scenario_2();
		
		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		wr.getLinkWith("1/28/2011").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// Verify instructions are visible
		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Flu Diet"));
		// Click the edit link
		assertEquals(8, tbl.getRowCount());
		assertEquals(4, tbl.getColumnCount());
		wr = tbl.getTableCell(2, 3).getLinkWith("Edit").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());

		// check that the form now contains the values we're editing
		form = wr.getFormWithID("patientInstructionsForm");
		assertEquals("Flu Diet", form.getParameterValue("name"));
		assertEquals("http://www.webmd.com/cold-and-flu/flu-guide/what-to-eat-when-you-have-the-flu", 
					 form.getParameterValue("url"));
		assertEquals("Eat a healthy diet to help you get over the flu faster! Take your vitamins and drink lots of fluids", 
				     form.getParameterValue("comment"));
		
		form.setParameter("comment", "I hate the flu!");
		form.getButtonWithID("updatePatientInstructionsButton").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// Verify instructions are visible
		tbl = wr.getTableWithID("patientInstructionsTable");
		rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Flu Diet"));
		// verify the table is the same size as before
		assertEquals(8, tbl.getRowCount());
		assertEquals(4, tbl.getColumnCount());
		
		// verify that the comments have changed
		assertEquals("I hate the flu!", tbl.getCellAsText(2, 1));
	}
	
	/**
	 * testSecondEntry
	 * @throws Exception
	 */
	public void testSecondEntry() throws Exception {
		gen.hcp4();
		gen.uc44_acceptance_scenario_2();
		
		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		wr.getLinkWith("1/28/2011").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());

		form = wr.getFormWithID("patientInstructionsForm");
		form.setParameter("name", "How to get rid of the flu in 5 minutes.");
		form.setParameter("url", "http://www.example.com");
		form.setParameter("comment", "You should try this miracle cure. It is not bogus or anything.");
		form.getButtonWithID("addPatientInstructionsButton").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// Verify instructions are visible
		WebTable tbl = wr.getTableWithID("patientInstructionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Flu Diet"));
		assertTrue(rows[3].getText().contains("How to get rid of the flu in 5 minutes."));
		// verify the table has one more row then before
		assertEquals(9, tbl.getRowCount());
		assertEquals(4, tbl.getColumnCount());
		
	}

}
