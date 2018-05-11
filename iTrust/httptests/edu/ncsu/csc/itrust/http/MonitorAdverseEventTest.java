package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class MonitorAdverseEventTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.patient2();
		gen.hcp0();
		gen.pha1();
		gen.patient1();
		//gen.patient2();
		gen.patient3();
		gen.patient4();
		gen.patient10();
		gen.patient13();
	}

	@Override
	protected void tearDown() throws Exception {
		gen.clearAllTables();
	}

	/*
	 * HCP 9000000000 prescribed Citalopram Hydrobromide (54868-4985) 
	 * to Patient 2 (on his 5/10/2007 Office Visit) 
	 * with start date 6/15/2007, end date 12/15/2007, 
	 * dosage 10mg, and instructions “Take once daily with water” 
	 * 
	 * Patient 2 has reported an Adverse Event for the prescription drug 
	 * Citalopram Hydrobromide (54868-4985) on 8/12/2007 at 3:10PM 
	 * with description “Stomach cramps and migraine headaches after taking this drug” 
	 * 
	 * Authenticate PHA
	 * MID: 7000000001
	 * Password: pw
	 * Choose option Monitor Adverse Events
	 * Choose date range 02/05/1990 through 10/15/2009
	 * Choose to view adverse events
	 * Select the 8/12/2007 Adverse Event Report by Andy Programmer, 
	 * for prescription drug Citalopram Hydrobromide (ND Code: 54868-4985)
	 * Send email to patient requesting more information
	 */
	public void testViewDrugAdverseEvents () throws Exception {

		gen.adverseEvent1();
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
		
		// click Monitor Adverse Events
		wr = wr.getLinkWith("Monitor Adverse Events").click();
		assertEquals("iTrust - Monitor Adverse Events", wr.getTitle());
		
		// choose date range
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("startDate", "02/05/1990");
		form.getScriptableObject().setParameterValue("endDate", "10/15/2009");
		form.getButtons()[2].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Citalopram Hydrobromide(548684985)"));
		// get details of report
		wr = wr.getLinkWith("Get Details").click();
		assertLogged(TransactionType.ADVERSE_EVENT_VIEW, 7000000001L, 0L, "");
		
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getText().contains("Citalopram Hydrobromide (548684985)"));
		assertTrue(wr.getText().contains("2007-08-12 15:10:00.0"));
		assertTrue(wr.getText().contains("Stomach cramps and migraine headaches after taking this drug"));
		// request more information
		form = wr.getForms()[0];
		form.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Request sent"));
		assertLogged(TransactionType.ADVERSE_EVENT_REQUEST_MORE, 7000000001L, 0L, "Requested more info");
	}

	/*
	 * HCP 9000000000 administered Hepatitis B immunization
	 * to Patient 1 (on his 3/22/2009 Office Visit) 
	 * 
	 * Patient 1 has reported an Adverse Event for the 
	 * Hepatitis B immunization (90371) on 5/19/2009 at 8:34AM 
	 * with description “A rash began spreading outward from the injection spot” 
	 * 
	 * Authenticate PHA
	 * MID: 7000000001
	 * Password: pw
	 * Choose option Monitor Adverse Events
	 * Choose date range 08/05/2000 through 10/17/2009
	 * Choose to view adverse events
	 * Select the 8/12/2007 Adverse Event Report by Random Person, 
	 * for Hepatitis B immunization (CPT Code: 90371)
	 * Remove adverse event report
	 */
	public void testRemoveImmunizationAdverseEventReport() throws Exception {
		gen.adverseEvent2();
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
		
		// click Monitor Adverse Events		
		wr = wr.getLinkWith("Monitor Adverse Events").click();
		assertEquals("iTrust - Monitor Adverse Events", wr.getTitle());
		
		//choose date range
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("startDate", "08/05/2000");
		form.getScriptableObject().setParameterValue("endDate", "10/17/2009");
		form.getButtons()[3].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Hepatitis B(90371)"));
		// get details of report
		wr = wr.getLinkWith("Get Details").click();
		assertLogged(TransactionType.ADVERSE_EVENT_VIEW, 7000000001L, 0L, "");
		
		assertTrue(wr.getText().contains("Random Person"));
		assertTrue(wr.getText().contains("Hepatitis B (90371)"));
		assertTrue(wr.getText().contains("2009-05-19 08:34:00.0"));
		assertTrue(wr.getText().contains("A rash began spreading outward from the injection spot"));
		// remove report
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Report successfully removed"));
		assertLogged(TransactionType.ADVERSE_EVENT_REMOVE, 7000000001L, 0L, "");
	}
	
	/*
	 * HCP 9000000000 administered Hepatitis B immunization
	 * to 6 patients between 12/31/2008 and 5/19/2009, who all
	 * reported adverse events
	 * 
	 * Authenticate PHA
	 * MID: 7000000001
	 * Password: pw
	 * Choose option Monitor Adverse Events
	 * Choose date range 08/05/2000 through 10/17/2009
	 * Choose to view adverse events
	 * Select to view bar chart for Hepatitis B immunization (CPT Code: 90371)
	 */
	
	public void testGetBarChart() throws Exception {
		gen.adverseEvent3();
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
		
		// click Monitor Adverse Events		
		wr = wr.getLinkWith("Monitor Adverse Events").click();
		assertEquals("iTrust - Monitor Adverse Events", wr.getTitle());
		
		// choose date range
		WebForm form = wr.getForms()[0];
		form.getScriptableObject().setParameterValue("startDate", "08/05/2000");
		form.getScriptableObject().setParameterValue("endDate", "10/17/2009");
		form.getButtons()[3].click();
		wr = wc.getCurrentPage();
		WebTable wt = wr.getTableStartingWith("Immunization");
		assertTrue(wt.getRowCount() == 7);
		// view bar chart
//		form = wr.getForms()[0];
		wr = wr.getLinkWith("View Chart").click();
		assertTrue(wr.getText().contains("src=\"/iTrust/charts/"));
		assertLogged(TransactionType.ADVERSE_EVENT_CHART_VIEW, 7000000001L, 0L, "");
	}
}