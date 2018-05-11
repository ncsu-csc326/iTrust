package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.enums.FlagValue;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class ObstetricsTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		//gen.uc63(); //no longer needed because the uc64() method calls it
		gen.uc64();
		gen.hcp0(); //Kelly Doctor
		gen.patient21(); //Princess Peach
		gen.patient22();
	}

	/* Initializing Obstetrics Records UC63 */
	public void testAddPatientNoPriors() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "400");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "7/1/2014");
		form.setParameter("date", "9/23/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 400L, "");
	}
	
	/**
	 * Tests that you can add a patient with 2 priors
	 * @throws Exception
	 */
	public void testAddPatient2Priors() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID",
				"401");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// make sure previous pregnancies are listed
		assertTrue(wr.getText().contains("2010"));
		assertTrue(wr.getText().contains("2012"));
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "7/26/2014");
		form.setParameter("date", "10/14/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401L, "");
	}
	
	/**
	 * Tests that you can add a patient with a prior
	 * @throws Exception
	 */
	public void testAddPatientEnterPrior() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID",
				"402");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "8/24/2014");
		form.setParameter("date", "10/4/2014");
		
		form.submit();
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", 
				wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record "
				+ "successfully added"));
		assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 
				9000000012L, 402L, "");
	}
	
	/**
	 * Tests adding a male patient
	 * @throws Exception
	 */
	public void testAddPatientMale() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "403");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
	}
			
	/**
	 * Tests viewing a patient as non obstetrics hcp
	 * @throws Exception
	 */
	public void testViewPatientNonObstetricsHCP() throws Exception {
		testAddPatient2Priors();
		// login HCP Kelly Doctor (not obstetrics)
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// go to Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// view a previous record
		wr = wr.getLinkWith("10/14/2014").click();
		assertEquals("iTrust - View Obstetrics Record", wr.getTitle());	
	}
	
	/**
	 * Tests adding a non existent patient
	 * @throws Exception
	 */
	public void testAddNonExistentPatient() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"Somebody Else");
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("0"));
	}
	
	/**
	 * Tests adding a patient by their id
	 * @throws Exception
	 */
	public void testAddPatientByID() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"404");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "03/14/2013");
		form.setParameter("date", "03/22/2013");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial",
				wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record "
				+ "successfully added"));
		assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 
				9000000012L, 404L, "");
	}
	
	/**
	 * Tests viewing a patient obstetrics hcp
	 * @throws Exception
	 */
	public void testViewPatientObstetricsHCP() throws Exception {
		testAddPatientByID();
		// login HCP Harry Potter
		WebConversation wc = login("9000000013", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000013L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"404");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// view a previous record
		wr = wr.getLinkWith("03/22/2013").click();
		assertEquals("iTrust - View Obstetrics Record", wr.getTitle());
		assertLogged(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, 
				9000000013L, 404L, "");
	}
	
	/**
	 * Tests adding a patient with future lmp
	 * @throws Exception
	 */
	public void testAddPatientFutureLMP() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"405");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", 
				wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "05/14/2045");
		form.setParameter("date", "10/04/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger error message
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		assertTrue(wr.getText().contains("Last menstrual period cannot be"
				+ " after Date of visit"));
	}
	
	/**
	 * Tests adding a patient but chaning mind
	 * @throws Exception
	 */
	public void testAddPatientChangeMind() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"406");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		wr.getLinkWith("Back to Home").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
	}
	
	/**
	 * Tests UC64
	 * @throws Exception
	 */
	/* Adding Obstetrics Office Visits UC64 */
	public void testAddObstetricsOV() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		// search for and select Daria Griffin
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"400");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		WebForm form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "10/07/2014");
		form.setParameter("weight", "137");
		form.setParameter("bloodPressureS", "103");
		form.setParameter("bloodPressureD", "62");
		form.setParameter("fhr", "152");
		form.setParameter("fhu", "14");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit "
				+ "successfully added"));
	}
	
	/**
	 * Tests edit obstetrics
	 * @throws Exception
	 */
	public void testAddEditObstetricsOV() throws Exception {
		//-----Add-----//
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		// search for and select Brenna Lowery
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		WebForm form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/25/2014");
		form.setParameter("weight", "147.2");
		form.setParameter("bloodPressureS", "104");
		form.setParameter("bloodPressureD", "58");
		form.setParameter("fhr", "143");
		form.setParameter("fhu", "17");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit "
				+ "successfully added"));
		
		//-----Edit-----//
		// click on the desired Office Visit link
		wr = wr.getLinkWith("11/25/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		form.setParameter("fhu", "18");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit"
				+ " successfully edited"));
	}
	
	/**
	 * Tests adding obstetrics as non ob hcp
	 * @throws Exception
	 */
	public void testAddObstetricsNonObHCP() throws Exception {
		// login HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		// click should trigger page redirect to getPatientID
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		//enter somebody and submit to make sure you end up on the Edit 
		//Office Visit page
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID",
				"21");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
	}
	
	/**
	 * Tests adding when pregnancy is over
	 * @throws Exception
	 */
	public void testAddObstetricsOVPregnancyOver() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		// search for and select Mary Hadalamb
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"404");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		WebForm form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "03/01/2014");
		form.setParameter("weight", "145.6");
		form.setParameter("bloodPressureS", "101");
		form.setParameter("bloodPressureD", "60");
		form.setParameter("fhr", "158");
		form.setParameter("fhu", "24");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("The patient chosen is not a current"
				+ " obstetrics patient"));
	}
	
	/**
	 * Tests adding when the ov is not initialized
	 * @throws Exception
	 */
	public void testAddObstetricsOVNotInitialized() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		// search for and select Princess Peach
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"21");
		patientForm.getButtons()[1].click();
		//button click should redirect to the obstetrics homepage, then throw 
		//an error and come back
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("The patient chosen is not a"
				+ " current obstetrics patient"));
	}
	
	/**
	 * Tests the full control flow path for the twins flag.
	 * <ol>
	 * <li>Init patient</li>
	 * <li>Verify twins not set in initial pregnancy record</li>
	 * <li>Add a new office visit (twins box not checked)</li>
	 * <li>Verify twins not set in initial pregnancy record</li>
	 * <li>Edit office visit, set twins flag</li>
	 * <li>Verify twins set in initial pregnancy record</li>
	 * <li>Edit office visit, verify twins flag set</li>
	 * <li>Edit office visit, uncheck twins flag</li>
	 * <li>Verify twins not set in initial pregnancy record</li>
	 * </ol>
	 * @throws Exception
	 */
	public void testAllTwinsFlag() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "21");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "10/14/2014");
		form.setParameter("date", "10/29/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		// click open the new record
		wr = wr.getLinkWith("10/29/2014-Initial").click();
		form = wr.getFormWithID("status");
		//Verify not twins yet
		assertEquals(null, form.getParameterValue("twins"));
		
		// goto add page
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/05/2014");
		form.setParameter("weight", "140.2");
		form.setParameter("bloodPressureS", "104");
		form.setParameter("bloodPressureD", "58");
		form.setParameter("fhr", "143");
		form.setParameter("fhu", "17");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit successfully added"));
		
		// go back and verify still not twins
		wr = wr.getLinkWith("10/29/2014-Initial").click();
		form = wr.getFormWithID("status");
		//Verify not twins yet
		assertEquals(null, form.getParameterValue("twins"));
		//go back
		wr = wr.getLinkWith("Back to Home").click();
		
		// click on the desired Office Visit link
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		form.setParameter("twins", "on");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		wr = wc.getCurrentPage();
		//go back into the visit and make sure the field is checked
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		assertEquals("on", form.getParameterValue("twins"));
		//go back
		wr = wr.getLinkWith("Back to Home").click();
		
		// go back and verify now twins is set
		wr = wr.getLinkWith("10/29/2014-Initial").click();
		form = wr.getFormWithID("status");
		//Verify not twins yet
		assertEquals("on", form.getParameterValue("twins"));
		//go back
		wr = wr.getLinkWith("Back to Home").click();
		
		// click on the desired Office Visit link
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		form.removeParameter("twins");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		wr = wc.getCurrentPage();
		// go back and verify uncheck worked
		wr = wr.getLinkWith("10/29/2014-Initial").click();
		form = wr.getFormWithID("status");
		//Verify not twins yet
		assertEquals(null, form.getParameterValue("twins"));
	}
	
	/**
	 * Tests the full control flow path for the low-lying placenta flag.
	 * <ol>
	 * <li>Init patient</li>
	 * <li>Add a new office visit (placenta box not checked)</li>
	 * <li>Edit office visit, verify placenta flag not set</li>
	 * <li>Edit office visit, set placenta flag</li>
	 * <li>Edit office visit, verify placenta flag set</li>
	 * <li>Edit office visit, uncheck placenta flag</li>
	 * <li>Edit office visit, verify placenta flag not set</li>
	 * </ol>
	 * @throws Exception
	 */
	public void testAllPlacentaFlag() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "21");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "10/14/2014");
		form.setParameter("date", "10/29/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		// goto add page
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/05/2014");
		form.setParameter("weight", "140.2");
		form.setParameter("bloodPressureS", "104");
		form.setParameter("bloodPressureD", "58");
		form.setParameter("fhr", "143");
		form.setParameter("fhu", "17");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit successfully added"));

		//go back into the visit and make sure the field is checked
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		assertEquals(null, form.getParameterValue("placenta"));
		//go back
		wr = wr.getLinkWith("Back to Home").click();
		wr = wc.getCurrentPage();
		
		// click on the desired Office Visit link
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		form.setParameter("placenta", "on");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		wr = wc.getCurrentPage();
		
		//go back into the visit and make sure the field is checked
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		assertEquals("on", form.getParameterValue("placenta"));
		//go back
		wr = wr.getLinkWith("Back to Home").click();
		wr = wc.getCurrentPage();
		
		// click on the desired Office Visit link
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		form.removeParameter("placenta");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		wr = wc.getCurrentPage();
		
		//go back into the visit and make sure the field is checked
		wr = wr.getLinkWith("11/05/2014-Office Visit").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("officeVisit");
		assertEquals(null, form.getParameterValue("placenta"));
		//go back
		wr = wr.getLinkWith("Back to Home").click();
		wr = wc.getCurrentPage();
	}
	
	//Begin UC66
	
	public void testDocumentObOVAgeHBPWeight() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "10/05/2014");
		form.setParameter("date", "10/29/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		//Now begins test unique stuff
		
		// goto add page
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/05/2014");
		form.setParameter("weight", "125");
		form.setParameter("bloodPressureS", "100");
		form.setParameter("bloodPressureD", "91");
		form.setParameter("fhr", "160");
		form.setParameter("fhu", "14");
		wr = form.submit();
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit successfully added"));

		assertTrue(wr.getText().contains(FlagValue.HighBloodPressure.toString()));
		assertTrue(wr.getText().contains(FlagValue.AdvancedMaternalAge.toString()));
		assertTrue(wr.getText().contains(FlagValue.WeightChange.toString()));
	}
	
	public void testDocumentObOVHighFHR() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "10/05/2014");
		form.setParameter("date", "10/29/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		//Now begins test unique stuff
		
		// goto add page
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/05/2014");
		form.setParameter("weight", "140");
		form.setParameter("bloodPressureS", "130");
		form.setParameter("bloodPressureD", "80");
		form.setParameter("fhr", "171");
		form.setParameter("fhu", "14");
		wr = form.submit();
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit successfully added"));

		assertTrue(wr.getText().contains(FlagValue.AbnormalFHR.toString()));
		assertTrue(wr.getText().contains(FlagValue.AdvancedMaternalAge.toString()));
	}
	
	public void testDocumentAbnormalFHRBoundaryValue() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "10/05/2014");
		form.setParameter("date", "10/29/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		//Now begins test unique stuff
		
		// goto add page
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/05/2014");
		form.setParameter("weight", "140");
		form.setParameter("bloodPressureS", "130");
		form.setParameter("bloodPressureD", "80");
		form.setParameter("fhr", "105");
		form.setParameter("fhu", "14");
		wr = form.submit();
		
		//should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit successfully added"));

		assertTrue(!wr.getText().contains(FlagValue.AbnormalFHR.toString()));
		assertTrue(wr.getText().contains(FlagValue.AdvancedMaternalAge.toString()));
	}
	
	
	//Begin UC67
	
	public void testAddAllergy() throws Exception {
		
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("PHR Information").click();
		// search for and select Brenna Lowery
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		
		
		// fill in the office visit part of the form
		WebForm form = wr.getFormWithID("addAllergyForm");
		form.setParameter("description", "Penicillin");
		//submit the page
		wr = form.submit();
		
		//validate current page updated
		assertTrue(wr.getText().contains("Penicillin"));
				
		//go to the obstetrics homepage
		wr = wr.getLinkWith("View Obstetrics Records").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		//validate obstetrics page updated
		assertTrue(wr.getText().contains("Penicillin"));
	}
	
	public void testAddSecondAllergy() throws Exception {
		testAddAllergy();
		
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("PHR Information").click();
		// search for and select Brenna Lowery
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		
		// fill in the office visit part of the form
		WebForm form = wr.getFormWithID("addAllergyForm");
		form.setParameter("description", "Humans");
		//submit the page
		wr = form.submit();
		
		//validate current page updated
		assertTrue(wr.getText().contains("Penicillin"));
		assertTrue(wr.getText().contains("Humans"));
		
		//go to the obstetrics homepage
		wr = wr.getLinkWith("View Obstetrics Records").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		//validate obstetrics home updated
		assertTrue(wr.getText().contains("Penicillin"));
		assertTrue(wr.getText().contains("Humans"));
	}
	
	public void testAddInvalidAllergy() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Add Obstetrics Office Visit
		wr = wr.getLinkWith("PHR Information").click();
		// search for and select Brenna Lowery
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		
		// fill in the office visit part of the form
		WebForm form = wr.getFormWithID("addAllergyForm");
		form.setParameter("description", "!@#$");
		//submit the page
		wr = form.submit();
		
		//validate current page updated
		assertTrue(!wr.getText().contains("!@#$"));
		assertTrue(wr.getText().contains("This form has not been validated correctly. The following field are not properly filled in: [Allergy Description: Up to 30 characters, letters, numbers, and a space]"));
		
		//go to the obstetrics homepage
		wr = wr.getLinkWith("View Obstetrics Records").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		//validate obstetrics home updated
		assertTrue(!wr.getText().contains("!@#$"));
	}
	
	public void testBasicLaborDeliveryReport() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "401");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		WebForm form = wr.getForms()[0];
		form.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		form = wr.getFormWithID("newRecord");
		form.setParameter("lmp", "10/05/2014");
		form.setParameter("date", "10/29/2014");
		wr = form.submit((SubmitButton) form.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		//Now begins test unique stuff
		
		// click Labor and Delivery Report
		wr = wr.getLinkWith("Labor and Delivery Report").click();
		assertEquals("iTrust - Labor and Delivery Report", wr.getTitle());
		
		//Spot check pregnancy data
		assertTrue(wr.getText().contains("Miscarriage"));
		assertTrue(wr.getText().contains("Vaginal Delivery"));
		
		//Spot check EDD data
		assertTrue(wr.getText().contains("07/12/2015"));
		
		
	}
	
	public void testLaborDeliveryReportFlags() throws Exception {
		// login HCP Kathryn Evans
		WebConversation wc = login("9000000012", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		
		// click on Obstetrics Home
		wr = wr.getLinkWith("Obstetrics Home").click();
		// select specified patient
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "21");
		patientForm.getButtons()[1].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		
		// click Add Obstetrics Record
		patientForm = wr.getForms()[0];
		patientForm.getButtons()[0].click();
		// button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Initialize Obstetrics Record", wr.getTitle());
		
		// fill out the form and submit
		patientForm = wr.getFormWithID("newRecord");
		patientForm.setParameter("lmp", "10/05/2014");
		patientForm.setParameter("date", "10/29/2014");
		wr = patientForm.submit((SubmitButton) patientForm.getButtonWithID("submit"));
		// submit button click should trigger page redirect
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		// success message should be displayed
		assertEquals(ADDRESS + "auth/hcp/obstetricsHome.jsp?initial", wr.getURL().toString());
		assertTrue(wr.getText().contains("Obstetrics Record successfully added"));
		
		// now add an allergy
		wr = wr.getLinkWith("PHR Information").click();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		
		// fill in the office visit part of the form
		WebForm form = wr.getFormWithID("addAllergyForm");
		form.setParameter("description", "Penicillin");
		// submit the page
		wr = form.submit();
		
		// validate current page updated
		assertTrue(wr.getText().contains("Penicillin"));
			
		wr = wr.getLinkWith("Add Obstetrics Office Visit").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Add Obstetrics Office Visit", wr.getTitle());
		
		// fill in the form and submit it
		form = wr.getFormWithID("officeVisit");
		form.setParameter("date", "11/05/2014");
		form.setParameter("weight", "140");
		form.setParameter("bloodPressureS", "190");
		form.setParameter("bloodPressureD", "80");
		form.setParameter("fhr", "200");
		form.setParameter("fhu", "14");
		form.setParameter("twins", "on");
		wr = form.submit();
		
		// should redirect to the obstetrics homepage
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics", wr.getTitle());
		assertTrue(wr.getText().contains("Obstetrics Office Visit successfully added"));
		
		// validate that appropriate conditions were flagged
		assertTrue(wr.getText().contains(FlagValue.AbnormalFHR.toString()));
		assertTrue(wr.getText().contains(FlagValue.Twins.toString()));
		assertTrue(wr.getText().contains(FlagValue.HighBloodPressure.toString()));

		// add a pre-existing condition
		wr = wr.getLinkWith("Patient Pre-existing Conditions").click();
		assertEquals("iTrust - Obstetrics Record Pre-Existing Conditions", wr.getTitle());
		
		form = wr.getFormWithID("existingConditions");
		form.setParameter("condition", "Diabetes");
		wr = form.submit();
		
		// validate that it was added successfully
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Obstetrics Record Pre-Existing Conditions", wr.getTitle());
		assertTrue(wr.getText().contains("Diabetes"));
	
		// click Labor and Delivery Report
		wr = wr.getLinkWith("Labor and Delivery Report").click();
		assertEquals("iTrust - Labor and Delivery Report", wr.getTitle());
		
		// check pregnancy data: flags, pre-existing conditions, allergies, etc.
		assertTrue(wr.getText().contains("Penicillin"));
		assertTrue(wr.getText().contains("Diabetes"));
		assertTrue(wr.getText().contains(FlagValue.AbnormalFHR.toString()));
		assertTrue(wr.getText().contains(FlagValue.Twins.toString()));
		assertTrue(wr.getText().contains(FlagValue.HighBloodPressure.toString()));
		assertTrue(wr.getText().contains(FlagValue.MaternalAllergies.toString()));
		assertTrue(wr.getText().contains(FlagValue.PreExistingConditions.toString()));		
	}

}
