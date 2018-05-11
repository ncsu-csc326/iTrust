package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
//import com.meterware.httpunit.FormControl;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import com.meterware.httpunit.TableRow;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 6
 * Test designatedand ViewHCPCase 
 *
 */

public class DesignateAndViewHCPUseCaseTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.patient_hcp_vists();
	}
	
	/**
	 * Test testReportSeenHCPs0
	 * @throws Exception
	 */
	public void testReportSeenHCPs0() throws Exception {

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");

		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		WebTable table = (WebTable)wr.getElementWithID("hcp_table");
		TableRow rows[] = table.getRows();
		
		assertTrue(rows[0].getText().contains("HCP Name"));
		assertTrue(rows[1].getText().contains("Gandalf Stormcrow"));
		assertTrue(rows[2].getText().contains("Mary Shelley"));
		assertTrue(rows[3].getText().contains("Lauren Frankenstein"));
		assertTrue(rows[4].getText().contains("Jason Frankenstein"));
		assertTrue(rows[5].getText().contains("Kelly Doctor"));

	}
	
	public void testReportSeenHCPs1() throws Exception {

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		WebForm form = wr.getFormWithName("mainForm"); // .getFormWithID("mainForm");
		
		//form.setCheckbox("doctor", "Jason Frankenstein", false);
		form.getScriptableObject().setParameterValue("removeID", "Jason Frankenstein");
		//form.getScriptableObject().setParameterValue("doctor", "Jason Frankenstein");
		//wr.getElementsWithName(doctor)[0]
		
		//form.getScriptableObject().submit();
		//wr = wc.getCurrentPage();
		wr = wr.getForms()[0].submit();
		assertLogged(TransactionType.LHCP_VIEW, 2L, 0L, "");
		
		WebTable table = (WebTable)wr.getElementWithID("hcp_table");
		TableRow rows[] = table.getRows();
		
		assertTrue(rows[4].getText().contains("Jason Frankenstein"));
		
	}
	
	public void testReportSeenHCPs2() throws Exception {

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");

		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		WebForm form = wr.getFormWithID("searchForm");
		form.setParameter("filter_name", "Frank");
		form.setParameter("filter_specialty", "pediatrician");
		wr = form.submit();
		assertEquals("iTrust - My Providers", wr.getTitle());
		WebTable table = (WebTable)wr.getElementWithID("hcp_table");
		TableRow rows[] = table.getRows();
		assertTrue(rows[1].getText().contains("Lauren Frankenstein"));
	}
}
