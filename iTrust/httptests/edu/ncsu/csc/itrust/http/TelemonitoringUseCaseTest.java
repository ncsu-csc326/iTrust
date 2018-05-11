package edu.ncsu.csc.itrust.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 34
 */
public class TelemonitoringUseCaseTest extends iTrustHTTPTest {
	
	/**
	 * setUp
	 */
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * testAddPatientsToMonitor
	 * @throws Exception
	 */
	public void testAddPatientsToMonitor() throws Exception {		
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Edit Patient List
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm wf = wr.getForms()[0];
		// Allow Blood Pressure, Weight, and Pedometer
		wf.setCheckbox("bloodPressure", true);
		wf.setCheckbox("weight", true);
		wf.setCheckbox("pedometer", true);
		assertEquals("Add Andy Programmer", wf.getButtons()[0].getValue());
		wf.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		assertLogged(TransactionType.PATIENT_LIST_ADD, 9000000000L, 2L, "");
	}

	/**
	 * testRemovePatientsToMonitor
	 * @throws Exception
	 */
	public void testRemovePatientsToMonitor() throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		gen.remoteMonitoring2();
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Edit Patient List
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Remove Random Person", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Random Person Removed"));
		assertLogged(TransactionType.PATIENT_LIST_REMOVE, 9000000000L, 1L, "");
	}
	
	/**
	 * testReportPatientStatus
	 * @throws Exception
	 */
	public void testReportPatientStatus() throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		gen.remoteMonitoring2();
		// login Patient
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		//Click Report Status
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("systolicBloodPressure", "100");
		dataForm.getScriptableObject().setParameterValue("diastolicBloodPressure", "75");
		dataForm.getScriptableObject().setParameterValue("glucoseLevel", "120");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 1L, 1L, "");
	}
	
	/**
	 * testReportPatientWeightAndPedometer
	 * @throws Exception
	 */
	public void testReportPatientWeightAndPedometer () throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		gen.remoteMonitoring2();
		// login Patient
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		//Click Report Status
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		WebForm wf = wr.getForms()[0];
		wf.setParameter("weight", "174");
		wf.setParameter("pedometerReading", "8238");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 1L, 1L, "");
	}
	
	/**
	 * testViewMonitoringList
	 * @throws Exception
	 */
	public void testViewMonitoringList() throws Exception {
		//Sets up all preconditions listed in acceptance test
		gen.remoteMonitoring3();
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Monitor Patients").click();
		//Verify all data
		WebTable table = wr.getTableStartingWithPrefix("Patient Physiologic Statistics");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        
        assertEquals("Random Person (MID 1)", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(2, 1).contains("08:00:00"));
		assertEquals("160", table.getCellAsText(2, 2));
		assertEquals("110", table.getCellAsText(2, 3));
		assertEquals("60", table.getCellAsText(2, 4));
		assertEquals("Andy Programmer", table.getCellAsText(2, 5));		
		//Highlighting for abnormal data
		assertEquals("#ffff00", table.getRows()[2].getAttribute("bgcolor"));
		
		assertEquals("Random Person (MID 1)", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(3, 1).contains("07:15:00"));
		assertEquals("100", table.getCellAsText(3, 2));
		assertEquals("70", table.getCellAsText(3, 3));
		assertEquals("90", table.getCellAsText(3, 4));
		assertEquals("FirstUAP LastUAP", table.getCellAsText(3, 5));
		
		assertEquals("Random Person (MID 1)", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(4, 1).contains("05:30:00"));
		assertEquals("90", table.getCellAsText(4, 2));
		assertEquals("60", table.getCellAsText(4, 3));
		assertEquals("80", table.getCellAsText(4, 4));
		assertEquals("Random Person", table.getCellAsText(4, 5));
		
		assertEquals("Baby Programmer (MID 5)", table.getCellAsText(5, 0));
		assertEquals("No Information Provided", table.getCellAsText(5, 1));
		assertEquals("", table.getCellAsText(5, 2));
		assertEquals("", table.getCellAsText(5, 3));
		assertEquals("", table.getCellAsText(5, 4));
		assertEquals("", table.getCellAsText(5, 5));
		//Highlighting for no entry
		assertEquals("#ff6666", table.getRows()[5].getAttribute("bgcolor"));
		
		assertLogged(TransactionType.TELEMEDICINE_DATA_VIEW, 9000000000L, 0L, "");
	}
	
	/**
	 * testViewWeightAndPedometerReports
	 * @throws Exception
	 */
	public void testViewWeightAndPedometerReports () throws Exception {
		//Sets up all preconditions listed in acceptance test
		gen.remoteMonitoring5();
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Monitor Patients").click();
		//Verify all data
		WebTable table = wr.getTableStartingWithPrefix("Patient External Statistics");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        
        assertEquals("Random Person (MID 1)", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(2, 1).contains("07:17:00"));
		assertEquals("186.5", table.getCellAsText(2, 3));
		assertEquals("", table.getCellAsText(2, 4));
		assertEquals("Random Person", table.getCellAsText(2, 5));
		//Highlighting for abnormal data
		assertEquals("#ffff00", table.getRows()[2].getAttribute("bgcolor"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_VIEW, 9000000000L, 0L, "");
		
	}
	
	/**
	 * testUAPReportPatientStatus
	 * @throws Exception
	 */
	public void testUAPReportPatientStatus() throws Exception{
		gen.remoteMonitoringUAP();
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		assertEquals("iTrust - View Monitored Patients", wr.getTitle());
		wr = wr.getLinkWith("Andy Programmer").click();
		assertEquals("iTrust - Report Status", wr.getTitle());
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("systolicBloodPressure", "100");
		dataForm.getScriptableObject().setParameterValue("diastolicBloodPressure", "75");
		dataForm.getScriptableObject().setParameterValue("glucoseLevel", "120");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 8000000009L, 2L, "");
	}
	
	/**
	 * testRepresentativeReportPatientStatus
	 * @throws Exception
	 */
	public void testRepresentativeReportPatientStatus() throws Exception {
		gen.remoteMonitoring4();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		assertEquals("iTrust - Report Status", wr.getTitle());
		wr = wr.getLinkWith("Random Person").click();
		wr = wc.getCurrentPage();
		WebForm wf = wr.getForms()[0];
		wf.setParameter("glucoseLevel", "120");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 2L, 1L, "");
	}
	
	/**
	 * testRepresentativeReportWeight
	 * @throws Exception
	 */
	public void testRepresentativeReportWeight() throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		//Also add three reports
		gen.remoteMonitoring2();
		// login Patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//Click Report Status
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		wr = wr.getLinkWith("Random Person").click();
		wr = wc.getCurrentPage();
		WebForm wf = wr.getForms()[0];
		wf.setParameter("weight", "174");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 2L, 1L, "");
		
	}
	
	/**
	 * testUAPReportPatientPedometerReading
	 * @throws Exception
	 */
	public void testUAPReportPatientPedometerReading () throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		//Also add three reports
		gen.remoteMonitoring2();
		gen.remoteMonitoringUAP();
		// login Patient
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//Click Report Status
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Andy Programmer").click();
		wr = wc.getCurrentPage();
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("pedometerReading", "9163");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 8000000009L, 2L, "");
		
	}
	
	/**
	 * testUAPAddPatientToMonitorTest
	 * @throws Exception
	 */
	public void testUAPAddPatientToMonitorTest() throws Exception{
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		assertLogged(TransactionType.PATIENT_LIST_ADD, 8000000009L, 2L, "");
	}

	/**
	 * testUAPAddHCPMonitor
	 * @throws Exception
	 */
	public void testUAPAddHCPMonitor() throws Exception{
		gen.remoteMonitoring8();
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");

		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		assertLogged(TransactionType.PATIENT_LIST_ADD, 8000000009L, 2L, "");
		
		//go to reporting page
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertEquals("iTrust - Report Status", wr.getTitle());
		WebForm wf = wr.getForms()[0];
		wf.setParameter("systolicBloodPressure", "110");
		wf.setParameter("diastolicBloodPressure", "85");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 8000000009L, 2L, "");

		//logout
		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 8000000009L, 8000000009L, "");
		
		//log back in
		WebConversation wcHCP = login("9000000000", "pw");
		wr = wcHCP.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Monitor Patients").click();
		WebTable table = wr.getTableStartingWithPrefix("Patient Physiologic Statistics");
		
        assertEquals("Andy Programmer (MID 2)", table.getCellAsText(2, 0));
		assertEquals("110", table.getCellAsText(2, 2));
		assertEquals("85", table.getCellAsText(2, 3));
		assertEquals("", table.getCellAsText(2, 4));
		assertLogged(TransactionType.TELEMEDICINE_DATA_VIEW, 9000000000L, 0L, "");
	}
	
	/**
	 * testUAPAddReportDeleteCannotReport
	 * @throws Exception
	 */
	public void testUAPAddReportDeleteCannotReport() throws Exception{
		gen.remoteMonitoring8();
		//log in to iTrust
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//add Patient 2 to reporting list
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		assertLogged(TransactionType.PATIENT_LIST_ADD, 8000000009L, 2L, "");
		
		//go to reporting page
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertEquals("iTrust - Report Status", wr.getTitle());
		wr = wc.getCurrentPage();
		WebForm wf = wr.getForms()[0];
		wf.setParameter("systolicBloodPressure", "100");
		wf.setParameter("diastolicBloodPressure", "75");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 8000000009L, 2L, "");

		//remove Patient 2 from reporting list
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm confirmFormNew = wr.getForms()[0];
		assertEquals("Remove Andy Programmer", confirmFormNew.getButtons()[0].getValue());
		confirmFormNew.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Removed"));
		assertLogged(TransactionType.PATIENT_LIST_REMOVE, 8000000009L, 2L, "");
	}
	
	// Test for UC34
	/**
	 * testWeightHighlighting
	 * @throws Exception
	 */
	public void testWeightHighlighting() throws Exception{
		gen.remoteMonitoring6();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Monitor Patients").click();
		WebTable table = wr.getTableStartingWithPrefix("Patient External Statistics");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        
        assertEquals("Random Person (MID 1)", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(2, 1).contains("07:17:00"));
		assertEquals("70.0", table.getCellAsText(2, 2));
		assertEquals("192.5", table.getCellAsText(2, 3));
		assertEquals("", table.getCellAsText(2, 4));
		assertEquals("Random Person", table.getCellAsText(2, 5));
		//Highlighting for abnormal data
		assertEquals("#ffff00", table.getRows()[2].getAttribute("bgcolor"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_VIEW, 9000000000L, 0L, "");
	}
	
	// Test for UC34
	/**
	 * testDetailedExternalData
	 * @throws Exception
	 */
	public void testDetailedExternalData() throws Exception {
		gen.remoteMonitoring6();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Monitor Patients").click();
		wr = wr.getLinkWith("Random Person (MID 1)").click();
		
		int ONE_DAY = 24 * 60 * 60 * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		
		java.util.Date date = new java.util.Date();
		date.setTime(date.getTime() - 3*ONE_DAY);
		Date yesterday = new Date();
		yesterday.setTime(yesterday.getTime() - ONE_DAY);
		Date twoDaysAgo = new Date(yesterday.getTime() - ONE_DAY);
		
		WebForm wf = wr.getFormWithID("datebuttons");
		wf.getScriptableObject().setParameterValue("startDate", sdf.format(date));
		wr = wf.submit(wf.getSubmitButton("Get Records"));
		
		WebTable table = wr.getTableStartingWithPrefix("Patient Weight/Pedometer Statistics");

		// First entry:
		assertEquals(sdf2.format(new Date()) + " 07:17:00.0", table.getCellAsText(2, 0));
		assertEquals("70.0", table.getCellAsText(2, 1));
		assertEquals("192.5", table.getCellAsText(2, 2));
		assertEquals("", table.getCellAsText(2, 3));
		
		// Second entry:
		assertEquals(sdf2.format(yesterday) + " 07:48:00.0", table.getCellAsText(3, 0));
		assertEquals("70.0", table.getCellAsText(3, 1));
		assertEquals("", table.getCellAsText(3, 2));
		assertEquals("8153", table.getCellAsText(3, 3));
		
		// Third entry:
		assertEquals(sdf2.format(twoDaysAgo) + " 08:19:00.0", table.getCellAsText(4, 0));
		assertEquals("70.0", table.getCellAsText(4, 1));
		assertEquals("180.0", table.getCellAsText(4, 2));
		assertEquals("", table.getCellAsText(4, 3));
		assertLogged(TransactionType.TELEMEDICINE_DATA_VIEW, 9000000000L, 0L, "");
	}
	
	// Test for UC34
	/**
	 * testReportPatientHeight
	 * @throws Exception
	 */
	public void testReportPatientHeight() throws Exception {
		gen.remoteMonitoring7();
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		WebForm wf = wr.getForms()[0];
		wf.setParameter("height", "73.2");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		assertLogged(TransactionType.TELEMEDICINE_DATA_REPORT, 1L, 1L, "");
		
		wr = wr.getLinkWith("Report Telemedicine Status").click();
		wf = wr.getForms()[0];
		wf.setParameter("height", "73.2");
		wr = wf.submit();
		assertTrue(wr.getText().contains("Invalid entry: Patient height entries for today cannot exceed 1."));
	}
}
