package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class ViewEmergencyReportTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.ndCodes();
		gen.hospitals();
		gen.hcp1();
		gen.hcp2();
		gen.hcp3();
		gen.er4();
		gen.patient9();
		gen.UC32Acceptance();
		gen.clearLoginFailures();
	}
	//9000000006
	public void testGenerateReport() throws Exception {
		
		WebConversation wc = login("9000000006", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - ER Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000006L, 0L, "");
		
		wr = wr.getLinkWith("Emergency Patient Report").click();


		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "99");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		
        assertTrue(wr.getText().contains("Darryl Thompson"));

        wr = wr.getLinkWith("Logout").click();
        assertTrue(wr.getText().contains("patient-centered"));
        
        wc = login("9900000000", "pw");
        wr = wc.getCurrentPage();
        
        assertEquals("iTrust - HCP Home", wr.getTitle());
        wr = wr.getLinkWith("Email History").click();
        assertTrue(wr.getText().contains("tarehart@iTrust.org"));
        assertLogged(TransactionType.EMERGENCY_REPORT_VIEW, 9000000006L, 99L, "");
        
	}
}
