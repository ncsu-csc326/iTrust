package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * ViewHealthRecordsHistoryTest
 */
public class ViewHealthRecordsHistoryTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * testOfficeVisit4MonthOldViewHealthMetrics
	 * @throws Exception
	 */
	public void testOfficeVisit4MonthOldViewHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Brynn McClain (MID 101)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "101");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		//Verify Document Office Visit page
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Click Yes, Document Office Visit
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		//Verify Edit Office Visit page
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		//Add a new office visit
		form = wr.getFormWithID("mainForm");
		//Enter 10/01/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/01/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Brynn can start eating rice cereal mixed with breast milk or formula once a day" for Notes
		form.setParameter("notes", "Brynn can start eating rice cereal mixed with breast milk or formula once a day.");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 16.5 lbs for Weight
		form.setParameter("weight", "16.5");
		//Enter 22.3 in for Length
		form.setParameter("height", "22.3");
		//Enter 16.1 in for Head Circumference
		form.setParameter("headCircumference", "16.1");
		//Select '1 - non-smoking household' for household smoking status
		form.setParameter("householdSmokingStatus", "1");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		
		//Click Basic Health Information link
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Basic Health Information").click();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify baby health record table displays
		WebTable hrTable = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 1 row of health records
		assertEquals(3, hrTable.getRowCount());
		//Verify table contents
		
		//Row 1 values
		//Office visit date
		assertEquals("10/01/2013", hrTable.getCellAsText(2, 0));
		//Patient weight
		assertEquals("16.5lbs", hrTable.getCellAsText(2, 2));
		//Patient length
		assertEquals("22.3in", hrTable.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("16.1in", hrTable.getCellAsText(2, 4));
		//Patient household smoking status
		assertEquals("Non-smoking household", hrTable.getCellAsText(2, 5));
		
		
	}
	
	/**
	 * testViewHealthMetricsByHCP
	 * @throws Exception
	 */
	public void testViewHealthMetricsByHCP() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Caldwell Hudson (MID 102)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "102");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());

		//Verify baby health record table displays
		WebTable hrTable = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 5 rows of health records
		assertEquals(7, hrTable.getRowCount());
		//Verify table contents
		
		//Row 1 values
		//Office visit date
		assertEquals("10/28/2013", hrTable.getCellAsText(2, 0));
		//Patient weight
		assertEquals("30.2lbs", hrTable.getCellAsText(2, 2));
		//Patient length
		assertEquals("34.7in", hrTable.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("19.4in", hrTable.getCellAsText(2, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(2, 5));
		
		//Row 2 values
		//Office visit date
		assertEquals("02/02/2012", hrTable.getCellAsText(3, 0));
		//Patient weight
		assertEquals("15.8lbs", hrTable.getCellAsText(3, 2));
		//Patient length
		assertEquals("25.7in", hrTable.getCellAsText(3, 1));
		//Patient head circumference
		assertEquals("17.1in", hrTable.getCellAsText(3, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(3, 5));
		
		//Row 3 values
		//Office visit date
		assertEquals("12/01/2011", hrTable.getCellAsText(4, 0));
		//Patient weight
		assertEquals("12.1lbs", hrTable.getCellAsText(4, 2));
		//Patient length
		assertEquals("22.5in", hrTable.getCellAsText(4, 1));
		//Patient head circumference
		assertEquals("16.3in", hrTable.getCellAsText(4, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(4, 5));
		
		//Row 4 values
		//Office visit date
		assertEquals("11/01/2011", hrTable.getCellAsText(5, 0));
		//Patient weight
		assertEquals("10.3lbs", hrTable.getCellAsText(5, 2));
		//Patient length
		assertEquals("21.1in", hrTable.getCellAsText(5, 1));
		//Patient head circumference
		assertEquals("15.3in", hrTable.getCellAsText(5, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(5, 5));
		
		//Row 5 values
		//Office visit date
		assertEquals("10/01/2011", hrTable.getCellAsText(6, 0));
		//Patient weight
		assertEquals("8.3lbs", hrTable.getCellAsText(6, 2));
		//Patient length
		assertEquals("19.6in", hrTable.getCellAsText(6, 1));
		//Patient head circumference
		assertEquals("14.5in", hrTable.getCellAsText(6, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(6, 5));		
	}
	
	/**
	 * testOfficeVisit5YrOldViewHealthMetrics
	 * @throws Exception
	 */
	public void testOfficeVisit5YrOldViewHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Fulton Gray (MID 103)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "103");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify youth health record table displays
		WebTable table = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 3 rows of health records
		assertEquals(7, table.getRowCount());
		//Verify table contents
		
		//Row 1 youth values
		//Office visit date
		assertEquals("10/14/2013", table.getCellAsText(5, 0));
		//Patient weight
		assertEquals("37.9lbs", table.getCellAsText(5, 2));
		//Patient length
		assertEquals("42.9in", table.getCellAsText(5, 1));
		//Patient head circumference
		assertEquals("95/65 mmHg", table.getCellAsText(5, 4));
		//Patient household smoking status
		assertEquals("Outdoor smokers", table.getCellAsText(5, 5));
		
		//Row 2 youth values
		//Office visit date
		assertEquals("10/15/2012", table.getCellAsText(6, 0));
		//Patient weight
		assertEquals("35.8lbs", table.getCellAsText(6, 2));
		//Patient length
		assertEquals("41.3in", table.getCellAsText(6, 1));
		//Patient head circumference
		assertEquals("95/65 mmHg", table.getCellAsText(6, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", table.getCellAsText(6, 5));
		
	
		//Row 1 baby values
		//Office visit date
		assertEquals("10/01/2011", table.getCellAsText(2, 0));
		//Patient weight
		assertEquals("36.5lbs", table.getCellAsText(2, 2));
		//Patient length
		assertEquals("39.3in", table.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("19.9in", table.getCellAsText(2, 4));
		//Patient household smoking status
		assertEquals("Indoor smokers", table.getCellAsText(2, 5));		
	}
	
	/**
	 * testOfficeVisit20YrOldViewHealthMetrics
	 * @throws Exception
	 */
	public void testOfficeVisit20YrOldViewHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Daria Griffin (MID 104)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "104");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable hrTable = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 3 rows of health records
		assertEquals(5, hrTable.getRowCount());
		//Verify table contents
				
		//Row 1 values
		//Office visit date
		assertEquals("10/25/2013", hrTable.getCellAsText(2, 0));
		//Patient weight
		assertEquals("124.3lbs", hrTable.getCellAsText(2, 2));
		//Patient length
		assertEquals("62.3in", hrTable.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("100/75 mmHg", hrTable.getCellAsText(2, 5));
		//Patient household smoking status
		assertEquals("Non-smoking household", hrTable.getCellAsText(2, 7));
		//Patient smoking status
		assertEquals("N", hrTable.getCellAsText(2, 6));
		//Patient HDL levels
		assertEquals("65 mg/dL", hrTable.getCellAsText(2, 8));
		//Patient LDL levels
		assertEquals("102 mg/dL", hrTable.getCellAsText(2, 9));
		//Patient triglycerides
		assertEquals("147 mg/dL", hrTable.getCellAsText(2, 10));
		//Patient BMI
		assertEquals("22.51", hrTable.getCellAsText(2, 3));
		assertEquals("Normal", hrTable.getCellAsText(2, 4));
				
		//Row 2 values
		//Office visit date
		assertEquals("10/20/2012", hrTable.getCellAsText(3, 0));
		//Patient weight
		assertEquals("120.7lbs", hrTable.getCellAsText(3, 2));
		//Patient length
		assertEquals("62.3in", hrTable.getCellAsText(3, 1));
		//Patient head circumference
		assertEquals("107/72 mmHg", hrTable.getCellAsText(3, 5));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(3, 7));
		//Patient smoking status
		assertEquals("Y", hrTable.getCellAsText(3, 6));
		//Patient HDL levels
		assertEquals("63 mg/dL", hrTable.getCellAsText(3, 8));
		//Patient LDL levels
		assertEquals("103 mg/dL", hrTable.getCellAsText(3, 9));
		//Patient triglycerides
		assertEquals("145 mg/dL", hrTable.getCellAsText(3, 10));
		//Patient BMI
		assertEquals("21.86", hrTable.getCellAsText(3, 3));
		assertEquals("N/A", hrTable.getCellAsText(3, 4));
				
		//Row 3 values
		//Office visit date
		assertEquals("10/10/2011", hrTable.getCellAsText(4, 0));
		//Patient weight
		assertEquals("121.3lbs", hrTable.getCellAsText(4, 2));
		//Patient length
		assertEquals("62.3in", hrTable.getCellAsText(4, 1));
		//Patient head circumference
		assertEquals("105/73 mmHg", hrTable.getCellAsText(4, 5));
		//Patient household smoking status
		assertEquals("Indoor smokers", hrTable.getCellAsText(4, 7));
		//Patient smoking status
		assertEquals("Y", hrTable.getCellAsText(4, 6));
		//Patient HDL levels
		assertEquals("64 mg/dL", hrTable.getCellAsText(4, 8));
		//Patient LDL levels
		assertEquals("102 mg/dL", hrTable.getCellAsText(4, 9));
		//Patient triglycerides
		assertEquals("143 mg/dL", hrTable.getCellAsText(4, 10));
		//Patient BMI
		assertEquals("21.97", hrTable.getCellAsText(4, 3));
		assertEquals("N/A", hrTable.getCellAsText(4, 4));
	}
	
	/**
	 * testOfficeVisit24YrOldViewHealthMetrics
	 * @throws Exception
	 */
	public void testOfficeVisit24YrOldViewHealthMetrics() throws Exception{
		//Login as Patient Thane Ross (MID 105)
		WebConversation wc = login("105", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 105L, 0L, "");
		
		//Click View My Records link
		wr = wr.getLinkWith("View My Records").click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/patient/viewMyRecords.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable hrTable = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 1 row of health records
		assertEquals(3, hrTable.getRowCount());
		//Verify table contents
				
		//Row 1 values
		//Office visit date
		assertEquals("10/25/2013", hrTable.getCellAsText(2, 0));
		//Patient weight
		assertEquals("210.1lbs", hrTable.getCellAsText(2, 2));
		//Patient length
		assertEquals("73.1in", hrTable.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("160/100 mmHg", hrTable.getCellAsText(2, 5));
		//Patient household smoking status
		assertEquals("Non-smoking household", hrTable.getCellAsText(2, 7));
		//Patient smoking status
		assertEquals("N", hrTable.getCellAsText(2, 6));
		//Patient HDL levels
		assertEquals("37 mg/dL", hrTable.getCellAsText(2, 8));
		//Patient LDL levels
		assertEquals("141 mg/dL", hrTable.getCellAsText(2, 9));
		//Patient triglycerides
		assertEquals("162 mg/dL", hrTable.getCellAsText(2, 10));
		//Patient BMI
		assertEquals("27.64", hrTable.getCellAsText(2, 3));
		assertEquals("Overweight", hrTable.getCellAsText(2, 4));
	}
	
	/**
	 * testHCPLoggingAction
	 * @throws Exception
	 */
	public void testHCPLoggingAction() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for invalid patient MID 0000000222
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "102");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());
		
		wc = login("102", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		assertTrue(wr.getText().contains("Shelly Vang</a> viewed your health records history"));
		
		
	}
	
	/**
	 * testDeletedHealthRecord
	 * @throws Exception
	 */
	public void testDeletedHealthRecord() throws Exception{
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Random Person (MID 1)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		//Verify Document Office Visit page
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Click Yes, Document Office Visit
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		//Verify Edit Office Visit page
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		//Add a new office visit
		form = wr.getFormWithID("mainForm");
		//Enter 10/01/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/01/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Random has consumed unknown seed 32912" for Notes
		form.setParameter("notes", "Random has consumed unknown seed 32912");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 165.8 lbs for Weight
		form.setParameter("weight", "165.8");
		//Enter 74 in for Height
		form.setParameter("height", "74");
		//Enter 110/75 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "110");
		form.setParameter("bloodPressureD", "75");
		//Select '1 - non-smoking household' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "1");
		//Select '3 - Former smoker' for Patient Smoking Status
		form.setParameter("isSmoker", "3");
		//Enter 68 for HDL
		form.setParameter("cholesterolHDL", "68");
		//Enter 107 for LDL
		form.setParameter("cholesterolLDL", "107");
		//Enter 162 for Triglycerides
		form.setParameter("cholesterolTri", "162");
		
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("Health information successfully updated."));
		
		//Remove health record
		wr = wr.getLinkWith("Remove").click();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		
		//Click Basic Health Information link
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Basic Health Information").click();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable hrTable = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 1 row of health records
		assertEquals(4, hrTable.getRowCount());
		//Verify table contents
				
		//Row 1 values
		//Office visit date
		assertEquals("06/11/2007", hrTable.getCellAsText(2, 0));
		//Patient weight
		assertEquals("185.0lbs", hrTable.getCellAsText(2, 2));
		//Patient length
		assertEquals("72.0in", hrTable.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("107/104 mmHg", hrTable.getCellAsText(2, 5));
		//Patient household smoking status
		assertEquals("Non-smoking household", hrTable.getCellAsText(2, 7));
		//Patient smoking status
		assertEquals("N", hrTable.getCellAsText(2, 6));
		//Patient HDL levels
		assertEquals("41 mg/dL", hrTable.getCellAsText(2, 8));
		//Patient LDL levels
		assertEquals("104 mg/dL", hrTable.getCellAsText(2, 9));
		//Patient triglycerides
		assertEquals("101 mg/dL", hrTable.getCellAsText(2, 10));
		//By personnel Kelly Doctor
		assertEquals("Kelly Doctor", hrTable.getCellAsText(2, 13));
		//Patient BMI
		assertEquals("25.09", hrTable.getCellAsText(2, 3));
		assertEquals("Overweight", hrTable.getCellAsText(2, 4));
	}
	
	/**
	 * testNoHealthRecordsExistByHCP
	 * @throws Exception
	 */
	public void testNoHealthRecordsExistByHCP() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Brynn McClain (MID 101)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "101");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify that message displays to user
		assertTrue(wr.getText().contains("No health records available"));
		
	}
	
	/**
	 * testNoHealthRecordsExistByPatient
	 * @throws Exception
	 */
	public void testNoHealthRecordsExistByPatient() throws Exception{
		//Login as Patient Brynn McClain (MID 101)
		WebConversation wc = login("101", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 101L, 0L, "");
		
		//Click View My Records link
		wr = wr.getLinkWith("View My Records").click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/patient/viewMyRecords.jsp", wr.getURL().toString());
		
		//Verify that message displays to user
		assertTrue(wr.getText().contains("No health records available"));
	}
	
	/**
	 * testOfficeVisitDateIsBirthDate
	 * @throws Exception
	 */
	public void testOfficeVisitDateIsBirthDate() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Brynn McClain (MID 101)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "101");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		//Verify Document Office Visit page
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Click Yes, Document Office Visit
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		//Verify Edit Office Visit page
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		//Add a new office visit
		form = wr.getFormWithID("mainForm");
		//Enter 05/01/2016 for the Office Visit Date
		form.setParameter("visitDate", "05/01/2016");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Brynn is growing into a beautiful sunflower" for Notes
		form.setParameter("notes", "Brynn is growing into a beautiful sunflower.");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 41.2 lbs for Weight
		form.setParameter("weight", "41.2");
		//Enter 42.8 in for Length
		form.setParameter("height", "42.8");
		//Enter 123/64 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "123");
		form.setParameter("bloodPressureD", "64");
		//Select '1 - non-smoking household' for household smoking status
		form.setParameter("householdSmokingStatus", "1");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		
		//Click Basic Health Information link
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Basic Health Information").click();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify youth health record table displays
		WebTable youthTable = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 1 row of health records
		assertEquals(3, youthTable.getRowCount());
		//Verify table contents
				
		//Row 1 youth values
		//Office visit date
		assertEquals("05/01/2016", youthTable.getCellAsText(2, 0));
		//Patient weight
		assertEquals("41.2lbs", youthTable.getCellAsText(2, 2));
		//Patient length
		assertEquals("42.8in", youthTable.getCellAsText(2, 1));
		//Patient head circumference
		assertEquals("123/64 mmHg", youthTable.getCellAsText(2, 4));
		//Patient household smoking status
		assertEquals("Non-smoking household", youthTable.getCellAsText(2, 5));
		
	}
	
	
	
	
}