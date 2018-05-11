package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * OfficeVisitBasicHealthTest contains http unit tests for editing a patient's basic 
 * health metrics during an office visit.
 */
public class OVBasicHealthTest extends iTrustHTTPTest {
	
	/**
	 * This is called before each test to set up each one. It clears all the database tables
	 * and generates a new set of test data.
	 */
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is 5 
	 * months old. This test uses patient Brynn McClain (born May 1, 2013).
	 * 
	 * @throws Exception
	 */
	public void testOfficeVisit5MonthOldHealthMetrics() throws Exception{
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
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is 2 
	 * years old. This test uses patient Caldwell Hudson (born September 29, 2011). This
	 * test also adds a Penicillin prescription and a Streptococcal sore throat diagnosis.
	 * 
	 * @throws Exception
	 */
	public void testOfficeVisit2YrOldHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Caldwell Hudson (MID 102)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "102");
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
		//Enter 10/28/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/28/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Diagnosed with strep throat. Avoid contact with others for first 24 hours of antibiotics." for Notes
		form.setParameter("notes", "Diagnosed with strep throat. Avoid contact with others for first 24 hours of antibiotics.");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 30.2 lbs for Weight
		form.setParameter("weight", "30.2");
		//Enter 34.7 in for Length
		form.setParameter("height", "34.7");
		//Enter 19.4 in for Head Circumference
		form.setParameter("headCircumference", "19.4");
		//Select '3 - indoor smokers' for household smoking status
		form.setParameter("householdSmokingStatus", "3");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		
		//Enter prescription 
		form = wr.getFormWithID("prescriptionForm");
		//Select Penicillin (664662530) for the Medication
		form.setParameter("medID", "664662530");
		//Enter 50 mg for the Dosage
		form.setParameter("dosage", "50");
		//Enter 10/28/2013 for the Start Date
		form.setParameter("startDate", "10/28/2013");
		//Enter 11/03/2013 for the End Date
		form.setParameter("endDate", "11/03/2013");
		//Enter "Take three times a day" for the Instructions
		form.setParameter("instructions", "Take three times a day");
		//Click the Add Prescription button
		form.getButtonWithID("addprescription").click();
		
		//Verify "Prescription information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Prescription information successfully updated."));
		
		//Enter Diagnosis
		form = wr.getFormWithID("diagnosisForm");
		//Select '34.00 - Streptococcal sore throat' for the diagnosis
		form.setParameter("ICDCode", "34.00");
		//Click the Add Diagnosis button
		form.getButtonWithID("add_diagnosis").click();
		
