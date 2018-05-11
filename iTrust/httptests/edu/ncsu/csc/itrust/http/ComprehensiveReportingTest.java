package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;


public class ComprehensiveReportingTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/*
	 * An HCP 9000000000, Admin 9000000001, and Patient MID 2 have been entered into the system. 
	 * The HCP logs in and requests a comprehensive report for patient 2. 
	 * The Admin logs in and approves the new request for patient 2. 
	 * The HCP views the approved comprehensive patient report.
	 */
	public void testComprehensiveAcceptanceSuccess() throws Exception {
		// The HCP logs in and requests a comprehensive report for patient 2.
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("My Report Requests").click();
		assertTrue(wr.getText().contains("Report Requests"));		
		wr = wr.getLinkWith("Add a new Report Request").click();
		assertTrue(wr.getText().contains("Please Select a Patient"));
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Report Request Accepted"));
		assertLogged(TransactionType.COMPREHENSIVE_REPORT_ADD, 9000000000L, 2L, "Report ID:");
	}
	
	/*
	 * An HCP 9000000000 has been entered into the system. 
	 * The HCP logs in and selects request comprehensive report. 
	 * HCP enters patient 22. The system responds that patient 22 cannot be found.
	 */
	public void testHCPChoosesInvalidPatient() throws Exception {
		// The HCP logs in and requests a comprehensive report for patient 2.
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		wr = wr.getLinkWith("My Report Requests").click();
		assertTrue(wr.getText().contains("Report Requests"));
		wr = wr.getLinkWith("Add a new Report Request").click();
		assertTrue(wr.getText().contains("Please Select a Patient"));
        wr = wc.getResponse(ADDRESS + "/util/getUser.jsp");
        //assertEquals("iTrust - Find User", wr.getTitle());
        wr.getForms()[0].setParameter("mid", "260");
        wr = wr.getForms()[0].submit();
        assertTrue(wr.getText().contains("User does not exist"));
        assertNotLogged(TransactionType.COMPREHENSIVE_REPORT_ADD, 9000000000L, 23L, "Report ID:");
	}
	
	/*
	 * An HCP 9000000000 and Patient MID 2 have been entered into the system. 
	 * The HCP logs in and selects request comprehensive report. 
	 * HCP enters patient 2. 
	 * The system responds with the name of this patient "Andy Programmer" and requests confirmation. 
	 * The HCP realizes this is the incorrect patient and cancels the request.
	 */
	public void testHCPChoosesIncorrectPatient() throws Exception {	
		// The HCP logs in and requests a comprehensive report for patient 2.
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("My Report Requests").click();
		assertTrue(wr.getText().contains("Report Requests"));
		wr = wr.getLinkWith("Add a new Report Request").click();
		assertTrue(wr.getText().contains("Please Select a Patient"));
        wr = wc.getResponse(ADDRESS + "/util/getUser.jsp");
        assertEquals("iTrust - Find User", wr.getTitle());
        wr.getForms()[0].setParameter("mid", "2");
        wr = wr.getForms()[0].submit();
        assertTrue(wr.getText().contains("Andy Programmer"));
        wr = wr.getForms()[0].submit(); // Find another user button
        assertEquals("iTrust - Find User", wr.getTitle());
        assertNotLogged(TransactionType.COMPREHENSIVE_REPORT_ADD, 9000000000L, 2L, "Report ID:");
	}
}
