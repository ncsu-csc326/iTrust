package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 2
 */
public class PersonnelUseCaseTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * testAddER
	 * @throws Exception
	 */
	public void testAddER() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add ER
		wr = wr.getLinkWith("Add ER").click();
		assertEquals("iTrust - Add ER", wr.getTitle());
	}
	
	/**
	 * testCreateER
	 * @throws Exception
	 */
	public void testCreateER() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add ER
		wr = wr.getLinkWith("Add ER").click();
		// add the ER
		assertEquals("iTrust - Add ER", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "Nick");
		form.setParameter("lastName", "Oftime");
		form.setParameter("email", "nick@itrust.com");
		wr = form.submit();
		WebTable table = wr.getTables()[0];
		String newMID = table.getCellAsText(1, 1);
		// Verify new emergency responder data is present
		assertTrue(wr.getText().contains("New ER Nick Oftime succesfully added!"));
		assertLogged(TransactionType.ER_CREATE, 9000000001L, Long.parseLong(newMID), "");
	}
	
	/**
	 * testEditERDetails
	 * @throws Exception
	 */
	public void testEditERDetails() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add ER
		wr = wr.getLinkWith("Add ER").click();
		// add the ER
		assertEquals("iTrust - Add ER", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "Nick");
		form.setParameter("lastName", "Oftime");
		form.setParameter("email", "nick@itrust.com");
		wr = form.submit();
		WebTable table = wr.getTables()[0];
		String newMID = table.getCellAsText(1, 1);
		// Verify new emergency responder data is present
		assertTrue(wr.getText().contains("New ER Nick Oftime succesfully added!"));
		assertLogged(TransactionType.ER_CREATE, 9000000001L, Long.parseLong(newMID), "");
		
		wr = wr.getLinkWith("Continue to personnel information.").click();
		
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
		assertLogged(TransactionType.ER_EDIT, 9000000001L, Long.parseLong(newMID), "");
	}
}