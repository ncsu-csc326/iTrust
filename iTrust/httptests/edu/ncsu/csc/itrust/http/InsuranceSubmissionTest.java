package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.beans.BillingBean;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * A test of uc 60 enhancements. These are all the acceptance tests.
 */
public class InsuranceSubmissionTest extends iTrustHTTPTest {
	
	//MIDs for various users.
	private static final long JOHN_SMITH = 313;
	private static final long MARIA_LOPEZ = 314;
	private static final long JUAN_CARLOS = 315;
	private static final long ALEX_PAUL = 316;
	
	private static final long MIKE_JONES = 9000000012L;
	private static final long DANIEL_WILLIAMS = 9000000013L;
	private static final long JANE_SMITH = 9000000014L;
	private static final long ROGER_KING = 9000000015L;
	
	//Passwords
	private static final String PW = "pw";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.hospitals();
		gen.hospitals1();
		gen.hcp0();
		gen.uc51();
		gen.uc60();
	}

	private static WebResponse submitPayment(WebResponse wr, String holder, String id,
			String provider, String add1, String add2, String city,
			String state, String zip, String phone) throws Exception {
		WebForm pay = wr.getForms()[0];
		pay.setParameter("type", "Ins");
		pay.setParameter("insHolder", holder);
		pay.setParameter("insProvider", provider);
		pay.setParameter("insID", id);
		pay.setParameter("insAdd1", add1);
		pay.setParameter("insAdd2", add2);
		pay.setParameter("insCity", city);
		pay.setParameter("insState", state);
		pay.setParameter("insZip", zip);
		pay.setParameter("insPhone", phone);
		wr = pay.submit();
		return wr;
	}
	
	public void testUAPApproval() throws Exception{
		//Log in as John Smith
		WebConversation wc = login("" + JOHN_SMITH, PW);
		WebResponse wr = wc.getCurrentPage();
		//Pay a bill with insurance.
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Shelly Vang"));
		wr = wr.getLinkWith("01/10/2014").click();
		assertTrue(wr.getText().contains("$150"));
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("General Checkup"));
		wr = submitPayment(wr, "John Smith", "1234567A01", "ABC Insurance", "365 Broad St", "", "Raleigh", "NC", "27606", "919-112-8234");
		wr.getLinkWith("Logout").click();
		
		//Login as UAP Mike Jones
		wc = login("" + MIKE_JONES, PW);
		wr = wc.getCurrentPage();
		//Approve the claim
		wr = wr.getLinkWith("View Insurance Claims").click();
		wr = wr.getLinkWith(new SimpleDateFormat("MM/dd/YYYY").format(new Date())).click();
		wr.getForms()[0].getButtons()[0].click();
		assertLogged(TransactionType.UAP_INITIAL_APPROVAL, MIKE_JONES, MIKE_JONES, "");
		wr = wc.getCurrentPage();
		wr.getLinkWith("Logout").click();

		//Make sure the bill is approved.
		wc = login("" + JOHN_SMITH, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains(BillingBean.APPROVED));
	}
	public void testUAPDenialThenApproval() throws Exception{
		WebConversation wc = null;
		WebResponse wr = null;
		for(int i = 0; i < 2; i++){
			//Login as Maria Lopez
			wc = login("" + MARIA_LOPEZ, PW);
			wr = wc.getCurrentPage();
			wr = wr.getLinkWith("My Bills").click();
			assertTrue(wr.getText().contains("Kelly Doctor"));
			//Pay the bill
			wr = wr.getLinkWith("02/17/2014").click();
			assertTrue(wr.getText().contains("$250"));
			assertTrue(wr.getText().contains("Le Awesome Hospital"));
			assertTrue(wr.getText().contains("Mammogram"));
			wr = submitPayment(wr, "Maria Lopez", "4447157D13", "GMX Insurance", "113 Seaboard Ave", "", "Raleigh", "NC", "27604", "919-468-1537");
			wr.getLinkWith("Logout").click();
			
			//Login as UAP Daniel Williams
			wc = login("" + DANIEL_WILLIAMS, PW);
			wr = wc.getCurrentPage();
			wr = wr.getLinkWith("View Insurance Claims").click();
			//Deny / approve the claim
			wr = wr.getLinkWith(new SimpleDateFormat("MM/dd/YYYY").format(new Date())).click();
			wr.getForms()[0].getButtons()[1-i].click();
			if(i == 0)
				assertLogged(TransactionType.UAP_INITIAL_DENIAL, DANIEL_WILLIAMS, DANIEL_WILLIAMS, "");
			else if( i == 1 )
				assertLogged(TransactionType.UAP_SECOND_APPROVAL, DANIEL_WILLIAMS, DANIEL_WILLIAMS, "");
			wr = wc.getCurrentPage();
			wr.getLinkWith("Logout").click();
		}
		//Make sure the bill is approved
		wc = login("" + MARIA_LOPEZ, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains(BillingBean.APPROVED));
	}
	public void testTwoUAPDenials() throws Exception{
		WebConversation wc = null;
		WebResponse wr = null;
		for(int i = 0; i < 2; i++){
			//login as Juan Carlos.
			wc = login("" + JUAN_CARLOS, PW);
			wr = wc.getCurrentPage();
			wr = wr.getLinkWith("My Bills").click();
			assertTrue(wr.getText().contains("Shelly Vang"));
			//Pay the bill with insurance.
			wr = wr.getLinkWith("02/07/2014").click();
			assertTrue(wr.getText().contains("$350"));
			assertTrue(wr.getText().contains("Facebook Rehab Center"));
			assertTrue(wr.getText().contains("Ultrasound"));
			wr = submitPayment(wr, "Juan Carlos", "9871932F25", "LZA Insurance", "222 Noname Dr", "", "Raleigh", "NC", "27604", "919-222-6579");
			wr.getLinkWith("Logout").click();
			
			//Login as Jane Smith
			wc = login("" + JANE_SMITH, PW);
			wr = wc.getCurrentPage();
			wr = wr.getLinkWith("View Insurance Claims").click();
			wr = wr.getLinkWith(new SimpleDateFormat("MM/dd/YYYY").format(new Date())).click();
			//Deny the claim.
			wr.getForms()[0].getButtons()[1].click();
			wr = wc.getCurrentPage();
			if(i == 0)
				assertLogged(TransactionType.UAP_INITIAL_DENIAL, JANE_SMITH, JANE_SMITH, "");
			else if( i == 1 )
				assertLogged(TransactionType.UAP_SECOND_DENIAL, JANE_SMITH, JANE_SMITH, "");
			wr.getLinkWith("Logout").click();
		}
		
		//Login as Juan Carlos
		wc = login("" + JUAN_CARLOS, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		//Make sure that it is denied and you cant pay with insurance.
		assertTrue(wr.getText().contains(BillingBean.DENIED));
		wr = wr.getLinkWith("02/07/2014").click();
		assertFalse(wr.getText().contains("Insurance"));

		//Pay with a credit card.
		WebForm pay = wr.getForms()[0];
		pay.setParameter("type", "CC");
		pay.setParameter("ccNumber", "4539592576502361");
		pay.setParameter("ccType", "Visa");
		pay.setParameter("ccHolder", "Juan Carlos");
		pay.setParameter("billAddress", "412 Conifer Dr, Raleigh, NC 27606");
		pay.setParameter("cvv", "007");
		wr = pay.submit();
		
		assertTrue(wr.getText().contains("Payment Information"));
		
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains(BillingBean.SUBMITTED));
	}
	public void testUAPDenialThenCC() throws Exception{
		//Login as Alex Paul
		WebConversation wc = login("" + ALEX_PAUL, PW);
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Kelly Doctor"));
		wr = wr.getLinkWith("01/25/2014").click();
		//Pay the bill with insurance.
		assertTrue(wr.getText().contains("$250"));
		assertTrue(wr.getText().contains("Central Hospital"));
		assertTrue(wr.getText().contains("Colonoscopy"));
		wr = submitPayment(wr, "Alex Paul", "7772510K99", "YYY Insurance", "525 Grumpy Dr", "", "Raleigh", "NC", "21337", "919-555-9876");
		wr.getLinkWith("Logout").click();
		
		//Login as UAP Roger King
		wc = login("" + ROGER_KING, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View Insurance Claims").click();
		//View the claim and deny it.
		wr = wr.getLinkWith(new SimpleDateFormat("MM/dd/YYYY").format(new Date())).click();
		wr.getForms()[0].getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Logout").click();
		
		//Login as Alex Paul
		wc = login("" + ALEX_PAUL, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		//See the claim is denied, and pay it with a credit card.
		assertTrue(wr.getText().contains(BillingBean.DENIED));
		wr = wr.getLinkWith("01/25/2014").click();
		assertTrue(wr.getText().contains("Insurance"));

		WebForm pay = wr.getForms()[0];
		pay.setParameter("type", "CC");
		pay.setParameter("ccNumber", "343570480641495");
		pay.setParameter("ccType", "AmericanExpress");
		pay.setParameter("ccHolder", "Alex Paul");
		pay.setParameter("billAddress", "206 Crest Road, Raleigh, NC 27606");
		pay.setParameter("cvv", "0123");
		wr = pay.submit();
		
		assertTrue(wr.getText().contains("Payment Information"));
		
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains(BillingBean.SUBMITTED));
	}
}
