package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;

public class DependencyExtensionHTTPTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testViewableDependentsInMyDemographics() throws Exception{
		WebConversation wc = login("2", "pw");	
		WebResponse wr = wc.getCurrentPage();
		TestDataGenerator gen = new TestDataGenerator();
		gen.doBaby();
		
		wr = wr.getLinkWith("My Demographics").click();
		assertTrue(wr.getText().contains("Baby Programmer"));
	}
	
	public void testEditableDependentsInMyDemographics() throws Exception{
		WebConversation wc = login("2", "pw");	
		WebResponse wr = wc.getCurrentPage();
		TestDataGenerator gen = new TestDataGenerator();
		gen.doBaby();
		
		wr = wr.getLinkWith("My Demographics").click();
		assertTrue(wr.getText().contains("Baby Programmer"));
		
		WebForm demographicsForm = wr.getForms()[1];
		demographicsForm.setParameter("firstName", "BabyO");
		
		demographicsForm.submit();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
	}
	
	public void testNoDependentsInMyDemographics() throws Exception{
		WebConversation wc = login("2", "pw");	
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("My Demographics").click();
		assertFalse(wr.getText().contains("Baby Programmer"));
	}
}

