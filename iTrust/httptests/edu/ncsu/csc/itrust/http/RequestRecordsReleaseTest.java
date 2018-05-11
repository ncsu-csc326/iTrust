package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * RequestRecordsReleaseTest contains http unit tests for requesting and viewing
 * patient records release requests via a patient, HCP, or UAP role. 
 */
public class RequestRecordsReleaseTest extends iTrustHTTPTest {
	
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
	 * testPatientRequestNewRecordsRelease
	 * @throws Exception
	 */
	public void testPatientRequestNewRecordsRelease() throws Exception{
		//Login as patient Caldwell Hudson
		WebConversation wc = login("102", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 102L, 0L, "");
		
		wr = wr.getLinkWith("Request Records Release").click();
		assertEquals("iTrust - Records Release Request History", wr.getTitle());
		WebForm form = wr.getForms()[1];
		form.getButtonWithID("submitReq").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
		form = wr.getFormWithID("mainForm");
		form.setParameter("releaseHospital", "1");
		form.setParameter("recFirstName", "Mike");
		form.setParameter("recLastName", "Myers");
		form.setParameter("recPhone", "919-123-1234");
		form.setParameter("recEmail", "mike.myers@hospital.org");
		form.setParameter("recHospitalName", "Testing Hospital");
		form.setParameter("recHospitalAddress1", "101 Testing Hospital Drive");
		form.setParameter("recHospitalAddress2", "");
		form.setParameter("recHospitalCity", "Raleigh");
		form.setParameter("recHospitalState", "NC");
		form.setParameter("recHospitalZip", "27606");
		
		form.setParameter("releaseJustification", "Annual records request");
		
		form.setParameter("verifyForm", "true");
		form.setParameter("digitalSig", "Caldwell Hudson");
		
		form.getButtonWithID("submit").click();
		assertLogged(TransactionType.PATIENT_RELEASE_HEALTH_RECORDS, 102L, 102L, "");
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Request successfully sent"));
		assertTrue(wr.getText().contains("Pending"));
		
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("First name: Mike"));
		assertTrue(wr.getText().contains("Last name: Myers"));
		assertTrue(wr.getText().contains("Phone number: 919-123-1234"));
		assertTrue(wr.getText().contains("Email address: mike.myers@hospital.org"));
		assertTrue(wr.getText().contains("Hospital: Testing Hospital"));
		System.out.println(wr.getText());
		assertTrue(wr.getText().contains("Hospital address: 101 Testing Hospital Drive, Raleigh, NC 27606"));
		assertTrue(wr.getText().contains("Annual records request"));		
	}
	
	/**
	 * testMedicalRecordsRelease_Patient_NoSignature
	 */
	public void testMedicalRecordsRelease_Patient_NoSignature() throws Exception{
		//Login as patient Caldwell Hudson
		WebConversation wc = login("102", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 102L, 0L, "");
		
		wr = wr.getLinkWith("Request Records Release").click();
		assertEquals("iTrust - Records Release Request History", wr.getTitle());
		WebForm form = wr.getForms()[1];
		form.getButtonWithID("submitReq").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
		form = wr.getFormWithID("mainForm");
		form.setParameter("releaseHospital", "1");
		form.setParameter("recFirstName", "Mike");
		form.setParameter("recLastName", "Myers");
		form.setParameter("recPhone", "919-123-1234");
		form.setParameter("recEmail", "mike.myers@hospital.org");
		form.setParameter("recHospitalName", "Testing Hospital");
		form.setParameter("recHospitalAddress1", "101 Testing Hospital Drive");
		form.setParameter("recHospitalAddress2", " ");
		form.setParameter("recHospitalCity","Raleigh");
		form.setParameter("recHospitalState", "NC");
		form.setParameter("recHospitalZip", "27606");
		form.setParameter("releaseJustification", "Annual records request");
		
		
		form.getButtonWithID("submit").click();

		//if we are still on the same page then we know that the the submission failed and our test has bassed.
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
			
	}
	
	/**
	 * testMedicalRecordsRelease_Patient_NotAllFields
	 * @throws Exception
	 */
	public void testMedicalRecordsRelease_Patient_NotAllFields() throws Exception{
		//Login as patient Caldwell Hudson
		WebConversation wc = login("102", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 102L, 0L, "");
		
		wr = wr.getLinkWith("Request Records Release").click();
		assertEquals("iTrust - Records Release Request History", wr.getTitle());
		WebForm form = wr.getForms()[1];
		form.getButtonWithID("submitReq").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
		form = wr.getFormWithID("mainForm");
		form.setParameter("releaseHospital", "1");
		
		form.setParameter("verifyForm", "true");
		form.setParameter("digitalSig", "Caldwell Hudson");
		
		form.getButtonWithID("submit").click();		
		wr = wc.getCurrentPage();
		
		//if we are still on the same page then we know that the the submission failed and our test has bassed.
		assertEquals("iTrust - Records Release Request", wr.getTitle());
				
	}
	
	/**
	 * testPatientViewApprovedRequest
	 * @throws Exception
	 */
	public void testPatientViewApprovedRequest() throws Exception{
		//Login as patient Caldwell Hudson
		WebConversation wc = login("102", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 102L, 0L, "");
		
		wr = wr.getLinkWith("Request Records Release").click();
		assertEquals("iTrust - Records Release Request History", wr.getTitle());
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("08/17/2012"));
		
		WebTable requestsTable = wr.getTableWithID("requestHistory");
		
		requestsTable.getTableCell(4, 3).getLinkWith("View").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Approved"));
		
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("First name: Monica"));
		assertTrue(wr.getText().contains("Last name: Brown"));
		assertTrue(wr.getText().contains("Phone number: 329-818-7734"));
		assertTrue(wr.getText().contains("Email address: monica.brown@hartfordradiology.com"));
		assertTrue(wr.getText().contains("Hospital: Hartford Radiology Ltd."));
		assertTrue(wr.getText().contains("Hospital address: 8941 Hargett Way, Hartford, CT 01243"));
		
	}
	
	/**
	 * testHCPApprovesRequest
	 * @throws Exception
	 */
	public void testHCPApprovesRequest() throws Exception{
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		wr = wr.getLinkWith("3").click();
		assertEquals("iTrust - View Records Release Requests", wr.getTitle());
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("08/08/2010"));
		
		WebTable requestsTable = wr.getTableWithID("mailbox");
		assertEquals(4, requestsTable.getRowCount());
		
		requestsTable.getTableCell(3, 3).getLinkWith("View").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Pending"));
		assertTrue(wr.getText().contains("Fozzie Bear"));
		
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("First name: Brian"));
		assertTrue(wr.getText().contains("Last name: McIntyre"));
		assertTrue(wr.getText().contains("Phone number: 744-239-9117"));
		assertTrue(wr.getText().contains("Email address: mcintyre@kellerheart.com"));
		assertTrue(wr.getText().contains("Hospital: Keller Drive Heart Specialists"));
		assertTrue(wr.getText().contains("Hospital address: 622 Center Wood Avenue, Savannah, GA 42991"));
		
		WebForm form = wr.getForms()[0];
		form.getButtonWithID("Approve").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Approved"));
		assertLogged(TransactionType.HCP_RELEASE_APPROVAL, 9000000000L, 22L, "");
	}
	
	/**
	 * testHCPDeniesRequest
	 * @throws Exception
	 */
	public void testHCPDeniesRequest() throws Exception{
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		wr = wr.getLinkWith("3").click();
		assertEquals("iTrust - View Records Release Requests", wr.getTitle());
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("10/18/2013"));
		
		WebTable requestsTable = wr.getTableWithID("mailbox");
		assertEquals(4, requestsTable.getRowCount());
		
		requestsTable.getTableCell(2, 3).getLinkWith("View").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Pending"));
		assertTrue(wr.getText().contains("Caldwell Hudson"));
		
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("First name: Michael"));
		assertTrue(wr.getText().contains("Last name: Garrison"));
		assertTrue(wr.getText().contains("Phone number: 528-912-9103"));
		assertTrue(wr.getText().contains("Email address: mkgarrison@fairfaxchiro.com"));
		assertTrue(wr.getText().contains("Hospital: Fairfax Chiropractic"));
		assertTrue(wr.getText().contains("Hospital address: 72 Waywind Street, Hartford, CT 01241"));
		assertTrue(wr.getText().contains("Major back pain referral"));
		
		
		WebForm form = wr.getForms()[1];
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		form.getButtonWithID("Deny").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Denied"));
		assertLogged(TransactionType.HCP_RELEASE_DENIAL, 9000000000L, 102L, "");
	}
	
	/**
	 * testUAPDeniesRequest
	 * @throws Exception
	 */
	public void testUAPDeniesRequest() throws Exception{
		//Login as UAP FirstUAP LastUAP
		WebConversation wc = login("8000000009", "uappass1");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
				
		wr = wr.getLinkWith("3").click();
		assertEquals("iTrust - View Records Release Requests", wr.getTitle());
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("11/23/2013"));
		
		WebTable requestsTable = wr.getTableWithID("mailbox");
		assertEquals(4, requestsTable.getRowCount());
		
		requestsTable.getTableCell(1, 3).getLinkWith("View").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Pending"));
		assertTrue(wr.getText().contains("Fozzie Bear"));
		
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("First name: Connor"));
		assertTrue(wr.getText().contains("Last name: DunBar"));
		assertTrue(wr.getText().contains("Phone number: 919-733-1991"));
		assertTrue(wr.getText().contains("Email address: c.dunbar@rexhospital.org"));
		assertTrue(wr.getText().contains("Hospital: Rex Hospital"));
		assertTrue(wr.getText().contains("Hospital address: 1829 Lake Boone Trail, Raleigh, NC 27612"));
		assertTrue(wr.getText().contains("Blood test requested from specialist"));
		
		
		WebForm form = wr.getForms()[1];
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		form.getButtonWithID("Deny").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Denied"));
		assertLogged(TransactionType.UAP_RELEASE_DENIAL, 8000000009L, 22L, "");
	}
	
	/**
	 * testUAPViewsApprovedRequest
	 * @throws Exception
	 */
	public void testUAPViewsApprovedRequest() throws Exception{
		//Login as UAP FirstUAP LastUAP
		WebConversation wc = login("8000000009", "uappass1");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		wr = wr.getLinkWith("Records Release Requests").click();
		assertEquals("iTrust - Records Release Requests", wr.getTitle());
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("05/03/2008"));
		WebTable requestsTable = wr.getTableWithID("requestHistory");
		requestsTable.getTableCell(8, 3).getLinkWith("View").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Approved"));
		assertTrue(wr.getText().contains("Random Person"));
		
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("First name: Harold"));
		assertTrue(wr.getText().contains("Last name: McClain"));
		assertTrue(wr.getText().contains("Phone number: 916-991-4124"));
		assertTrue(wr.getText().contains("Email address: hmcclain@easternhealth.com"));
		assertTrue(wr.getText().contains("Hospital: East Health Services"));
		assertTrue(wr.getText().contains("9002 Asheville Avenue, Cary, NC 27511"));
		assertTrue(wr.getText().contains("Referred for services"));
	}
	
	public void testViewDependents() throws Exception{
		
		//Login as patient Caldwell Hudson
		WebConversation wc = login("2", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Request Records Release").click();
		assertEquals("iTrust - Records Release Request History", wr.getTitle());
		WebForm form = wr.getFormWithID("mainForm");
		try{
		   form.setParameter("selectedPatient", "3");
		} catch (NullPointerException e) {
			assertTrue(true);
		}
		
		//assertEquals(form.getParameterValue("selectedPatient"), "310");
	}
	
	/**
	 * testInvalidInputSQLInjection
	 */
	public void testInvalidInputSQLInjection() throws Exception{
		//Login as patient Caldwell Hudson
		WebConversation wc = login("102", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 102L, 0L, "");
		
		wr = wr.getLinkWith("Request Records Release").click();
		assertEquals("iTrust - Records Release Request History", wr.getTitle());
		WebForm form = wr.getForms()[1];
		form.getButtonWithID("submitReq").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		
		form = wr.getFormWithID("mainForm");
		form.setParameter("releaseHospital", "1");
		form.setParameter("recFirstName", "\'");
		form.setParameter("recLastName", "\'");
		form.setParameter("recPhone", "\'");
		form.setParameter("recEmail", "\'");
		form.setParameter("recHospitalName", "\'");
		form.setParameter("recHospitalAddress1", "\'");
		form.setParameter("recHospitalAddress2", "\'");
		form.setParameter("recHospitalCity","\'");
		form.setParameter("recHospitalState", "\'");
		form.setParameter("recHospitalZip", "\'");
		form.setParameter("releaseJustification", "Annual records request");
		
		
		form.getButtonWithID("submit").click();

		//if we are still on the same page then we know that the the submission failed and our test has bassed.
		assertEquals("iTrust - Records Release Request", wr.getTitle());
		form = wr.getFormWithID("mainForm");
		assertTrue(form.getParameterValue("recFirstName").equals("\'"));
		assertTrue(form.getParameterValue("recLastName").equals("\'"));
		assertTrue(form.getParameterValue("recPhone").equals("\'"));
		assertTrue(form.getParameterValue("recHospitalName").equals("\'"));
		assertTrue(form.getParameterValue("recHospitalAddress1").equals("\'"));
		assertTrue(form.getParameterValue("recHospitalState").equals("\'"));
		
		
			
	}

}
