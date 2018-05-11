package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 29
 */
public class ExperiencedLHCPsUseCaseTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.patient_hcp_vists();
		gen.hcp_diagnosis_data();		
	}

	public void testViewDiagnoses() throws Exception {		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("My Diagnoses").click();
		assertEquals("iTrust - My Diagnoses", wr.getTitle());
		assertLogged(TransactionType.DIAGNOSES_LIST_VIEW, 1L, 1L, "");
		
		assertTrue(wr.getText().contains("Echovirus(79.10)"));
		assertTrue(wr.getText().contains("Acute Lycanthropy(250.00)"));
	}
	
	public void testViewDiagnosisEchoVirus() throws Exception {
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("My Diagnoses").click();
		assertEquals("iTrust - My Diagnoses", wr.getTitle());
		assertLogged(TransactionType.DIAGNOSES_LIST_VIEW, 1L, 1L, "");
		
		wr = wr.getLinkWith("Echovirus(79.10)").click();
		assertLogged(TransactionType.EXPERIENCED_LHCP_FIND, 1L, 0L, "");	
	}
	
	public void testViewHCPDetails() throws Exception {
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("My Diagnoses").click();
		assertEquals("iTrust - My Diagnoses", wr.getTitle());
		assertLogged(TransactionType.DIAGNOSES_LIST_VIEW, 1L, 1L, "");
		
		wr = wr.getLinkWith("Echovirus(79.10)").click();
		wr = wr.getLinkWith("Jason Frankenstein").click();

		assertEquals("iTrust - View Personnel Details", wr.getTitle());
		assertTrue(wr.getText().contains("Jason Frankenstein"));	
		assertLogged(TransactionType.PERSONNEL_VIEW, 1L, 9000000004L, "");
	}
}
