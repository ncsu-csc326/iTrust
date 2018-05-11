package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 */
public class OfficeVisitBillingTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.standardData();
		gen.uc60();
	}
	

	public void testPaymentLogging() throws Exception {
		WebConversation wc = login("311", "pw");
		WebResponse wr = wc.getCurrentPage();
		WebLink bills = wr.getLinkWith("My Bills");
		assertNotNull(bills);
		bills.click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/patient/myBills.jsp", wr.getURL().toString());
		assertTrue(wr.getText().contains("12/02/2013"));
		WebLink link = wr.getLinkWith("12/02/2013");
		assertNotNull(link);
		link.click();

	    WebResponse billPage = wc.getCurrentPage();
		WebForm form = billPage.getForms()[0];
		String[] apptValues = form.getOptionValues("type");
		form.setParameter("type", apptValues[1]);
		form.setParameter("insHolder", "Sean Ford");
		form.setParameter("insID", "2324198");
		form.setParameter("insProvider", "Blue Cross");
		form.setParameter("insAdd1", "123 Fake Street");
		form.setParameter("insCity", "Raleigh");
		form.setParameter("insState", "NC");
		form.setParameter("insZip", "27607");
		form.setParameter("insPhone", "555-555-5555");
		
		billPage = form.submit();
		assertLogged(TransactionType.PATIENT_SUBMITS_INSURANCE, 311L, 311L, "");
	}
	
	public void testBadPolicyID() throws Exception{
		WebConversation wc = login("311", "pw");
		WebResponse wr = wc.getCurrentPage();
		WebLink bills = wr.getLinkWith("My Bills");
		assertNotNull(bills);
		bills.click();
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("12/02/2013").click();

		WebForm form = wr.getForms()[0];
		String[] apptValues = form.getOptionValues("type");
		form.setParameter("type", apptValues[1]);
		form.setParameter("insHolder", "Sean Ford");
		form.setParameter("insID", "!@##%*()");
		form.setParameter("insProvider", "Blue Cross");
		form.setParameter("insAdd1", "123 Fake Street");
		form.setParameter("insCity", "Raleigh");
		form.setParameter("insState", "NC");
		form.setParameter("insZip", "27607");
		form.setParameter("insPhone", "555-555-5555");
		
		form.submit();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Insurance IDs must consist of alphanumeric characters."));
		wr = wr.getLinkWith("My Bills").click();
		
		assertTrue(wr.getText().contains("Unsubmitted"));
	}
	
	public void testViewBilling() throws Exception{
		WebConversation wc = login("311", "pw");
		WebResponse wr = wc.getCurrentPage();
		WebLink bills = wr.getLinkWith("My Bills");
		assertNotNull(bills);
		bills.click();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View My Bills", wr.getTitle());
		
		assertTrue(wr.getText().contains("03/08/2012"));
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("Submitted"));
		assertTrue(wr.getText().contains("12/02/2013"));
		assertTrue(wr.getText().contains("Meredith Palmer"));
		assertTrue(wr.getText().contains("Unsubmitted"));
		
		WebLink wl = wr.getLinkWith("12/02/2013");
		 
		wr = wl.click();
		
		assertTrue(wr.getText().contains("Central Hospital"));
		assertTrue(wr.getText().contains("General Checkup"));
		assertTrue(wr.getText().contains("Sean needs to lower his sodium intake."));
		
	}
	
	public void testInsurance() throws Exception{
		WebConversation wc = login("9000000011", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "309");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "02/06/2014");
		form.setParameter("notes", "Patient seems to be doing well, Rob is encouraged to consume more iron.");
		form.setParameter("hospitalID", "9");
		form.setCheckbox("isBilled", true);
		//form.setParameter("billAmt", "1000");
		String[] apptValues = form.getOptionValues("apptType");
		form.setParameter("apptType", apptValues[0]);
		form.submit();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Logout").click();
		
		wc = login("309", "pw");
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("02/06/2014").click();
		
		form = wr.getForms()[0];
		form.setParameter("type", "Ins");
		form.setParameter("insHolder", "Rob Peterson");
		form.setParameter("insID", "2324199");
		form.setParameter("insProvider", "Blue Cross");
		form.setParameter("insAdd1", "123 Fake Street");
		form.setParameter("insAdd2", "123 Faker Street");
		form.setParameter("insCity", "Raleigh");
		form.setParameter("insState", "NC");
		form.setParameter("insZip", "27606");
		form.setParameter("insPhone", "555-555-5555");
		form.submit();
		
		wr = wc.getCurrentPage();
		//assertTrue(wr.getText().contains("Payment Confirmation"));
		assertLogged(TransactionType.PATIENT_SUBMITS_INSURANCE, 309L, 309L, "");
		
	}
	
	public void testCreditCard() throws Exception{
		WebConversation wc = login("9000000011", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "310");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/21/2014");
		form.setParameter("notes", "Theresa has been complaining of extreme fatigue. Bloodwork sent in for analysis.");
		form.setParameter("hospitalID", "9");
		form.setCheckbox("isBilled", true);
		//form.setParameter("billAmt", "1000");
		String[] apptValues = form.getOptionValues("apptType");
		form.setParameter("apptType", apptValues[0]);
		form.submit();
		
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Logout").click();
		
		wc = login("310", "pw");
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		wr = wr.getLinkWith("01/21/2014").click();
		
		form = wr.getForms()[0];
		form.setParameter("type", "CC");
		form.setParameter("ccHolder", "Theresa Clark");
		form.setParameter("billAddress", "123 Fake Street, Raleigh, NC 27607");
		form.setParameter("ccType", "MasterCard");
		form.setParameter("ccNumber", "5593090746812380");
		form.setParameter("cvv", "000");
		form.submit();
		
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.PATIENT_PAYS_BILL, 310L, 310L, "");
		assertTrue(wr.getText().contains("Payment Information"));
		
	}
	

	public void testCancelPayment() throws Exception{
		WebConversation wc = login("311", "pw");
		WebResponse wr = wc.getCurrentPage();
		WebLink bills = wr.getLinkWith("My Bills");
		assertNotNull(bills);
		bills.click();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View My Bills", wr.getTitle());
		
		assertTrue(wr.getText().contains("03/08/2012"));
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("Submitted"));
		assertTrue(wr.getText().contains("12/02/2013"));
		assertTrue(wr.getText().contains("Meredith Palmer"));
		assertTrue(wr.getText().contains("Unsubmitted"));
		
		wr = wr.getLinkWith("12/02/2013").click();
		
		WebForm form = wr.getForms()[0];
		
		form = wr.getForms()[0];
		form.setParameter("type", "Ins");
		form.setParameter("insHolder", "Sean Ford");
		form.setParameter("insID", "2324198");
		form.setParameter("insProvider", "Blue Cross");
		form.setParameter("insAdd1", "123 Fake Street");
		form.setParameter("insCity", "Raleigh");
		form.setParameter("insState", "NC");
		form.setParameter("insZip", "27607");
		form.setParameter("insPhone", "555-555-5555");
		form.getButtons()[1].click();
		
		//assertTrue(wr.getText().contains("Payment has been cancelled."));
		wr = wr.getLinkWith("My Bills").click();
		
		assertTrue(wr.getText().contains("Unsubmitted"));
		
	}
	
	public void testBillNotification() throws Exception {

		WebConversation wc = login("310", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertFalse(wr.getText().contains("new bill"));
		wr = wr.getLinkWith("Logout").click();
		
		wc = login("9000000011", "pw");
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "310");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/21/2014");
		form.setParameter("notes", "Theresa has been complaining of extreme fatigue. Bloodwork sent in for analysis.");
		form.setParameter("hospitalID", "9");
		form.setCheckbox("isBilled", true);
		//form.setParameter("billAmt", "1000");
		String[] apptValues = form.getOptionValues("apptType");
		form.setParameter("apptType", apptValues[0]);
		form.submit();
		
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Logout").click();
		
		wc = login("310", "pw");
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("1</a> new bill"));
	}
	

	public void testVisitNotBilled() throws Exception {

		WebConversation wc = login("9000000011", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "310");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/21/2014");
		form.setParameter("notes", "Theresa has been complaining of extreme fatigue. Bloodwork sent in for analysis.");
		form.setParameter("hospitalID", "9");
		//form.setParameter("billAmt", "1000");
		String[] apptValues = form.getOptionValues("apptType");
		form.setParameter("apptType", apptValues[0]);
		form.submit();
		
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Logout").click();
		
		wc = login("310", "pw");
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertNull(wr.getLinkWith("1/21/2014"));
		wr = wr.getLinkWith("Home").click();
		wr = wr.getLinkWith("View My Records").click();
		wr = wc.getCurrentPage();
		String s = wr.getText();
		assertTrue(s.contains("Jan 21, 2014"));
	}
	
	
	
}
