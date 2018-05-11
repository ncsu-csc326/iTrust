package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * ViewMyRecordsTest
 */
public class ViewMyRecordsTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.ndCodes();
		gen.uap1();
		gen.patient2();
		gen.patient1();
		gen.patient4();
		gen.hcp0();
		gen.clearLoginFailures();
		gen.hcp3();
	}

	/*
	 * Authenticate Patient
	 * MID: 2
	 * Password: pw
	 * Choose option View My Records
	 */
	/**
	 * testViewRecords3
	 * @throws Exception
	 */
	public void testViewRecords3() throws Exception {
		// login patient 2
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		// click on View My Records
		wr = wr.getLinkWith("My Records").click();
		assertTrue(wr.getText().contains("210.0lbs"));
		assertTrue(wr.getText().contains("500 mg/dL"));
		// click on a particular office visit to check medication and diagnoses
		wr = wr.getLinkWith("Jun 10, 2007").click();
		assertTrue(wr.getText().contains("Diabetes with ketoacidosis"));
		assertTrue(wr.getText().contains("Prioglitazone"));
		assertTrue(wr.getText().contains("Tetracycline"));
		assertTrue(wr.getText().contains("Notes:"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
	}

	/*
	 * Authenticate Patient
	 * MID: 4
	 * Password: pw
	 * Choose option View My Records
	 */
	/**
	 * testViewRecords4
	 * @throws Exception
	 */
	public void testViewRecords4() throws Exception {
		// login patient who has no records
		WebConversation wc = login("4", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 4L, 0L, "");
		
		// upon viewing records, make sure that no exceptions are thrown
		wr = wr.getLinkWith("My Records").click();
		assertFalse(wr.getText().contains("Exception"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 4L, 4L, "");
	}

	/*
	 * Authenticate Patient
	 * MID: 2
	 * Password: pw
	 * Choose option View My Records
	 * Choose to view records for mid 1, the person he represents.
	 */
	/**
	 * testViewRecords5
	 * @throws Exception
	 */
	public void testViewRecords5() throws Exception {
		// login patient 2
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		// click View My Records
		wr = wr.getLinkWith("My Records").click();
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		WebTable wt = wr.getTableStartingWith("Patients Andy Represents");
		
		assertEquals("Random Person", wt.getTableCell(2, 0).getLinkWith("Random Person").getText() );
		wr = wt.getTableCell(2, 0).getLinkWith("Random Person").click();
		
		// check to make sure you are viewing patient 1's records
		assertTrue(wr.getText().contains("You are currently viewing your representee's records"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 1L, "");
	}
}
