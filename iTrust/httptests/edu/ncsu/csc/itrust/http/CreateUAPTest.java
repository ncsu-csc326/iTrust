package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class CreateUAPTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/*
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose Add UAP option
	 * Last name: Watson
	 * First name: Doctor
	 * Street address 1: 1234 Varsity Ln
	 * Street address 2: 2nd Floor
	 * City: Cary
	 * State: SC
	 * Zip code: 12345-1234
	 * Phone: 704-100-1000
	 */
	public void testCreateUAP1() throws Exception {
		// login UAP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Add UAP
		wr = wr.getLinkWith("UAP").click();
		// add the UAP
		assertEquals("iTrust - Add UAP", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "Drake");
		form.setParameter("lastName", "Ramoray");
		form.setParameter("email", "drake@drake.com");
		wr = form.submit();
		WebTable table = wr.getTables()[0];
		String newMID = table.getCellAsText(1, 1);
		// edit the UAP
		wr = wr.getLinkWith("Continue").click();
		assertEquals("iTrust - Edit Personnel", wr.getTitle());
		form = wr.getForms()[0];		
		form.setParameter("firstName", "Doctor");
		form.setParameter("lastName", "Watson");
		form.setParameter("streetAddress1", "1234 Varsity Ln");
		form.setParameter("streetAddress2", "2nd Lane");
		form.setParameter("city", "Cary");
		form.setParameter("state", "NC");
		form.setParameter("zip", "12345-1234");
		form.setParameter("phone", "704-100-1000");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.UAP_CREATE, 9000000000L, Long.parseLong(newMID), "");
	}
}
