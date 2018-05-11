package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class CreateHCPTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
		gen.cptCodes();
	}

	/*
	 * Authenticate admin 90000000001
	 * Choose Add HCP option
	 * Physican type not currently implemented
	 * [Role: Licensed Physician]
	 * [Enabled: true]
	 * Last name: Williams
	 * First name: Laurie
	 * Email: laurie@ncsu.edu
	 * Street address 1: 900 Main Campus Dr
	 * Street address 2: BOX 2509
	 * City: Raleigh
	 * State: NC
	 * Zip code: 27606-1234
	 * Phone: 919-100-1000
	 */
	public void testCreateValidHCP() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add HCP
		wr = wr.getLinkWith("Add HCP").click();
		// add the hcp
		assertEquals("iTrust - Add HCP", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "Laurie");
		form.setParameter("lastName", "Williams");
		form.setParameter("email", "laurie@ncsu.edu");
		wr = form.submit();
		// edit the hcp
		WebTable table = wr.getTables()[0];
		String newMID = table.getCellAsText(1, 1);		
		wr = wr.getLinkWith("Continue").click();
		assertEquals("iTrust - Edit Personnel", wr.getTitle());
		form = wr.getForms()[0];
		form.setParameter("streetAddress1", "900 Main Campus Dr");
		form.setParameter("streetAddress2", "Box 2509");
		form.setParameter("city", "Raleigh");
		form.setParameter("state", "NC");
		form.setParameter("zip", "27606-1234");
		form.setParameter("phone", "919-100-1000");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.LHCP_CREATE, 9000000001L, Long.parseLong(newMID), "");
	}
	public void testEditHospitalAssignments() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit HCP Assignment to Hospital
		wr = wr.getLinkWith("Edit HCP Assignment to Hospital").click();
		assertEquals("iTrust - Please Select a Personnel", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Kelly");
		wr.getForms()[1].setParameter("LAST_NAME", "Doctor");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		// assign hospital
		assertEquals("iTrust - Hospital Staffing Assignments", wr.getTitle());
		WebLink[] weblinks = wr.getLinks();
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("Assign")) {
				wr = weblinks[i].click();
				assertTrue(wr.getText().contains("HCP has been assigned"));
				assertLogged(TransactionType.LHCP_ASSIGN_HOSPITAL, 9000000001L, 9000000000L, "");
				break;
			}
		}
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("Unassign")) {
				wr = weblinks[i].click();
				assertTrue(wr.getText().contains("HCP has been unassigned"));
				assertLogged(TransactionType.LHCP_REMOVE_HOSPITAL, 9000000001L, 9000000000L, "");
				break;
			}
		}
	}
	
}