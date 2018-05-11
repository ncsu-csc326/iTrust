package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebForm;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * 
 * Credit Card Validator Test
 * 
 * Valid credit card numbers taken from http://www.darkcoding.net/credit-card-numbers/
 * They are allegedly not active numbers.
 * 
 */
public class CreditCardValidatorTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.patient1();
		gen.clearLoginFailures();
	}


	public void testGoodMasterCards() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "MASTERCARD");
		
		
		form.setParameter("creditCardNumber", "5593090746812380");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "5437693863890467");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "5343017708937494");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");

		
	}
	
	
	public void testBadMasterCards() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "MASTERCARD");
		
		
		form.setParameter("creditCardNumber", "1593090746812380");
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "4539592576502361"); // Legit Visa
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
	}
	
	public void testGoodVisas() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "VISA");
		
		
		form.setParameter("creditCardNumber", "4539592576502361");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "4716912133362668");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "4485333709241203");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");

		
	}
	
	
	public void testBadVisas() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "VISA");
		
		
		form.setParameter("creditCardNumber", "5593090746812380"); // Legit Mastercard
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "6437693863890467");
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
	}
	
	public void testGoodDiscovers() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "DISCOVER");
		
		
		form.setParameter("creditCardNumber", "6011263089803439");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "6011953266156193");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "6011726402628022");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");

		
	}
	
	
	public void testBadDiscovers() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "DISCOVER");
		
		
		form.setParameter("creditCardNumber", "5593090746812380"); // Legit Mastercard
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "6437693863890467");
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
	}
	
	/*
	 * AMEX stands for American Express.
	 */
	public void testGoodAmex() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "AMEX");
		
		
		form.setParameter("creditCardNumber", "343570480641495");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "377199947956764");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "344558915054011");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");

		
	}
	
	
	public void testBadAmex() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "AMEX");
		
		
		form.setParameter("creditCardNumber", "5593090746812380"); // Legit Mastercard
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "6437693863890467");
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Number]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
	}
	
	public void testEmptyTypeEmptyNumber() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "");
		
		
		form.setParameter("creditCardNumber", "");
		wr = form.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
	}
	
	
	public void testEmptyTypeFilledNumber() throws Exception {
		// login patient 2
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		// click on My Demographics
		wr = wr.getLinkWith("My Demographics").click();
		assertLogged(TransactionType.DEMOGRAPHICS_VIEW, 1L, 1L, "");
		
		WebForm form = wr.getForms()[0];
		form.setParameter("creditCardType", "");
		
		
		form.setParameter("creditCardNumber", "5593090746812380"); // Legit Mastercard
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Type]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");
		
		form.setParameter("creditCardNumber", "6437693863890467"); // Not Legit anything
		wr = form.submit();
		assertTrue(wr.getText().contains("not properly filled in: [Credit Card Type]"));
		assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 1L, 1L, "");

		
	}
	
}
