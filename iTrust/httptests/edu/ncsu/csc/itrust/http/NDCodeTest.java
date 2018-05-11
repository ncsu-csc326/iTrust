/**
 * 
 */
package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 *  
 *
 */
public class NDCodeTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
		gen.ndCodes();
		gen.ndCodes1();
		gen.ndCodes2();
		gen.ndCodes3();
		gen.ndCodes4();
	}
	
	/*
	 * Authenticate admin 90000000001
	 * Choose Edit ND Codes
	 * Code: 30142-416
	 * Description: "Aspirin"
	 */
	public void testRemoveNDCode() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code1", "08109");
		form.setParameter("code2", "6");
		form.setParameter("description", "Aspirin");
		form.getSubmitButtons()[3].click();
		assertLogged(TransactionType.DRUG_CODE_REMOVE, 9000000001L, 0L, "081096");		
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editNDCodes.jsp"));
		assertTrue(wr.getText().contains("Success: 081096 - Aspirin removed"));
	}

	/*
	 * Authenticate admin 90000000001
	 * Choose Edit ND Codes
	 * Code: 00060-431
	 * Description: "Benzoyl Peroxide"
	 */
	public void testUpdateNDCode() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code1", "00060");
		form.setParameter("code2", "431");
		form.setParameter("description", "Benzoyl Peroxidez");
		form.getSubmitButtons()[2].click();
		assertLogged(TransactionType.DRUG_CODE_EDIT, 9000000001L, 0L, "00060431");	
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editNDCodes.jsp"));
		assertTrue(wr.getText().contains("Success: 1 row(s) updated"));
	}
}
