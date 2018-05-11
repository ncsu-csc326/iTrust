package edu.ncsu.csc.itrust.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class ViewAccessLogTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.patient2();
		gen.patient1();
		gen.patient4();
		gen.hcp0();
		gen.hcp3();
		gen.er4();
	}

	/*
	 * HCP 9000000000 has viewed PHR of patient 2.
	 * Authenticate Patient
	 * MID: 2
	 * Password: pw
	 * Choose option View Access Log
	 */
	public void testViewAccessLog1() throws Exception {
		// clear operational profile
		gen.transactionLog();
		// hcp views phr of patient 2
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click Edit PHR
		wr = wr.getLinkWith("PHR Information").click();
		assertEquals(ADDRESS + "auth/getPatientID.jsp?forward=hcp-uap/editPHR.jsp", wr.getURL().toString());
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editPHR.jsp", wr.getURL().toString());
		assertLogged(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, 9000000000L, 2L, "");
		
		// login patient 2
		wc = login("2", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		// click on View Access Log
		wr = wr.getLinkWith("Access Log").click();
		// check the table that displays the access log
		
		WebTable table = wr.getTableStartingWithPrefix("Date");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        
			assertTrue(table.getCellAsText(1, 0).contains(dateFormat.format(date)));
		assertEquals("Kelly Doctor", table.getCellAsText(1, 1));
		assertTrue(table.getCellAsText(1, 3).contains("View personal health information"));		
		assertLogged(TransactionType.ACCESS_LOG_VIEW, 2L, 0L, "");
	}

	/*
	 * HCP 9000000000 has viewed PHR of patient 2 on 11/11/2007.
	 * Authenticate Patient
	 * MID: 2
	 * Password: pw
	 * Choose option View Access Log
	 * Choose date range 6/22/2000 through 6/23/2000
	 */
	public void testViewAccessLog2() throws Exception {
		// clear operational profile
		gen.transactionLog();
		// login patient 2
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		// click on View Access Log
		wr = wr.getLinkWith("Access Log").click();
		// select the date range and submit
		WebForm form = wr.getForms()[0];
		form.setParameter("startDate", "06/22/2000");
		form.setParameter("endDate", "06/23/2000");
		form.getSubmitButtons()[0].click();
		WebResponse add = wc.getCurrentPage();
		assertFalse(add.getText().contains("Exception"));
		assertLogged(TransactionType.ACCESS_LOG_VIEW, 2L, 0L, "");
	}

	/*
	 * Authenticate Patient
	 * MID: 1
	 * Password: pw
	 * Choose option View Access Log
	 */
	public void testViewAccessLog3() throws Exception {
		// clear operational profile
		gen.transactionLog();
		// make sure that no exceptions are thrown even though patient 1 has nothing in the view
		// access log
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("Access Log").click();
		assertFalse(wr.getText().contains("Exception"));
		assertLogged(TransactionType.ACCESS_LOG_VIEW, 1L, 0L, "");
	}
	
	public void testViewAccessLogByDate() throws Exception {
		gen.transactionLog2();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Access Log").click();
		assertFalse(wr.getText().contains("Exception"));

		WebForm form = wr.getForms()[0];
		form.setParameter("startDate", "03/01/2008");
		form.setParameter("endDate", "12/01/2008");
		form.getSubmitButtons()[0].click();
		
		wr = wr.getLinkWith("Date").click();
		/*
		WebTable table = wr.getTableStartingWithPrefix("Date");
		assertTrue(table.getCellAsText(1, 3).contains("View emergency report"));
		assertTrue(table.getCellAsText(2, 3).contains("Edit Office Visits"));
		assertTrue(table.getCellAsText(3, 3).contains("View prescription report"));
		assertTrue(table.getCellAsText(4, 3).contains("View risk factors"));
		assertLogged(TransactionType.ACCESS_LOG_VIEW, 2L, 0L, "");
		*/
		
	}
	
	public void testViewAccessLogByRole() throws Exception {
		gen.transactionLog3();
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("Access Log").click();
		assertFalse(wr.getText().contains("Exception"));

		WebForm form = wr.getForms()[0];
		form.setParameter("startDate", "02/01/2008");
		form.setParameter("endDate", "09/22/2009");
		form.getSubmitButtons()[0].click();
		form.getScriptableObject().setParameterValue( "sortBy", "role" ); 
		wr = form.submit();
		
		WebTable table = wr.getTableStartingWithPrefix("Date");
		assertTrue(table.getCellAsText(1, 2).contains("Emergency Responder"));
		assertTrue(table.getCellAsText(2, 2).contains("LHCP"));
		assertTrue(table.getCellAsText(3, 2).contains("LHCP"));
		assertTrue(table.getCellAsText(4, 2).contains("LHCP"));
		assertTrue(table.getCellAsText(5, 2).contains("Personal Health Representative"));
		assertTrue(table.getCellAsText(6, 2).contains("UAP"));
		
		assertLogged(TransactionType.ACCESS_LOG_VIEW, 1L, 0L, "");
		
	}
	
	/**
	 * Verifies that a representative is able to view the access log of a representee.
	 * @throws Exception
	 */
	public void testViewAccessLogRepresentativeView() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		
		WebConversation wc = login("24", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		wr = wr.getLinkWith("Access Log").click();
		assertEquals("iTrust - View My Access Log", wr.getTitle());
		
		//select Dare Devil from the dropdown menu
		WebForm representeeSelectForm = wr.getFormWithID("logMIDSelectionForm");
		representeeSelectForm.setParameter("logMID", "23");
		representeeSelectForm.submit();
		
		wr = wc.getCurrentPage();
		
		assertFalse(wr.getText().contains("Beaker Beaker"));
		assertTrue(wr.getText().contains("<td >2007-06-23 06:55:59.0</td>"));

		wr = wr.getLinkWith("Role").click();
		assertEquals("iTrust - View My Access Log", wr.getTitle());
		assertFalse(wr.getText().contains("Beaker Beaker"));
		assertTrue(wr.getText().contains("<td >2007-06-23 06:55:59.0</td>"));

		wr = wr.getLinkWith("Access Log").click();
		assertEquals("iTrust - View My Access Log", wr.getTitle());

		assertFalse(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("<td >2007-06-25 06:54:59.0</td>"));
	}
	
	/**
	 * Verifies that a non-representative is not shown a representee to select.
	 * @throws Exception
	 */
	public void testViewAccessLogNonRepresentativeView1() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		
		WebConversation wc = login("23", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		wr = wr.getLinkWith("Access Log").click();
		assertEquals("iTrust - View My Access Log", wr.getTitle());
		assertFalse(wr.getText().contains("Devils Advocate"));
	}
	
	/**
	 * Verifies that DLHCP information is correctly hidden in the Access Log for a non-representative.
	 * @throws Exception
	 */
	public void testViewAccessLogNonRepresentativeView2() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		
		WebConversation wc = login("9000000007", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());

		wr = wr.getLinkWith("Patient Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "5");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Patient", wr.getTitle());
		
		wr = wr.getLinkWith("Basic Health Information").click();
		assertEquals("iTrust - Edit Basic Health Record", wr.getTitle());
		
		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());

		wc = login("9000000000", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());

		wr = wr.getLinkWith("Patient Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "5");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Patient", wr.getTitle());

		wr = wr.getLinkWith("PHR Information").click();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());

		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());
		
		wc = login("9000000000", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());

		wr = wr.getLinkWith("Patient Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "5");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Patient", wr.getTitle());
		
		wr = wr.getLinkWith("Basic Health Information").click();
		assertEquals("iTrust - Edit Basic Health Record", wr.getTitle());

		wr = wr.getLinkWith("Patient Information").click();
		assertEquals("iTrust - Edit Patient", wr.getTitle());

		wr = wr.getLinkWith("PHR Information").click();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());

		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());
		
		wc = login("5", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		wr = wr.getLinkWith("Access Log").click();
		assertEquals("iTrust - View My Access Log", wr.getTitle());

		assertFalse(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("Beaker Beaker"));

	}
	
	/**
	 * Verifies that the access log correctly handle bad date inputs
	 * @throws Exception
	 */
	public void testViewAccessLogBadDateHandling() throws Exception
	{
		gen.clearAllTables();
		gen.standardData();
		
		WebConversation wc = login("23", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		wr = wr.getLinkWith("Access Log").click();
		assertEquals("iTrust - View My Access Log", wr.getTitle());

		WebForm dateForm = wr.getForms()[0];
		dateForm.setParameter("startDate", "6/22/2007");
		dateForm.setParameter("endDate", "6/21/2007");
		dateForm.submit();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View My Access Log", wr.getTitle());
		assertTrue(wr.getText().contains("<h2>Information not valid</h2><div class=\"errorList\">Start date must be before end date!<br /></div>"));

		dateForm = wr.getForms()[0];
		dateForm.setParameter("startDate", "June 22nd, 2007");
		dateForm.setParameter("endDate", "6/23/2007");
		dateForm.submit();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View My Access Log", wr.getTitle());
		assertTrue(wr.getText().contains("<h2>Information not valid</h2><div class=\"errorList\">Enter dates in MM/dd/yyyy<br /></div>"));
		
		//This test is currently commented out because the bug is due to "functionality" in the SimpleDataFormat class which assumes that month 13 === 1
		/*
		dateForm = wr.getForms()[0];
		dateForm.setParameter("startDate", "13/01/2010");
		dateForm.setParameter("endDate", "6/24/2011");
		dateForm.submit();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View My Access Log", wr.getTitle());
		assertTrue(wr.getText().contains("<h2>Information not valid</h2><div class=\"errorList\">Enter dates in MM/dd/yyyy<br /></div>"));
		*/
	}

}
