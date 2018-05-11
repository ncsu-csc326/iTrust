package edu.ncsu.csc.itrust.http;

public class ConsultationTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.ndCodes();
		gen.hospitals();
		gen.hcp0();
		gen.hcp3();
		gen.patient1();
		gen.patient2();
		gen.patient5();
		
		gen.clearLoginFailures();
	}

	/*
	 * 
	 */
	public void testSubmitAndReceiveConsultation() throws Exception {
/*		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Consultations").click();
		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Send a Consultation"));
		
		
		wr.getForms()[0].setParameter("patient", "5");
		wr.getForms()[0].setParameter("hcp", "9000000003");
		wr = wr.getForms()[0].submit();
		
		assertTrue(wr.getText().contains("Consultation Form"));
		
		wr.getForms()[0].setParameter("msg", "Test1");
		wr = wr.getForms()[0].submit();
		assertLogged(TransactionType.CONSULTATION_REFERRAL_CREATE, 9000000000L, 5L, "");
		
		assertTrue(wr.getText().contains("Thank you, your Consultation Request was sent."));

		assertTrue(wr.getText().contains("Test1"));
		
		
		/*
		 * Time to receive the consultation.
		 */
/*		
		wc = login("9000000003", "pw");
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Consultations").click();
		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[1].click();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("View Pending Consultations"));
		
		assertTrue(wr.getText().contains("Kelly Doctor"));	
*/
	}
	
	
	
	
public void testSubmitAndEditConsultation() throws Exception {
	/*	
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Consultations").click();
		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Send a Consultation"));
		
		
		wr.getForms()[0].setParameter("patient", "5");
		wr.getForms()[0].setParameter("hcp", "9000000003");
		wr = wr.getForms()[0].submit();
		
		assertTrue(wr.getText().contains("Consultation Form"));
		
		wr.getForms()[0].setParameter("msg", "Test1");
		wr = wr.getForms()[0].submit();
		
		assertTrue(wr.getText().contains("Thank you, your Consultation Request was sent."));
		assertLogged(TransactionType.CONSULTATION_REFERRAL_CREATE, 9000000000L, 5L, "");

		assertTrue(wr.getText().contains("Test1"));
		
		wr = wr.getLinkWith("Consultations").click();
		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("View Pending Consultations"));
		
		assertTrue(wr.getText().contains("Baby Programmer (5)"));
*/
	}


	public void testReceiveAndEditConsultation() throws Exception {
/*		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Consultations").click();
		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Send a Consultation"));
		
		
		wr.getForms()[0].setParameter("patient", "5");
		wr.getForms()[0].setParameter("hcp", "9000000003");
		wr = wr.getForms()[0].submit();
		
		assertTrue(wr.getText().contains("Consultation Form"));
		
		wr.getForms()[0].setParameter("msg", "Test3");
		wr = wr.getForms()[0].submit();
		
		assertTrue(wr.getText().contains("Thank you, your Consultation Request was sent."));
		assertLogged(TransactionType.CONSULTATION_REFERRAL_CREATE, 9000000000L, 5L, "");
	
		assertTrue(wr.getText().contains("Test3"));
		
		
		
		/*
		 * Time to receive the consultation.
		 */
	/*	
		wc = login("9000000003", "pw");
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Consultations").click();
		

		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("View Pending Consultations"));
		assertLogged(TransactionType.CONSULTATION_REFERRAL_VIEW, 9000000003L, 0L, "");
		
		assertTrue(wr.getText().contains("Baby Programmer (5)"));
			
		/*
		 * Time to review the consultation.
		 */
/*		
		wc = login("9000000000", "pw");
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Consultations").click();
		

		assertTrue(wr.getText().contains("HCP Consultations"));
		
		wr.getForms()[0].getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Pending"));	
*/	
	}
	
	


}