		//Verify "Diagnosis information successfully updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Diagnosis information successfully updated"));
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is 5 
	 * years old. This test uses patient Fulton Gray (born October 10, 2008).
	 * 
	 * @throws Exception
	 */
	public void testOfficeVisit5YrOldHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Fulton Gray (MID 103)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "103");
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
		//Enter 10/14/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/14/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Fulton has all required immunizations to start kindergarten next year." for Notes
		form.setParameter("notes", "Fulton has all required immunizations to start kindergarten next year.");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 37.9 lbs for Weight
		form.setParameter("weight", "37.9");
		//Enter 42.9 in for Height
		form.setParameter("height", "42.9");
		//Enter 95/65 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "95");
		form.setParameter("bloodPressureD", "65");
		//Select '2 - outdoor smokers' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "2");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is 20 
	 * years old. This test uses patient Daria Griffin (born October 25, 1993).
	 * 
	 * @throws Exception
	 */
	public void testOfficeVisit20YrOldHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Daria Griffin (MID 104)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "104");
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
		//Enter 10/25/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/25/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Patient is healthy" for Notes
		form.setParameter("notes", "Patient is healthy");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 124.3 lbs for Weight
		form.setParameter("weight", "124.3");
		//Enter 62.3 in for Height
		form.setParameter("height", "62.3");
		//Enter 110/75 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "110");
		form.setParameter("bloodPressureD", "75");
		//Select '1 - non-smoking household' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "1");
		//Select '3 - Former smoker' for Patient Smoking Status
		form.setParameter("isSmoker", "3");
		//Enter 65 for HDL
		form.setParameter("cholesterolHDL", "65");
		//Enter 102 for LDL
		form.setParameter("cholesterolLDL", "102");
		//Enter 147 for Triglycerides
		form.setParameter("cholesterolTri", "147");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is 20 
	 * years old. This test uses patient Thane Ross (born January 3, 1989).
	 * 
	 * @throws Exception
	 */
	public void testOfficeVisit24YrOldHealthMetrics() throws Exception{
		//Login as HCP Shelly Vang
		WebConversation wc = login("8000000011", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000011L, 0L, "");
		
		//Click Document Office Visit Link
		wr = wr.getLinkWith("Document Office Visit").click();
		//Search for Thane Ross (MID 105)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "105");
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
		//Enter 10/25/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/25/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Thane should consider modifying diet and exercise to avoid future heart disease" for Notes
		form.setParameter("notes", "Thane should consider modifying diet and exercise to avoid future heart disease");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 210.1 lbs for Weight
		form.setParameter("weight", "210.1");
		//Enter 73.1 in for Height
		form.setParameter("height", "73.1");
		//Enter 160/100 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "160");
		form.setParameter("bloodPressureD", "100");
		//Select '1 - non-smoking household' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "1");
		//Select '4 - Never smoker' for Patient Smoking Status
		form.setParameter("isSmoker", "4");
		//Enter 37 for HDL
		form.setParameter("cholesterolHDL", "37");
		//Enter 141 for LDL
		form.setParameter("cholesterolLDL", "141");
		//Enter 162 for Triglycerides
		form.setParameter("cholesterolTri", "162");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
	}
	
	
	
	/**
	 * Create an office visit and enter health metrics for a patient that is >20 
	 * years old. This test uses patient Random Person and no patient smoking status
	 * is entered into the Health Metrics. An invalid error message is displayed.
	 * @throws Exception
	 */
	public void testNoPatientSmokingStatusSpecified() throws Exception{
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
		//Enter 12/25/2013 for the Office Visit Date
		form.setParameter("visitDate", "12/25/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Came in complaining of splinters" for Notes
		form.setParameter("notes", "Came in complaining of splinters");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 500 lbs for Weight
		form.setParameter("weight", "500");
		//Enter 48.0 in for Height
		form.setParameter("height", "48.0");
		//Enter 180/110 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "180");
		form.setParameter("bloodPressureD", "110");
		//Select '1 - non-smoking household' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "1");
		//Do not enter a Patient Smoking Status
		//Enter 40 for HDL
		form.setParameter("cholesterolHDL", "40");
		//Enter 190 for LDL
		form.setParameter("cholesterolLDL", "190");
		//Enter 500 for Triglycerides
		form.setParameter("cholesterolTri", "500");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" message
		//Verify "Smoker must be an integer in [0,10]" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Smoker must be an integer in [0,10]"));
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is >20 
	 * years old. This test uses patient Random Person and no patient high blood pressure
	 * is entered into the Health Metrics. An invalid error message is displayed. 
	 * @throws Exception
	 */
	public void testNoHighBloodPressure() throws Exception{
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
		//Enter 12/01/2013 for the Office Visit Date
		form.setParameter("visitDate", "12/01/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "It doesn't matter" for Notes
		form.setParameter("notes", "It doesn't matter");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 160 lbs for Weight
		form.setParameter("weight", "160");
		//Enter 55.5 in for Height
		form.setParameter("height", "55.5");
		//Enter /70 mmHg for Blood Pressure
		//Note that no bloodPressureN is inputed
		form.setParameter("bloodPressureN", "");
		form.setParameter("bloodPressureD", "70");
		//Select '1 - non-smoking household' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "1");
		//Select '4 - Never smoker' for Patient Smoking Status
		form.setParameter("isSmoker", "4");
		//Enter 50 for HDL
		form.setParameter("cholesterolHDL", "50");
		//Enter 100 for LDL
		form.setParameter("cholesterolLDL", "100");
		//Enter 345 for Triglycerides
		form.setParameter("cholesterolTri", "345");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" message
		//Verify "Systolic blood pressure must be an integer in [0,999]" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Systolic blood pressure must be an integer in [0,999]"));
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is >20 
	 * years old. This test uses patient Random Person and invalid characters are
	 * entered to measure HDL cholesterol into the Health Metrics. An invalid 
	 * error message is displayed. 
	 * @throws Exception
	 */
	public void testInvalidCharactersForHDL() throws Exception{
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
		//Enter 10/31/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/31/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Testing invalid input" for Notes
		form.setParameter("notes", "Testing invalid input");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 150 lbs for Weight
		form.setParameter("weight", "150");
		//Enter 55.5 in for Height
		form.setParameter("height", "55.5");
		//Enter 120/70 mmHg for Blood Pressure
		form.setParameter("bloodPressureN", "120");
		form.setParameter("bloodPressureD", "70");
		//Select '1 - non-smoking household' for Household Smoking Status
		form.setParameter("householdSmokingStatus", "1");
		//Select '4 - Never smoker' for Patient Smoking Status
		form.setParameter("isSmoker", "4");
		//Enter AA for HDL
		form.setParameter("cholesterolHDL", "AA");
		//Enter 101 for LDL
		form.setParameter("cholesterolLDL", "101");
		//Enter 222 for Triglycerides
		form.setParameter("cholesterolTri", "222");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" message
		//Verify "Cholesterol HDL must be an integer in [0,89]" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Cholesterol HDL must be an integer in [0,89]"));
	}
	
	
	/**
	 * Create an office visit for a patient that is >20 years old. This test
	 * uses patient Random Person. The date of the Office Visit is set to an
	 * invalid date (Leap Day of 2014) and the date is automatically adjusted
	 * to account for this. 
	 * @throws Exception
	 */
	public void testLeapDay2014() throws Exception{
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
		//Enter 02/29/2014 for the Office Visit Date
		form.setParameter("visitDate", "02/29/2014");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Testing invalid leap year" for Notes
		form.setParameter("notes", "Testing invalid leap year");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Verify that date changed for to March 1st, 2014.
		form = wr.getFormWithID("mainForm");
		assertEquals(form.getParameterValue("visitDate"), "03/01/2014");
	}
	
	/**
	 * Create an office visit and enter health metrics for a patient that is under 3 
	 * years old. This test uses patient Brynn McClain. No head circumference is entered
	 * and an invalid message is displayed.
	 * @throws Exception
	 */
	public void testZeroHeadCircumferenceForUnderThreeYearOld() throws Exception{
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
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
		//Enter 10/26/2013 for the Office Visit Date
		form.setParameter("visitDate", "10/26/2013");
		//Select Central Hospital for the hospital
		form.setParameter("hospitalID", "9");
		//Enter "Brynn is a very healthy baby." for Notes
		form.setParameter("notes", "Brynn is a very healthy baby.");
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		//Enter Health Metrics
		form = wr.getFormWithID("healthRecordForm");
		//Enter 90 lbs for Weight
		form.setParameter("weight", "90");
		//Enter 36.0 in for Length
		form.setParameter("height", "36.0");
		//Enter 0 in for Head Circumference
		form.setParameter("headCircumference", "0");
		//Select '1 - non-smoking household' for household smoking status
		form.setParameter("householdSmokingStatus", "1");
		//Click the Add Record button
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" message
		//Verify "Head Circumference must be greater than 0" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Head Circumference must be greater than 0"));
		

	}
}
