package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Makes sure the "specialty" field is available when adding an HCP,
 * creates an HCP with the specialty "Medicine", and makes sure
 * that HCP was successfully created.
 */
public class CreateLTSpecTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testSpecialtyOnForm() throws Exception{
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Add HCP
		wr = wr.getLinkWith("Add LT").click();
		
		// add the hcp
		assertEquals("iTrust - Add LT", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "New");
		form.setParameter("lastName", "Person");
		form.setParameter("email", "nperson@gmail.com");
		assertTrue(form.hasParameterNamed("specialty"));
		form.setParameter("specialty", "general");
		assertEquals("general", form.getParameterValue("specialty"));
		wr = form.submit();
		// make sure LT was added
		WebTable table = wr.getTables()[0];
		assertTrue(wr.getText().contains("New LT New Person successfully added!"));
		String newMID = table.getCellAsText(1, 1);
		assertTrue(newMID.contains("500000"));
		assertLogged(TransactionType.LT_CREATE, 9000000001L, Long.parseLong(newMID), "");
		
	}
}
