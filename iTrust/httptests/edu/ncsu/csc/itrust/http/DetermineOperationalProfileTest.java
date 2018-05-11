package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class DetermineOperationalProfileTest extends iTrustHTTPTest {
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.tester();
	}


	/*
	 * Precondition: Sample data is in the database. CreatePatient2 has passed.
	 * Login with user 9999999999 and password pw.
	 */
	public void testDetermineOperationalProfile() throws Exception {
		// login as uap and add a patient
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse home = wc.getResponse(ADDRESS + "auth/uap/home.jsp");
		assertEquals("iTrust - UAP Home", home.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		WebResponse wr = home.getLinkWith("Add Patient").click();
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "bob");
		form.setParameter("lastName", "bob");
		form.setParameter("email", "bob@bob.com");
		wr = form.submit();
		WebTable table1 = wr.getTables()[0];
		String newMID = table1.getCellAsText(1, 1);
		assertLogged(TransactionType.PATIENT_CREATE, 8000000009L, Long.parseLong(newMID), "");
		
		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 8000000009L, 8000000009L, "");
		//wr = wr.getLinkWith("Home").click();
		// login as tester to check the operational profile
		wc = login("9999999999", "pw");
		wr = wc.getCurrentPage();
			
		WebTable table = wr.getTableStartingWithPrefix("Operation");
		
		assertEquals("Create a patient", table.getCellAsText(getRowNumber("Create a patient"), 0));
		assertEquals("1", table.getCellAsText(8, 1)); //was 1
		assertEquals("17%", table.getCellAsText(8, 2)); //was 17%
		assertEquals("1", table.getCellAsText(8, 3)); //was 1
		assertEquals("20%", table.getCellAsText(8, 4)); //was 20%
		assertEquals("0", table.getCellAsText(8, 5)); //was 0
		assertEquals("0%", table.getCellAsText(8, 6)); //was 0
		//now check the totals are correct
		
		assertEquals("Add Medical procedure code", table.getCellAsText(getRowNumber("Add Medical procedure code"), 0));
		assertEquals("0", table.getCellAsText(45, 1)); //was 4 - supposed to be 3
		assertEquals("0", table.getCellAsText(45, 3)); //was 3 - supposed to be 2
		assertEquals("0", table.getCellAsText(45, 5)); //supposed to be 1
		assertLogged(TransactionType.OPERATIONAL_PROFILE_VIEW, 9999999999L, 0L, "");
	}
	
	public int getRowNumber(String description)
	{
		TransactionType[] values = TransactionType.values();
		int rownumber = 0;
		for (int i=0; i<values.length; i++)
		{
			if (description.equals(values[i].getDescription()))
				rownumber = i+1;
		}
		
		return rownumber;
	}
}
