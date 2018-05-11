package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class EditApptTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testEditAppt() throws Exception {
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("View My Appointments").click();
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 9000000000L, 0L, "");
		
		WebTable table = wr.getTables()[0];
		wr = table.getTableCell(table.getRowCount()-1, 5).getLinkWith("Edit/Remove").click();
		assertTrue(wr.getText().contains("Andy Programmer"));
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("comment", "New comment!");
		
		SubmitButton[] buttons = wf.getSubmitButtons();
		wr = wf.submit(buttons[0]);	// Submit as "Change"
		assertTrue(wr.getText().contains("Success: Appointment changed"));
		assertLogged(TransactionType.APPOINTMENT_EDIT, 9000000000L, 2L, "");
	}
	
	/**
	 * testSetPassedDate
	 * @throws Exception
	 */
	public void testSetPassedDate() throws Exception {
		// login hcp
		gen.uc22();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		wr = wr.getLinkWith("View My Appointments").click();
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 9000000000L, 0L, "");
		
		WebTable table = wr.getTables()[0];
		int row = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getCellAsText(i, 0).equals("Anakin Skywalker")) {
				row = i;
				break;
			}
		}
		
		wr = table.getTableCell(row, 5).getLinkWith("Edit/Remove").click();
		assertTrue(wr.getText().contains("Anakin Skywalker"));
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("schedDate", "10/10/2009");
		
		wf.getSubmitButtonWithID("changeButton").click();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("The scheduled date of this appointment")); 
		assertTrue(wr.getText().contains("has already passed."));
		assertNotLogged(TransactionType.APPOINTMENT_EDIT, 9000000000L, 100L, "");
	}
	
	/**
	 * testRemoveAppt
	 * @throws Exception
	 */
	public void testRemoveAppt() throws Exception {
		// login hcp
		gen.uc22();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("View My Appointments").click();
		assertLogged(TransactionType.APPOINTMENT_ALL_VIEW, 9000000000L, 0L, "");
		
		WebTable table = wr.getTables()[0];
		int row = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getCellAsText(i, 0).equals("Anakin Skywalker")) {
				row = i;
				break;
			}
		}
		
		wr = table.getTableCell(row, 5).getLinkWith("Edit/Remove").click();
		assertTrue(wr.getText().contains("Anakin Skywalker"));
		WebForm wf = wr.getFormWithID("mainForm");
		
		wf.getSubmitButtonWithID("removeButton").click();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Success: Appointment removed"));
		assertLogged(TransactionType.APPOINTMENT_REMOVE, 9000000000L, 100L, "");
	}
}
