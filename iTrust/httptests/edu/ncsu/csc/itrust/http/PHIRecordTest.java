package edu.ncsu.csc.itrust.http;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class PHIRecordTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.patient2();
		gen.patient1();
		gen.patient4();
		gen.hcp0();
	}
	
	/*
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: 0
	 * Weight: 0
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * HDL: 50
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	 */
	/**
	 * testCreatePHIRecord
	 * @throws Exception
	 */
	public void testCreatePHIRecord() throws Exception {
		// login as hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getResponse(ADDRESS + "auth/hcp/home.jsp");
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 2L, "");
		
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "0");
		form.setParameter("weight", "0");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "50");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid",
		//"Height must be greater than 0", and "Weight must be greater than 0" messages
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Height must be greater than 0")); 
		assertTrue(wr.getText().contains("Weight must be greater than 0"));
	}

	/*
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: 10 inches
	 * Weight: 400 pounds
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * Household Smoking Status: Non-smoking household
	 * HDL: 50
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	 */
	/**
	 * testCreatePHIRecord1
	 * @throws Exception
	 */
	public void testCreatePHIRecord1() throws Exception {
		//Login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 2L, "");
		
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "10");
		form.setParameter("weight", "400");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "50");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		//Verify create health metrics log
		assertLogged(TransactionType.CREATE_BASIC_HEALTH_METRICS, 9000000000L, 2L, "");
	}

	/*
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: **
	 * Weight: 400 pounds
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * Household Smoking Status: Non-smoking household
	 * HDL: 50
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	 */
	/**
	 * testCreatePHIRecord6
	 * @throws Exception
	 */
	public void testCreatePHIRecord6() throws Exception {
		// login as hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getResponse(ADDRESS + "auth/hcp/home.jsp");
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
		
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 2L, "");
		
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "**");
		form.setParameter("weight", "400");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "50");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" and
		//"Height: Up to 3-digit number + up to 1 decimal place" messages
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Height: Up to 3-digit number + up to 1 decimal place"));
	}

	/*
	 * Authenticate HCP
	 * MID 9000000000
	 * Password: pw
	 * Choose Chronic Disease Risks
	 * Select and confirm patient 2.
	 */
	/**
	 * testDetectExistingHeartDiseaseRiskTest
	 * @throws Exception
	 */
	public void testDetectExistingHeartDiseaseRiskTest() throws Exception {
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click Chronic Disease Risks
		wr = wr.getLinkWith("Chronic Disease Risks").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=hcp-uap/chronicDiseaseRisks.jsp", wr.getURL().toString());
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/chronicDiseaseRisks.jsp", wr.getURL().toString());
		assertLogged(TransactionType.RISK_FACTOR_VIEW, 9000000000L, 2L, "");
		
		// make sure the correct factors for heart disease are displayed
		assertTrue(wr.getText().contains("Patient is male"));
		assertTrue(wr.getText().contains("Patient's body mass index is over 30"));
		assertTrue(wr.getText().contains("Patient has hypertension"));
		assertTrue(wr.getText().contains("Patient has bad cholesterol"));
		assertTrue(wr.getText().contains("Patient is or was a smoker"));
		assertTrue(wr.getText().contains("Patient has had related diagnoses"));
		assertTrue(wr.getText().contains("Patient has a family history of this disease"));
	}
	
	/**
	 * testNoHealthRecordException
	 * @throws Exception
	 */
	public void testNoHealthRecordException() throws Exception{
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");		
		// click Chronic Disease Risks
		wr = wr.getLinkWith("Chronic Disease Risks").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=hcp-uap/chronicDiseaseRisks.jsp", wr.getURL().toString());
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "4");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/chronicDiseaseRisks.jsp", wr.getURL().toString());
		// make sure the correct factors for heart disease are displayed
		assertTrue(wr.getText().contains("No Data"));
		assertNotLogged(TransactionType.RISK_FACTOR_VIEW, 9000000000L, 4L, "");
	}

	/*
	 * Choose Add Patient option.
	 * Last name: Kent
	 * First name: Clark
	 * Contact email: clark@ncsu.edu
	 * Street address 1: 1100 Main Campus Dr
	 * Street address 2: Rm 500
	 * City: Raleigh
	 * State: NC
	 * Zip code: 27606-4321
	 * Phone: 919-555-2000
	 * Emergency contact name: Lana Lang
	 * Emergency contact phone: 919-400-4000 
	 * Insurance company name: BlueCross
	 * Insurance company address 1: 1000 Walnut Street
	 * Insurance company address 2: Room 5454
	 * Insurance company city: Cary
	 * Insurance company state: NC 
	 * Insurance company zip code: 27513-9999
	 * Insurance company phone: 919-300-3000
	 * Insurance identification: BLUE0000000001
	 */
	/**
	 * testCreatePatient1
	 * @throws Exception
	 */
	public void testCreatePatient1() throws Exception {
		// login as uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		WebResponse addPatient = wr.getLinkWith("Add Patient").click();
		// add a patient with valid information
		WebForm form = addPatient.getForms()[0];
		form.setParameter("firstName", "Clark");
		form.setParameter("lastName", "Kent");
		form.setParameter("email", "clark@ncsu.edu");
		WebResponse editPatient = form.submit();
		WebTable table = editPatient.getTables()[0];
		String newMID = table.getCellAsText(1, 1);
		assertTrue(editPatient.getText().contains("New patient Clark Kent successfully added!"));
		assertLogged(TransactionType.PATIENT_CREATE, 8000000009L, Long.parseLong(newMID), "");
		
		editPatient = editPatient.getLinkWith("Continue to patient information.").click();
		assertEquals("iTrust - Edit Patient", editPatient.getTitle());
		form = editPatient.getForms()[1];
		form.setParameter("streetAddress1", "1100 Main Campus Dr");
		form.setParameter("streetAddress2", "Rm 500");
		form.setParameter("city", "Raleigh");
		form.setParameter("state", "NC");
		form.setParameter("zip", "27606-4321");
		form.setParameter("phone", "919-555-2000");
		form.setParameter("emergencyName", "Lana Lang");
		form.setParameter("emergencyPhone", "919-400-4000");
		form.setParameter("icName", "BlueCross");
		form.setParameter("icAddress1", "1000 Walnut Street");
		form.setParameter("icAddress2", "Room 5454");
		form.setParameter("icCity", "Cary");
		form.setParameter("icState", "NC");
		form.setParameter("icZip", "27513-9999");
		form.setParameter("icPhone", "919-300-3000");
		form.setParameter("icID", "BLUE0000000001");
		form.getButtons()[2].click();
		WebResponse add = wc.getCurrentPage();
		assertTrue(add.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 8000000009L, Long.parseLong(newMID), "");
	}

	/*
	 * Auuthenticate UAP
	 * MID 8000000009
	 * Password: uappass1
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: 10 inches
	 * Weight: 400 pounds
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * Household Smoking Status: Non-smoking household
	 * HDL: 50
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	 */
	/**
	 * testCreatePHIRecord2
	 * @throws Exception
	 */
	public void testCreatePHIRecord2() throws Exception {
		// login as uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getResponse(ADDRESS + "auth/uap/home.jsp");
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
				
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 8000000009L, 2L, "");
				
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "10");
		form.setParameter("weight", "400");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "50");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		//Verify create health metrics log
		assertLogged(TransactionType.CREATE_BASIC_HEALTH_METRICS, 8000000009L, 2L, "");
	}

	/*
	 * Auuthenticate UAP
	 * MID 8000000009
	 * Password: pw
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: 10 inches
	 * Weight: 400 pounds
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * Household Smoking Status: Non-smoking household
	 * HDL: 150
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	 */
	/**
	 * testCreatePHIRecord3
	 * @throws Exception
	 */
	public void testCreatePHIRecord3() throws Exception {
		// login as uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getResponse(ADDRESS + "auth/uap/home.jsp");
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
				
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 8000000009L, 2L, "");
				
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "10");
		form.setParameter("weight", "400");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "150");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" and 
		//"Cholesterol HDL must be an integer in [0,89]" messages
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Cholesterol HDL must be an integer in [0,89]"));	
	}

	/*
	 * Auuthenticate UAP
	 * MID 8000000009
	 * Password: uappass1
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: 10 inches
	 * Weight: <'>
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * Household Smoking Status: Non-smoking household
	 * HDL: 50
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	*/
	/**
	 * testCreatePHIRecord4
	 * @throws Exception
	 */
	public void testCreatePHIRecord4() throws Exception {
		// login hcp
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
				
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 8000000009L, 2L, "");
				
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "10");
		form.setParameter("weight", "<'>");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "50");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" and 
		//"Weight: Up to 4-digit number + up to 1 decimal place" messages
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Weight: Up to 4-digit number + up to 1 decimal place"));
	}

	/*
	 * Auuthenticate UAP
	 * MID 8000000009
	 * Password: uappass1
	 * Choose Edit Basic Health History
	 * enter 0000000002 and confirm
	 * Enter fields:
	 * Height: 10 inches
	 * Weight: 40000 pounds
	 * Blood Pressure: 999/000
	 * Smokes: Y
	 * Household Smoking Status: Non-smoking household
	 * HDL: 50
	 * LDL: 200
	 * Triglycerides: 200
	 * Confirm and approve entries
	 */
	/**
	 * testCreatePHIRecord5
	 * @throws Exception
	 */
	public void testCreatePHIRecord5() throws Exception {
		// login hcp
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//Click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		//Choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
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
		//Click the create button
		form.getButtonWithID("update").click();
				
		//Verify "Information Successfully Updated" message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 8000000009L, 2L, "");
				
		//Get health record form
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "10");
		form.setParameter("weight", "40000");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.setParameter("bloodPressureN", "999");
		form.setParameter("bloodPressureD", "000");
		form.setParameter("cholesterolHDL", "50");
		form.setParameter("cholesterolLDL", "200");
		form.setParameter("cholesterolTri", "200");
		form.getButtonWithID("addHR").click();
		
		//Verify "Information not valid" and 
		//"Weight: Up to 4-digit number + up to 1 decimal place" messages
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Weight: Up to 4-digit number + up to 1 decimal place"));
	}
}
