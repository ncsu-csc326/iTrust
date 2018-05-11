package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 24
 */
public class SurveyUseCaseTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
		gen.patient2();		
	}

	/*
	 * Precondition:
	 * Patient 2 and HCP 9000000000 are in the system. 
	 * Patient 2 had an office visit with HCP 9000000000 on 6/10/2007. 
	 * Patient 2 has successfully authenticated.
	 * Description:
	 * Patient 2 chooses to view his records.
	 * Patient 2 clicks a link next to his office visit on 6/10/2007 to take satisfaction survey.
	 * He inputs the following information and submits:
	 * 15 minutes 
	 * 10 minutes 
	 * 3 
	 * 5
	 * Expected Results:
	 * The survey answers are stored and the event is logged.
	 */
	public void testTakeSatisfactionSurveySuccess() throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("My Records").click();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		WebTable wt = wr.getTableStartingWith("Office Visits");
		wr = wt.getTableCell(1, 1).getLinkWith("Complete Visit Survey").click();
		assertTrue(wr.getText().contains("iTrust Patient Survey for Office Visit on Jun 10, 2007"));
		WebForm form = wr.getForms()[0];
		form.setParameter("waitingMinutesString", "15");
		form.setParameter("examMinutesString", "10");
		form.setParameter("Satradios", "satRadio3");
		form.setParameter("Treradios", "treRadio5");
		wr = form.submit();
		assertTrue(wr.getText().contains("Survey Successfully Submitted"));
		assertLogged(TransactionType.SATISFACTION_SURVEY_TAKE, 2L, 2L, "");
		
		// make sure survey cannot be taken again
		wt = wr.getTableStartingWith("Office Visits");
		try {
			wr = wt.getTableCell(1, 1).getLinkWith("Complete Visit Survey").click();
			fail("the link should have disappeared");
		} catch (NullPointerException ex) {
			assertEquals(null, ex.getMessage());
		}
	}
	
	/*
	 * Precondition:
	 * Patient 2 and HCP 9000000000 are in the system. 
	 * Patient 2 had an office visit with HCP 9000000000 on 6/10/2007. 
	 * Patient 2 has successfully authenticated.
	 * Description:
	 * Patient 2 chooses to view his records.
	 * Patient 2 clicks a link next to his office visit on 6/10/2007 to take satisfaction survey.
	 * He inputs the following information and submits:
	 * [none] 
	 * 10 minutes 
	 * 3 
	 * [none]
	 * Expected Results:
	 * The survey answers are stored and the event is logged.
	 */
	public void testTakeSatisfactionSurveySuccess2() throws Exception{
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("My Records").click();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		WebTable wt = wr.getTableStartingWith("Office Visits");
		wr = wt.getTableCell(1, 1).getLinkWith("Complete Visit Survey").click();
		assertTrue(wr.getText().contains("iTrust Patient Survey for Office Visit on Jun 10, 2007"));
		WebForm form = wr.getForms()[0];
		form.setParameter("examMinutesString", "10");
		form.setParameter("Satradios", "satRadio3");
		wr = form.submit();
		assertTrue(wr.getText().contains("Survey Successfully Submitted"));
		assertLogged(TransactionType.SATISFACTION_SURVEY_TAKE, 2L, 2L, "");
		
		// make sure survey cannot be taken again
		wt = wr.getTableStartingWith("Office Visits");
		try {
			wr = wt.getTableCell(1, 1).getLinkWith("Complete Visit Survey").click();
			fail("the link should have disappeared");
		} catch (NullPointerException ex) {
			assertEquals(null, ex.getMessage());
		}
	}
	
	/*
	 * Precondition:
	 * Patient 2 and HCP 9000000000 are in the system. 
	 * Patient 2 had an office visit with HCP 9000000000 on 6/10/2007. 
	 * Patient 2 has successfully authenticated.
	 * Description:
	 * Patient 2 chooses to view his records.
	 * Patient 2 clicks a link next to his office visit on 6/10/2007 to take satisfaction survey.
	 * Patient 2 changes his mind and decides to cancel his input.
	 */
	public void testTakeSatisfactionSurveyCancel() throws Exception{
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("My Records").click();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
		
		WebTable wt = wr.getTableStartingWith("Office Visits");
		wr = wt.getTableCell(1, 1).getLinkWith("Complete Visit Survey").click();
		assertTrue(wr.getText().contains("iTrust Patient Survey for Office Visit on Jun 10, 2007"));
		WebForm form = wr.getForms()[0];
		form.setParameter("examMinutesString", "10");
		form.setParameter("Satradios", "satRadio3");
		
		// patient changes his mind and cancels his input
		wr = wr.getLinkWith("Home").click();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Records").click();
		assertTrue(wr.getText().contains("Patient Information"));
		wt = wr.getTableStartingWith("Office Visits");
		
		// make sure survey CAN still be taken.  This will throw an exception if the survey is not available
		wr = wt.getTableCell(1, 1).getLinkWith("Complete Visit Survey").click();	
		assertNotLogged(TransactionType.SATISFACTION_SURVEY_TAKE, 2L, 2L, "");
	}
}
