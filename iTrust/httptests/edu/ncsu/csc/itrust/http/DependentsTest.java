package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Test of use case 58 and 59, which focus on patients being
 * dependents of other patients.
 */
public class DependentsTest extends iTrustHTTPTest {
	
	public static final String ADDRESS = "http://localhost:8080/iTrust/auth/hcp-uap/addPatient.jsp";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * Test adding a new patient as a dependent
	 * @throws Exception
	 *
	 */
	public void testAddDependentPatient() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Navigate to the Add Patient page
		wr = wc.getResponse(ADDRESS);
        //assertTrue(wr.getText().contains("Please enter in the name of the new patient"));
      
        //Add new dependent patient Bob Marley
		WebForm addPatientForm = wr.getForms()[0];
		addPatientForm.setParameter("firstName", "Bob");
		addPatientForm.setParameter("lastName", "Marley");
		addPatientForm.setParameter("email", "bmarley@test.com");
		addPatientForm.setCheckbox("isDependent", true);
		addPatientForm.setParameter("repId", "102");
		wr = addPatientForm.submit();
		assertTrue(wr.getText().contains("successfully added"));
		assertLogged(TransactionType.HCP_CREATED_DEPENDENT_PATIENT, 9000000000L, Long.parseLong(wr.getTables()[0].getCellAsText(1, 1)), "");
	}
	
	/**
	 * Tests adding a dependent / representative relationship to existing patients
	 * @throws Exception
	 */
	public void testEditDependentRepresentative() throws Exception {
		//Log in as Kelly Doctor
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Navigate to the edit personal representatives page
		wr = wr.getLinkWith("Representatives").click();
		//Search for Fulton Gray (MID 103)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "103");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getTitle().equals("iTrust - Manage Representatives"));
		
		//Add Caldwell Hudson as a representative
		WebForm wf = wr.getFormWithName("mainForm");
		wf.getButtons()[0].click();
		wr = wc.getCurrentPage();
		wf.getScriptableObject().setParameterValue("UID_repID", "102");
		wf.getButtons()[0].click();
		
		wf.submit();
		wr = wc.getCurrentPage();

		assertEquals("Caldwell Hudson", wr.getTables()[0].getCellAsText(2, 0));
	}
	
	/**
	 * Tests that a dependent cannot login
	 * @throws Exception
	 */
	public void testDependentLogin() throws Exception {
		//Load UC58 data
		gen.uc58();
		
		//Assure that Bob Marley the dependent can't log in
		WebConversation wc = new WebConversation();
		WebResponse loginResponse = wc.getResponse("http://localhost:8080/iTrust/");
		WebForm form = loginResponse.getForms()[0];
		form.setParameter("j_username", "580");
		form.setParameter("j_password", "pw");
		loginResponse.getForms()[0].submit();
		
		//If user is not on login page, then user logged in successfully.
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
	}
	
	/**
	 * Tests that a list of a depedent's representatives is displayed to them
	 * @throws Exception
	 */
	public void testListRepresentatives() throws Exception {
		//Load UC58 data
		gen.uc58();
		
		//Log in as Kelly Doctor
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Navigate to the edit personal representatives page
		wr = wr.getLinkWith("Representatives").click();
		//Search for Bob Marley (MID 580)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "581");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getTitle().equals("iTrust - Manage Representatives"));
		
		assertEquals("Bob Marley", wr.getTables()[0].getCellAsText(2, 0));
	}
	
	/**
	 * Tests to make sure representatives can't be dependents themselves
	 * @throws Exception
	 */
	public void testRepresentativeNotDependent() throws Exception {
		//Load UC58 data
		gen.uc58();
		
		//Log in as Kelly Doctor
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Navigate to the edit personal representatives page
		wr = wr.getLinkWith("Representatives").click();
		//Search for Bob Marley (MID 580)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "580");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getTitle().equals("iTrust - Manage Representatives"));
		assertTrue(wr.getText().contains("Bob Marley is a dependent."));
		assertTrue(wr.getText().contains("Dependent users cannot represent others."));
	}
	
	public void testRequestRecordsForDependent() throws Exception {
		//Load uc59 data
		gen.uc59();
		
		//Log in as Bob Ross (MID 750)
		WebConversation wc = login("750", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 750L, 0L, "");
		
		//Navigate to records for Billy Ross
		wr = wr.getLinkWith("Request Records Release").click();
		
		//Submit request for dependent Billy Ross
		WebForm dependentForm = wr.getFormWithID("dependentForm");
		dependentForm.setParameter("selectedPatient", "0");
		dependentForm.submit();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Billy Ross"));
		
		//Submit new request for Billy Ross
		wr = wr.getFormWithID("submitRequest").submit();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
		//Fill in medical records release form
		WebForm form = wr.getFormWithID("mainForm");
		form.setParameter("releaseHospital", "1");
		form.setParameter("recFirstName", "Benedict");
		form.setParameter("recLastName", "Cucumberpatch");
		form.setParameter("recPhone", "555-666-7777");
		form.setParameter("recEmail", "a@b.com");
		form.setParameter("recHospitalName", "Rex Hospital");
		form.setParameter("recHospitalAddress1", "123 Broad St.");
		form.setParameter("recHospitalAddress2", " ");
		form.setParameter("recHospitalCity", "Cary");
		form.setParameter("recHospitalState", "NC");
		form.setParameter("recHospitalZip", "27164");
		form.setParameter("releaseJustification", "Moving");
		form.setParameter("verifyForm", "true");
		form.setParameter("digitalSig", "Bob Ross");
		form.getButtonWithID("submit").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Request successfully sent"));
		
		//Check to see that the dependent's record is added
		wr = wr.getLinkWith("Request Records Release").click();
		//Submit request for dependent Billy Ross
		dependentForm = wr.getFormWithID("dependentForm");
		dependentForm.setParameter("selectedPatient", "0");
		dependentForm.submit();
	}
	
	public void testRequestRecordsWithDependentSignature() throws Exception {
		gen.uc59();
		
		//Log in as Bob Ross (MID 750)
		WebConversation wc = login("750", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 750L, 0L, "");
		
		//Navigate to records for Billy Ross
		wr = wr.getLinkWith("Request Records Release").click();
		
		//Submit request for dependent Billy Ross
		WebForm dependentForm = wr.getFormWithID("dependentForm");
		dependentForm.setParameter("selectedPatient", "0");
		dependentForm.submit();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Billy Ross"));
		
		//Submit new request for Billy Ross
		wr = wr.getFormWithID("submitRequest").submit();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
		//Fill in medical records release form
		WebForm form = wr.getFormWithID("mainForm");
		form.setParameter("releaseHospital", "1");
		form.setParameter("recFirstName", "Benedict");
		form.setParameter("recLastName", "Cucumberpatch");
		form.setParameter("recPhone", "555-666-7777");
		form.setParameter("recEmail", "a@b.com");
		form.setParameter("recHospitalName", "Rex Hospital");
		form.setParameter("recHospitalAddress1", "123 Broad St.");
		form.setParameter("recHospitalAddress2", " ");
		form.setParameter("recHospitalCity", "Cary");
		form.setParameter("recHospitalState", "NC");
		form.setParameter("recHospitalZip", "27164");
		form.setParameter("releaseJustification", "Moving");
		form.setParameter("verifyForm", "true");
		form.setParameter("digitalSig", "Billy Ross");
		form.getButtonWithID("submit").click();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		assertTrue(wr.getText().contains("Error"));
	}
	
	public void testRequestRecordsForNotRepresentedDependent() throws Exception {
		gen.uc59();
		
		//Log in as Kelly Doctor
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Representatives").click();
		//Search for Bob Ross (MID 750)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "750");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getTitle().equals("iTrust - Manage Representatives"));
		
		//Remove the representative
		wr.getLinkWith("Remove").click();
		wr.getLinkWith("Logout").click();
		
		//Log in as Bob Ross
		wc = login("750", "pw");
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Request Records Release").click();
		
		try {
			//Submit request for dependent Billy Ross
			WebForm dependentForm = wr.getFormWithID("dependentForm");
			dependentForm.setParameter("selectedPatient", "Billy Ross");
			dependentForm.submit();
			
			fail(); // should throw exception
		} catch (Exception e) {
			assertEquals("iTrust - Records Release Request History", wr.getTitle());
		}
	}
	
	public void testViewRequestedRecordsForDependent() throws Exception {
		gen.uc59();
		
		//Log in as Bob Ross
		WebConversation wc = login("750", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 750L, 0L, "");
		
		//Request records
		wr = wr.getLinkWith("Request Records Release").click();
		//Submit request for dependent Billy Ross
		WebForm dependentForm = wr.getFormWithID("dependentForm");
		dependentForm.setParameter("selectedPatient", "0");
		dependentForm.submit();
		
		//View medical records release form
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View").click();
		assertEquals("iTrust - View My Records", wr.getTitle());
	}
	
	public void testApproveRecordsRequestForDependent() throws Exception {
		gen.uc59();
		
		//Log in as Kelly doctor
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		//Request records
		wr = wr.getLinkWith("Records Release Requests").click();
		//Approve the most recent record
		WebTable requestsTable = wr.getTableWithID("requestHistory");
		requestsTable.getTableCell(5, 3).getLinkWith("View").click();
		assertEquals("iTrust - Records Release Requests", wr.getTitle());
		wr = wc.getCurrentPage();
		wr.getFormWithID("approveButton").submit();
				
		//View medical records release form
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		assertTrue(wr.getText().contains("Approved"));
	}
}
