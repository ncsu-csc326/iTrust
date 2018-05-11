package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 10
 */
public class PersonalHealthRecordsUseCaseTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * testEditPatient
	 * @throws Exception
	 */
	public void testEditPatient() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("PHR Information").click();
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
	}
	
	/**
	 * testInvalidPatientDates
	 * @throws Exception
	 */
	public void testInvalidPatientDates() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Patient Information").click();
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 9000000000L, 2L, "");
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("dateOfDeathStr", "01/03/2050");
		wr = editPatientForm.submit();
		assertTrue(wr.getText().contains("future"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 9000000000L, 2L, "");
		
	}
	
	/**
	 * testInvalidPatientBirthDates
	 */
	public void testInvalidPatientBirthDates() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Patient Information").click();
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 9000000000L, 2L, "");
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("dateOfDeathStr", "");
		editPatientForm.getScriptableObject().setParameterValue("dateOfBirthStr", "01/03/2050");
		wr = editPatientForm.submit();
		assertTrue(wr.getText().contains("future"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 9000000000L, 2L, "");
	}
	
	/**
	 * testRepresent
	 * @throws Exception
	 */
	public void testRepresent() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("PHR Information").click();     
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
		
		wr = wr.getLinkWith("Baby Programmer").click();
		
		// Clicking on a representee's name takes you to their records
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getText().contains("Diabetes with ketoacidosis"));
		assertTrue(wr.getText().contains("Grandparent"));
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 5L, "");
		wr = wr.getLinkWith("Random Person").click();
		assertTrue(wr.getText().contains("nobody@gmail.com"));
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 1L, "");
	}

	/**
	 * testAllergy
	 * @throws Exception
	 */
	public void testAllergy() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("PHR Information").click();      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
		
		// Add allergy
		WebForm wf = wr.getFormWithName("AddAllergy");
		wf.getScriptableObject().setParameterValue("description", "081096");
		wr = wf.submit();
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 2L, "");
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Allergy Added"));
	}
	
	/**
	 * testAllergy2
	 * @throws Exception
	 */
	public void testAllergy2() throws Exception {
		// Login
				WebConversation wc = login("9000000000", "pw");
				WebResponse wr = wc.getCurrentPage();
				assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
				wr = wr.getLinkWith("PHR Information").click();      
				WebForm patientForm = wr.getForms()[0];
				patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
				patientForm.getButtons()[1].click();
				wr = patientForm.submit();
				assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
				
				// Add allergy
				WebForm wf = wr.getFormWithName("AddAllergy");
				wf.getScriptableObject().setParameterValue("description", "664662530");
				wr = wf.submit();
				assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 2L, "");
				
				wr = wc.getCurrentPage();
				assertTrue(wr.getText().contains("664662530 - Penicillin"));
	}
	
	/**
	 * testEditSmokingStatus
	 * @throws Exception
	 */
	public void testEditSmokingStatus() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
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
		
		//Create Health Metrics Record
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("height", "56");
		form.setParameter("weight", "111");
		form.setParameter("isSmoker", "2");
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
		
		//Change the smoking status
		form = wr.getFormWithID("healthRecordForm");
		form.setParameter("isSmoker", "1");
		form.setParameter("householdSmokingStatus", "1");
		form.getButtonWithID("addHR").click();
		
		//Verify "Health information successfully updated." message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Health information successfully updated."));
		//Verify edit health metrics log
		assertLogged(TransactionType.EDIT_BASIC_HEALTH_METRICS, 9000000000L, 2L, "");
	}

	/**
	 * testAddAdditionalDemographics1
	 * @throws Exception
	 */
	public void testAddAdditionalDemographics1() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Patient Information").click();
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 9000000000L, 2L, "");
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("religion", "Jedi");
		wr = editPatientForm.submit();
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 9000000000L, 2L, "");
		
		wr = wr.getLinkWith("PHR Information").click();
        assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		WebTable table = wr.getTableStartingWith("Patient Information");
		assertTrue(table.getCellAsText(6, 0).contains("Religion:"));
		assertTrue(table.getCellAsText(6, 1).contains("Jedi"));
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
	}

	/**
	 * testAddAdditionDemographicss2
	 * @throws Exception
	 */
	public void testAddAdditionalDemographics2() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Patient Information").click();
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 9000000000L, 2L, "");
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("spiritualPractices", "Sleeps in class");
		wr = editPatientForm.submit();
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 9000000000L, 2L, "");
		
		wr = wr.getLinkWith("PHR Information").click();
        assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		WebTable table = wr.getTableStartingWith("Patient Information");
		assertTrue(table.getCellAsText(8, 0).contains("Spiritual Practices:"));
		assertTrue(table.getCellAsText(8, 1).contains("Sleeps in class"));
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
	}

	/**
	 * testAddAdditionDemographics3
	 * @throws Exception
	 */
	public void testAddAdditionalDemographics3() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Patient Information").click();
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 9000000000L, 2L, "");
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("alternateName", "Randy");
		wr = editPatientForm.submit();
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 9000000000L, 2L, "");
		
		wr = wr.getLinkWith("PHR Information").click();
        assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		WebTable table = wr.getTableStartingWith("Patient Information");
		assertTrue(table.getCellAsText(9, 0).contains("Alternate Name:"));
		assertTrue(table.getCellAsText(9, 1).contains("Randy"));
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
	}
	
	/**
	 * testAddDupAllergy
	 * @throws Exception
	 */
	public void testAddDupAllergy() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		//Logs in as Kelly Doctor
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("PHR Information").click();
		//Clicks PHR Info
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());

		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "25");
		patientForm.getButtons()[1].click();
		//Selects Patient Trend Setter
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPHR.jsp", wr.getURL().toString());
		
		WebForm form = wr.getForms()[0];
		form = wr.getFormWithName("AddAllergy");
		form.setParameter("description", "664662530");
		form.submit();
		//Add Penicillin Allergy (will be firstFound on today's date)
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Allergy Added"));
		//No error should appear when this allergy is added.
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 25L, "");
		
		form = wr.getFormWithName("AddAllergy");
		form.setParameter("description", "664662530");
		form.submit();
		//Add Penicillin Allergy again
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Allergy 664662530 - Penicillin has already been added for Trend Setter."));
		//This is the error that should appear when this allergy is added a second time.
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 25L, "");

	}
	
	/**
	 * testAddAllergyPrevRX
	 * @throws Exception
	 */
	public void testAddAllergyPrevRX() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		//Logs in as Kelly Doctor
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		wr = wr.getLinkWith("Document Office Visit").click();
		//Clicks Document OV
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "25");
		patientForm.getButtons()[1].click();
		//Selects Patient Trend Setter
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		//Clicks Yes, Document OV
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/01/2012");
		form.setParameter("notes", "just some more notes");
		form.getButtonWithID("update").click();
		//Create new OV on date 01/01/2012
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 25L, "Office visit");
		
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "664662530");
		form.setParameter("dosage", "60");
		form.setParameter("startDate", "01/01/2012");
		form.setParameter("endDate", "01/31/2012");
		form.setParameter("instructions", "Take three times daily with food.");
		form.getButtonWithID("addprescription").click();
		//Add Penicillin RX, 60mg, 01/01/2012 - 01/31/2012, thrice daily w/ food
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Prescription information successfully updated."));
		assertLogged(TransactionType.OFFICE_VISIT_EDIT, 9000000000L, 25L, "");
	
		wr = wr.getLinkWith("PHR Information").click(); //Clicks PHR Info
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
	
		form = wr.getFormWithName("AddAllergy");
		form.setParameter("description", "664662530");
		form.submit();
		//Add Penicillin Allergy (will be firstFound on today's date)
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Allergy Added"));
		//No error should appear when this allergy is added.
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 25L, "");
	
	}
	
	/**
	 * testAddAllergyFutRX
	 * @throws Exception
	 */
	public void testAddAllergyFutRX() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		//Logs in as Kelly Doctor
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("All Patients").click();
		assertEquals("iTrust - View All Patients", wr.getTitle());
		assertLogged(TransactionType.PATIENT_LIST_VIEW, 9000000000L, 0L, "");
	
		wr = wr.getLinkWith("Anakin Skywalker").click();
		//Select Anakin Skywalker, this seemed to be easier than what the acceptance tests did.
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 100L, "");
		
		/* Since we don't worry about preconditions and it takes us straight to
		 * the correct page anyway, we can skip a lot of stuff here.
		 */
		WebForm form = wr.getForms()[0];
		form = wr.getFormWithName("AddAllergy");
		form.setParameter("description", "483012382");
		form.submit();
		//Add M-minene Allergy (will be firstFound on today's date)
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Medication 483012382 - Midichlominene is currently prescribed to Anakin Skywalker."));
		//This is the error that should appear when this allergy is added.
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 100L, "");
	
	}
	
	/**
	 * testAddAllergyExistRX
	 * @throws Exception
	 */
	public void testAddAllergyExistRX() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		//Logs in as Kelly Doctor
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		wr = wr.getLinkWith("Document Office Visit").click();
		//Clicks Document OV
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "25");
		patientForm.getButtons()[1].click();
		//Selects Patient Trend Setter
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		//Clicks Yes, Document OV
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "02/01/2012");
		form.setParameter("notes", "just some notes");
		form.getButtonWithID("update").click();
		//Create new OV on date 02/01/2012
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 25L, "Office visit");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, 7);
		
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "00882219");
		form.setParameter("dosage", "100");
		form.setParameter("startDate", "02/01/2012");
		form.setParameter("endDate", format.format(cal.getTime()));
		form.setParameter("instructions", "Take once daily");
		form.getButtonWithID("addprescription").click();
		//Add Lantus RX, 100mg, 02/01/2012 - 08/01/2012, once daily
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Prescription information successfully updated."));
		assertLogged(TransactionType.OFFICE_VISIT_EDIT, 9000000000L, 25L, "");
	
		wr = wr.getLinkWith("PHR Information").click(); //Clicks PHR Info
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 25L, "");
	
		form = wr.getFormWithName("AddAllergy");
		form.setParameter("description", "00882219");
		form.submit();
		//Add Lantus Allergy (will be firstFound on today's date)
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Medication 00882219 - Lantus is currently prescribed to Trend Setter."));
		//This is the error that should appear when this allergy is added.
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, 9000000000L, 25L, "");
	
	}

}
