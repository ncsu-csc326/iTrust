package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class MaintainStandardsTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
		gen.cptCodes();
	}	

	/*
	 * Precondition: Admin exists in database and has authenticated successfully.
	 * Admin Chooses to Administer CPT Codes
	 * Admin Inputs Code 90736 with Description "Shingles Vaccine"
	 * Admin Submits
	 */
	public void testMaintainStandardsList1() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit CPT codes
		wr = wr.getLinkWith("Edit CPT ProcedureCodes").click();
		assertTrue(wr.getURL().toString().contains("auth/admin/editCPTProcedureCodes.jsp"));
		// add the cpt code
		WebForm form = wr.getForms()[0];
		form.setParameter("code", "90736");
		form.setParameter("description", "Shingles Vaccine");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Success: 90736 - Shingles Vaccine"));
		assertLogged(TransactionType.MEDICAL_PROCEDURE_CODE_ADD, 9000000001L, 0L, "");
	}
}
