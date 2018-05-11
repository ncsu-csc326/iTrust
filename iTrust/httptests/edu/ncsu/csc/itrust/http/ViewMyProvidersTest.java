package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


/**
 * Test class for the viewVisitedHCPs.jsp
 */
public class ViewMyProvidersTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.clearAllTables();
		gen.standardData();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		gen.clearAllTables();
	}
	
	/**
	 * testViewMyProviders1
	 * @throws Exception
	 */
	public void testViewMyProviders1() throws Exception {
		// login patient 1
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		WebTable wt = wr.getTableStartingWith("HCP Name");
		// Row count is number of HCPs (2) + header row + footer row == 4.
		assertEquals(4, wt.getRowCount());
		assertEquals("Gandalf Stormcrow", wt.getCellAsText(1, 0));
		assertEquals("Kelly Doctor", wt.getCellAsText(2, 0));
		
		// Filter on last name.
		wr.getForms()[1].setParameter("filter_name", "Doctor");
		wr = wr.getForms()[1].submit();
		
		assertEquals("iTrust - My Providers", wr.getTitle());
		wt = wr.getTableStartingWith("HCP Name");
		// Only Kelly Doctor should be listed now.
		assertEquals(3, wt.getRowCount());
		assertEquals("Kelly Doctor", wt.getCellAsText(1, 0));
		// Gandalf Stormcrow is no longer listed.
		assertFalse("Gandalf Stormcrow".equals(wt.getCellAsText(2, 0)));
	}
	
	/**
	 * testViewMyProviders2
	 * @throws Exception
	 */
	public void testViewMyProviders2() throws Exception {
		// login patient 1
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		WebTable wt = wr.getTableStartingWith("HCP Name");
		// Row count is number of HCPs (2) + header row + footer row == 4.
		assertEquals(4, wt.getRowCount());
		assertEquals("Gandalf Stormcrow", wt.getCellAsText(1, 0));
		assertEquals("Kelly Doctor", wt.getCellAsText(2, 0));
		
		// Filter on specialty.
		wr.getForms()[1].setParameter("filter_specialty", "surgeon");
		wr = wr.getForms()[1].submit();

		// Only Kelly Doctor should be listed now... no more Gandalf Stormcrow.
		assertEquals("iTrust - My Providers", wr.getTitle());
		wt = wr.getTableStartingWith("HCP Name");
		assertEquals(3, wt.getRowCount());
		assertEquals("Kelly Doctor", wt.getCellAsText(1, 0));
		assertFalse("Gandalf Stormcrow".equals(wt.getCellAsText(2, 0)));
	}

}
