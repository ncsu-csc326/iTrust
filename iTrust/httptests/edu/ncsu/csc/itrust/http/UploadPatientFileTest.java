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
public class UploadPatientFileTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testHCPPatientUploadValidData() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadValidData.csv"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Upload Successful"));
		assertTrue(wr.getText().contains("<td>Michael</td>"));
		assertTrue(wr.getText().contains("<td>Marley</td>"));
		assertTrue(wr.getText().contains("<td>Michael</td>"));
		assertTrue(wr.getText().contains("<td>Bazik</td>"));
		assertTrue(wr.getText().contains("<td>Barry</td>"));
		assertTrue(wr.getText().contains("<td>Peddycord</td>"));
	}
	
	public void testHCPPatientUploadRequiredFieldMissing() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadRequiredFieldMissing.csv"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("File upload was unsuccessful"));
		assertTrue(wr.getText().contains("Required field \"email\" is missing"));
	}
	
	public void testHCPPatientUploadInvalidField() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadInvalidField.csv"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("File upload was unsuccessful"));
		assertTrue(wr.getText().contains("Field \"invalidfield\" is invalid"));
	}
	
	public void testHCPPatientUploadInvalidData() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadInvalidData.csv"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("File upload was successful, but some patients could not be added"));
		assertTrue(wr.getText().contains("Field number mismatch on line 3"));
		assertTrue(wr.getText().contains("Field number mismatch on line 4"));
		assertTrue(wr.getText().contains("Input validation failed for patient &quot;not,valid first&quot;!"));
		assertTrue(wr.getText().contains("Input validation failed for patient &quot;Not valid&quot"));
		assertTrue(wr.getText().contains("<td>Correct</td>"));
		assertTrue(wr.getText().contains("<td>Number</td>"));
	}
	
	public void testHCPPatientUploadEmptyFile() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadEmptyFile.csv"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("File upload was unsuccessful"));
		assertTrue(wr.getText().contains("File is not valid CSV file"));
	}
	
	public void testHCPPatientUploadDuplicateField() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadDuplicateField.csv"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("File upload was unsuccessful"));
		assertTrue(wr.getText().contains("Duplicate field \"firstName\""));
	}
	
	public void testHCPPatientUploadBinaryData() throws Exception {
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();

		
		wr = wr.getLinkWith("Upload Patient File").click();
		assertEquals("iTrust - Upload Patient File", wr.getTitle());

		
		WebForm wf = wr.getFormWithName("mainForm");
		wf.setParameter("patientFile", new File("testing-files/sample_patientupload/HCPPatientUploadBinaryData.doc"));
		wf.getButtonWithID("sendFile").click();

		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("File upload was unsuccessful"));
	}
}
