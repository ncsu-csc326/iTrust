package edu.ncsu.csc.itrust.http;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.DBBuilder;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

@SuppressWarnings("unused")
public class AppointmentTypeTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.standardData();
	}
	
	public void testAddAppointmentType() throws Exception {
		// Login
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Edit Appointment Types").click();
		
		//Fill form
		assertEquals("iTrust - Maintain Appointment Types", wr.getTitle());
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
		WebForm wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("name", "Immunization");
		wf.getScriptableObject().setParameterValue("duration", "30");
		wr = wf.submit(wf.getSubmitButton("add"));
		assertLogged(TransactionType.APPOINTMENT_TYPE_ADD, 9000000001L, 0L, "");
		
		assertTrue(wr.getTables()[1].getRows()[8].getText().contains("Immunization"));
		assertTrue(wr.getTables()[1].getRows()[8].getText().contains("30"));
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
	}
	
	public void testEditAppointmentTypeDuration() throws Exception {
		// Login
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Edit Appointment Types").click();
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
		
		//Choose Type to edit
		WebForm wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("name", "Physical");
		wf.getScriptableObject().setParameterValue("duration", "45");
		wr = wf.submit(wf.getSubmitButton("update"));
		assertLogged(TransactionType.APPOINTMENT_TYPE_EDIT, 9000000001L, 0L, "");
		
		assertTrue(wr.getText().contains("Success: Physical - Duration: 45 updated"));
		assertTrue(wr.getTables()[1].getRows()[4].getText().contains("Physical"));
		assertTrue(wr.getTables()[1].getRows()[4].getText().contains("45"));
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
	}
	
	public void testEditAppointmentTypeDurationStringInput() throws Exception {
		// Login
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Edit Appointment Types").click();
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
		
		//Choose Type to edit
		WebForm wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("name", "Physical");
		wf.getScriptableObject().setParameterValue("duration", "foo");
		wr = wf.submit(wf.getSubmitButton("update"));
		assertNotLogged(TransactionType.APPOINTMENT_TYPE_EDIT, 9000000001L, 0L, "");
		
		assertTrue(wr.getText().contains("Error: Physical - Duration: must be an integer value."));
		assertTrue(wr.getURL().toString().contains("/auth/admin/editApptType.jsp"));
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
	}
	
	public void testScheduleAppointment() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Schedule Appointment").click();
		
		//Select Patient
		WebForm wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		wr = wf.submit();
		
		wf = wr.getFormWithID("mainForm");
		
		int year = Calendar.getInstance().get(Calendar.YEAR) + 1;
		String scheduledDate = "07/06/" + year;
		wf.getScriptableObject().setParameterValue("apptType", "General Checkup");
		wf.getScriptableObject().setParameterValue("schedDate", scheduledDate);
		wf.getScriptableObject().setParameterValue("time1", "09");
		wf.getScriptableObject().setParameterValue("time2", "00");
		wf.getScriptableObject().setParameterValue("time3", "AM");
		wf.getScriptableObject().setParameterValue("comment", "This is the next checkup for your blood pressure medication.");
		wr = wf.submit();
		assertLogged(TransactionType.APPOINTMENT_ADD, 9000000000L, 1L, "");
		
		assertEquals("iTrust - Schedule an Appointment", wr.getTitle());
		wr = wr.getLinkWith("View My Appointments").click();
		assertTrue(wr.getText().contains(scheduledDate+" 09:00 AM"));
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 9000000000L, 0L, "");

	}
	
	public void testPatientViewUpcomingAppointments() throws Exception {
		gen.clearAppointments();
		gen.appointmentCase1();
		
		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View My Appointments").click();
		
		// Create timestamp
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Timestamp time = new Timestamp(new Date().getTime());
		
		//Check Table
		//Row 1
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("Kelly Doctor"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("General Checkup"));
		Timestamp time1 = new Timestamp(time.getTime()+(14*24*60*60*1000));
		String dt1 = dateFormat.format(new Date(time1.getTime()));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains(dt1+" 10:30 AM"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("45 minutes"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("Read Comment"));
		
		//Row 2
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("Kelly Doctor"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("Consultation"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("06/04/"+(Calendar.getInstance().get(Calendar.YEAR)+1)+" 10:30 AM"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("30 minutes"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("Read Comment"));
		
		//Row 3
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("Kelly Doctor"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("Colonoscopy"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("10/14/"+(Calendar.getInstance().get(Calendar.YEAR)+1)+" 08:00 AM"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("90 minutes"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("No Comment"));
		
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 2L, 0L, "");
	}
	
	public void testHcpViewUpcomingAppointments() throws Exception {
		// Create DB for this test case
		gen.clearAppointments();
		gen.appointmentCase2();
		
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View My Appointments").click();
		
		// Create timestamp
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Timestamp time = new Timestamp(new Date().getTime());
		
		//Check Table
		//Row 1
		Timestamp time1 = new Timestamp(time.getTime()+(7*24*60*60*1000));
		String dt1 = dateFormat.format(new Date(time1.getTime()));
		assertTrue(wr.getTables()[0].getRows()[1].getAttribute("style").contains("font-weight: bold;"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("Random Person"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("Consultation"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains(dt1+" 09:10 AM"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("30 minutes"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("No Comment"));
		
		//Row 2
		assertTrue(wr.getTables()[0].getRows()[2].getAttribute("style").contains("font-weight: bold;"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("Baby Programmer"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("General Checkup"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains(dt1+" 09:30 AM"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("45 minutes"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains("Read Comment"));
		
		//Row 3
		Timestamp time2 = new Timestamp(time.getTime()+(10*24*60*60*1000));
		String dt2 = dateFormat.format(new Date(time2.getTime()));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("Baby Programmer"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("General Checkup"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains(dt2+" 04:00 PM"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("45 minutes"));
		assertTrue(wr.getTables()[0].getRows()[3].getText().contains("Read Comment"));
		
		//Row 4
		Timestamp time3 = new Timestamp(time.getTime()+(14*24*60*60*1000));
		String dt3 = dateFormat.format(new Date(time3.getTime()));
		assertTrue(wr.getTables()[0].getRows()[4].getAttribute("style").contains("font-weight: bold;"));
		assertTrue(wr.getTables()[0].getRows()[4].getText().contains("Random Person"));
		assertTrue(wr.getTables()[0].getRows()[4].getText().contains("Ultrasound"));
		assertTrue(wr.getTables()[0].getRows()[4].getText().contains(dt3+" 01:30 PM"));
		assertTrue(wr.getTables()[0].getRows()[4].getText().contains("30 minutes"));
		assertTrue(wr.getTables()[0].getRows()[4].getText().contains("No Comment"));
		
		//Row 5
		assertTrue(wr.getTables()[0].getRows()[5].getAttribute("style").contains("font-weight: bold;"));
		assertTrue(wr.getTables()[0].getRows()[5].getText().contains("Andy Programmer"));
		assertTrue(wr.getTables()[0].getRows()[5].getText().contains("General Checkup"));
		assertTrue(wr.getTables()[0].getRows()[5].getText().contains(dt3+" 01:45 PM"));
		assertTrue(wr.getTables()[0].getRows()[5].getText().contains("45 minutes"));
		assertTrue(wr.getTables()[0].getRows()[5].getText().contains("No Comment"));
		
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 9000000000L, 0L, "");
	}
	
	public void testAddAppointmentTypeLengthZero() throws Exception {
		// Login
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Edit Appointment Types").click();
		
		//Fill form
		assertEquals("iTrust - Maintain Appointment Types", wr.getTitle());
		assertLogged(TransactionType.APPOINTMENT_TYPE_VIEW, 9000000001L, 0L, "");
		WebForm wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("name", "Immunization");
		wf.getScriptableObject().setParameterValue("duration", "0");
		wr = wf.submit(wf.getSubmitButton("add"));
		
		assertTrue(wr.getText().contains("This form has not been validated correctly."));
	}
}
