package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class BMICalculationTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testUnderweightEC() throws Exception {
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Tom Nook (MID 106)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "106");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable table = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 3 rows of health records
		assertEquals(5, table.getRowCount());
		//Verify table contents
		
		//Underweight Equivalence Class - Row 3
		//Office visit date
		assertEquals("01/01/2013", table.getCellAsText(4, 0));
		//Patient BMI
		assertEquals("9.0", table.getCellAsText(4, 3));
		//Patient Weight Status
		assertEquals("Underweight", table.getCellAsText(4, 4));
		
		//Underweight Boundary Value (Low) - Row 2
		//Office visit date
		assertEquals("01/02/2013", table.getCellAsText(3, 0));
		//Patient BMI
		assertEquals("0.1", table.getCellAsText(3, 3));
		//Patient Weight Status
		assertEquals("Underweight", table.getCellAsText(3, 4));
		
		//Underweight Boundary Value (High) - Row 1
		//Office visit date
		assertEquals("01/03/2013", table.getCellAsText(2, 0));
		//Patient BMI
		assertEquals("18.4", table.getCellAsText(2, 3));
		//Patient Weight Status
		assertEquals("Underweight", table.getCellAsText(2, 4));
	}
	
	public void testNormalEC() throws Exception {
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Hannah Montana (MID 107)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "107");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable table = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 3 rows of health records
		assertEquals(5, table.getRowCount());
		//Verify table contents
		
		//Normal Weight Equivalence Class - Row 3
		//Office visit date
		assertEquals("01/01/2013", table.getCellAsText(4, 0));
		//Patient BMI
		assertEquals("21.75", table.getCellAsText(4, 3));
		//Patient Weight Status
		assertEquals("Normal", table.getCellAsText(4, 4));
		
		//Normal Weight Boundary Value (Low) - Row 2
		//Office visit date
		assertEquals("01/02/2013", table.getCellAsText(3, 0));
		//Patient BMI
		assertEquals("18.5", table.getCellAsText(3, 3));
		//Patient Weight Status
		assertEquals("Normal", table.getCellAsText(3, 4));
		
		//Normal Weight Boundary Value (High) - Row 1
		//Office visit date
		assertEquals("01/03/2013", table.getCellAsText(2, 0));
		//Patient BMI
		assertEquals("24.9", table.getCellAsText(2, 3));
		//Patient Weight Status
		assertEquals("Normal", table.getCellAsText(2, 4));
	}
	
	public void testOverweightEC() throws Exception {
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Hank Hill (MID 108)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "108");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable table = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 3 rows of health records
		assertEquals(5, table.getRowCount());
		//Verify table contents
		
		//Overweight Equivalence Class - Row 3
		//Office visit date
		assertEquals("01/01/2013", table.getCellAsText(4, 0));
		//Patient BMI
		assertEquals("27.5", table.getCellAsText(4, 3));
		//Patient Weight Status
		assertEquals("Overweight", table.getCellAsText(4, 4));
		
		//Overweight Boundary Value (Low) - Row 2
		//Office visit date
		assertEquals("01/02/2013", table.getCellAsText(3, 0));
		//Patient BMI
		assertEquals("25.0", table.getCellAsText(3, 3));
		//Patient Weight Status
		assertEquals("Overweight", table.getCellAsText(3, 4));
		
		//Overweight Boundary Value (High) - Row 1
		//Office visit date
		assertEquals("01/03/2013", table.getCellAsText(2, 0));
		//Patient BMI
		assertEquals("29.9", table.getCellAsText(2, 3));
		//Patient Weight Status
		assertEquals("Overweight", table.getCellAsText(2, 4));
	}
	
	public void testObeseEC() throws Exception {
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Snorlax (MID 109)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "109");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable table = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 2 rows of health records
		assertEquals(4, table.getRowCount());
		//Verify table contents
		
		//Obese Equivalence Class - Row 2
		//Office visit date
		assertEquals("01/01/2013", table.getCellAsText(3, 0));
		//Patient BMI
		assertEquals("50.0", table.getCellAsText(3, 3));
		//Patient Weight Status
		assertEquals("Obese", table.getCellAsText(3, 4));
		
		//Overweight Boundary Value (Low) - Row 1
		//Office visit date
		assertEquals("01/02/2013", table.getCellAsText(2, 0));
		//Patient BMI
		assertEquals("30.0", table.getCellAsText(2, 3));
		//Patient Weight Status
		assertEquals("Obese", table.getCellAsText(2, 4));
	}
	
	public void testAgeBounds() throws Exception {
		//Login as HCP Kelly Doctor
		WebConversation wc = login("9000000000", "pw");	
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Click Basic Health Information link
		wr = wr.getLinkWith("Basic Health Information").click();
		//Search for Bart Simpson (MID 110)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "110");
		//Click on first MID button
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		//Verify Basic Health Information page
		assertEquals(ADDRESS + "auth/hcp-uap/viewBasicHealth.jsp", wr.getURL().toString());
		
		//Verify adult health record table displays
		WebTable table = wr.getTableWithID("HealthRecordsTable");
		//Verify the table has the provided information: Header, field descriptions, 3 rows of health records
		assertEquals(5, table.getRowCount());
		//Verify table contents
		
		//Under 20yrs Equivalence Class - Row 3
		//Office visit date
		assertEquals("01/01/2003", table.getCellAsText(4, 0));
		//Patient BMI
		assertEquals("21.76", table.getCellAsText(4, 3));
		//Patient Weight Status (Not Applicable)
		assertEquals("N/A", table.getCellAsText(4, 4));
		
		//Under 20yrs Boundary Value (High) - Row 2
		//Office visit date
		assertEquals("12/31/2009", table.getCellAsText(3, 0));
		//Patient BMI
		assertEquals("21.76", table.getCellAsText(3, 3));
		//Patient Weight Status (Not Applicable)
		assertEquals("N/A", table.getCellAsText(3, 4));
		
		//Over 20yrs Boundary Value (Low) - Row 1
		//Office visit date
		assertEquals("01/01/2010", table.getCellAsText(2, 0));
		//Patient BMI
		assertEquals("21.76", table.getCellAsText(2, 3));
		//Patient Weight Status
		assertEquals("Normal", table.getCellAsText(2, 4));
	}
}
