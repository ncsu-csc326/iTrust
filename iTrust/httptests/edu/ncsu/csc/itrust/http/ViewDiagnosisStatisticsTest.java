package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Test Diagnosis Trends page
 */
public class ViewDiagnosisStatisticsTest extends iTrustHTTPTest {

	/**
	 * Sets up the test. Clears the tables then adds necessary data
	 */
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		
		gen.standardData();
		gen.patient_hcp_vists();
		gen.hcp_diagnosis_data();
	}

	/*
	 * Authenticate PHA
	 * MID 7000000001
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 72.00
	 * ZipCode: 27695
	 * StartDate: 06/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_PHAView1
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_PHAView1() throws Exception {
		// pha views diagnosis statistics for mumps
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
				
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
		
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "72.00");
		form.getScriptableObject().setParameterValue("zipCode", "27695");
		form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 7000000001L, 0L, "");
		
		WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
		assertTrue(table.getCellAsText(1, 2).contains("0"));
		assertTrue(table.getCellAsText(1, 3).contains("2"));
	}
	
	/*
	 * Authenticate PHA
	 * MID 7000000001
	 * Password: pw
	 * Choose "Epidemics"
	 * Enter Fields:
	 * Diagnosis: 84.50 Malaria
	 * ZipCode: 12345
	 * StartDate: 1/23/12
	 * Threshold: [leave blank]
	 */
	/**
	 * testViewDiagnosisTrendsEpidemic_InvalidThreshold
	 * @throws Exception
	 */
	public void testViewDiagnosisTrendsEpidemic_InvalidThreshold() throws Exception {
		// pha views diagnosis statistics for mumps
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
				
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
		
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "84.50");
		form.getScriptableObject().setParameterValue("zipCode", "12345");
		form.getScriptableObject().setParameterValue("startDate", "01/23/2012");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 7000000001L, 0L, "");
	}
	
	/*
	 * Authenticate HCP
	 * MID 9000000008
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 27695
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 * Document new office visit
	 * Add new diagnosis (487.00)
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 27695
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_LHCPObserveIncrease
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_LHCPObserveIncrease() throws Exception {
		// hcp views diagnosis statistics for influenza
		// login hcp
		WebConversation wc = login("9000000008", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000008L, 0L, "");
								
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
							
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "487.00");
		form.getScriptableObject().setParameterValue("zipCode", "27695");
		form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
				
		WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
		long local1 = Long.parseLong(table.getCellAsText(1, 2));
		long region1 = Long.parseLong(table.getCellAsText(1, 3));
		
		
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		// choose patient 25
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "25");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// click Yes, Document Office Visit
		form = wr.getForms()[0];
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		// add a new office visit
		form = wr.getFormWithID("mainForm");
		form.setParameter("visitDate", "09/28/2011");
		form.setParameter("notes", "I like diet-coke");
		form.getButtonWithID("update").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000008L, 25L, "Office visit");
		// add a new diagnosis
		form = wr.getFormWithID("diagnosisForm");
		form.setParameter("ICDCode", "487.00");
		form.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Diagnosis information successfully updated."));
		
		
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
												
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "487.00");
		form.getScriptableObject().setParameterValue("zipCode", "27606-1234");
		form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
						
		table = wr.getTableWithID("diagnosisStatisticsTable");
		long local2 = Long.parseLong(table.getCellAsText(1, 2));
		long region2 = Long.parseLong(table.getCellAsText(1, 3));
		assertEquals(local1+1, local2);
		assertEquals(region1+1, region2);
	}

	/*
	 * Authenticate HCP
	 * MID 9000000008
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 276
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_InvalidZip
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_InvalidZip() throws Exception {
		// hcp views diagnosis statistics for influenza using an invalid zip code
		// login hcp
		WebConversation wc = login("9000000008", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000008L, 0L, "");
						
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "487.00");
		form.getScriptableObject().setParameterValue("zipCode", "276");
		form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
			
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Zip Code must be 5 digits!"));
	}
	
	/*
	 * Authenticate HCP
	 * MID 9000000000
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 276
	 * StartDate: 09/28/2011, EndDate: 08/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_InvalidDates
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_InvalidDates() throws Exception {
		// hcp views diagnosis statistics for malaria with invalid dates
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
						
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());		
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "84.50");
		form.getScriptableObject().setParameterValue("zipCode", "27519");
		form.getScriptableObject().setParameterValue("startDate", "09/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "08/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
			
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("Start date must be before end date!"));
	}
	
	/*
	 * Authenticate HCP
	 * MID 9000000008
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 27695
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 27606
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_SameRegionCount
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_SameRegionCount() throws Exception {
		// hcp views diagnosis statistics for malaria in 2 zip codes. ensures the region matches
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
								
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "84.50");
		form.getScriptableObject().setParameterValue("zipCode", "27695");
		form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
		
		WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
		long region1 = Long.parseLong(table.getCellAsText(1, 3));
		
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "84.50");
		form.getScriptableObject().setParameterValue("zipCode", "27606");
		form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
		
		table = wr.getTableWithID("diagnosisStatisticsTable");
		long region2 = Long.parseLong(table.getCellAsText(1, 3));
		
		assertEquals(region1, region2);
	}
	
	/*
	 * Authenticate HCP
	 * MID 9000000000
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 84.50
	 * ZipCode: 276
	 * StartDate: 09/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_SameDateStartEnd
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_SameDateStartEnd() throws Exception {
		// hcp views diagnosis statistics for malaria
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
	
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
			
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "84.50");
		form.getScriptableObject().setParameterValue("zipCode", "27519");
		form.getScriptableObject().setParameterValue("startDate", "09/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
					
		WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
		assertTrue(table.getCellAsText(1, 2).contains("0"));
		assertTrue(table.getCellAsText(1, 3).contains("0"));
	}
	
	/*
	 * Authenticate HCP
	 * MID 9000000008
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: 487.00
	 * ZipCode: 27607
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_RegionNotLess
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_RegionNotLess() throws Exception {
		// hcp views diagnosis statistics for mumps
		// login hcp
		WebConversation wc = login("9000000008", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000008L, 0L, "");
								
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "487.00");
		form.getScriptableObject().setParameterValue("zipCode", "27607");
		form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
		
		WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
		long local = Long.parseLong(table.getCellAsText(1, 2));
		long region = Long.parseLong(table.getCellAsText(1, 3));
		assertTrue(local <= region);
	}
	
	/*
	 * Authenticate HCP
	 * MID 9000000008
	 * Password: pw
	 * Choose "Diagnosis Trends"
	 * Enter Fields:
	 * ICDCode: ""
	 * ZipCode: 276
	 * StartDate: 08/28/2011, EndDate: 09/28/2011
	 */
	/**
	 * testViewDiagnosisTrends_NoDiagnosisSelected
	 * @throws Exception
	 */
	public void testViewDiagnosisTrends_NoDiagnosisSelected() throws Exception {
		// pha views diagnosis statistics without selecting a diagnosis
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
						
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
				
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "trends");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
		
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "");
		form.getScriptableObject().setParameterValue("zipCode", "27695");
		form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
		form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 7000000001L, 0L, "");
			
		assertTrue(wr.getText().contains("Information not valid"));
		assertTrue(wr.getText().contains("ICDCode must be valid diagnosis!"));
	}
	
	/**
	 * viewDiagnosisEpidemics_NoEpidemicRecords
	 * @throws Exception
	 */
	public void viewDiagnosisEpidemics_NoEpidemicRecords() throws Exception {
		// hcp views diagnosis statistics for mumps
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
		
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
		// View Epidemic
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "84.50");
		form.getScriptableObject().setParameterValue("zipCode", "38201");
		form.getScriptableObject().setParameterValue("startDate", "06/02/2010");
		form.getScriptableObject().setParameterValue("threshold", "110");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 9000000000L, 0L, "");
		
		assertTrue(wr.getText().contains("There is no epidemic occurring in the region."));
	}
	
	/**
	 * viewDiagnosisEpidemics_YesEpidemic
	 * @throws Exception
	 */
	public void viewDiagnosisEpidemics_YesEpidemic() throws Exception {
		
		gen.influenza_epidemic();
		
		// hcp views diagnosis statistics for mumps
		// login hcp
		WebConversation wc = login("9000000007", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000007L, 0L, "");
				
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
		
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "487.00");
		form.getScriptableObject().setParameterValue("zipCode", "27607");
		form.getScriptableObject().setParameterValue("startDate", "11/02/2011");
		form.getScriptableObject().setParameterValue("threshold", "");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 9000000007L, 0L, "");
		
		assertTrue(wr.getText().contains("THERE IS AN EPIDEMIC OCCURRING IN THIS REGION!"));
	}
	
	/**
	 * viewDiagnosisEpidemics_NoEpidemic
	 * @throws Exception
	 */
	public void viewDiagnosisEpidemics_NoEpidemic() throws Exception {
		
		gen.influenza_epidemic();
		
		// pha views diagnosis statistics for mumps
		// login pha
		WebConversation wc = login("7000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - PHA Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
				
		// click Diagnosis Trends
		wr = wr.getLinkWith("Diagnosis Trends").click();
		
		WebForm form = wr.getFormWithID("formSelectFlow");
		form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
				
		// View Trend
		form = wr.getFormWithID("formMain");
		form.getScriptableObject().setParameterValue("icdCode", "487.00");
		form.getScriptableObject().setParameterValue("zipCode", "27607");
		form.getScriptableObject().setParameterValue("startDate", "02/15/2010");
		form.getScriptableObject().setParameterValue("threshold", "");
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
		assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 7000000001L, 0L, "");
		
		assertTrue(wr.getText().contains("There is no epidemic occurring in the region."));
	}

}
