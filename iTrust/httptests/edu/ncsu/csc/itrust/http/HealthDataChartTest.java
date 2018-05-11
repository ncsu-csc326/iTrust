package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class HealthDataChartTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.healthData();
	}

	/*
	 * HCP 9000000000 has reported Basic Health History for
	 * Patient 2 (4 entries).
	 * 
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose option Basic Health Information
	 * Choose patient 2
	 * Select to view line chart for weight.
	 */
	public void testGetWeightLineChart() throws Exception {
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click Basic Health Information		
		wr = wr.getLinkWith("Basic Health Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		// choose date range
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HCP_VIEW_BASIC_HEALTH_METRICS, 9000000000L, 2L, "");
		// view line chart
		wr = wr.getLinkWithID("viewWeightChart").click();
		assertEquals("iTrust - Weight Chart", wr.getTitle());
		assertTrue(wr.getText().contains("src=\"/iTrust/charts/"));
		assertLogged(TransactionType.BASIC_HEALTH_CHARTS_VIEW, 9000000000L, 2L, "Weight");
	}
	
	/*
	 * HCP 9000000000 has reported Basic Health History for
	 * Patient 2 (2 entries).
	 * 
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose option Basic Health Information
	 * Choose patient 2
	 * View BMI data for 10-30-2007 and 8-12-2007.
	 */
	public void testCalculateBMI() throws Exception {
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click Basic Health Information		
		wr = wr.getLinkWith("Basic Health Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		// choose date range
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HCP_VIEW_BASIC_HEALTH_METRICS, 9000000000L, 2L, "");
		
		//Make sure these values are present.
		WebTable table = wr.getTableStartingWith("Andy Programmer's Basic Adult Health History");
		boolean matchedOctober = false;
		boolean matchedAugust = false;
		for(int i=table.getRowCount()-1; i>=0; i--) {
			if(table.getCellAsText(i, 10).equals("2007-08-12 08:34:58.0") && table.getCellAsText(i, 2).equals("37.34")) {
				matchedAugust = true;
			} else if(table.getCellAsText(i, 10).equals("2007-10-30 10:54:22.0") && table.getCellAsText(i, 2).equals("38.24")) {
				matchedOctober = true;
			}
			
			if(matchedOctober && matchedAugust) {
				break;
			}
		}
		
	}
	
	/*
	 * HCP 9000000000 has reported Basic Health History for
	 * Patient 2 (4 entries).
	 * 
	 * Authenticate HCP
	 * MID: 9000000000
	 * Password: pw
	 * Choose option Basic Health Information
	 * Choose patient 2
	 * Select to view line chart for weight.
	 */
	public void testGetHeightLineChart() throws Exception {
		// login hcp
		//gen.healthData2();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click Basic Health Information		
		wr = wr.getLinkWith("Basic Health Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		// choose date range
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HCP_VIEW_BASIC_HEALTH_METRICS, 9000000000L, 2L, "");
		
		// view line chart
		wr = wr.getLinkWithID("viewHeightChart").click();
		assertEquals("iTrust - Height Chart", wr.getTitle());
		assertTrue(wr.getText().contains("src=\"/iTrust/charts/"));
		assertLogged(TransactionType.BASIC_HEALTH_CHARTS_VIEW, 9000000000L, 2L, "Height");
	}
}