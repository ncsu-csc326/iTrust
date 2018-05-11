package edu.ncsu.csc.itrust.http;

import java.io.File;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.DAOFactoryTest;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * 
 *         This class tests the UI for adding a LOINC file. 
 * 
 */
@SuppressWarnings("unused")
public class EditLOINCDataHTTPTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testAddLOINCFile() throws Exception {
		// Log in as Admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");

		// Click 'Edit LOINC Codes'
		wr = wr.getLinkWith("Edit LOINC Codes").click();
		assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
		assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L, "");

		wr.getFormWithName("mainForm").getButtonWithID("import").click();
		wr = wc.getCurrentPage();

		// Import the LOINC database file
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("loincFile", new File("testing-files/sample_loinc/sampleLoinc.txt"));
		wf.setParameter("ignoreDupData", "1");
		wf.getButtonWithID("sendFile").click();
		assertLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L, "");

		// Check the Edit LOINC page for the updated codes
		wr = wr.getLinkWith("Return to LOINC Codes List").click();
		WebTable wt = wr.getTables()[1];
		assertEquals(33 + 2, wt.getRowCount()); // 33+2 is 33 rows and a header and subheader

		assertFalse(wt.getText().contains("THIS ONE IS DIFFERENT"));
	}

	public void testAddLOINCFileNoIgnore() throws Exception {
		// Log in as Admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");

		// Click 'Edit LOINC Codes'
		wr = wr.getLinkWith("Edit LOINC Codes").click();
		assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
		assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L, "");

		wr.getFormWithName("mainForm").getButtonWithID("import").click();
		wr = wc.getCurrentPage();

		// Import the LOINC database file
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("loincFile", new File("testing-files/sample_loinc/sampleLoinc.txt"));
		wf.setParameter("ignoreDupData", "0");
		wf.getButtonWithID("sendFile").click();
		assertLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L, "");

		// Check the Edit LOINC page for the updated codes
		wr = wr.getLinkWith("Return to LOINC Codes List").click();
		WebTable wt = wr.getTables()[1];
		assertEquals(33 + 2, wt.getRowCount()); // 33+2 is 33 rows and a header and subheader
		assertTrue(wt.getText().contains("THIS ONE IS DIFFERENT"));
	}

	public void testUploadLOINCFileInvalidLines() throws Exception {
		// Log in as Admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");

		// Click 'Edit LOINC Codes'
		wr = wr.getLinkWith("Edit LOINC Codes").click();
		assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
		assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L, "");

		wr.getFormWithName("mainForm").getButtonWithID("import").click();
		wr = wc.getCurrentPage();

		// Import the LOINC database file
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("loincFile", new File("testing-files/sample_loinc/invalidLine.txt"));
		wf.setParameter("ignoreDupData", "0");
		wf.getButtonWithID("sendFile").click();
		assertLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L, "");
		wr = wc.getCurrentPage();
		assertTrue(wr
				.getText()
				.contains(
						"ERROR, LINE 2: \"10054-5\"\t\"I skip rest of fields\" This form has not been validated correctly. The following field are not properly filled in: [You must have a Lab Procedure Code, Component and Kind Of Property]"));
		assertTrue(wr.getText().contains(
				"Successfully added 2 lines of new LOINC data. Updated 0 lines of existing LOINC data."));
	}

	public void testUploadBadLOINCFile() throws Exception {
		// Log in as Admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");

		// Click 'Edit LOINC Codes'
		wr = wr.getLinkWith("Edit LOINC Codes").click();
		assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
		assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L, "");

		wr.getFormWithName("mainForm").getButtonWithID("import").click();
		wr = wc.getCurrentPage();

		// Import the LOINC database file
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("loincFile", new File("testing-files/sample_loinc/badLoincFile.txt"));
		wf.setParameter("ignoreDupData", "1");
		wf.getButtonWithID("sendFile").click();
		assertNotLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L, "");
		wr = wc.getCurrentPage();
		assertTrue(wr
				.getText()
				.contains(
						"IGNORED LINE 1: This file contains no LOINC data and should fail the LOINC file verification process."));
		assertTrue(wr.getText().contains(
				"File invalid. No LOINC data added."));
		
		// Check the Edit LOINC page for the updated codes
		wr = wr.getLinkWith("Return to LOINC Codes List").click();
		WebTable wt = wr.getTables()[1];
		assertEquals(4 + 2, wt.getRowCount()); // 4+2 accounts for header and subheader
	}

	// Not possible to send nonexistant file to browser
	/*
	 * public void testAddLOINCFileFailure() throws Exception { // Log in as Admin WebConversation wc =
	 * login("9000000001", "pw"); WebResponse wr = wc.getCurrentPage();
	 * assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L,"");
	 * 
	 * // Click 'Edit LOINC Codes' wr = wr.getLinkWith("Edit LOINC Codes").click();
	 * assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
	 * assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L,"");
	 * 
	 * // Attempt to import the missing LOINC database file WebForm wf = wr.getFormWithName("uploadForm");
	 * wf.setParameter("loincFile", new File("src/test/resources/testing-files/invalidLoincDb.txt"));
	 * wf.setParameter("ignoreDupData", "1"); wf.getButtonWithID("sendFile").click();
	 * 
	 * assertNotLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L,""); assertTrue(
	 * wr.getText().contains("File not found: invalidLoincDb.txt") ); }
	 */

	/*
	 * public void testAddLOINCFileVerificationFailure() throws Exception { // Log in as Admin WebConversation
	 * wc = login("9000000001", "pw"); WebResponse wr = wc.getCurrentPage();
	 * assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L,"");
	 * 
	 * // Click 'Edit LOINC Codes' wr = wr.getLinkWith("Edit LOINC Codes").click();
	 * assertEquals("iTrust - Maintain LOINC Codes", wr.getTitle());
	 * assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L,"");
	 * 
	 * // Attempt to import the bad LOINC database file WebForm wf = wr.getFormWithName("uploadForm");
	 * wf.setParameter("loincFile", new File("src/test/resources/testing-files/sample_loinc/badLoincFile.txt"));
	 * wf.setParameter("ignoreDupData", "1"); wf.getButtonWithID("sendFile").click();
	 * 
	 * assertNotLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L,""); assertTrue(
	 * wr.getText().contains("Invalid file format: badLoincFile.txt") ); }
	 */

}
