package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;
//import com.meterware.httpunit.WebTable;

public class ViewExpiredPrescriptionsTest extends iTrustHTTPTest {
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
		gen.patient9();
		gen.hcp0();
		gen.hcp1();
		gen.hcp2();
		gen.clearLoginFailures();
		gen.hcp3();
	}

	/*
	 * Authenticate Patient
	 * MID: 2
	 * Password: pw
	 * Choose option My Expired Prescription Reports
	 */
	public void testViewExpired1() throws Exception {
		// login patient 2
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		// click on My Expired Prescription Reports
		wr = wr.getLinkWith("My Expired Prescription Reports").click();
		assertTrue(wr.getText().contains("00904-2407"));
		assertFalse(wr.getText().contains("9000000000"));
		// click on a particular office visit to check medication and diagnoses
		wr = wr.getLinkWith("Kelly Doctor").click();  // This click does not work at the moment.
		assertTrue(wr.getText().contains("surgeon"));
		assertTrue(wr.getText().contains("4321 My Road St"));
		assertTrue(wr.getText().contains("New York"));
		assertTrue(wr.getText().contains("NY"));
		assertTrue(wr.getText().contains("10453"));
		assertTrue(wr.getText().contains("999-888-7777"));
		assertTrue(wr.getText().contains("kdoctor@iTrust.org"));
		assertFalse(wr.getText().contains("9000000000"));
		assertLogged(TransactionType.EXPIRED_PRESCRIPTION_VIEW, 2L, 2L, "");
		
	}
	
	/*
	 * Authenticate Patient
	 * MID: 99
	 * Password: pw
	 * Choose option My Expired Prescription Reports
	 */
	public void testViewExpired2() throws Exception {
		// login patient 9
		WebConversation wc = login("99", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 99L, 0L, "");
		
		// click on My Expired Prescription Reports
		wr = wr.getLinkWith("My Expired Prescription Reports").click();
		assertTrue(wr.getText().contains("00904-2407"));
		assertTrue(wr.getText().contains("08109-6"));
		assertFalse(wr.getText().contains("9000000000"));
		assertFalse(wr.getText().contains("9900000000"));
		// click on a particular office visit to check medication and diagnoses
		wr = wr.getLinkWith("Tester Arehart").click();  // This click does not work at the moment.
		assertTrue(wr.getText().contains("Neurologist"));
		assertTrue(wr.getText().contains("2110 Thanem Circle"));
		assertTrue(wr.getText().contains("Raleigh"));
		assertTrue(wr.getText().contains("NC"));
		assertTrue(wr.getText().contains("999-888-7777"));
		assertTrue(wr.getText().contains("tarehart@iTrust.org"));
		assertFalse(wr.getText().contains("9900000000"));
		assertLogged(TransactionType.EXPIRED_PRESCRIPTION_VIEW, 99L, 99L, "");
	}
	
	/*
	 * Authenticate Patient
	 * MID: 99
	 * Password: pw
	 * Choose option My Expired Prescription Reports
	 */
	public void testViewExpired3() throws Exception {
		// login patient 9
		WebConversation wc = login("99", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 99L, 0L, "");
		
		// click on My Expired Prescription Reports
		wr = wr.getLinkWith("My Expired Prescription Reports").click();
		assertTrue(wr.getText().contains("00904-2407"));
		assertTrue(wr.getText().contains("08109-6"));
		assertFalse(wr.getText().contains("9000000000"));
		assertFalse(wr.getText().contains("9900000000"));
		assertLogged(TransactionType.EXPIRED_PRESCRIPTION_VIEW, 99L, 99L, "");
		// click on a particular office visit to check medication and diagnoses
		wr = wr.getLinkWith("Kelly Doctor").click();  // This click does not work at the moment.
		assertTrue(wr.getText().contains("surgeon"));
		assertTrue(wr.getText().contains("4321 My Road St"));
		assertTrue(wr.getText().contains("New York"));
		assertTrue(wr.getText().contains("NY"));
		assertTrue(wr.getText().contains("10453"));
		assertTrue(wr.getText().contains("999-888-7777"));
		assertTrue(wr.getText().contains("kdoctor@iTrust.org"));
		assertFalse(wr.getText().contains("9000000000"));		
	}
	
	/*
	 * Make sure that missing contact info is represented as blanks.
	 * MID: 99
	 * Password: pw
	 * Choose option My Expired Prescription Reports
	 */
	public void testViewExpired4() throws Exception {
		// login patient 9
		WebConversation wc = login("99", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 99L, 0L, "");
		
		// click on My Expired Prescription Reports
		wr = wr.getLinkWith("My Expired Prescription Reports").click();
		assertTrue(wr.getText().contains("00904-2407"));
		assertTrue(wr.getText().contains("08109-6"));
		assertFalse(wr.getText().contains("9000000000"));
		assertFalse(wr.getText().contains("9900000000"));
		assertLogged(TransactionType.EXPIRED_PRESCRIPTION_VIEW, 99L, 99L, "");
		// click on a particular office visit to check medication and diagnoses
		wr = wr.getLinkWith("Jimmy Incomplete").click();  // This click does not work at the moment.
		assertFalse(wr.getText().contains("null"));
		assertFalse(wr.getText().contains("AK"));
		assertFalse(wr.getText().contains("9990000000"));
	}

}
