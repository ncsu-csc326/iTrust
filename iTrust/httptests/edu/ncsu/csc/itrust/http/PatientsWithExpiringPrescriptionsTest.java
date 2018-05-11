package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.TableRow;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class PatientsWithExpiringPrescriptionsTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.ndCodes();
		gen.hospitals();
		gen.hcp1();
		gen.hcp2();
		gen.hcp3();
		gen.patient9();
		gen.patient10();
		gen.patient11();
		gen.patient12();
		gen.patient13();
		gen.patient14();
		
		//gen.UC32Acceptance();
		//gen.clearLoginFailures();
	}

	/*
	 * An equivalence class test for a patient who should appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires in 5 days)
	 * The prescriptions were NOT made on the same visit as a special-diagnosis.
	 */
	public void testPatient9() throws Exception {
		
		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("9900000000"));
		assertTrue(wr.getText().contains("Darryl"));
		assertTrue(wr.getText().contains("Thompson"));
		assertTrue(wr.getText().contains("a@b.com"));
		assertTrue(wr.getText().contains("919-555-6709"));
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 99L, "");
	}
	
	/*
	 * An equivalence class test for a patient who should NOT appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires in 10 days)
	 */
	public void testPatientTen() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("Zappic Clith"));
		assertNotLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 10L, "");
	}
	
	/*
	 * A boundary-value test for a patient who should appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires in 7 days)
	 * Diagnosed with 493.99
	 */
	public void testPatientEleven() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertTrue(wr.getText().contains("Marie"));
		assertTrue(wr.getText().contains("Thompson"));
		assertTrue(wr.getText().contains("e@f.com"));
		assertTrue(wr.getText().contains("919-555-9213"));
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 11L, "");
	}
	
	/*
	 * A boundary-value test for a patient who should NOT appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires in 8 days)
	 */
	public void testPatientTwelve() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("9900000000"));
		assertFalse(wr.getText().contains("Blammo"));
		assertFalse(wr.getText().contains("Volcano"));
		assertNotLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 12L, "");
	}
	
	/*
	 * An equivalence class test for a patient who should NOT appear on the list.
	 * (Designated Tester Arehart, NOT special-diagnosis-history, prescription expires in 5 days)
	 */
	public void testPatientThirteen() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("9900000000"));
		assertFalse(wr.getText().contains("Blim Cildron"));
		assertNotLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 13L, "");
	}
	
	/*
	 * A boundary-value test for a patient who should appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires today)
	 * Diagnosed with 459.99 (This is the closest possible to 460 because the table uses
	 *  decimal(5,2) )
	 */
	public void testPatientFourteen() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("9900000000"));
		assertTrue(wr.getText().contains("Zack"));
		assertTrue(wr.getText().contains("Arthur"));
		assertTrue(wr.getText().contains("k@l.com"));
		assertTrue(wr.getText().contains("919-555-1234"));	
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 14L, "");
	}
	
	/*
	 * A boundary-value test for a patient who should NOT appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expired yesterday)
	 */
	public void testPatientFifteen() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("9900000000"));
		assertFalse(wr.getText().contains("Malk"));
		assertFalse(wr.getText().contains("Flober"));
		assertNotLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 15L, "");
	}
	
	/*
	 * A boundary-value test for a patient who should appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires today)
	 */
	public void testPatientOrdering() throws Exception {

		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9900000000L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		
		WebTable table = wr.getTableStartingWith("Tester Arehart");
		TableRow rows[] = table.getRows();
		
		assertTrue(rows[2].getText().contains("Zack Arthur"));
		assertTrue(rows[3].getText().contains("Darryl Thompson"));
		assertTrue(rows[4].getText().contains("Marie Thompson"));
		
		assertTrue(wr.getText().contains("Tester Arehart</th>"));
		assertFalse(wr.getText().contains("9900000000"));
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 99L, "");
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 11L, "");
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9900000000L, 14L, "");
	}
	
	/*
	 * A boundary-value test for a patient who should appear on the list.
	 * (Designated Tester Arehart, special-diagnosis-history, prescription expires today)
	 */
	public void testAcceptance() throws Exception {
		gen.UC32Acceptance();

		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000003L, 0L, "");
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		
		WebTable table = wr.getTableStartingWith("Gandalf Stormcrow");
		TableRow rows[] = table.getRows();
		
		assertTrue(rows[2].getText().contains("Andy Koopa"));
		assertTrue(rows[3].getText().contains("David Prince"));
		
		assertTrue(wr.getText().contains("Gandalf Stormcrow</th>"));
		assertFalse(wr.getText().contains("9000000003"));
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9000000003L, 16L, "");
		assertLogged(TransactionType.PRECONFIRM_PRESCRIPTION_RENEWAL, 9000000003L, 17L, "");
	}

}
