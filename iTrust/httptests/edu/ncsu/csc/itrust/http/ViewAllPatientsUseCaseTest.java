package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 28
 */
public class ViewAllPatientsUseCaseTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		HttpUnitOptions.setScriptingEnabled(false);
		gen.clearAllTables();
		gen.standardData();		
	}

	/*
	 * Precondition:
	 * LHCP 9000000000 and Patients 1-4 are in the database 
	 * Office Visits 11, 902-911, 111, and 1 are in the database. 
	 * LHCP 9000000000 has authenticated successfully.
	 * Description:
	 * 1. LHCP clicks on "View All Patients" link.
	 * Expected Results:
	 * A list of the following should be displayed:
     * Random person
     * Andy programmer
     * Bowser Koopa
	 */
	/**
	 * testViewAllPatients
	 * @throws Exception
	 */
	public void testViewAllPatients() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("All Patients").click();
		assertEquals("iTrust - View All Patients", wr.getTitle());
		assertLogged(TransactionType.PATIENT_LIST_VIEW, 9000000000L, 0L, "");
		
		WebTable wt = wr.getTableStartingWith("Patient");
		assertEquals("10/10/2008", wt.getTableCell(1, 2).getText());
		assertEquals("09/14/2009", wt.getTableCell(2, 2).getText());
		assertEquals("", wt.getTableCell(4, 2).getText());
		
		assertEquals("344 Bob Street  Raleigh NC 27607", wt.getTableCell(2, 1).getText());

	}
}
