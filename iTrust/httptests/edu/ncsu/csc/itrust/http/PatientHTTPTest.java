package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class PatientHTTPTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testChangePassword() throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");

		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 2L, 2L, "");
		
		assertEquals("iTrust - Login", wr.getTitle());
		wr = wr.getLinkWith("Reset Password").click();

		WebForm wf = wr.getForms()[1];
		wf.setParameter("role", "patient");
		wf.setParameter("mid", "2");
		wr = wf.submit();
		
		wf = wc.getCurrentPage().getForms()[1];
		wf.setParameter("answer", "good");
		wf.setParameter("password", "password2");
		wf.setParameter("confirmPassword", "password2");
		wr = wf.submit();
		
		assertTrue(wr.getText().contains("Password changed"));
		assertLogged(TransactionType.PASSWORD_RESET, 2L, 2L, "");
		
		wc = login("2", "pw");
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Failed login"));
		
		wc = login("2", "password2");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		assertTrue(wr.getTitle().equals("iTrust - Patient Home"));
	}
	
	public void testViewPrescriptionRecords1() throws Exception {
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("Prescription Records").click();
		assertEquals("iTrust - Get My Prescription Report", wr.getTitle());
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("<i>No prescriptions found</i>"));
		assertLogged(TransactionType.PRESCRIPTION_REPORT_VIEW, 1L, 1L, "");
	}
	
	public void testViewPrescriptionRecords2() throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Prescription Records").click();
		assertEquals("iTrust - Get My Prescription Report", wr.getTitle());
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.PRESCRIPTION_REPORT_VIEW, 2L, 2L, "");
		WebTable wt = wr.getTableStartingWith("Andy Programmer");
//		assertEquals("00904-2407", wt.getCellAsText(2, 0));
//		assertEquals("Tetracycline", wt.getCellAsText(2, 1));
//		assertEquals("10/10/2006 to 10/11/2006", wt.getCellAsText(2, 2));
//		assertEquals("Kelly Doctor", wt.getCellAsText(2, 3));
//		assertEquals("00904-2407", wt.getCellAsText(3, 0));
//		assertEquals("Tetracycline", wt.getCellAsText(3, 1));
//		assertEquals("10/10/2006 to 10/11/2006", wt.getCellAsText(3, 2));
//		assertEquals("Kelly Doctor", wt.getCellAsText(3, 3));
		assertEquals("64764-1512", wt.getCellAsText(2, 0));
		assertEquals("Prioglitazone", wt.getCellAsText(2, 1));
		assertEquals("10/10/2006 to 10/11/2020", wt.getCellAsText(2, 2));
		assertEquals("Kelly Doctor", wt.getCellAsText(2, 3));
	}
	
	public void testCodeInjection() throws Exception {
		WebConversation wc = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		WebResponse wr = wc.getResponse(ADDRESS + "auth/patient/myDiagnoses.jsp?icd=%3Cscript%3Ewindow.location=%22http://bit.ly/4kb77v%22%3C/script%3E");
		assertFalse(wr.getText().contains("RickRoll'D"));
		assertTrue(wr.getTitle().contains("iTrust - My Diagnoses"));
		assertLogged(TransactionType.DIAGNOSES_LIST_VIEW, 2L, 2L, "");
	}
}
