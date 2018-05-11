package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebTable;
//import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class PrescriptionInstructionsTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
		gen.ndCodes();
		gen.patient1();
		gen.patient2();
		gen.patient4();
		
	}
	
	public void testUC11() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("06/10/2007").click();
		assertLogged(TransactionType.OFFICE_VISIT_VIEW, 9000000000L, 2L, "Office visit");
		
		/* If you fail here, you need to make the first column a link */
		assertTrue("First column in prescription table must be a link",
				wr.getLinkWith("Tetracycline (009042407)") != null); 
		
		wr = wr.getLinkWith("Tetracycline (009042407)").click();
		
		/* The rest of this test must be written once an instruction-modifying
		 * jsp has been spec'd out.
		 */
		
		assertTrue(wr.getText().contains("Edit Prescription Instructions"));
		
		WebForm wf = wr.getForms()[0];
		
		assertEquals("5", wf.getParameterValue("dosage"));
		assertEquals("Take twice daily", wf.getParameterValue("instructions"));
		
		wf.setParameter("dosage", "10");
		wf.setParameter("instructions", "Take thrice daily");
		
		wr = wf.submit();
		
//		assertTrue(wr.getText().contains("Prescription Updated!"));

		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("information successfully updated"));
		
		WebTable tbl = wr.getTableWithID("prescriptionsTable");
		assertEquals("10mg", tbl.getTableCell(2, 1).getText());
		assertEquals("Take thrice daily", tbl.getTableCell(2, 3).getText());
		
		/*WebTable wt = wr.getTableStartingWith("Prescription Updated!");
		assertEquals("10mg", wt.getTableCell(2, 1).getText());
		assertEquals("Take thrice daily", wt.getTableCell(2, 3).getText());*/
		assertLogged(TransactionType.PRESCRIPTION_EDIT, 9000000000L, 2L, "");
	}
	
	
	public void testUC19() throws Exception {
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Prescription Records").click();
		assertEquals("iTrust - Get My Prescription Report", wr.getTitle());
		WebForm viewForm = wr.getForms()[0];
		viewForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.PRESCRIPTION_REPORT_VIEW, 2L, 2L, "");
		
		assertTrue(wr.getText().contains("Prioglitazone"));
		
		/* If you fail here, you need to make the first column a link */
		assertTrue("First column in prescription table must be a link", 
				wr.getLinkWith("64764-1512") != null); 
		
		wr = wr.getLinkWith("64764-1512").click();
		
		/* The rest of this test must be written once an instruction-modifying
		 * jsp has been spec'd out.
		 */
		

		assertTrue(wr.getText().contains("Prescription Information"));
		
		WebTable wt = wr.getTableStartingWith("Prescription Information");
		assertEquals("5mg", wt.getTableCell(2, 2).getText());
		assertEquals("Take twice daily", wt.getTableCell(2, 4).getText());
	}
	
	
	
	/* We have determined that Theme 1 does not apply to UC29 because
	 * UC29 displays a list of medications, not a list of prescriptions.
	 * The difference is that medications do not store any information about
	 * dosage, instructions, or particular dates or office visits.
	 * 
	 * Theme 1 says we should add functionality whenever the user is viewing
	 * a list of prescriptions, and this is not a list of prescriptions.
	 * 
	 * Furthermore, we may imply from the use of medication beans instead
	 * of prescription beans that a conscious choice was made to avoid
	 * divulging specific prescription information in this use case, so
	 * it would not be prudent to change that.
	 */
	
	
	
	
	public void testUC29() throws Exception {
		
		gen.standardData();
		gen.patient_hcp_vists();
		gen.hcp_diagnosis_data();
		
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("My Diagnoses").click();
		assertEquals("iTrust - My Diagnoses", wr.getTitle());
		assertLogged(TransactionType.DIAGNOSES_LIST_VIEW, 1L, 1L, "");
		
		wr = wr.getLinkWith("Echovirus(79.10)").click();
			
		WebTable table = wr.getTableStartingWithPrefix("HCP");
        
		assertTrue(table.getCellAsText(1, 0).contains("Jason"));
		assertTrue(table.getCellAsText(1, 1).contains("2"));
		assertTrue(table.getCellAsText(2, 0).contains("Lauren"));
		assertTrue(table.getCellAsText(2, 1).contains("1"));
	}
	
	
	public void testUC31() throws Exception {
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("My Expired Prescription Reports").click();
		assertEquals("iTrust - Get My Expired Prescription Reports", wr.getTitle());

		assertTrue(wr.getText().contains("Tetracycline"));
		
		/* If you fail here, you need to make the first column a link */
		assertTrue("First column in prescription table must be a link", 
				wr.getLinkWith("00904-2407") != null); 
		
		wr = wr.getLinkWith("00904-2407").click();
		
		/* The rest of this test must be written once an instruction-modifying
		 * jsp has been spec'd out.
		 */
		
		assertTrue(wr.getText().contains("Prescription Information"));
		
		WebTable wt = wr.getTableStartingWith("Prescription Information");
		assertEquals("5mg", wt.getTableCell(2, 2).getText());
		assertEquals("Take twice daily", wt.getTableCell(2, 4).getText());
		assertLogged(TransactionType.EXPIRED_PRESCRIPTION_VIEW, 2L, 2L, "");
	}
}
