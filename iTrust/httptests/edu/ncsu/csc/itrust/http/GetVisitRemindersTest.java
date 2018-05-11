package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.DateUtil;

/**
 * Use Case 17
 */
public class GetVisitRemindersTest extends iTrustHTTPTest {
	protected void setUp() throws Exception{
		super.setUp();
		//gen.clearAllTables();
		gen.hcp0();
	}
	
	public void testGetVisitReminders_TestInitialPage() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Office Visit Reminders").click();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		assertTrue(wr.getText().contains("<h2>Patients Needing Visits</h2>"));
		assertTrue(wr.getElementsWithName("ReminderType")[0].getText().contains("Diagnosed Care Needers"));
		assertTrue(wr.getElementsWithName("ReminderType")[0].getText().contains("Flu Shot Needers"));
		assertTrue(wr.getElementsWithName("ReminderType")[0].getText().contains("Immunization Needers"));
		assertEquals("Get Reminders", wr.getElementsWithName("getReminders")[0].getAttribute("value"));
	}
	
	public void testGetVisitReminders_DiagnosedCareNeeders() throws Exception {
		gen.standardData();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Office Visit Reminders").click();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		WebForm wf = wr.getFormWithID("reminderForm");
		wf.setParameter("ReminderType", "Diagnosed Care Needers");
		wf.getButtonWithID("getReminders").click();
		wr = wc.getCurrentPage();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		assertEquals(4, wr.getTables().length);

		WebTable table = wr.getTables()[0];
		assertEquals("Zappic Clith", table.getCellAsText(1, 1));
		assertEquals("919-555-9213", table.getCellAsText(2, 1));
		
		table = wr.getTables()[1];
		assertEquals("Random Person", table.getCellAsText(1, 1));
		assertEquals("919-971-0000", table.getCellAsText(2, 1));
		
		table = wr.getTables()[2];
		assertEquals("Anakin Skywalker", table.getCellAsText(1, 1));
		assertEquals("919-419-5555", table.getCellAsText(2, 1));
		
		table = wr.getTables()[3];
		assertEquals("Darryl Thompson", table.getCellAsText(1, 1));
		assertEquals("919-555-6709", table.getCellAsText(2, 1));
	}
	
	/**
	 * testGetVisitReminders_FluShotNeeders
	 * @throws Exception
	 */
	public void testGetVisitReminders_FluShotNeeders() throws Exception {
		gen.standardData();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Office Visit Reminders").click();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		WebForm wf = wr.getFormWithID("reminderForm");
		wf.setParameter("ReminderType", "Flu Shot Needers");
		wf.getButtonWithID("getReminders").click();
		wr = wc.getCurrentPage();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		assertEquals(4, wr.getTables().length);
		
		boolean thisYear = DateUtil.currentlyInMonthRange(8, 11);
		String pretext = "Missed";
		if (thisYear)
			pretext = "Currently Missing";
		
		WebTable table = wr.getTables()[0];
		assertEquals("NoRecords Has", table.getCellAsText(1, 1));
		assertEquals("919-971-0000", table.getCellAsText(2, 1));
		assertEquals(pretext + " Medication:    Flu Shot", table.getCellAsText(3, 1));

		table = wr.getTables()[1];
		assertEquals("Bad Horse", table.getCellAsText(1, 1));
		assertEquals("919-123-4567", table.getCellAsText(2, 1));
		assertEquals(pretext + " Medication:    Flu Shot", table.getCellAsText(3, 1));
		
		table = wr.getTables()[2];
		assertEquals("Care Needs", table.getCellAsText(1, 1));
		assertEquals("919-971-0000", table.getCellAsText(2, 1));
		assertEquals(pretext + " Medication:    Flu Shot", table.getCellAsText(3, 1));
		
		table = wr.getTables()[3];
		assertEquals("Random Person", table.getCellAsText(1, 1));
		assertEquals("919-971-0000", table.getCellAsText(2, 1));
		assertEquals(pretext + " Medication:    Flu Shot", table.getCellAsText(3, 1));
	}
	
	/**
	 * testGetVisitReminders_ImmunizationNeeders
	 * @throws Exception
	 */
	public void testGetVisitReminders_ImmunizationNeeders() throws Exception {
		gen.standardData();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Office Visit Reminders").click();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		WebForm wf = wr.getFormWithID("reminderForm");
		wf.setParameter("ReminderType", "Immunization Needers");
		wf.getButtonWithID("getReminders").click();
		wr = wc.getCurrentPage();
		
		assertEquals("iTrust - Visit Reminders", wr.getTitle());
		assertEquals(0, wr.getTables().length);
		
//		WebTable table = wr.getTables()[0];
//		assertEquals("Baby A", table.getCellAsText(1, 1));
//		assertEquals("919-971-0000", table.getCellAsText(2, 1));
//		assertEquals("Needs Immunization:    " +
//				"90371 Hepatitis B (6 months), " +
//				"90681 Rotavirus (6 months), " +
//				"90696 Diphtheria, Tetanus, Pertussis (15 weeks), " +
//				"90669 Pneumococcal (12 months), " +
//				"90649 Human Papillomavirus (9 years, 6 months)", table.getCellAsText(3, 1));
//
//		table = wr.getTables()[1];
//		assertEquals("Baby C", table.getCellAsText(1, 1));
//		assertEquals("919-971-0000", table.getCellAsText(2, 1));
//		assertEquals("Needs Immunization:    " +
//				"90371 Hepatitis B (1 month), " +
//				"90696 Diphtheria, Tetanus, Pertussis (6 weeks), " +
//				"90396 Varicella (12 months), " +
//				"90633 Hepatits A (18 months)", 
//				table.getCellAsText(3, 1));
	}
}
