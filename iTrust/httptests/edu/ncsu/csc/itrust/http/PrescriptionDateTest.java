package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 */
public class PrescriptionDateTest extends iTrustHTTPTest 
{

	/**
	 * setUP
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
		gen.ndCodes();
		gen.patient1();
		gen.patient2();
		gen.patient4();
	}
	
	/**
	 * testeditOVPPrescription
	 * @throws Exception
	 */
	public void testeditOVPPrescription() throws Exception
	{
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
		
		WebForm form = wr.getFormWithID("prescriptionForm");
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		//assertTrue(wr.getText().contains("Prescription Information"));
		
		form.setParameter("medID", "009042407");
		form.setParameter("dosage", "5");
		form.setParameter("startDate", "10/12/13");
		form.setParameter("endDate", "10/01/13");
		form.setParameter("instructions", "Take thrice daily");
		
        wr = form.submit();
		
//		assertTrue(wr.getText().contains("Prescription Updated!"));

		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("Information not valid"));
		
		
		
		//WebTable tbl = wr.getTableWithID("prescriptionsTable");
		
		
	}

}
