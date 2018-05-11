package edu.ncsu.csc.itrust.http;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.meterware.httpunit.TableRow;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.Button;

import edu.ncsu.csc.itrust.DBBuilder;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

@SuppressWarnings("unused")
public class AppointmentTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.standardData();
	}
	
	public void testAddApptPatientDeceased() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Schedule Appointment").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.submit();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Cannot schedule appointment"));
	}
	
	public void testAddApptConflictOverride() throws Exception {
		// Login
		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Schedule Appointment").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "42");
		patientForm.submit();
		wr = wc.getCurrentPage();
		
		WebForm apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "General Checkup");
		apptForm.setParameter("schedDate", "08/24/2014");
		apptForm.setParameter("time1", "09");
		apptForm.setParameter("time2", "00");
		apptForm.setParameter("time3", "AM");
		wr = apptForm.submit();
		
		//TODO: Fix test case.
		
//		assertTrue(wr.getText().contains("Success: General Checkup for"));
//		assertLogged(TransactionType.APPOINTMENT_ADD, 9000000003L, 42L, "");
		
		/*wr = wr.getLinkWith("Select a Different Patient").click();
		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "5");
		patientForm.submit();
		wr = wc.getCurrentPage();
		
		apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "Ultrasound");
		apptForm.setParameter("schedDate", "08/24/2014");
		apptForm.setParameter("time1", "09");
		apptForm.setParameter("time2", "15");
		apptForm.setParameter("time3", "AM");
		wr = apptForm.submit();
		
		assertTrue(wr.getText().contains("Warning"));
		System.out.println(wr.getText());
		apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "Ultrasound");
		apptForm.setParameter("schedDate", "08/24/2014");
		apptForm.setParameter("time1", "09");
		apptForm.setParameter("time2", "15");
		apptForm.setParameter("time3", "AM");
		apptForm.getSubmitButtonWithID("overrideButton").click();
		wr=wc.getCurrentPage();
		
		System.out.println(wr.getText());
		
		assertTrue(wr.getText().contains("Success"));
		assertLogged(TransactionType.APPOINTMENT_ADD,9000000003L,5L,"");
		assertLogged(TransactionType.APPOINTMENT_CONFLICT_OVERRIDE,9000000003L,5L,"");
		*/
		
	}
	
	public void testEditApptConflictCancel() throws Exception {
		gen.uc22();
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View My Appointments").click();
		
		TableRow[] rows = wr.getTableStartingWithPrefix("Patient").getRows();
		

		DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		DateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 12);
		c.set(Calendar.HOUR, 9);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.MINUTE, 45);
		String date = format.format(c.getTime());
		int rowIndex = 0;
		for(TableRow row : rows){
			
			if(row.getText().contains(date) && row.getText().contains("Random Person")){ 
				break;
			}
			rowIndex++;
		}
		wr=wr.getTableStartingWithPrefix("Patient").getTableCell(rowIndex, 5).getLinkWith("Edit").click();
		
		c.add(Calendar.DATE, -5);
		c.set(Calendar.HOUR, 1);
		c.set(Calendar.AM_PM, Calendar.PM);
		c.set(Calendar.MINUTE, 45);
		
		WebForm apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "General Checkup");
		apptForm.setParameter("schedDate", format2.format(c.getTime()));
		apptForm.setParameter("time1", "01");
		apptForm.setParameter("time2", "45");
		apptForm.setParameter("time3", "PM");
		apptForm.getSubmitButton("editApptButton", "Change").click();
		
		wr = wc.getCurrentPage();
	
		assertTrue(wr.getText().contains("Warning"));
		assertNotLogged(TransactionType.APPOINTMENT_ADD, 9000000000L, 1L, "");
		
		
		apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "General Checkup");
		apptForm.setParameter("schedDate", format2.format(c.getTime()));
		apptForm.setParameter("time1", "02");
		apptForm.setParameter("time2", "00");
		apptForm.setParameter("time3", "PM");
		apptForm.getSubmitButton("editApptButton", "Change").click();
		
		wr = wc.getCurrentPage();
	
		assertFalse(wr.getText().contains("Warning"));
		assertLogged(TransactionType.APPOINTMENT_EDIT, 9000000000L, 1L, "");
		
		
	}
	public void testAddApptConflictNoOverride() throws Exception {
		gen.uc22();
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Schedule Appointment").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "100");
		patientForm.submit();
		wr = wc.getCurrentPage();
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 7);		
		
		WebForm apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "Physical");
		apptForm.setParameter("schedDate", format.format(cal.getTime()));
		apptForm.setParameter("time1", "09");
		apptForm.setParameter("time2", "45");
		apptForm.setParameter("time3", "AM");
		wr = apptForm.submit();
		
		assertTrue(wr.getText().contains("Warning"));
		assertNotLogged(TransactionType.APPOINTMENT_ADD, 9000000000L, 100L, "");
		
		
	}
	
	public void testViewApptWithConflicts() throws Exception{

		gen.uc22();
			
		// Login
		WebConversation wc = login("100", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View My Appointments").click();
		
		//assertTrue(wr.getTableStartingWithPrefix("Patient").getRows()[1].getAttribute("style").contains("bold"));
		assertTrue(wr.getTableStartingWithPrefix("Patient").getRows()[2].getAttribute("style").contains("bold"));
		
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 100L, 0L, "");
	}
	
	public void testAddApptSameEndStartTimes() throws Exception{
		
		gen.uc22();
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Schedule Appointment").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "100");
		patientForm.submit();
		wr = wc.getCurrentPage();
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		WebForm apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "Physical");
		apptForm.setParameter("schedDate", format.format(cal.getTime()));
		apptForm.setParameter("time1", "10");
		apptForm.setParameter("time2", "30");
		apptForm.setParameter("time3", "AM");
		wr = apptForm.submit();
		
		assertTrue(wr.getText().contains("Success: Physical for"));
		assertLogged(TransactionType.APPOINTMENT_ADD, 9000000000L, 100L, "");
		
	}
	
	public void testAddApptInvalidDate() throws Exception {
		gen.uc22();
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Schedule Appointment").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "100");
		patientForm.submit();
		wr = wc.getCurrentPage();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		WebForm apptForm = wr.getForms()[0];
		apptForm.setParameter("apptType", "Physical");
		apptForm.setParameter("schedDate", "38/38/2025");
		apptForm.setParameter("time1", "10");
		apptForm.setParameter("time2", "30");
		apptForm.setParameter("time3", "AM");
		wr = apptForm.submit();
		
		assertFalse(wr.getText().contains("Success: Physical for"));
		assertNotLogged(TransactionType.APPOINTMENT_ADD, 9000000000L, 100L, "");
	}
}
