package edu.ncsu.csc.itrust.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.meterware.httpunit.TableRow;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.DiagnosesDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PrescriptionsDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
/**
 *  test PrescriptionRefactoring
 */
public class PrescriptionRefactoringUseCaseTest extends iTrustHTTPTest {
	
	
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * setUp
	 */
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	/*private String dateString(String d) {
		SimpleDateFormat("MM/dd/yyyy").format()
	}*/
	
	private long getVisitID(WebResponse wr) throws Exception {
		WebForm form = wr.getFormWithID("mainForm");
		String ovIDStr = form.getParameterValue("ovID");
		return Long.parseLong(ovIDStr);
	}
	
	private String todayOffsetStr(int offset) {
		return dateOffsetStr(new Date(), offset);
	}
	
	private String dateOffsetStr(Date date, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, offset);
		return dateFormat.format(cal.getTime());
	}
	/**
	 *  test acceptance scenario1
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testAcceptanceScenario1() throws Exception {
		gen.hcp4();
		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		form.setParameter("visitDate", dateString);
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "Showing signs of dehydration.");
		form.getButtonWithID("update").click();
		
		// Check that it was created
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000004L, 1L, "Office visit");
		
		// Check database.
		long ovid = getVisitID(wr);
		OfficeVisitDAO ovDAO = factory.getOfficeVisitDAO();
		assertTrue(ovDAO.checkOfficeVisitExists(ovid, 1));
		OfficeVisitBean ovbean = ovDAO.getOfficeVisit(ovid);
		assertEquals("2", ovbean.getHospitalID());
		assertEquals("Showing signs of dehydration.", ovbean.getNotes());
		assertEquals(dateString, ovbean.getVisitDateStr());
		
		// Add diagnosis 
		form = wr.getFormWithID("diagnosisForm");
		form.setParameter("ICDCode", "79.10");
		form.getButtonWithID("add_diagnosis").click();
		
		// Check page and database.
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("information successfully updated"));
		DiagnosesDAO diagDAO = factory.getDiagnosesDAO();
		assertEquals(1, diagDAO.getList(ovid).size());
		DiagnosisBean diagBean = diagDAO.getList(ovid).get(0);
		assertEquals(diagBean.getICDCode(), "79.10");
	}
	/**
	 *  test acceptance scenario2
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testAcceptanceScenario2() throws Exception {
		gen.officeVisit8();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// Select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());

		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		/*WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		wr.getLinkWith("02/01/2011").click();

		assertEquals("iTrust - Document Office Visit", wr.getTitle());*/
		
		// Add the information to the form
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "01/01/2011");
		form.setParameter("hospitalID", "3");
		form.setParameter("notes", "Complains of acute fatigue.");
		form.getButtonWithID("update").click();
		
		// Check that it was created
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 2L, "Office visit");
		
		// add a prescription
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "081096");
		form.setParameter("dosage", "200"); 
		form.setParameter("startDate", "02/01/2011"); 
		form.setParameter("endDate", "02/15/2011"); 
		form.setParameter("instructions", "Take every six hours with food.");
		wr = form.submit();

		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 2L, "");
		
		// there was an allergy warning
		assertTrue(wr.getText().contains("Allergy: Aspirin"));
		form = wr.getFormWithID("prescriptionForm");
		form.getButtonWithID("cancel").click();
		
		// remain on the same page
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 2L, "");
		
		PrescriptionsDAO pDAO = factory.getPrescriptionsDAO();
		long ovid = getVisitID(wr);
		assertEquals(0, pDAO.getList(ovid).size());
	}
	/**
	 *  test acceptance scenario3
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testAcceptanceScenario3() throws Exception {
		gen.officeVisit5();
		
		WebConversation wc = login("9000000003", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// Select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "5");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		String dateString = todayOffsetStr(-1);
		wr.getLinkWith(dateString).click();
		
		// Check that the page contains the existing office visit
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());

		form = wr.getFormWithID("mainForm");
		assertEquals(dateString, form.getParameterValue("visitDate"));
		assertEquals("3", form.getParameterValue("hospitalID"));
		assertEquals("Hates getting shots", form.getParameterValue("notes"));
		
		WebTable tbl = wr.getTableWithID("labProceduresTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("13495-7"));

		tbl = wr.getTableWithID("immunizationsTable");
		rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Measles, Mumps, Rubella"));
		assertTrue(rows[3].getText().contains("Hepatitis B"));
		assertTrue(rows[4].getText().contains("Poliovirus"));
		
		// Add a new prescription.
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "664662530");
		form.setParameter("dosage", "150"); 
		form.setParameter("startDate", todayOffsetStr(0)); 
		form.setParameter("endDate", todayOffsetStr(21)); 
		form.setParameter("instructions", "Take once daily with water.");
		wr = form.submit();
		
		// Check page and database.
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("information successfully updated"));
		PrescriptionsDAO preDAO = factory.getPrescriptionsDAO();
		assertEquals(1, preDAO.getList(380).size());
		PrescriptionBean preBean = preDAO.getList(380).get(0);
		assertEquals(preBean.getMedication().getNDCode(), "664662530");
	}
	/**
	 *  test acceptance scenario4
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testAcceptanceScenario4() throws Exception {
		gen.officeVisit6();
		gen.hcp4();
		gen.ndCodes3();

		WebConversation wc = login("9000000004", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		String dateString = "02/02/2011";
		wr.getLinkWith(dateString).click();
		
		// Check that the page contains the existing office visit
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertEquals(390, getVisitID(wr));

		form = wr.getFormWithID("mainForm");
		assertEquals(dateString, form.getParameterValue("visitDate"));
		assertEquals("2", form.getParameterValue("hospitalID"));
		assertEquals("Second medical visit in two days", form.getParameterValue("notes"));

		WebTable tbl = wr.getTableWithID("prescriptionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Aspirin"));
		
		// Add a new prescription.
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "678771191");
		form.setParameter("dosage", "400"); 
		form.setParameter("startDate", "02/02/2011"); 
		form.setParameter("endDate", "02/16/2011"); 
		form.setParameter("instructions", "Take once daily");
		wr = form.submit();
		
		// Verify that warning occurred
		assertTrue(wr.getText().contains("Currently Prescribed: Aspirin"));
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("overrideCode", "00006");
		form.getButtonWithID("continue").click();

		// Check page and database.
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("information successfully updated"));
		PrescriptionsDAO preDAO = factory.getPrescriptionsDAO();
		assertEquals(2, preDAO.getList(390).size());
		PrescriptionBean preBean = preDAO.getList(390).get(1);
		assertEquals("678771191", preBean.getMedication().getNDCode());
	}
	/**
	 *  test acceptance scenario5
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testAcceptanceScenario5() throws Exception {
		gen.officeVisit7();
		gen.hcp5();
		gen.ndCodes3();
		
		WebConversation wc = login("9000000005", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// Select the office visit from yesterday
		String dateString = "01/26/2011";
		wr.getLinkWith(dateString).click();
		
		// Check that the page contains the existing office visit
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertEquals(395, getVisitID(wr));
		
		WebTable tbl = wr.getTableWithID("prescriptionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Ibuprofen"));
		
		// edit the prescription
		wr = tbl.getTableCell(2, 0).getLinkWith("Ibuprofen").click();
		assertEquals("iTrust - Edit Prescription Information", wr.getTitle());
		form = wr.getForms()[0];
		form.setParameter("dosage", "1000");
		form.setParameter("instructions", "Take as many as you want");
		wr = form.submit();
		
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("information successfully updated"));
		PrescriptionsDAO preDAO = factory.getPrescriptionsDAO();
		assertEquals(1, preDAO.getList(395).size());
		PrescriptionBean preBean = preDAO.getList(395).get(0);
		assertEquals("678771191", preBean.getMedication().getNDCode());
		assertEquals(1000, preBean.getDosage());
		assertEquals("Take as many as you want", preBean.getInstructions());
		
		tbl = wr.getTableWithID("prescriptionsTable");
		assertEquals("Ibuprofen (678771191)", tbl.getTableCell(2, 0).getText());
		assertEquals("1000mg", tbl.getTableCell(2, 1).getText());
		assertEquals("Take as many as you want", tbl.getTableCell(2, 3).getText());
	}
	/**
	 *  test Prescription logging
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	
	public void testEditPrescriptionLogging() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		String dateString = "02/02/2011";
		form.setParameter("visitDate", dateString);
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "It's a sunny day.");
		form.getButtonWithID("update").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 1L, "");
		assertNotLogged(TransactionType.PRESCRIPTION_EDIT, 9000000000L, 1L, "");
		assertNotLogged(TransactionType.PRESCRIPTION_REMOVE, 9000000000L, 1L, "");
		
		// add a prescription
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "081096");
		form.setParameter("dosage", "200"); 
		form.setParameter("startDate", "02/02/2011"); 
		form.setParameter("endDate", "02/09/2011"); 
		form.setParameter("instructions", "Take daily with water.");
		wr = form.submit();
		
		assertLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 1L, "");

		// edit the prescription
		WebTable tbl = wr.getTableWithID("prescriptionsTable");
		wr = tbl.getTableCell(2, 0).getLinkWith("Aspirin").click();
		assertEquals("iTrust - Edit Prescription Information", wr.getTitle());
		form = wr.getForms()[0];
		form.setParameter("dosage", "400");
		wr = form.submit();

		assertLogged(TransactionType.PRESCRIPTION_EDIT, 9000000000L, 1L, "");
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// remove the prescription
		tbl = wr.getTableWithID("prescriptionsTable");
		TableRow[] rows = tbl.getRows();
		assertTrue(rows[2].getText().contains("Aspirin"));
		wr = tbl.getTableCell(2, 5).getLinkWith("Remove").click();

		assertLogged(TransactionType.PRESCRIPTION_REMOVE, 9000000000L, 1L, "");
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
	}
	/**
	 *  test illegal characters
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testIllegalCharacters() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// try to add a new office visit
		form = wr.getFormWithID("mainForm");
		String dateString = "02/02/2011";
		form.setParameter("visitDate", dateString);
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "&ampersand&");
		form.getButtonWithID("update").click();

		// check for error message
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Input not valid"));
		assertNotLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000000L, 1L, "");
	}
	/**
	 *  test Prescrition instructions
	 *  throws Exception
	 *   
	 *   
	 *
	 */
	public void testPrescriptionNoInstructions() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		String dateString = "02/02/2011";
		form.setParameter("visitDate", dateString);
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "It's a sunny day.");
		form.getButtonWithID("update").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 1L, "");
		
		// add a prescription, but without instructions
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "081096");
		form.setParameter("dosage", "200"); 
		form.setParameter("startDate", "02/02/2011"); 
		form.setParameter("endDate", "02/09/2011");
		wr = form.submit();

		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 1L, "");
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Instructions: Up to 300 alphanumeric characters, with space, and other punctuation"));
	}

	/**
	 * testEditPrescriptionNoOverrideReason
	 * @throws Exception
	 */
	public void testEditPrescriptionNoOverrideReason() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		
		// select the patient
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		String dateString = "02/02/2011";
		form.setParameter("visitDate", dateString);
		form.setParameter("hospitalID", "2");
		form.setParameter("notes", "It's a sunny day.");
		form.getButtonWithID("update").click();

		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		assertNotLogged(TransactionType.PRESCRIPTION_ADD, 9000000000L, 1L, "");
		
		// add a prescription, but without instructions
		form = wr.getFormWithID("prescriptionForm");
		form.setParameter("medID", "664662530");
		form.setParameter("dosage", "200"); 
		form.setParameter("startDate", "02/02/2011"); 
		form.setParameter("endDate", "02/09/2011");
		form.setParameter("instructions", "Take every day");
		wr = form.submit();
		
		assertTrue(wr.getText().contains("Allergy: Penicillin"));
		form = wr.getFormWithID("prescriptionForm");
		form.getButtonWithID("continue").click();

		// The allergy warning is still displayed
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Allergy: Penicillin"));
	}
	
	/**
	 * testReasonCodesAddNew
	 */
	public void testReasonCodesAddNew() {
		
	}
}
