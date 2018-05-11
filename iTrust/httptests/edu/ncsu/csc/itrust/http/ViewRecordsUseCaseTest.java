package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 9
 */
public class ViewRecordsUseCaseTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testViewMyRecords() throws Exception {
		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		// Records page contains patient information
		assertTrue(wr.getText().contains("Patient Information"));
	}
	
	public void testRepresent() throws Exception {
		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		wr = wr.getLinkWith("Baby Programmer").click();
		
		// Clicking on a representee's name takes you to their records
		assertTrue(wr.getText().contains("You are currently viewing your representee's records"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 5L, "");
	}
	
	public void testDoctor() throws Exception {
		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		wr = wr.getLinkWith("Kelly Doctor").click();
		assertTrue(wr.getText().contains("kdoctor@iTrust.org"));
	}
	
	/*
	 * Precondition:
	 * Patient 2 and all his data have been loaded into iTrust
	 * Patient 2 has successfully authenticated
	 * Description:
	 * 1. Patient 2 chooses to view his records
	 * 2. Chooses link to office visit "6/10/2007"
	 * Expected Result:
	 * The following data should be displayed: Office Visit Details Date: 06/10/2007
	 *   HCP: Kelly Doctor (9000000000)
	 *   Diagnoses
	 *   ICD Code	Description
	 *   No Diagnoses for this visit
	 *   Medications
	 *   No Medications on record
	 *   Procedures
	 *   No Procedures on record
	 */
	public void testViewPatientOfficeVisit() throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		assertTrue(wr.getText().contains("Patient Information"));
		WebTable wt = wr.getTableStartingWith("Office Visits");
		wr = wt.getTableCell(2, 0).getLinkWith("Jun 10, 2007").click();
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("Diabetes with ketoacidosis"));
		assertTrue(wr.getText().contains("64764-1512"));
		assertTrue(wr.getText().contains("Injection procedure"));
	}
}
