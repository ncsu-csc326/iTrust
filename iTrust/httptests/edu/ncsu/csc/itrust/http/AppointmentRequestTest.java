/**
 * 
 */
package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 *
 */
public class AppointmentRequestTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.standardData();
		gen.hcp9();
		gen.apptRequestConflicts();
	}
	
	public void testAppointmnetRequestExpire() throws Exception {
		WebConversation wc = login("9000000010", "pw"); //log in as Zoidberg
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - View My Appointment Requests", wr.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_VIEW, 9000000010L, 0L, "");
		
		assertFalse(wr.getText().contains("Request from: Philip Fry"));
		
	}
	
	public void testHCPAppointmentRequestConflictReject() throws Exception {
		WebConversation wcP = login("26", "pw"); //log in as Fry
		WebResponse wrP = wcP.getCurrentPage();
		assertEquals("iTrust - Patient Home", wrP.getTitle());
		
		wrP = wrP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 7);
		
		WebForm wfP = wrP.getForms()[0];
		wfP.getScriptableObject().setParameterValue("apptType", "General Checkup");
		wfP.getScriptableObject().setParameterValue("startDate", format.format(cal.getTime()));
		wfP.getScriptableObject().setParameterValue("time1", "01");
		wfP.getScriptableObject().setParameterValue("time2", "45");
		wfP.getScriptableObject().setParameterValue("time3", "PM");
		wfP.getScriptableObject().setParameterValue("lhcp", "9000000010");
		wrP = wfP.submit();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		assertTrue(wrP.getText().contains("conflicts with other existing appointments"));
		assertTrue(wrP.getText().contains("The following nearby time slots are available"));

		wrP.getForms()[2].submit();
		assertLogged(TransactionType.APPOINTMENT_REQUEST_SUBMITTED, 26L, 9000000010L, "");
		
		wrP = wrP.getLinkWith("Logout").click(); //log out as Fry
		
		WebConversation wcHCP = login("9000000010", "pw"); //log in as Zoidberg
		WebResponse wrHCP = wcHCP.getCurrentPage();
		assertEquals("iTrust - HCP Home", wrHCP.getTitle());
		
		wrHCP = wrHCP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - View My Appointment Requests", wrHCP.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_VIEW, 9000000010L, 0L, "");
		
		assertTrue(wrHCP.getText().contains("4:00"));
		
		wrHCP = wrHCP.getLinkWith("Approve").click();
	}
	
	
	public void testHCPAppointmentRequestNoConflictApprove() throws Exception {
		WebConversation wcP = login("26", "pw"); //log in as Fry
		WebResponse wrP = wcP.getCurrentPage();
		assertEquals("iTrust - Patient Home", wrP.getTitle());
		
		wrP = wrP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		WebForm wfP = wrP.getForms()[0];
		wfP.getScriptableObject().setParameterValue("apptType", "General Checkup");
		wfP.getScriptableObject().setParameterValue("startDate", format.format(cal.getTime()));
		wfP.getScriptableObject().setParameterValue("time1", "09");
		wfP.getScriptableObject().setParameterValue("time2", "45");
		wfP.getScriptableObject().setParameterValue("time3", "AM");
		wfP.getScriptableObject().setParameterValue("lhcp", "9000000010");
		wrP = wfP.submit();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		assertTrue(wrP.getText().contains("Your appointment request has been saved and is pending."));
		assertLogged(TransactionType.APPOINTMENT_REQUEST_SUBMITTED, 26L, 9000000010L, "");
		wrP = wrP.getLinkWith("Logout").click(); //log out as Fry
		
		WebConversation wcHCP = login("9000000010", "pw"); //log in as Zoidberg
		WebResponse wrHCP = wcHCP.getCurrentPage();
		assertEquals("iTrust - HCP Home", wrHCP.getTitle());
		
		wrHCP = wrHCP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - View My Appointment Requests", wrHCP.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_VIEW, 9000000010L, 0L, "");
		
		wrP = wrHCP.getLinkWith("Approve").click(); //not sure how to do this
		
		assertEquals("iTrust - View My Appointment Requests", wrP.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_APPROVED, 9000000010L, 26L, "");
		
	}
	
	public void testHCPAppointmentRequestNoConflictReject() throws Exception {
		WebConversation wcP = login("26", "pw"); //log in as Fry
		WebResponse wrP = wcP.getCurrentPage();
		assertEquals("iTrust - Patient Home", wrP.getTitle());
		
		wrP = wrP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		WebForm wfP = wrP.getForms()[0];
		wfP.getScriptableObject().setParameterValue("apptType", "General Checkup");
		wfP.getScriptableObject().setParameterValue("startDate", format.format(cal.getTime()));
		wfP.getScriptableObject().setParameterValue("time1", "09");
		wfP.getScriptableObject().setParameterValue("time2", "45");
		wfP.getScriptableObject().setParameterValue("time3", "AM");
		wfP.getScriptableObject().setParameterValue("lhcp", "9000000010");
		wrP = wfP.submit();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		assertTrue(wrP.getText().contains("Your appointment request has been saved and is pending."));
		assertLogged(TransactionType.APPOINTMENT_REQUEST_SUBMITTED, 26L, 9000000010L, "");
		wrP = wrP.getLinkWith("Logout").click(); //log out as Fry
		
		WebConversation wcHCP = login("9000000010", "pw"); //log in as Zoidberg
		WebResponse wrHCP = wcHCP.getCurrentPage();
		assertEquals("iTrust - HCP Home", wrHCP.getTitle());
		
		wrHCP = wrHCP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - View My Appointment Requests", wrHCP.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_VIEW, 9000000010L, 0L, "");
		
		wrP = wrHCP.getLinkWith("Reject").click(); //not sure how to do this
		
		assertEquals("iTrust - View My Appointment Requests", wrP.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_REJECTED, 9000000010L, 26L, "");
		
	}
	
	public void testPatientAppointmentRequestConflict() throws Exception {
		WebConversation wcP = login("26", "pw"); //log in as Fry
		WebResponse wrP = wcP.getCurrentPage();
		assertEquals("iTrust - Patient Home", wrP.getTitle());
		
		wrP = wrP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 7);
		
		WebForm wfP = wrP.getForms()[0];
		wfP.getScriptableObject().setParameterValue("apptType", "General Checkup");
		wfP.getScriptableObject().setParameterValue("startDate", format.format(cal.getTime()));
		wfP.getScriptableObject().setParameterValue("time1", "01");
		wfP.getScriptableObject().setParameterValue("time2", "45");
		wfP.getScriptableObject().setParameterValue("time3", "PM");
		wfP.getScriptableObject().setParameterValue("lhcp", "9000000010");
		wrP = wfP.submit();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		assertTrue(wrP.getText().contains("conflicts with other existing appointments"));
		assertTrue(wrP.getText().contains("The following nearby time slots are available"));

		wrP = wrP.getForms()[2].submit();
		
		assertLogged(TransactionType.APPOINTMENT_REQUEST_SUBMITTED, 26L, 9000000010L, "");
		assertTrue(wrP.getText().contains("Your appointment request has been saved and is pending."));
		
		wrP = wrP.getLinkWith("Logout").click(); //log out as Fry
		
		WebConversation wcHCP = login("9000000010", "pw"); //log in as Zoidberg
		WebResponse wrHCP = wcHCP.getCurrentPage();
		assertEquals("iTrust - HCP Home", wrHCP.getTitle());
		
		wrHCP = wrHCP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - View My Appointment Requests", wrHCP.getTitle());
		assertLogged(TransactionType.APPOINTMENT_REQUEST_VIEW, 9000000010L, 0L, "");
		
		assertTrue(wrHCP.getText().contains("4:00"));
		
	}
	
	public void testPatientAppointmentRequestNoConflict() throws Exception {
		WebConversation wcP = login("26", "pw"); //log in as Fry
		WebResponse wrP = wcP.getCurrentPage();
		assertEquals("iTrust - Patient Home", wrP.getTitle());
		
		wrP = wrP.getLinkWith("Appointment Requests").click();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		WebForm wfP = wrP.getForms()[0];
		wfP.getScriptableObject().setParameterValue("apptType", "General Checkup");
		wfP.getScriptableObject().setParameterValue("startDate", format.format(cal.getTime()));
		wfP.getScriptableObject().setParameterValue("time1", "09");
		wfP.getScriptableObject().setParameterValue("time2", "45");
		wfP.getScriptableObject().setParameterValue("time3", "PM");
		wfP.getScriptableObject().setParameterValue("lhcp", "9000000010");
		wrP = wfP.submit();
		assertEquals("iTrust - Appointment Requests", wrP.getTitle());

		assertLogged(TransactionType.APPOINTMENT_REQUEST_SUBMITTED, 26L, 9000000010L, "");
		assertTrue(wrP.getText().contains("Your appointment request has been saved and is pending."));
		
	}
}
