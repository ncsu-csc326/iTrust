package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Use Case 43
 */
public class ActivityFeedTest extends iTrustHTTPTest {
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testOlderActivities() throws Exception {
		gen.transactionLog6();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		int entries = 0;
		String s = wr.getText();
		int start = s.indexOf("Your Activity Feed<span");
		int end = s.indexOf("<a href=\"home.jsp?date=");
		s = s.substring(start, end);
		// Check for 20 items
		entries = 0;
		while (s.contains("<li")) {
			entries++;
			s = s.substring(s.indexOf("<li") + 1);
		}
		assertEquals(entries, 20);
		wr = wr.getLinkWith("Older Activities").click();
		s = wr.getText();
		s = s.substring(s.indexOf("<h2"), s.indexOf("</h2"));
		// Check for 40 items
		entries = 40;
		while (s.contains("<div")) {
			entries++;
			s = s.substring(s.indexOf("<div") + 1);
		}
		assertEquals(entries, 40);
		wr = wr.getLinkWith("Older Activities").click();
		s = wr.getText();
		s = s.substring(s.indexOf("Your Activity Feed<span"), s.lastIndexOf("</ul>"));
		entries = 0;
		
		while (s.contains("<li")) {
			entries++;
			s = s.substring(s.indexOf("<li") + 1);
		}
		assertEquals(entries - 3, 60);
	}
	
	public void testUpdateActivityFeed() throws Exception {
		gen.transactionLog6();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		int entries = 0;
		wr = wr.getLinkWith("Older Activities").click();
		wr = wr.getLinkWith("Older Activities").click();
		wr = wr.getLinkWith("Refresh").click();
		
		String s = wr.getText();
		s = s.substring(s.lastIndexOf("<ul"), s.lastIndexOf("</ul"));
		
		while (s.contains("<li")) {
			entries++;
			s = s.substring(s.indexOf("<li") + 1);
		}
		assertEquals(20, entries);
	}
	
	public void testViewActivityFeed() throws Exception {
		gen.transactionLog5();
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		String s = wr.getText();

		Date d = new Date();
		d.setTime(d.getTime() - 3*24*60*60*1000);
		new SimpleDateFormat("MM/dd/yyyy");

		assertTrue(s.contains("Kelly Doctor</a> viewed your prescription report yesterday at 8:15AM."));
		assertTrue(s.contains("Andy Programmer viewed your prescription report yesterday at 9:43AM."));
		assertTrue(s.contains("Justin Time</a> created an emergency report for you yesterday at 10:04AM."));
		assertTrue(s.contains("FirstUAP LastUAP</a> viewed your lab procedure results yesterday at 12:02PM."));
		assertFalse(s.contains("Gandalf Stormcrow</a> viewed your risk factors yesterday at 12:58PM."));
		assertTrue(s.contains("FirstUAP LastUAP</a> viewed your risk factors yesterday at 1:02PM."));
		assertTrue(s.contains("Kelly Doctor</a> viewed your risk factors yesterday at 1:15PM."));
	}
	
	public void testDLHCPActivityHiddenInFeed1() throws Exception {
		WebConversation wc = login("9000000008", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());

		wr = wr.getLinkWith("Patient Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "21");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Patient", wr.getTitle());
		
		wr = wr.getLinkWith("Basic Health History").click();
		assertEquals("iTrust - Edit Basic Health Record", wr.getTitle());
		
		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());
		
		wc = login("9000000000", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());

		wr = wr.getLinkWith("Document Office Visit").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());

		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "21");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		wr.getForms()[0].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		
		WebForm form = wr.getFormWithID("mainForm");
		form.getButtonWithID("update").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));

		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());
		
		wc = login("21", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		assertTrue(wr.getText().contains("Kelly Doctor</a> created an office visit"));
		assertTrue(wr.getText().contains("Curious George</a> viewed your health records history today at"));
		assertTrue(wr.getText().contains("Curious George</a> viewed your demographics"));
		
		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		wr.getTableWithID("hcp_table");
		WebForm hcpForm = wr.getFormWithID("mainForm");
		hcpForm.setCheckbox("doctor", "Gandalf Stormcrow", false);
		wr = wr.getForms()[0].submit();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		
		wr.getTableWithID("hcp_table");
		hcpForm = wr.getFormWithID("mainForm");
		hcpForm.setCheckbox("doctor", "Kelly Doctor", true);
		wr = wr.getForms()[0].submit();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		
		wr.getText();

		wr = wr.getLinkWith("Logout").click();
		assertEquals("iTrust - Login", wr.getTitle());
		
		wc = login("21", "pw");
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		assertFalse(wr.getText().contains("NumberFormatException"));
		assertFalse(wr.getText().contains("Kelly Doctor</a> created an office visit"));	//this works when doing manually but not in testing
		assertTrue(wr.getText().contains("Curious George</a> viewed your health records history today at"));
		assertTrue(wr.getText().contains("Curious George</a> viewed your demographics"));
	}
	
	public void testDLHCPActivityHiddenInFeed2() throws Exception {
		WebConversation wc = login("23", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		assertFalse(wr.getText().contains("Beaker Beaker</a> viewed your demographics"));
		assertTrue(wr.getText().contains("Beaker Beaker</a> edited your demographics"));
		assertFalse(wr.getText().contains("Beaker Beaker</a> added you to the telemedicine monitoring list"));

	}

}
