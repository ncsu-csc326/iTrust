package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Cases 9, 11 & 17
 */
public class ImmunizationUseCasesTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testDocumentAndViewImmunizations() throws Exception {

		boolean check = false;
		
		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000003L, 0L, "");
		
		wr = wr.getLinkWith("Office Visit Reminders").click();
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		// Select "Immunization Needers"
		WebForm wf = wr.getFormWithID("reminderForm");
		wf.setParameter("ReminderType", "Immunization Needers");
		// Select "Get Reminders"
		wr = wf.submit();
		// Check for content
		WebTable[] tables = wr.getTables();
		
		for (WebTable t: tables) {
			if ("Patient Information".equals(t.getCellAsText(0, 0))) {
				if ("Bowser Koopa".equals(t.getCellAsText(1, 1))) {
					//assertEquals("Bowser", t.getCellAsText(2, 1));
					assertEquals("Needs Immunization:    90371 Hepatitis B (birth), 90681 Rotavirus (6 weeks), 90696 Diphtheria, Tetanus, Pertussis (6 weeks), 90645 Haemophilus influenzae (6 weeks), 90669 Pneumococcal (6 weeks), 90712 Poliovirus (6 weeks), 90707 Measles, Mumps, Rubekka (12 months), 90396 Varicella (12 months), 90633 Hepatits A (12 months)",
								t.getCellAsText(3, 1));
					check = true;
				}
				else if ("Princess Peach".equals(t.getCellAsText(1, 1))) {
					//assertEquals("Princess", t.getCellAsText(2,1));
					assertEquals("Needs Immunization:    90371 Hepatitis B (birth), 90681 Rotavirus (6 weeks), 90696 Diphtheria, Tetanus, Pertussis (6 weeks), 90645 Haemophilus influenzae (6 weeks), 90669 Pneumococcal (6 weeks), 90712 Poliovirus (6 weeks), 90707 Measles, Mumps, Rubekka (12 months), 90396 Varicella (12 months), 90633 Hepatits A (12 months)",
								t.getCellAsText(3, 1));
					check = true;
				}
			}
		}
		assertTrue(check);
		assertLogged(TransactionType.PATIENT_REMINDERS_VIEW, 9000000003L, 0L, "");
	}

	/**
	 *  test update Immunization
	 *  throws Exception
	 *   
	 *   
	 *
	 */

	/**
	 *  test view Immunization
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testViewImmunizationRecord() throws Exception {
		
		WebConversation wc = login("6", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 6L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertEquals("iTrust - View My Records", wr.getTitle());
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 6L, 6L, "");
		
		WebTable table = wr.getTableStartingWith("Immunizations");
		assertEquals("90649", table.getCellAsText(2, 0));
		assertEquals("90649", table.getCellAsText(3, 0));
		assertEquals("90707", table.getCellAsText(4, 0));
		assertEquals("90396", table.getCellAsText(5, 0));
		assertEquals("90633", table.getCellAsText(6, 0));
		assertEquals("90645", table.getCellAsText(7, 0));
		assertEquals("90707", table.getCellAsText(8, 0));
		assertEquals("90396", table.getCellAsText(9, 0));
		assertEquals("90633", table.getCellAsText(10, 0));
		assertEquals("90696", table.getCellAsText(11, 0));
		assertEquals("90669", table.getCellAsText(12, 0));
		assertEquals("90712", table.getCellAsText(13, 0));
		assertEquals("90681", table.getCellAsText(14, 0));
		assertEquals("90696", table.getCellAsText(15, 0));
		assertEquals("90645", table.getCellAsText(16, 0));
		assertEquals("90669", table.getCellAsText(17, 0));
		assertEquals("90712", table.getCellAsText(18, 0));
		assertEquals("90681", table.getCellAsText(19, 0));
		assertEquals("90696", table.getCellAsText(20, 0));
		assertEquals("90645", table.getCellAsText(21, 0));
		assertEquals("90669", table.getCellAsText(22, 0));
		assertEquals("90712", table.getCellAsText(23, 0));
		assertEquals("90371", table.getCellAsText(24, 0));
		assertEquals("90371", table.getCellAsText(25, 0));
	}
	/**
	 *  test update ImmunizationRecord
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testViewImmunizationRecord2() throws Exception {
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertEquals("iTrust - View My Records", wr.getTitle());
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		WebTable table = wr.getTableStartingWith("Immunizations");
		assertEquals("No Data", table.getCellAsText(2, 0));
		
	}
	
	public void testDocumentImmunization() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "6");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		wr = wr.getLinkWith("07/10/2004").click();
		WebTable table = wr.getTableStartingWith("[Top]Immunizations");
		assertEquals("90649", table.getCellAsText(2, 0));
		assertLogged(TransactionType.OFFICE_VISIT_VIEW, 9000000000L, 6L, "Office visit");
		
	}
	/**
	 *  test update Immunization record2
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testDocumentImmunization2() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "7");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		wr = wr.getLinkWith("05/10/2006").click();
		WebTable table = wr.getTableStartingWith("[Top]Immunizations");
		assertEquals("90696", table.getCellAsText(2, 0));
		wr = wr.getLinkWith("Remove").click();
		table = wr.getTableStartingWith("[Top]Immunizations");
		assertEquals("No immunizations on record", table.getCellAsText(2, 0));
		assertLogged(TransactionType.OFFICE_VISIT_VIEW, 9000000000L, 7L, "Office visit");
		
	}
}
