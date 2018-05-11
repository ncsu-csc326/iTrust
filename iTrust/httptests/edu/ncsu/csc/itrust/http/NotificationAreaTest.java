package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import java.util.Date;

/**
 * Use Case 34
 * Use Case 60
 */
@SuppressWarnings("unused")
public class NotificationAreaTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.uc60();
	}
	
	public void testPatientViewDeclaredProviderFromNotificationCenter () throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertTrue(wr.getText().contains("Gandalf Stormcrow"));
		assertTrue(wr.getText().contains("999-888-7777"));
		assertTrue(wr.getText().contains("gstormcrow@iTrust.org"));
	}

	public void testHCPTelemedicineDetailsFromNotificationCenter () throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("MM/dd/yyyy");
		//String tomorrow = formatter.format(new Date((new Date()).getTime() + 86400000));
		gen.appointmentCase3();
		gen.remoteMonitoring3();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertTrue(wr.getText().contains("3 physiological status reports"));
		assertTrue(wr.getText().contains("0 weight/pedometer status reports"));
	}
			
	public void testRepresenteeAppointmentDetailsFromNotificationCenter() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("MM/dd/yyyy");
		String tomorrow = formatter.format(new Date((new Date()).getTime() + 86400000));
		gen.appointmentCase3();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith(tomorrow).click();
		wr = wc.getCurrentPage();	
		assertTrue(wr.getText().contains("<b>Patient:</b> Random Person"));
		assertTrue(wr.getText().contains("<b>Date/Time:</b> "+tomorrow+" 10:30 AM"));
		assertTrue(wr.getText().contains("<b>HCP:</b> Kelly Doctor"));
		assertTrue(wr.getText().contains("<b>Duration:</b> 45 minutes"));		
		assertTrue(wr.getText().contains("<b>Comments:</b>"));
		assertTrue(wr.getText().contains("General Checkup after your knee surgery."));
	}
	
	public void testUnreadMessagesCount() throws Exception {

		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());

		assertTrue(wr.getText().contains("12</a>"));

		

	}
	
	public void testUnpaidBillsCount() throws Exception {
		WebConversation wc = login("311", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertTrue(wr.getText().contains("1</a>"));
		assertTrue(wr.getText().contains("new bill."));
	}
}
