/**
 * 
 */
package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.beans.BillingBean;

/**
 * Test for enhancements for UC 60. These are all the black box tests I added
 * for UC60 enhancements.
 */
public class InsuranceNotificationTest extends iTrustHTTPTest {
	
	//MIDs for various people.
	private static final long JOHN_SMITH = 313;
	private static final long JUAN_CARLOS = 315;
	private static final long ALEX_PAUL = 316;
	
	private static final long ROGER_KING = 9000000015L;
	private static final long JANE_SMITH = 9000000014L;
	private static final long MIKE_JONES = 9000000012L;
	
	//password for users.
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
	public void testCantViewSubmitted() throws Exception{
		//Login as Alex Paul
		WebConversation wc = login("" + ALEX_PAUL, PW);
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		wr = wr.getLinkWith("01/25/2014").click();
		assertTrue(wr.getText().contains("Insurance"));

		//Pay with a credit card.
		WebForm pay = wr.getForms()[0];
		pay.setParameter("type", "CC");
		pay.setParameter("ccNumber", "343570480641495");
		pay.setParameter("ccType", "AmericanExpress");
		pay.setParameter("ccHolder", "Alex Paul");
		pay.setParameter("billAddress", "206 Crest Road, Raleigh, NC 27606");
		pay.setParameter("cvv", "0123");
		wr = pay.submit();
		
		wr = wr.getLinkWith("Logout").click();
		
		//Login as UAP Roger King
		wc = login("" + ROGER_KING, PW);
		wr = wc.getCurrentPage();
		//Make sure that you cant see the statement.
		assertTrue(wr.getText().contains("No Pending Insurance Claims."));
		wr = wr.getLinkWith("View Insurance Claims").click();
		
		assertTrue(wr.getText().contains("No claims to display."));
	}
	
	public void testClaimNotification() throws Exception{
		//Login as John Smith
		WebConversation wc = login("" + JOHN_SMITH, PW);
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Shelly Vang"));
		wr = wr.getLinkWith("01/10/2014").click();
		//Make an insurance claim.
		assertTrue(wr.getText().contains("$150"));
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("General Checkup"));
		wr = submitPayment(wr, "John Smith", "1234567A01", "ABC Insurance", "365 Broad St", "", "Raleigh", "NC", "27606", "919-112-8234");
		wr.getLinkWith("Logout").click();
		
		//Login as UAP Mike Jones
		wc = login("" + MIKE_JONES, PW);
		wr = wc.getCurrentPage();
		//Make sure the notifications are correct.
		assertTrue(wr.getText().contains("inboxUnread.png"));
		assertTrue(wr.getText().contains("1</a> Pending Insurance Claim."));
		wr = wr.getLinkWith("1").click();
		assertEquals("iTrust - View Insurance Claims", wr.getTitle());
	}
	public void testApprovalNotification() throws Exception{
		//Login as John Smith
		WebConversation wc = login("" + JOHN_SMITH, PW);
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Shelly Vang"));
		wr = wr.getLinkWith("01/10/2014").click();
		//Make an insurance claim
		assertTrue(wr.getText().contains("$150"));
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("General Checkup"));
		wr = submitPayment(wr, "John Smith", "1234567A01", "ABC Insurance", "365 Broad St", "", "Raleigh", "NC", "27606", "919-112-8234");
		wr.getLinkWith("Logout").click();
		
		//Login as UAP Mike Jones
		wc = login("" + MIKE_JONES, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View Insurance Claims").click();
		wr = wr.getLinkWith(new SimpleDateFormat("MM/dd/YYYY").format(new Date())).click();
		//Approve the claim
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Logout").click();
		
		//Login as John Smith
		wc = login("" + JOHN_SMITH, PW);
		wr = wc.getCurrentPage();
		//Check the notifications.
		assertTrue(wr.getText().contains("approved.png"));
		assertTrue(wr.getText().contains("1</a> approved insurance claim."));
		wr = wr.getLinkWith("1").click();
		assertEquals("iTrust - View My Bills", wr.getTitle());
		assertTrue(wr.getText().contains(BillingBean.APPROVED));
	}
	
	public void testDenialNotification() throws Exception{
		//Login as John Smith
		WebConversation wc = login("" + JOHN_SMITH, PW);
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Shelly Vang"));
		wr = wr.getLinkWith("01/10/2014").click();
		assertTrue(wr.getText().contains("$150"));
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("General Checkup"));
		//Make an insurance claim.
		wr = submitPayment(wr, "John Smith", "1234567A01", "ABC Insurance", "365 Broad St", "", "Raleigh", "NC", "27606", "919-112-8234");
		wr.getLinkWith("Logout").click();
		
		//Login as UAP Mike Jones
		wc = login("" + MIKE_JONES, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("View Insurance Claims").click();
		wr = wr.getLinkWith(new SimpleDateFormat("MM/dd/YYYY").format(new Date())).click();
		//Deny the claim
		wr.getForms()[0].getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr.getLinkWith("Logout").click();
		
		//Login as John Smith
		wc = login("" + JOHN_SMITH, PW);
		wr = wc.getCurrentPage();
		//Make sure the icon and the notification are correct.
		assertTrue(wr.getText().contains("denied.png"));
		assertTrue(wr.getText().contains("1</a> denied insurance claim."));
		wr = wr.getLinkWith("1").click();
		assertEquals("iTrust - View My Bills", wr.getTitle());
		assertTrue(wr.getText().contains(BillingBean.DENIED));
	}
	public void testMultiplePatients() throws Exception{
		//Login as John Smith
		WebConversation wc = login("" + JOHN_SMITH, PW);
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Shelly Vang"));
		wr = wr.getLinkWith("01/10/2014").click();
		assertTrue(wr.getText().contains("$150"));
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("General Checkup"));
		//Make an insurance claim
		wr = submitPayment(wr, "John Smith", "1234567A01", "ABC Insurance", "365 Broad St", "", "Raleigh", "NC", "27606", "919-112-8234");

		//Login as Juan Carlos
		wc = login("" + JUAN_CARLOS, PW);
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Bills").click();
		assertTrue(wr.getText().contains("Shelly Vang"));
		wr = wr.getLinkWith("02/07/2014").click();
		assertTrue(wr.getText().contains("$350"));
		assertTrue(wr.getText().contains("Facebook Rehab Center"));
		assertTrue(wr.getText().contains("Ultrasound"));
		//Make an insurance claim
		wr = submitPayment(wr, "Juan Carlos", "9871932F25", "LZA Insurance", "222 Noname Dr", "", "Raleigh", "NC", "27604", "919-222-6579");
		
		//Login as UAP Jane Smith
		wc = login("" + JANE_SMITH, PW);
		wr = wc.getCurrentPage();
		//Make sure that there are the proper number of claims and notifications.
		assertTrue(wr.getText().contains("2</a> Pending Insurance Claims."));
		wr = wr.getLinkWith("View Insurance Claims").click();
		int idx = wr.getText().indexOf(new SimpleDateFormat("MM/dd/YYYY").format(new Date()));
		assertTrue(wr.getText().substring(0, idx+11).contains(new SimpleDateFormat("MM/dd/YYYY").format(new Date())));
		assertTrue(wr.getText().substring(idx+1).contains(new SimpleDateFormat("MM/dd/YYYY").format(new Date())));
	}
}
