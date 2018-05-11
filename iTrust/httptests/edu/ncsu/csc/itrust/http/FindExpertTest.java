package edu.ncsu.csc.itrust.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.DBBuilder;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

@SuppressWarnings("unused")
public class FindExpertTest extends iTrustHTTPTest {
	
	public static final String location = "890 Oval Drive, Raleigh NC";

	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();	
		gen.uc47SetUp();
	}
	
	public void testAcceptanceFindExpertCloseRange() throws Exception {
		/*
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find an Expert").click();
		assertEquals("iTrust - Find an Expert", wr.getTitle());
		WebForm form = wr.getFormWithID("mainForm");
		
		form.setParameter("specialty", "surgeon");
		form.getSubmitButton("findExpert").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Find an Expert", wr.getTitle());
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("1 Stinson Drive, Raleigh NC, 27607"));
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("kdoctor@iTrust.org"));
		assertTrue(wr.getText().contains("999-888-7777"));
		*/
	}
	
	public void testAcceptanceFindExpertFarRange() throws Exception {
		/*
		String DIR = "src/test/resources/sql/data";
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		gen.hcp10();
		gen.hospitals1();		
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find an Expert").click();
		assertEquals("iTrust - Find an Expert", wr.getTitle());
		WebForm form = wr.getFormWithID("mainForm");
		
		form.setParameter("specialty", "surgeon");
		form.setParameter("range", "1");
		form.getSubmitButton("findExpert").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Wal-Mart Medical Center"));
		assertTrue(wr.getText().contains("10000 Ballantyne Commons Pkwy, Charlotte NC, 28277"));
		assertTrue(wr.getText().contains("Tony Bologna"));
		assertTrue(wr.getText().contains("tbologna@iTrust.org"));
		assertTrue(wr.getText().contains("999-888-7777"));
		assertTrue(wr.getText().contains("Health Institute Dr. E"));
		assertTrue(wr.getText().contains("1 Stinson Drive, Raleigh NC, 27607"));
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("kdoctor@iTrust.org"));	
		*/	
	}
	
	public void testAcceptanceFindExpertAltZipcode() throws Exception {
		/*
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		gen.hcp10();
		gen.hospitals1();

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find an Expert").click();
		assertEquals("iTrust - Find an Expert", wr.getTitle());
		WebForm form = wr.getFormWithID("mainForm");
		
		form.setParameter("specialty", "surgeon");
		form.setParameter("zipCode", "28277");
		form.getSubmitButton("findExpert").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Wal-Mart Medical Center"));
		assertTrue(wr.getText().contains("10000 Ballantyne Commons Pkwy, Charlotte NC, 28277"));
		assertTrue(wr.getText().contains("Tony Bologna"));
		assertTrue(wr.getText().contains("tbologna@iTrust.org"));
		assertTrue(wr.getText().contains("999-888-7777"));
		*/
	}
	
	public void testFindExpertNonNumericZipcode() throws Exception {
		/*
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find an Expert").click();
		assertEquals("iTrust - Find an Expert", wr.getTitle());
		WebForm form = wr.getFormWithID("mainForm");
		
		form.setParameter("zipCode", "bacon");
		wr = wc.getCurrentPage();
		assertFalse(wr.getText().contains("Wal-Mart Medical Center"));
		assertFalse(wr.getText().contains("Health Institute"));
		assertFalse(wr.getText().contains("999-888-7777"));
		*/
	}
	
	public void testFindExpertFieldPreservation() throws Exception {
		/*
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		gen.hcp10();
		gen.hospitals1();

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find an Expert").click();
		assertEquals("iTrust - Find an Expert", wr.getTitle());
		WebForm form = wr.getFormWithID("mainForm");
		
		form.setParameter("specialty", "surgeon");
		form.setParameter("zipCode", "28277");
		form.setParameter("range", "2");
		form.getSubmitButton("findExpert").click();
		wr = wc.getCurrentPage();
		form = wr.getFormWithID("mainForm");
		assertEquals(form.getParameterValue("specialty"), "surgeon");
		assertEquals(form.getParameterValue("zipCode"), "28277");
		assertEquals(form.getParameterValue("range"), "2");
		*/
	}
	
	protected void tearDown() throws FileNotFoundException, SQLException, IOException{
		gen.uc47TearDown();
	}
}
