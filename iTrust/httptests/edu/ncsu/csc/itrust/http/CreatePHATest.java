package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class CreatePHATest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
		gen.cptCodes();
	}

	/*
	 * Authenticate admin 90000000001
	 * Choose Add PHA option
	 * Last name: Blah
	 * First name: Bob
	 * Email: bobblah@blarg.com
	 */
	public void testCreateValidPHA() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add PHA
		wr = wr.getLinkWith("Add PHA").click();
		// add the pha
		assertEquals("iTrust - Add PHA", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "Bob");
		form.setParameter("lastName", "Blah");
		form.setParameter("email", "bobblah@blarg.com");
		wr = form.submit();
		WebTable table = wr.getTables()[0];
		String newMID = table.getCellAsText(1, 1);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("New PHA Bob Blah succesfully added!"));
		assertLogged(TransactionType.PHA_CREATE, 9000000001L, Long.parseLong(newMID), "");
	}
	
	public void testCreateNullPHA() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add PHA
		wr = wr.getLinkWith("Add PHA").click();
		// add the pha
		assertEquals("iTrust - Add PHA", wr.getTitle());
		WebForm form = wr.getForms()[0];
		wr = form.submit();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("This form has not been validated correctly. The following field are not properly filled in: [First name: Up to 20 Letters, space, ' and -, Last name: Up to 20 Letters, space, ' and -, Email: Up to 30 alphanumeric characters and symbols . and _ @]"));
		assertNotLogged(TransactionType.PHA_DISABLE, 9000000001L, 0L, "");
	}
	
	public void testCreateValidPHA2() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add PHA
		wr = wr.getLinkWith("Add PHA").click();
		// add the PHA
		assertEquals("iTrust - Add PHA", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "Tim");
		form.setParameter("lastName", "Agent");
		form.setParameter("email", "pha@timagent.com");
		wr = form.submit();
		WebTable table = wr.getTables()[0];
		String newMID = table.getCellAsText(1, 1);
		// edit the hcp
		wr = wr.getLinkWith("Continue").click();
		assertEquals("iTrust - Edit Personnel", wr.getTitle());
		form = wr.getForms()[0];
		form.setParameter("streetAddress1", "98765 Oak Hills Dr");
		form.setParameter("city", "Capitol City");
		form.setParameter("state", "NC");
		form.setParameter("zip", "28700-0458");
		form.setParameter("phone", "555-877-5100");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.PHA_CREATE, 9000000001L, Long.parseLong(newMID), "");
	}
}