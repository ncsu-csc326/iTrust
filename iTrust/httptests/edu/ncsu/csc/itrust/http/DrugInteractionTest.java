package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class DrugInteractionTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
	}

	/*
	 * Authenticate admin 90000000001
	 * Choose "Edit ND Codes"
	 * Choose "Edit Interactions"
	 * Choose "Adefovir" as one drug
	 * Choose "Aspirin" as the other drug
	 * Enter "May increase the risk and severity of nephrotoxicity due to additive effects on the kidney."
	 * Click submit
	 */
	public void testRecordDrugInteraction() throws Exception {
		gen.ndCodes1();
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Add HCP
		wr = wr.getLinkWith("Edit ND Codes").click();
		// add the hcp
		WebForm form = wr.getForms()[0];
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		form.getButtonWithID("editInt").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit ND Code Interactions", wr.getTitle());
		@SuppressWarnings("unused")
		WebForm intForm = wr.getForms()[0];
		/*intForm.getScriptableObject().setParameterValue("drug1", "61958-0501\n				Adefovir\n			");
		intForm.getScriptableObject().setParameterValue("drug2", "08109-6\n				Aspirin\n			");
		intForm.getScriptableObject().setParameterValue("description", "May increase the risk and severity of nephrotoxicity due to additive effects on the kidney.");
		intForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Interaction recorded successfully"));
		assertLogged(TransactionType.DRUG_INTERACTION_ADD, 9000000001L, 0L, "Drug");*/

	}
	
	/*
	 * Authenticate admin 90000000001
	 * Choose "Edit ND Codes"
	 * Choose "Tetracycline"
	 * Choose "Isotretinoin" interaction
	 * Click delete
	 */
	public void testDeleteDrugInteraction() throws Exception {
		HttpUnitOptions.setExceptionsThrownOnScriptError( false ); 
		gen.ndCodes2();
		gen.drugInteractions();
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		// Click Tetracycline
		wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Tetracycline").click();
		assertEquals(wr.getElementWithID("intDesc").getAttribute("visibility"), "");
		
		
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("codeToDelete", "548680955");
		form.getSubmitButton("delete").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Interaction deleted successfully"));
		assertLogged(TransactionType.DRUG_INTERACTION_DELETE, 9000000001L, 0L, "Drug");
		
	}
	
	/*
	 * Authenticate admin 90000000001
	 * Choose "Edit ND Codes"
	 * Choose "Edit Interactions"
	 * Choose "Adefovir" as both drug1 and drug2
	 * Enter "Mixing this drug with itself will cause the person taking it to implode."
	 * Click submit
	 */
	public void testRecordDrugInteractionSameDrugs() throws Exception {
		gen.ndCodes1();
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Add HCP
		wr = wr.getLinkWith("Edit ND Codes").click();
		// add the hcp
		WebForm form = wr.getForms()[0];
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit ND Code Interactions", wr.getTitle());
		/*WebForm intForm = wr.getForms()[0];
		intForm.getScriptableObject().setParameterValue("drug1", "61958-0501\n				Adefovir\n			");
		intForm.getScriptableObject().setParameterValue("drug2", "61958-0501\n				Adefovir\n			");
		intForm.getScriptableObject().setParameterValue("description", "Mixing this drug with itself will cause the person taking it to implode.");
		intForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Interactions can only be recorded between two different drugs"));
		assertNotLogged(TransactionType.DRUG_INTERACTION_EDIT, 9000000001L, 0L, "Drug");*/
	}
	
	public void testAddNewOverrideReason() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit OR Codes
		wr = wr.getLinkWith("Edit Override Reason Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain Override Reason Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("description", "Interaction not applicable to this patient");
		form.setParameter("code", "22222");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editORCodes"));
		assertTrue(wr.getText().contains("Success: 22222 - Interaction not applicable to this patient added"));
		assertLogged(TransactionType.OVERRIDE_CODE_ADD, 9000000001L, 0L, "");
	}
	
	public void testEditOverrideReason() throws Exception {
		gen.ORCodes();
		
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit OR Codes
		wr = wr.getLinkWith("Edit Override Reason Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain Override Reason Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code", "00001");
		form.setParameter("description", "Alerted interaction not super duper significant");
		form.getSubmitButtons()[1].click();
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editORCodes"));
		assertTrue(wr.getText().contains("Success"));
		assertLogged(TransactionType.OVERRIDE_CODE_EDIT, 9000000001L, 0L, "");
	
	}
}