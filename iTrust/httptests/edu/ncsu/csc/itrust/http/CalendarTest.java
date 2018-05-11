package edu.ncsu.csc.itrust.http;

import java.util.Calendar;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class CalendarTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.clearAllTables();
		gen.standardData();
		//gen.officeVisit5();
		gen.officeVisits();
	}

	@Override
	protected void tearDown() throws Exception {
		gen.clearAllTables();
		//gen.standardData();
	}

	public void testHCPViewAppointmentCalendar() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		// Click Calendar
		wr = wr.getLinkWith("Appointment Calendar").click();

		// check title
		assertTrue(wr.getTitle().contains("Appointment Calendar"));
		assertLogged(TransactionType.CALENDAR_VIEW, 9000000000L, 0L, "");

		// check for the right appointments
		String[][] tableData = wr.getTableWithID("calendarTable").asText();

		// On the 5th: 1:30PM - General Checkup
		for (String[] list : tableData) {
			for (String cell : list) {
				if (cell.startsWith("5")) {
					assertTrue(cell.contains("General Checkup"));
				}

			}
		}

		// On the 18th: 8:00AM - Colonoscopy
		for (String[] list : tableData) {
			for (String cell : list) {
				if (cell.startsWith("18")) {
					assertTrue(cell.contains("Colonoscopy"));
				}

			}
		}
		// On the 28th: 9:00AM - Physical
		for (String[] list : tableData) {
			for (String cell : list) {
				if (cell.startsWith("28")) {
					assertTrue(cell.contains("Physical"));
				}

			}
		}
	}

	public void testPatientViewFullCalendarOfficeVisitDetails() throws Exception {
		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();

		// Click Calendar
		wr = wr.getLinkWith("Full Calendar").click();

		// check title
		assertTrue(wr.getTitle().contains("Appointment Calendar"));
		assertLogged(TransactionType.CALENDAR_VIEW, 2L, 0L, "");

		// Patient 2 clicks the “487.00-Influenza” link on the 10th of the month.
		wr = wr.getLinkWithName("487.00-Influenza-10").click();

		// Date of Visit: <current month> 10, <current year>.
		// Physician: Kelly Doctor.
		// Note: Terrible cough.
		// Diagnoses: 487-Influenza.
		// Medical Procedures: 1270F-Injection Procedure.
		// Lab Procedure: No laboratory procedures on record.
		// Medications Prescribed: No prescriptions on record.
		// Immunizations: 90657-Influenza virus vaccine, split.

		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("Terrible cough."));
		assertTrue(wr.getText().contains("487.00"));
		assertTrue(wr.getText().contains("Influenza"));
		assertTrue(wr.getText().contains("No Medications on record"));
		assertTrue(wr.getText().contains("1270F"));
		assertTrue(wr.getText().contains("Injection procedure"));
		assertTrue(wr.getText().contains("90657"));
		assertTrue(wr.getText().contains("Influenza virus vaccine, split"));

	}

	public void testPatientViewFullCalendarPrescriptionDetails() throws Exception {
		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();

		// Click Calendar
		wr = wr.getLinkWith("Full Calendar").click();

		// check title
		assertTrue(wr.getTitle().contains("Appointment Calendar"));
		assertLogged(TransactionType.CALENDAR_VIEW, 2L, 0L, "");

		// Patient 2 clicks the “487.00-Influenza” link on the 10th of the month.
		wr = wr.getLinkWithName("664662530-Penicillin-21").click();

		// Date prescribed: <current month> 21, <current year>.
		// Physician: Gandalf Stormcrow.
		// Medication: 664662530-Penicillin.
		// Start Date: <Current month> 21, <current year>.
		// End Date: <60 days from the current date>.
		// Instructions: Administer every 6 hours after meals.

		assertTrue(wr.getText().contains("Gandalf Stormcrow"));
		assertTrue(wr.getText().contains("Penicillin (664662530)"));
		assertTrue(wr.getText().contains("250mg"));
		assertTrue(wr.getText().contains("Administer every 6 hours after meals"));
		assertLogged(TransactionType.PRESCRIPTION_REPORT_VIEW, 2L, 2L, "");

		// calculate date range
		Calendar cal = Calendar.getInstance();
		int month1 = cal.get(Calendar.MONTH) + 1;
		int day1 = 21;
		int year1 = cal.get(Calendar.YEAR);

		assertTrue(wr.getText().contains(month1 + "/" + day1 + "/" + year1 + " to "));
	}
	
	public void testHCPViewAppointmentCalendarDetails() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		// Click Calendar
		wr = wr.getLinkWith("Appointment Calendar").click();
		
		// check title
		assertTrue(wr.getTitle().contains("Appointment Calendar"));
		assertLogged(TransactionType.CALENDAR_VIEW, 9000000000L, 0L, "");
		
		WebLink[] links = wr.getLinks();
		int count = 0;
		//get the second link with General Checkup-5
		for(WebLink link : links) {
			if(link.getName().contains("General Checkup-5")) {
				count++;
				if(count == 2) {
					wr = link.click();
					break;
				}
				
			}
		}
		
		//ensure proper data is showing up
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getText().contains("General Checkup"));
		
		assertTrue(wr.getText().contains("45 minutes"));
		assertTrue(wr.getText().contains("No Comment"));
		
		//get the current month and year
		Calendar cal = Calendar.getInstance();
		int month1 = cal.get(Calendar.MONTH) + 1;
		int day1 = 5;
		int year1 = cal.get(Calendar.YEAR);
		assertTrue(wr.getText().contains(month1 + "/0" + day1 + "/" + year1 + " 09:10 AM"));
		
	}

}
