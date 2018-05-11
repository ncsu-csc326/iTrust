package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class AddPatientTest extends iTrustHTTPTest {
	public static final String ADDRESS = "http://localhost:8080/iTrust/auth/hcp-uap/addPatient.jsp";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testValidPatient() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wc.getResponse(ADDRESS);
		WebForm patientForm = wr.getForms()[0];
		patientForm.setParameter("firstName", "John");
		patientForm.setParameter("lastName", "Doe");
		patientForm.setParameter("email", "john.doe@example.com");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("successfully added"));
		
		// Get the new patient's MID and temporary password
		WebTable infoTable = wr.getTableStartingWith("New Patient Information");
		String mid = infoTable.getTableCell(1, 1).getText();
		String password = infoTable.getTableCell(2, 1).getText();
		
		// Ensure that we can login using the patient's credentials
		wc = login(mid, password);	
		assertLogged(TransactionType.HOME_VIEW, new Integer(mid), 0L, "");
	}
	
	public void testBlankPatientName() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// Test blank first name
		wr = wc.getResponse(ADDRESS);
		WebForm patientForm = wr.getForms()[0];
		patientForm.setParameter("firstName", "");
		patientForm.setParameter("lastName", "Doe");
		patientForm.setParameter("email", "john.doe@example.com");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("This form has not been validated correctly."));
		
		// Test blank last name
		wr = wc.getResponse(ADDRESS);
		patientForm = wr.getForms()[0];
		patientForm.setParameter("firstName", "John");
		patientForm.setParameter("lastName", "");
		patientForm.setParameter("email", "john.doe@example.com");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("This form has not been validated correctly."));
	}
	
	public void testInvalidPatientName() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// Test invalid first name
		wr = wc.getResponse(ADDRESS);
		WebForm patientForm = wr.getForms()[0];
		patientForm.setParameter("firstName", "----");
		patientForm.setParameter("lastName", "Doe");
		patientForm.setParameter("email", "john.doe@example.com");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("This form has not been validated correctly."));
		
		// Test invalid last name
		wr = wc.getResponse(ADDRESS);
		patientForm = wr.getForms()[0];
		patientForm.setParameter("firstName", "John");
		patientForm.setParameter("lastName", "----");
		patientForm.setParameter("email", "john.doe@example.com");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("This form has not been validated correctly."));
	}
	
	public void testInvalidPatientEmail() throws Exception{
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wc.getResponse(ADDRESS);
		WebForm patientForm = wr.getForms()[0];
		patientForm.setParameter("firstName", "John");
		patientForm.setParameter("lastName", "Doe");
		patientForm.setParameter("email", "---@---.com");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("This form has not been validated correctly."));
	}
}
