package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.Button;
import com.meterware.httpunit.TableRow;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class LabProceduresTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.hcp0();
		gen.patient2();
		gen.patient4();
		gen.loincs();
		gen.labProcedures();
	}
	
	public void testAddLabProcedureWithLabTech() throws Exception {
		gen.clearAllTables();
		gen.standardData();
				
		WebConversation wc = login("9000000000", "pw");
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
		// Select the office visit from specific date
		wr.getLinkWith("6/10/2007").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		form = wr.getFormWithID("labProcedureForm");
		form.setParameter("loinc", "10666-6");
		form.setParameter("labTech", "5000000001");
		form.setParameter("labProcPriority", "1");
		form.getButtonWithID("add_labProcedure").click();

		// check updated page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Lab Procedure information successfully updated."));
		assertLogged(TransactionType.LAB_PROCEDURE_ADD, 9000000000L, 2L, "");
		
		// Get row containing procedure.
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		TableRow[] rows = tbl.getRows();
		int rowi = 0;
		for (; rowi<rows.length; rowi++) {
			if (rows[rowi].getText().startsWith("| 10666-6")) {
				break;
			}
		}
		assertTrue(rowi < rows.length);
		// Verify data in table
		assertEquals("Lab Dude", tbl.getCellAsText(rowi, 1));
		assertEquals("In Transit", tbl.getCellAsText(rowi, 2)); // status
		assertEquals("", tbl.getCellAsText(rowi, 5)); // commentary
		assertEquals("", tbl.getCellAsText(rowi, 6)); // numerical results
		assertEquals("", tbl.getCellAsText(rowi, 7)); // lower bound
		assertEquals("", tbl.getCellAsText(rowi, 8)); // upper bound
		assertTrue(tbl.getCellAsText(rowi, 10).contains("Remove")); // action
		assertTrue(tbl.getCellAsText(rowi, 10).contains("Reassign")); // action
	}

	/**
	 * Tests adding a lab procedure with no lab tech selected. Verifies that an error message is displayed.
	 * @throws Exception
	 */
	public void testAddLabProcedureWithoutLabTech() throws Exception {
		gen.clearAllTables();
		gen.standardData();
				
		WebConversation wc = login("9000000000", "pw");
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
		// Select the office visit from specific date
		wr.getLinkWith("6/10/2007").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		form = wr.getFormWithID("labProcedureForm");
		form.setParameter("loinc", "10666-6");
		form.setParameter("labProcPriority", "1");
		form.getButtonWithID("add_labProcedure").click();

		// check updated page
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("A lab tech must be selected before adding a laboratory procedure."));
	}

	/**
	 * testReassignLabProcedure
	 * @throws Exception
	 */
	public void testReassignLabProcedure() throws Exception {
		gen.clearAllTables();
		gen.standardData();
				
		WebConversation wc = login("9000000000", "pw");
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
		// Select the office visit from specific date
		wr.getLinkWith("6/10/2007").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// Click the Reassign link.
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		assertEquals("Nice Guy", tbl.getCellAsText(2, 1));
		assertTrue(tbl.getCellAsText(2, 10).contains("Reassign"));
		wr = tbl.getTableCell(2, 10).getLinkWith("Reassign").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Reassign Lab Procedure", wr.getTitle());
		
		// Change the currently-assigned lab tech
		form = wr.getFormWithID("reassignLabProcedureForm");
		form.setParameter("newLabTech", "5000000001");
		form.setParameter("labProcPriority", "1");
		form.getButtonWithID("setLabTech").click();

		assertLogged(TransactionType.LAB_RESULTS_REASSIGN, 9000000000L, 2L, "");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());

		// Ensure the lab tech has been changed
		tbl = wr.getTableWithID("labProceduresTable");
		assertEquals("Lab Dude", tbl.getCellAsText(2, 1));
	}
	
	/**
	 * testPatientViewLabProcedureResults
	 * @throws Exception
	 */
	public void testPatientViewLabProcedureResults() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		
		WebConversation wc = login("22", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Lab Procedures").click();
		
		assertEquals("iTrust - View Lab Procedures", wr.getTitle());
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		assertEquals(51, tbl.getRowCount());
		
		int numericalColumn = 5;
		assertEquals("", tbl.getCellAsText(13, numericalColumn));
		assertEquals("", tbl.getCellAsText(12, numericalColumn));
		assertEquals("", tbl.getCellAsText(11, numericalColumn));
		
		assertEquals("7", tbl.getCellAsText(10, numericalColumn));
		assertEquals("", tbl.getCellAsText(9, numericalColumn));
		assertEquals("", tbl.getCellAsText(8, numericalColumn));
		
		assertEquals("", tbl.getCellAsText(7, numericalColumn));
		assertEquals("5.23", tbl.getCellAsText(6, numericalColumn));
		assertEquals("", tbl.getCellAsText(5, numericalColumn));
		
		assertEquals("", tbl.getCellAsText(4, numericalColumn));
		assertEquals("", tbl.getCellAsText(3, numericalColumn));
		assertEquals("18", tbl.getCellAsText(2, numericalColumn));
		
		int statusColumn = 3;
		assertEquals("In Transit", tbl.getCellAsText(13, statusColumn));
		assertEquals("Received", tbl.getCellAsText(12, statusColumn));
		assertEquals("Pending", tbl.getCellAsText(11, statusColumn));
		
		assertEquals("Completed", tbl.getCellAsText(10, statusColumn));
		assertEquals("In Transit", tbl.getCellAsText(9, statusColumn));
		assertEquals("Received", tbl.getCellAsText(8, statusColumn));
		
		assertEquals("Pending", tbl.getCellAsText(7, statusColumn));
		assertEquals("Completed", tbl.getCellAsText(6, statusColumn));
		assertEquals("In Transit", tbl.getCellAsText(5, statusColumn));
		
		assertEquals("Received", tbl.getCellAsText(4, statusColumn));
		assertEquals("Pending", tbl.getCellAsText(3, statusColumn));
		assertEquals("Completed", tbl.getCellAsText(2, statusColumn));
	}
	
	/**
	 * testPatient_LabProcedureView
	 * @throws Exception
	 */
	public void testPatient_LabProcedureView() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		gen.patientLabProcedures();
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Lab Procedures").click();
		
		assertEquals("iTrust - View Lab Procedures", wr.getTitle());
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		assertEquals(4, tbl.getRowCount());
		
		assertEquals("Kelly Doctor", tbl.getCellAsText(2, 0));
		assertEquals("11/20/2011", tbl.getCellAsText(2, 1));
		assertEquals("Microscopic Observation", tbl.getCellAsText(2, 2));
		assertEquals("Completed", tbl.getCellAsText(2, 3));
		
		assertEquals("Its all done", tbl.getCellAsText(2, 4));
		assertEquals("85", tbl.getCellAsText(2, 5));
		assertEquals("grams", tbl.getCellAsText(2, 6));
		assertEquals("Normal", tbl.getCellAsText(2, 7));
	}
	
	/**
	 * testPatient_LabProcedureView2
	 * @throws Exception
	 */
	public void testPatient_LabProcedureView2() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		gen.patientLabProcedures();
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Lab Procedures").click();
		
		assertEquals("iTrust - View Lab Procedures", wr.getTitle());
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		assertEquals(5, tbl.getRowCount());
		
		assertEquals("Kelly Doctor", tbl.getCellAsText(2, 0));
		assertEquals("10/20/2011", tbl.getCellAsText(2, 1));
		assertEquals("Specimen volume", tbl.getCellAsText(2, 2));
		assertEquals("Completed", tbl.getCellAsText(2, 3));
		
		assertEquals("", tbl.getCellAsText(2, 4));
		assertEquals("79", tbl.getCellAsText(2, 5));
		assertEquals("ml", tbl.getCellAsText(2, 6));
		assertEquals("Abnormal", tbl.getCellAsText(2, 7));
	}
	
	/**
	 * testPatient_LabProcedureViewChart
	 * @throws Exception
	 */
	public void testPatient_LabProcedureViewChart() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		gen.patientLabProcedures();
		
		WebConversation wc = login("21", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Lab Procedures").click();
		
		assertEquals("iTrust - View Lab Procedures", wr.getTitle());
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		assertEquals(5, tbl.getRowCount());
		
		wr = wr.getLinkWithID("viewResultsChart").click();
		assertEquals("Lab Procedure Results Chart", wr.getTitle());
	}

	/*
	 * Authenticate HCP 9000000000 and Patient 2. HCP 9000000000 has ordered lab procedure 10763-1 for patient
	 * 1 in an office visit on 7/20/2007. InputLabResults has successfully passed. All lab procedure test data
	 * is in database. HCP 9000000000 has authenticated successfully 1. The HCP chooses to view laboratory
	 * procedure results and selects patient 2 2. The LCHP sorts by dates of the last status update. 3. The
	 * LHCP chooses the top procedure (the procedure from InputLabResults). 4. The LHCP allows viewing access
	 * to the laboratory results.
	 */
	/**
	 * testHcpLabProc
	 * @throws Exception
	 */
	public void testHcpLabProc() throws Exception {
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Laboratory Procedures").click();
		// choose patient 1
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		// add the codes and description
		assertEquals("iTrust - View Laboratory Procedures", wr.getTitle());
		wr = wr.getLinkWith("Allow/Disallow Viewing").click();
		assertLogged(TransactionType.LAB_RESULTS_VIEW, 9000000000L, 2L, "");
	}

	/**
	 * testPatientViewLabResults
	 * @throws Exception
	 */
	public void testPatientViewLabResults() throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertEquals("iTrust - View My Records", wr.getTitle());
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
	}
	
	/**
	 * testLabProcedureInTransitToReceived
	 * @throws Exception
	 */
	public void testLabProcedureInTransitToReceived() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		WebConversation wc = login("5000000001", "pw");

		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Lab Tech Home", wr.getTitle());
		
		wr = wr.getLinkWith("All Lab Procedures").click();
		assertEquals("iTrust - View Laboratory Procedures", wr.getTitle());

		
		// Ensure the table of received lab procedures has only one entry
		WebTable tbl = wr.getTableWithID("receivedTable");
		assertEquals(3, tbl.getRowCount());
		assertEquals("Beaker Beaker", tbl.getCellAsText(2, 4));
		
		// Check table of in-transit lab procedures
		tbl = wr.getTableWithID("inTransitTable");
		assertEquals(20, tbl.getRowCount());
		String labProcID = tbl.getCellAsText(2, 0); // lab proc id is auto-assigned
		// Click "Set to Received" on Lab Procedure 
		WebForm form = wr.getFormWithID("inTransitForm");
		Button receivedButton = form.getButtons()[0];
		assertEquals("Set to Received", receivedButton.getText());
		receivedButton.click();
		
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Laboratory Procedures", wr.getTitle());
		
		// Ensure the table of received lab procedures contains the modified lab procedure.
		tbl = wr.getTableWithID("receivedTable");
		assertEquals(4, tbl.getRowCount());
		assertEquals(labProcID, tbl.getCellAsText(2, 0)); // check that the lab proc is now here
		
		// Check that table of in-transit lab procedures has no lab procedures
		tbl = wr.getTableWithID("inTransitTable");
		assertEquals(19, tbl.getRowCount());
	}
	
	

}
