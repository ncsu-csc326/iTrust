package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.beans.SurveyResultBean;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 25
 */
public class PhysicianSatisfactionUseCaseTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();		
	}

	/*
	 * 	Preconditions: Patient 2 is in the system and has authenticated successfully. 
	 *  HCP 9000000000 is in the system with address 4321 My Road St, PO BOX 2, CityName, NY, 12345-1234 and physician type Surgeon. 
	 *  Patient 2 has had 2 office visits with HCP 9000000000, and no other office visits are in the system. 
	 *  TakeSatisfactionSurveySuccess and TakeSatisfactionSurveySuccess2 have passed successfully.

	 *  1. Patient 2 chooses to view satisfaction survey results.
	 *  2. Patient 2 inputs Surgeon for physician type and zip code 12377.
	 *  3. Submit.
	 * 
	 */

	public void testSearchForHCPSurveyResults1() throws Exception {
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "10453");
		form.setParameter("hcpSpecialty", SurveyResultBean.SURGEON_SPECIALTY);
		//view current page to ensure data is correct
		wr = form.submit();
		assertTrue(wr.getText().contains("Survey Results"));
		assertTrue(wr.getText().contains("Kelly"));
		assertTrue(wr.getText().contains("Doctor"));
		assertTrue(wr.getText().contains("4321 My Road St"));
		assertTrue(wr.getText().contains("PO BOX 2"));
		assertTrue(wr.getText().contains("New York"));
		assertTrue(wr.getText().contains("NY"));
		assertTrue(wr.getText().contains("10453"));
		assertTrue(wr.getText().contains("surgeon"));
		assertLogged(TransactionType.SATISFACTION_SURVEY_VIEW, 2L, 0L, "");		
	}
	
	
	
	public void testSearchForHCPSurveyResults2() throws Exception {
		gen.surveyResults();
		
		//log in as uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "27613");
		form.setParameter("hcpSpecialty", SurveyResultBean.HEART_SPECIALTY);
		//view current page to ensure data is correct
		wr = form.submit();
		WebTable wt = wr.getTableStartingWith("LHCP Search Results:");
		assertEquals("Bad Doctor", wt.getTableCell(2, 0).getText());
		assertEquals("Avenue 1", wt.getTableCell(2, 1).getText());
		assertEquals("Avenue 2", wt.getTableCell(2, 2).getText());
		assertEquals("Raleigh", wt.getTableCell(2, 3).getText());
		assertEquals("NC", wt.getTableCell(2, 4).getText());
		assertEquals("27613", wt.getTableCell(2, 5).getText());
		assertEquals("Heart Specialist", wt.getTableCell(2, 6).getText());
		assertEquals("na", wt.getTableCell(2, 7).getText());
		assertEquals("20.00", wt.getTableCell(2, 8).getText());
		assertEquals("30.00", wt.getTableCell(2, 9).getText());
		assertEquals("1.00", wt.getTableCell(2, 10).getText());
		assertEquals("2.00", wt.getTableCell(2, 11).getText());
		assertEquals("75%", wt.getTableCell(2, 12).getText());
		assertLogged(TransactionType.SATISFACTION_SURVEY_VIEW, 8000000009L, 0L, "");		
	}
	
	
	
	public void testSearchForHCPSurveyResults3() throws Exception {
		gen.surveyResults();
		
		//log in as hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "27613-1234");
		form.setParameter("hcpSpecialty", SurveyResultBean.ANY_SPECIALTY);
		//view current page to ensure data is correct
		wr = form.submit();
		WebTable wt = wr.getTableStartingWith("LHCP Search Results:");
		assertEquals("Good Doctor", wt.getTableCell(2, 0).getText());
		assertEquals("Street 1", wt.getTableCell(2, 1).getText());
		assertEquals("Street 2", wt.getTableCell(2, 2).getText());
		assertEquals("Raleigh", wt.getTableCell(2, 3).getText());
		assertEquals("NC", wt.getTableCell(2, 4).getText());
		assertEquals("27613", wt.getTableCell(2, 5).getText());
		assertEquals("None", wt.getTableCell(2, 6).getText());
		assertEquals("na", wt.getTableCell(2, 7).getText());
		assertEquals("10.00", wt.getTableCell(2, 8).getText());
		assertEquals("10.00", wt.getTableCell(2, 9).getText());
		assertEquals("4.67", wt.getTableCell(2, 10).getText());
		assertEquals("5.00", wt.getTableCell(2, 11).getText());
		assertEquals("50%", wt.getTableCell(2, 12).getText());

		assertEquals("Bad Doctor", wt.getTableCell(3, 0).getText());
		assertEquals("Avenue 1", wt.getTableCell(3, 1).getText());
		assertEquals("Avenue 2", wt.getTableCell(3, 2).getText());
		assertEquals("Raleigh", wt.getTableCell(3, 3).getText());
		assertEquals("NC", wt.getTableCell(3, 4).getText());
		assertEquals("27613", wt.getTableCell(3, 5).getText());
		assertEquals("Heart Specialist", wt.getTableCell(3, 6).getText());
		assertEquals("na", wt.getTableCell(3, 7).getText());
		assertEquals("20.00", wt.getTableCell(3, 8).getText());
		assertEquals("30.00", wt.getTableCell(3, 9).getText());
		assertEquals("1.00", wt.getTableCell(3, 10).getText());
		assertEquals("2.00", wt.getTableCell(3, 11).getText());
		assertEquals("75%", wt.getTableCell(3, 12).getText());
		assertLogged(TransactionType.SATISFACTION_SURVEY_VIEW, 9000000000L, 0L, "");	
	}
	
	public void testSearchByHospitalSurveyResults1() throws Exception {
		gen.surveyResults();
		
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpHospitalID", "8181818181");
		form.setParameter("hcpSpecialty", SurveyResultBean.ANY_SPECIALTY);
		//view current page to ensure data is correct
		wr = form.submit();
		WebTable wt = wr.getTableStartingWith("LHCP Search Results:");
		assertEquals(5, wt.getRowCount());
		assertLogged(TransactionType.SATISFACTION_SURVEY_VIEW, 2L, 0L, "");
	}
	
	public void testSearchByHospitalSurveyResults2() throws Exception {
		gen.surveyResults();
		
		//log in as uap
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpHospitalID", "9191919191");
		form.setParameter("hcpSpecialty", SurveyResultBean.ANY_SPECIALTY);
		//view current page to ensure data is correct
		wr = form.submit();
		WebTable wt = wr.getTableStartingWith("LHCP Search Results:");
		assertEquals(4, wt.getRowCount());
		assertLogged(TransactionType.SATISFACTION_SURVEY_VIEW, 8000000009L, 0L, "");
	}
	
	public void testSearchByHospitalSurveyResults3() throws Exception {
		gen.surveyResults();
		
		//log in as hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpHospitalID", "8181818181");
		form.setParameter("hcpSpecialty", SurveyResultBean.HEART_SPECIALTY);
		//view current page to ensure data is correct
		wr = form.submit();
		WebTable wt = wr.getTableStartingWith("LHCP Search Results:");
		assertEquals(3, wt.getRowCount());
		assertLogged(TransactionType.SATISFACTION_SURVEY_VIEW, 9000000000L, 0L, "");
	}
	
	public void testSurveyResultsNoInput() throws Exception {
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		//view current page to ensure data is correct
		wr = form.submit();
		assertTrue(wr.getText().contains("You must enter either a zip code or a hospital ID."));
	}

	public void testSurveyResultsTooMuchInput() throws Exception {
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "27613");
		form.setParameter("hcpHospitalID", "1");
		wr = form.submit();
		//view current page to ensure data is correct
		assertTrue(wr.getText().contains("Data for both Zip Code and Hospital ID is not allowed.  Please choose either Zip or Hospital ID."));
	}

	public void testSurveyResultsZipCodeFormat1() throws Exception {
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "123");
		wr = form.submit();
		//view current page to ensure data is correct
		assertTrue(wr.getText().contains("Zip Code: xxxxx or xxxxx-xxxx"));
	}

	public void testSurveyResultsZipCodeFormat2() throws Exception {
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "123456");
		wr = form.submit();
		//view current page to ensure data is correct
		assertTrue(wr.getText().contains("Zip Code: xxxxx or xxxxx-xxxx"));
	}

	public void testSurveyResultsZipCodeFormat3() throws Exception {
		//log in as patient
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		//go to page with HCP Survey Results
		wr = wr.getLinkWith("Satisfaction Survey Results").click();
		assertTrue(wr.getText().contains("Search HCP Survey Results"));
		WebForm form = wr.getForms()[0];
		form.setParameter("hcpZip", "abc");
		wr = form.submit();
		//view current page to ensure data is correct
		assertTrue(wr.getText().contains("Zip Code: xxxxx or xxxxx-xxxx"));
	}
	
}

