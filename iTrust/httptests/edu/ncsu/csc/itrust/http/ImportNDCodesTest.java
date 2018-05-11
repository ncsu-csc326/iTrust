package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Use Case 38
 */
public class ImportNDCodesTest extends iTrustHTTPTest {
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
	}
	
	/*
	 * Test that drugs can be updated as a list
	 */
	public void testImportDrugs() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Import ND Codes", wr.getTitle());
		form = wr.getForms()[0];
		File f = tempNDCFile();
		form.setParameter("fileIn", f.getAbsoluteFile());
		form.setParameter("strategy", "ignore");
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebTable table = wr.getTableStartingWithPrefix("Current Drug ND Codes");
		assertEquals("05730-150", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains("ADVIL"));
		assertEquals("10544-591", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains("OxyContin"));
		assertEquals("11523-7197", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains("Claritin"));
		assertEquals("50458-513", table.getCellAsText(5, 0));
		assertTrue(table.getCellAsText(5, 1).contains("TYLENOL with Codeine"));
		//delete temp file
		f.delete();
	}
	
	/*
	 * Test that a new list of drugs can update an existing list
	 */
	public void testImportDrugs_UpdateDupes() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Import ND Codes", wr.getTitle());
		form = wr.getForms()[0];
		File f = tempNDCFile();
		form.setParameter("fileIn", f.getAbsoluteFile());
		form.setParameter("strategy", "ignore");
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebTable table = wr.getTableStartingWithPrefix("Current Drug ND Codes");
		assertEquals("05730-150", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains("ADVIL"));
		assertEquals("10544-591", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains("OxyContin"));
		assertEquals("11523-7197", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains("Claritin"));
		assertEquals("50458-513", table.getCellAsText(5, 0));
		assertTrue(table.getCellAsText(5, 1).contains("TYLENOL with Codeine"));
		//delete temp file
		f.delete();
		
		form = wr.getForms()[0];
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Import ND Codes", wr.getTitle());
		form = wr.getForms()[0];
		f = tempNDCFile2();
		form.setParameter("fileIn", f.getAbsoluteFile());
		form.setParameter("strategy", "update");
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		table = wr.getTableStartingWithPrefix("Current Drug ND Codes");
		assertEquals("05730-150", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains("ADVIL NEW"));
		assertEquals("10544-591", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains("OxyContin"));
		assertEquals("11523-7197", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains("Claritin"));
		assertEquals("50458-513", table.getCellAsText(5, 0));
		assertTrue(table.getCellAsText(5, 1).contains("TYLENOL with Coke"));
		//delete temp file
		f.delete();
	}
	
	
	/*
	 * Test the return button
	 */
	public void testImportDrugs_IgnoreDupes() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Import ND Codes", wr.getTitle());
		form = wr.getForms()[0];
		File f = tempNDCFile();
		form.setParameter("fileIn", f.getAbsoluteFile());
		form.setParameter("strategy", "ignore");
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebTable table = wr.getTableStartingWithPrefix("Current Drug ND Codes");
		assertEquals("05730-150", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains("ADVIL"));
		assertEquals("10544-591", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains("OxyContin"));
		assertEquals("11523-7197", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains("Claritin"));
		assertEquals("50458-513", table.getCellAsText(5, 0));
		assertTrue(table.getCellAsText(5, 1).contains("TYLENOL with Codeine"));
		//delete temp file
		f.delete();
		
		form = wr.getForms()[0];
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Import ND Codes", wr.getTitle());
		form = wr.getForms()[0];
		f = tempNDCFile2();
		form.setParameter("fileIn", f.getAbsoluteFile());
		form.setParameter("strategy", "ignore");
		form.getButtonWithID("import").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		table = wr.getTableStartingWithPrefix("Current Drug ND Codes");
		assertEquals("05730-150", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains("ADVIL"));
		assertEquals("10544-591", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains("OxyContin"));
		assertEquals("11523-7197", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains("Claritin"));
		assertEquals("50458-513", table.getCellAsText(5, 0));
		assertTrue(table.getCellAsText(5, 1).contains("TYLENOL with Codeine"));
		//delete temp file
		f.delete();
	}

	private File tempNDCFile() throws IOException {
		File f = File.createTempFile("ndcodes", null);
		FileWriter fw = new FileWriter(f);
		fw.write(
				"0573-0150	HUMAN OTC DRUG	ADVIL		IBUPROFEN	TABLET, COATED	ORAL	19840518		NDA	NDA018989	Pfizer Consumer Healthcare	IBUPROFEN	200	mg/1	Nonsteroidal Anti-inflammatory Drug [EPC], Cyclooxygenase Inhibitors [MoA], Nonsteroidal Anti-inflammatory Compounds [Chemical/Ingredient]	\n" +
				"50458-513	HUMAN PRESCRIPTION DRUG	TYLENOL with Codeine		ACETAMINOPHEN AND CODEINE PHOSPHATE	TABLET	ORAL	19770817		ANDA	ANDA085055	Janssen Pharmaceuticals, Inc.	ACETAMINOPHEN; CODEINE PHOSPHATE	300; 30	mg/1; mg/1		CIII\n" +
				"10544-591	HUMAN PRESCRIPTION DRUG	OxyContin		OXYCODONE HYDROCHLORIDE	TABLET, FILM COATED, EXTENDED RELEASE	ORAL	20100126		NDA	NDA020553	Blenheim Pharmacal, Inc.	OXYCODONE HYDROCHLORIDE	10	mg/1	Opioid Agonist [EPC], Full Opioid Agonists [MoA]	CII\n" +
				"11523-7197	HUMAN OTC DRUG	Claritin		LORATADINE	SOLUTION	ORAL	20110301		NDA	NDA020641	Schering Plough Healthcare Products Inc.	LORATADINE	5	mg/5mL		\n"
				);
		fw.flush();
		fw.close();
		return f;
	}

	private File tempNDCFile2() throws IOException {
		File f = File.createTempFile("ndcodes2", null);
		FileWriter fw = new FileWriter(f);
		fw.write(
				"0573-0150	HUMAN OTC DRUG	ADVIL NEW		IBUPROFEN	TABLET, COATED	ORAL	19840518		NDA	NDA018989	Pfizer Consumer Healthcare	IBUPROFEN	200	mg/1	Nonsteroidal Anti-inflammatory Drug [EPC], Cyclooxygenase Inhibitors [MoA], Nonsteroidal Anti-inflammatory Compounds [Chemical/Ingredient]	\n" +
				"50458-513	HUMAN PRESCRIPTION DRUG	TYLENOL with Coke		ACETAMINOPHEN AND CODEINE PHOSPHATE	TABLET	ORAL	19770817		ANDA	ANDA085055	Janssen Pharmaceuticals, Inc.	ACETAMINOPHEN; CODEINE PHOSPHATE	300; 30	mg/1; mg/1		CIII\n"
				);
		fw.flush();
		fw.close();
		return f;
	}
}
