package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class ViewMyReportRequestsTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.hcp0();
		gen.patient2();
	}
	
	public void testViewMyReportRequests() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
	
		wr = wr.getLinkWith("My Report Requests").click();
		assertFalse(wr.getText().contains("Exception"));
		wr = wr.getLinkWith("Add a new Report Request").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.COMPREHENSIVE_REPORT_ADD, 9000000000L, 2L, "");

		WebTable table = wr.getTableStartingWithPrefix("Report Requests");
		assertTrue(table.getCellAsText(2, 4).contains("Requested"));
		WebLink[] weblinks = wr.getLinks();
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("View")) {
				wr = weblinks[i].click();
				break;
			}
		}
		
		assertEquals("iTrust - Comprehensive Patient Report", wr.getTitle());
		
		wr = wr.getLinkWith("My Report Requests").click();
		table = wr.getTableStartingWithPrefix("Report Requests");
		assertTrue(table.getCellAsText(2, 4).contains("Viewed"));
		
		weblinks = wr.getLinks();
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("View")) {
				wr = weblinks[i].click();
				break;
			}
		}
		
		assertEquals("iTrust - Comprehensive Patient Report", wr.getTitle());
		assertLogged(TransactionType.COMPREHENSIVE_REPORT_VIEW, 9000000000L, 2L, "");
		
		
	}

}
