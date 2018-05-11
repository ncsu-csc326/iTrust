package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class ImmunizationReportTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testImmunizationReportKindergartenPatient() throws Exception {
		//Test data for kindergarten-aged patient viewing his own immunization report.
		//The following received immunizations should be displayed:
			//DTP (Diphtheria, Tetanus, Pertussis)
			//Polio
			//MMR (Measles, Mumps, Rubella)
			//Hib (Influenza)
			//Hepatitis B
			//Varicella
			//Hepatitis A
			//Rotavirus
		//No immunizations are needed.
		
		WebConversation wc = login("300", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Immunization Report").click();
		assertEquals("iTrust - View My Immunization Records", wr.getTitle());
		WebTable t = wr.getTables()[0];
		
		//Check table format
		assertEquals(13, t.getRowCount());
		assertEquals("CPT Code", t.getCellAsText(1, 0));
		assertEquals("Description", t.getCellAsText(1, 1));
		assertEquals("Date Received", t.getCellAsText(1, 2));
		assertEquals("HCP", t.getCellAsText(1, 3));
		assertEquals("CPT Code", t.getCellAsText(11, 0));
		assertEquals("Description", t.getCellAsText(11, 2));
		
		//Check the eight immunizations received by Adam Sandler
		//DTP
		assertEquals("90696", t.getCellAsText(2, 0));
		assertEquals("Diphtheria, Tetanus, Pertussis", t.getCellAsText(2, 1));
		assertEquals("Dec 31, 2012", t.getCellAsText(2, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(2, 3));
		//Polio
		assertEquals("90712", t.getCellAsText(3, 0));
		assertEquals("Poliovirus", t.getCellAsText(3, 1));
		assertEquals("Jan 1, 2013", t.getCellAsText(3, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(3, 3));
		//MMR
		assertEquals("90707", t.getCellAsText(4, 0));
		assertEquals("Measles, Mumps, Rubella", t.getCellAsText(4, 1));
		assertEquals("Jan 1, 2013", t.getCellAsText(4, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(4, 3));
		//Hep B
		assertEquals("90371", t.getCellAsText(5, 0));
		assertEquals("Hepatitis B", t.getCellAsText(5, 1));
		assertEquals("Jan 2, 2013", t.getCellAsText(5, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(5, 3));
		//Varicella
		assertEquals("90396", t.getCellAsText(6, 0));
		assertEquals("Varicella", t.getCellAsText(6, 1));
		assertEquals("Jan 3, 2013", t.getCellAsText(6, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(6, 3));
		//Rotavirus
		assertEquals("90681", t.getCellAsText(7, 0));
		assertEquals("Rotavirus", t.getCellAsText(7, 1));
		assertEquals("Jan 3, 2013", t.getCellAsText(7, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(7, 3));
		//Hep A
		assertEquals("90633", t.getCellAsText(8, 0));
		assertEquals("Hepatitis A", t.getCellAsText(8, 1));
		assertEquals("Jan 3, 2013", t.getCellAsText(8, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(8, 3));
		//Hib
		assertEquals("90645", t.getCellAsText(9, 0));
		assertEquals("Haemophilus influenzae", t.getCellAsText(9, 1));
		assertEquals("Jan 4, 2013", t.getCellAsText(9, 2));
		assertEquals("Kelly Doctor", t.getCellAsText(9, 3));
		
		//No immunizations needed for patient.
		assertEquals("No further immunizations needed", t.getCellAsText(12, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, 300L, 0L, "");
	}
	
	public void testImmunizationReportSixthGradePatient() throws Exception {
		//Test data for a sixth-grade aged patient viewing her own immunization report.
		//The following received immunizations should be displayed:
			//DTP
			//Polio
			//MMR
			//Hep A
			//Hep B
			//Varicella
			//Hib
			//Rotavirus
		//No immunizations are needed.
		
		//Log in as Natalie Portman
		WebConversation wc = login("301", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Immunization Report").click();
		assertEquals("iTrust - View My Immunization Records", wr.getTitle());
		WebTable t = wr.getTables()[0];
		assertEquals(13, t.getRowCount());
		
		//Check the eight immunizations received by Natalie Portman
		//DTP
		assertEquals("90696", t.getCellAsText(2, 0));
		//Polio
		assertEquals("90712", t.getCellAsText(3, 0));
		//MMR
		assertEquals("90707", t.getCellAsText(4, 0));
		//Hep B
		assertEquals("90371", t.getCellAsText(5, 0));
		//Rotavirus
		assertEquals("90681", t.getCellAsText(6, 0));
		//Hep A
		assertEquals("90633", t.getCellAsText(7, 0));
		//Varicella
		assertEquals("90396", t.getCellAsText(8, 0));
		//Hib
		assertEquals("90645", t.getCellAsText(9, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, 301L, 0L, "");
	}
	
	public void testImmunizationReportAdultPatient() throws Exception {
		//Test data for an adult patient viewing his own immunization report.
		//The following received immunizations should be displayed:
			//DTP (Diphtheria, Tetanus, Pertussis)
			//MMR (Measles, Mumps, Rubella)
			//Hepatitis B
		//No required immunizations are displayed.
			//No Polio needed because patient is >=18yrs
		
		WebConversation wc = login("302", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Immunization Report").click();
		assertEquals("iTrust - View My Immunization Records", wr.getTitle());
		WebTable t = wr.getTables()[0];
		assertEquals(8, t.getRowCount());
		
		//Check the three immunizations received by Will Smith
		//DTP
		assertEquals("90696", t.getCellAsText(2, 0));
		//MMR
		assertEquals("90707", t.getCellAsText(3, 0));
		//Hep B
		assertEquals("90371", t.getCellAsText(4, 0));
		
		//Check that no immunizations are required
		assertEquals("No further immunizations needed", t.getCellAsText(7, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, 302L, 0L, "");
	}
	
	public void testImmunizationReportNeedImmunziations() throws Exception {
		//Test data for patient with immunizations needed.
		//The following received immunizations should be displayed:
			//Rotavirus
			//Hep B
		//The following required immunizations should be displayed:
			//DTP
			//Polio
			//MMR
			//Hib
			//Varicella
		WebConversation wc = login("303", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Immunization Report").click();
		assertEquals("iTrust - View My Immunization Records", wr.getTitle());
		WebTable t = wr.getTables()[0];
		assertEquals(11, t.getRowCount());
		
		//Check the two immunizations received by Julia Roberts
		assertEquals("90681", t.getCellAsText(2, 0));
		assertEquals("90371", t.getCellAsText(3, 0));
		
		//Check the five required immunizations
		assertEquals("90696", t.getCellAsText(6, 0));
		assertEquals("90712", t.getCellAsText(7, 0));
		assertEquals("90707", t.getCellAsText(8, 0));
		assertEquals("90645", t.getCellAsText(9, 0));
		assertEquals("90396", t.getCellAsText(10, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, 303L, 0L, "");
	}
	
	public void testImmunizationReportPriorDiagnosis() throws Exception {
		//Test data for patient with prior Chicken Pox diagnosis.
		//The following immunizations should be displayed as received:
			//DTP
			//Polio
			//MMR
			//Hep B
			//Hib
			//Rotavirus
		//No required immunizations should be displayed.
			//No Varicella is needed because of prior Chicken Pox diagnosis
		WebConversation wc = login("305", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Immunization Report").click();
		assertEquals("iTrust - View My Immunization Records", wr.getTitle());
		WebTable t = wr.getTables()[0];
		assertEquals(11, t.getRowCount());
		
		//Check the six immunizations received by Christina Aguillera
		assertEquals("90681", t.getCellAsText(2, 0));
		assertEquals("90371", t.getCellAsText(3, 0));
		assertEquals("90696", t.getCellAsText(4, 0));
		assertEquals("90712", t.getCellAsText(5, 0));
		assertEquals("90707", t.getCellAsText(6, 0));
		assertEquals("90645", t.getCellAsText(7, 0));
		
		//Check that no immunizations are logged as required.
		assertEquals("No further immunizations needed", t.getCellAsText(10, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, 305L, 0L, "");
	}
	
	public void testImmunizationReportOverMaxAge() throws Exception {
		//Test data for patient over the max age for needing a vaccine.
		//No immunizations should be displayed as received.
		//The following required immunizations should be displayed:
			//DTP
			//MMR
			//No Hep B needed because patient is born before July 1994
		WebConversation wc = login("308", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Immunization Report").click();
		assertEquals("iTrust - View My Immunization Records", wr.getTitle());
		WebTable t = wr.getTables()[0];
		assertEquals(7, t.getRowCount());
		
		//Check that no immunizations logged as received
		assertEquals("No immunizations received.", t.getCellAsText(2, 0));
		
		//Check the two immunizations required
		//DTP
		assertEquals("90696", t.getCellAsText(5, 0));
		//MMR
		assertEquals("90707", t.getCellAsText(6, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, 308L, 0L, "");
	}
	
	public void testImmunizationReportHCPView() throws Exception {
		//Test data for HCP viewing a patient's immunization report.
		//No immunizations should be displayed as received.
		//The following required immunizations should be displayed:
			//DTP
			//MMR
			//No Hep B needed because patient is born before July 1994
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Immunization Report").click();
		// choose patient Charlie Chaplin (MID 308)
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "308");
		patientForm.getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		WebTable t = wr.getTables()[0];
		assertEquals(7, t.getRowCount());
		
		//Check that no immunizations logged as received
		assertEquals("No immunizations received.", t.getCellAsText(2, 0));
		
		//Check the two immunizations required
		//DTP
		assertEquals("90696", t.getCellAsText(5, 0));
		//MMR
		assertEquals("90707", t.getCellAsText(6, 0));
		
		//Check logging
		assertLogged(TransactionType.IMMUNIZATION_REPORT_HCP_VIEW, 9000000000L, 308L, "");
	}
}
