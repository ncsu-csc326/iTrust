package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * Use Case 42
 */
public class PendingApptTest extends iTrustHTTPTest{
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.pendingAppointmentAlert();
	}
	
	public void testPendingAppointmentAlert () throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Appointment requests."));
	}
	
	public void testAcceptAnAppointment() throws Exception{
		gen.clearAllTables();
		gen.standardData();
		gen.pendingAppointmentAlert();
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr.getLinkWith("Appointment Requests").click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Approve").click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Home").click();
		wr = wc.getCurrentPage();
		
		assertFalse(wr.getText().contains("Appointment requests."));
	}
	
	public void testConflictingAppt() throws Exception{
		gen.clearAllTables();
		gen.standardData();
		gen.pendingAppointmentConflict();
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr.getLinkWith("Appointment Requests").click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Approve").click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Home").click();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getLinkWith("1") != wr.getLinkWith("2"));
	}
	
	public void testDeclineAnAppointment() throws Exception{
		gen.clearAllTables();
		gen.standardData();
		gen.pendingAppointmentAlert();
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr.getLinkWith("Appointment Requests").click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Reject").click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Home").click();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("No appointment requests."));
	}
}