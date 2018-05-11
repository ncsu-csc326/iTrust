package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * ReferralsTest
 */
public class ReferralsTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/**
	 * testCreateNewReferral
	 * @throws Exception
	 */
	public void testCreateNewReferral() throws Exception {
		gen.officeVisit3();
		
		// login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from specific date
		wr.getLinkWith("9/17/2009").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// click on the add referral button
		form = wr.getFormWithID("createReferralForm");
		form.getButtonWithID("add_referral").click();

		// select the HCP
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Please Select a Personnel", wr.getTitle());
		form = wr.getForms()[1];
		form.setParameter("FIRST_NAME", "Gandalf");
		form.setParameter("LAST_NAME", "Stormcrow");
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		wr.getForms()[2].getButtons()[0].click();
		
		// we should be on the edit referral page now
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Referral", wr.getTitle());
		
		// fill in the form
		form = wr.getFormWithID("editReferralForm");
		form.setParameter("priority", "1");
		form.setParameter("referralDetails", "See Gandalf. He will translate the engravings on that ring for you.");
		form.getButtonWithID("submitCreate").click();
		
		// we should be back on the edit office visit page
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// see that referral is now in the table
		WebTable tbl = wr.getTableWithID("referralsTable");
		assertEquals("Gandalf Stormcrow", tbl.getCellAsText(2, 0)); // receiving HCP
		assertEquals("See Gandalf. He will translate the engravings on that ring for you.", tbl.getCellAsText(2, 1)); // notes
		assertEquals("1", tbl.getCellAsText(2, 2)); // priority
		String datestr = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		assertTrue(tbl.getCellAsText(2, 3).contains(datestr)); // time stamp
	}
	
	/**
	 * Tests deleting (canceling) an existing referral from the referrals list.
	 * @throws Exception Exceptions will be thrown if any of the .get*WithID() calls fail.
	 */
	public void testDeleteExistingReferral() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Sent Referrals").click();
		
		// select the patient
		WebForm form = wr.getFormWithID("editReferralForm");
		form.getButtons()[0].click();

		// we should be on the edit referral page now
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Referral", wr.getTitle());
		
		// click the delete button and confirm
		form = wr.getFormWithID("editReferralForm");
		form.getButtonWithID("submitDelete").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Referral", wr.getTitle());
		form = wr.getFormWithID("editReferralForm");
		form.getButtonWithID("submitCreate").click();

		// we should be back on the edit office visit page
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		
		//verify that no exceptions were thrown
		assertFalse(wr.getText().contains("NumberFormatException"));
		assertFalse(wr.getText().contains("<h2>Oops! Your page wasn't found</h2>"));
		assertFalse(wr.getText().contains("NullPointerException"));		
	}
	
	/**
	 * testModifyExistingReferral
	 * @throws Exception
	 */
	public void testModifyExistingReferral() throws Exception {
		// login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from specific date
		wr.getLinkWith("6/10/2007").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// get the row containing the referral to edit
		int rown = 3;
		WebTable tbl = wr.getTableWithID("referralsTable");
		assertTrue(tbl.getCellAsText(rown, 3).contains("07/09/2007"));
		form = tbl.getTableCell(rown, 4).getForms()[0];
		form.getButtons()[0].click();
		
		// we should be on the edit referral page now
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Referral", wr.getTitle());
		
		// fill in the form
		form = wr.getFormWithID("editReferralForm");
		form.setParameter("priority", "1");
		form.setParameter("referralDetails", "Gandalf will take care of you--for a price!");
		form.getButtonWithID("submitEdit").click();
		
		// we should be back on the edit office visit page
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		//verify that no exceptions were thrown
		assertFalse(wr.getText().contains("NumberFormatException"));
		assertFalse(wr.getText().contains("<h2>Oops! Your page wasn't found</h2>"));
		assertFalse(wr.getText().contains("NullPointerException"));

		// see that referral is now in the table
		tbl = wr.getTableWithID("referralsTable");
		assertEquals("Gandalf Stormcrow", tbl.getCellAsText(rown, 0)); // receiving HCP
		assertEquals("Gandalf will take care of you--for a price!", tbl.getCellAsText(rown, 1)); // notes
		assertEquals("1", tbl.getCellAsText(rown, 2)); // priority
		assertTrue(tbl.getCellAsText(rown, 3).contains("07/09/2007")); // time stamp
	}
	
	/**
	 * testHCPViewSentReferrals
	 * @throws Exception
	 */
	public void testHCPViewSentReferrals() throws Exception {
		gen.hcp4();
		gen.hcp5();
		gen.referral_sort_testdata();

		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Sent Referrals").click();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		WebTable tbl = wr.getTableWithID("sentReferralsTable");
		assertEquals(11, tbl.getRowCount());
		
		// original sort is by time stamp (descending)
		int updatedColumn = 2;
		assertEquals("12/22/2011\n00:00 AM", tbl.getCellAsText(2, updatedColumn));
		assertEquals("12/10/2011\n00:00 AM", tbl.getCellAsText(3, updatedColumn));
		assertEquals("12/01/2010\n00:00 AM", tbl.getCellAsText(4, updatedColumn));
		assertEquals("11/10/2010\n00:00 AM", tbl.getCellAsText(5, updatedColumn));
		assertEquals("10/13/2010\n00:00 AM", tbl.getCellAsText(6, updatedColumn));
		assertEquals("08/10/2010\n00:00 AM", tbl.getCellAsText(7, updatedColumn));
		assertEquals("11/30/2009\n00:00 AM", tbl.getCellAsText(8, updatedColumn));
		assertEquals("09/10/2009\n00:00 AM", tbl.getCellAsText(9, updatedColumn));
		assertEquals("10/10/2008\n00:00 AM", tbl.getCellAsText(10, updatedColumn));
		
		// Sort by receiving HCP
		wr.getFormWithID("sortByReceivingHCP").getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		tbl = wr.getTableWithID("sentReferralsTable");
		assertEquals(11, tbl.getRowCount());
		
		int receivingColumn = 0;
		assertEquals("Beaker Beaker", tbl.getCellAsText(2, receivingColumn));
		assertEquals("Beaker Beaker", tbl.getCellAsText(3, receivingColumn));
		assertEquals("Kelly Doctor", tbl.getCellAsText(4, receivingColumn));
		assertEquals("Kelly Doctor", tbl.getCellAsText(5, receivingColumn));
		assertEquals("Antonio Medico", tbl.getCellAsText(6, receivingColumn));
		assertEquals("Antonio Medico", tbl.getCellAsText(7, receivingColumn));
		assertEquals("Antonio Medico", tbl.getCellAsText(8, receivingColumn));
		assertEquals("Sarah Soulcrusher", tbl.getCellAsText(9, receivingColumn));
		assertEquals("Sarah Soulcrusher", tbl.getCellAsText(10, receivingColumn));
		
		// Sort by receiving HCP in reverse
		wr.getFormWithID("sortByReceivingHCP").getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		tbl = wr.getTableWithID("sentReferralsTable");
		assertEquals(11, tbl.getRowCount());

		assertEquals("Beaker Beaker", tbl.getCellAsText(10, receivingColumn));
		assertEquals("Beaker Beaker", tbl.getCellAsText(9, receivingColumn));
		assertEquals("Kelly Doctor", tbl.getCellAsText(8, receivingColumn));
		assertEquals("Kelly Doctor", tbl.getCellAsText(7, receivingColumn));
		assertEquals("Antonio Medico", tbl.getCellAsText(6, receivingColumn));
		assertEquals("Antonio Medico", tbl.getCellAsText(5, receivingColumn));
		assertEquals("Antonio Medico", tbl.getCellAsText(4, receivingColumn));
		assertEquals("Sarah Soulcrusher", tbl.getCellAsText(3, receivingColumn));
		assertEquals("Sarah Soulcrusher", tbl.getCellAsText(2, receivingColumn));
		
		// Sort by patient
		wr.getFormWithID("sortByPatient").getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		tbl = wr.getTableWithID("sentReferralsTable");
		assertEquals(11, tbl.getRowCount());

		int patientColumn = 1;
		assertEquals("Fozzie Bear", tbl.getCellAsText(2, patientColumn));
		assertEquals("Fozzie Bear", tbl.getCellAsText(3, patientColumn));
		assertEquals("Fozzie Bear", tbl.getCellAsText(4, patientColumn));
		assertEquals("Random Person", tbl.getCellAsText(5, patientColumn));
		assertEquals("Random Person", tbl.getCellAsText(6, patientColumn));
		assertEquals("Random Person", tbl.getCellAsText(7, patientColumn));
		assertEquals("Andy Programmer", tbl.getCellAsText(8, patientColumn));
		assertEquals("Andy Programmer", tbl.getCellAsText(9, patientColumn));
		assertEquals("Andy Programmer", tbl.getCellAsText(10, patientColumn));
		
		// Sort by priority
		wr.getFormWithID("sortByPriority").getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		tbl = wr.getTableWithID("sentReferralsTable");
		assertEquals(11, tbl.getRowCount());

		int priorityColumn = 3;
		assertEquals("1", tbl.getCellAsText(2, priorityColumn));
		assertEquals("1", tbl.getCellAsText(3, priorityColumn));
		assertEquals("1", tbl.getCellAsText(4, priorityColumn));
		assertEquals("2", tbl.getCellAsText(5, priorityColumn));
		assertEquals("2", tbl.getCellAsText(6, priorityColumn));
		assertEquals("2", tbl.getCellAsText(7, priorityColumn));
		assertEquals("3", tbl.getCellAsText(8, priorityColumn));
		assertEquals("3", tbl.getCellAsText(9, priorityColumn));
		assertEquals("3", tbl.getCellAsText(10, priorityColumn));
		
	}
	
	/**
	 * testHPCViewReferralsEdit
	 * @throws Exception
	 */
	public void testHPCViewReferralsEdit() throws Exception
	{
		gen.hcp4();
		gen.hcp5();
		gen.referral_sort_testdata();

		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Sent Referrals").click();
		assertEquals("iTrust - View Sent Referrals", wr.getTitle());
		WebTable tbl = wr.getTableWithID("sentReferralsTable");
		assertEquals(11, tbl.getRowCount());
		
		//click on the "Details" button
		wr.getFormWithID("editReferralForm").getButtons()[0].click();
		wr = wc.getCurrentPage();

		assertEquals("iTrust - Edit Referral", wr.getTitle());
		
		//click the "Save Changes" button
		wr.getFormWithID("editReferralForm").getButtonWithID("submitEdit").click();
		wr = wc.getCurrentPage();
		
		//verify that no exceptions were thrown
		assertFalse(wr.getText().contains("NumberFormatException"));
		assertFalse(wr.getText().contains("<h2>Oops! Your page wasn't found</h2>"));
		assertFalse(wr.getText().contains("NullPointerException"));

	}
	
	/**
	 * testPatientViewReferralDetails
	 * @throws Exception
	 */
	public void testPatientViewReferralsWithDetails() throws Exception {
		gen.patient2();
		gen.referrals();

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("My Referrals").click();
		
		//Check to see that a referral exists with a receiving HCP of Gandalf Stormcrow
		wr = wc.getCurrentPage();
		WebTable tbl = wr.getTableWithID("patientReferralsTable");
		assertTrue(tbl.getTableCell(3, 1).getText().contains("Gandalf Stormcrow"));
		
		//Click to see details about the referral and check to see Sending HCP as Kelly Doctor
		//Receiving doctor again is Gandalf Stormcrow
		tbl.getTableCell(3, 3).getLinkWith("View").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Referrals", wr.getTitle());
		WebTable reftbl = wr.getTableWithID("patientViewingReferral");
		assertTrue(reftbl.getTableCell(2, 1).getText().contains("Kelly Doctor"));
		assertTrue(reftbl.getTableCell(2, 2).getText().contains("Gandalf"));
		assertTrue(reftbl.getTableCell(2, 4).getText().contains("Gandalf will make sure that the virus"));
	}
	
	/**
	 * testPatientSendsMessageToReceivingHCP
	 * @throws Exception
	 */
	public void testPatientSendsMessageToReceivingHCP() throws Exception {
		gen.patient2();
		gen.referrals();

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("My Referrals").click();
		
		
		//Pull up the details of the very first referral in the table
		wr = wc.getCurrentPage();
		WebTable tbl = wr.getTableWithID("patientReferralsTable");
		assertEquals("Gandalf Stormcrow", tbl.getTableCell(3, 1).getText());
		assertEquals("07/15/2007 00:00 AM", tbl.getTableCell(3, 2).getText());
		tbl.getTableCell(3, 3).getLinkWith("View").click();
		
		//Click on link to send a message
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Referrals", wr.getTitle());
		WebTable reftbl = wr.getTableWithID("patientViewingReferral");
		assertEquals("06/10/2007", reftbl.getTableCell(2, 3).getText());
		assertEquals("Gandalf will make sure that the virus does not get past your immune system", reftbl.getTableCell(2, 4).getText());
		reftbl.getTableCell(2, 6).getLinkWith("Email Gandalf Stormcrow").click();
		
		//Fill out the message box and submit a message to the receiving HCP
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Referrals", wr.getTitle());
		assertTrue(wr.getText().contains("To Gandalf Stormcrow"));
		WebForm form = wr.getForms()[2];
		form.setParameter("messageBody", "I want an appointment!");
		form.getButtons()[0].click();
		
		//Check to see if there is a success message.
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Your message has successfully been sent!"));
		
	}

	/**
	 * testHCPViewsReferralsList
	 * @throws Exception
	 */
	public void testHCPViewsReferralsList() throws Exception {
		gen.referrals();
		
		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Received Referrals").click();
	
		//Checks referral list to see that it is sorted by priority
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Received Referrals", wr.getTitle());
		WebTable tbl = wr.getTableWithID("receivedReferralsTable");
		assertEquals("1", tbl.getTableCell(2, 0).getText());
		assertEquals("1", tbl.getTableCell(3, 0).getText());
		assertEquals("1", tbl.getTableCell(4, 0).getText());
		assertEquals("1", tbl.getTableCell(5, 0).getText());
		assertEquals("2", tbl.getTableCell(6, 0).getText());
		assertEquals("2", tbl.getTableCell(7, 0).getText());
		assertEquals("3", tbl.getTableCell(8, 0).getText());
		
	}
	
	/**
	 * testHCPViewOVFromReferral
	 * @throws Exception
	 */
	public void testHCPViewOVFromReferral() throws Exception{
		gen.referrals();
		
		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Received Referrals").click();
	
		//Clicks to view referral detail
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Received Referrals", wr.getTitle());
		WebTable tbl = wr.getTableWithID("receivedReferralsTable");
		tbl.getTableCell(3, 3).getLinkWith("View").click();
		
		//Clicks on the date in the referral detail to go to Office Visit page
		wr = wc.getCurrentPage();
		WebTable reftbl = wr.getTableWithID("receivedViewingReferral");
		reftbl.getTableCell(2, 3).getLinkWith("06/10/2007").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("Viewing information for"));
		
	}
	
}
