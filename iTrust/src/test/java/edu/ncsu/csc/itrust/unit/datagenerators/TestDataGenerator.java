package edu.ncsu.csc.itrust.unit.datagenerators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * This TestDataGenerator class is in charge of centralizing all of the test
 * data calls. Most of the SQL is in the sql/something.sql files. A few design
 * conventions:
 * 
 * <ul>
 * <li>Any time you're using this class, be sure to run the "clearAllTables"
 * first. This is not a very slow method (it's actually quite fast) but it
 * clears all of the tables so that no data from a previous test can affect your
 * current test.</li>
 * <li>We do not recommend having one test method call another test method
 * (except "standardData" or other intentionally "meta" methods). For example,
 * loincs() should not call patient1() first. Instead, put BOTH patient1() and
 * loincs() in your test case. If we keep this convention, then every time you
 * call a method, you know that ONLY your sql file is called and nothing else.
 * The alternative is a lot of unexpected, extraneous calls to some test methods
 * like patient1().</li>
 * </ul>
 * 
 * 
 * 
 */
public class TestDataGenerator {
	public static void main(String[] args) throws IOException, SQLException {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();

	}

	private String DIR = "sql/data";

	private DAOFactory factory;

	public TestDataGenerator() {
		this.factory = TestDAOFactory.getTestInstance();
	}

	public TestDataGenerator(String projectHome, DAOFactory factory) {
		this.DIR = projectHome + "/sql/data";
		this.factory = factory;
	}

	public void additionalOfficeVisits() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ovAdditional.sql");
	}

	public void admin1() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/admin1.sql");
	}

	public void apptRequestConflicts() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/apptRequestConflicts.sql");
	}

	public void pha0() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/pha0.sql");
	}

	public void admin2() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/admin2.sql");
	}

	public void appointmentCase1() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/appointmentCase1.sql");
	}

	public void appointmentCase2() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/appointmentCase2.sql");
	}

	public void clearAllTables() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/deleteFromAllTables.sql");
	}

	public void clearAppointments() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/clearAppointments.sql");
	}

	public void clearFakeEmail() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/clearFakeemail.sql");
	}

	public void clearMessages() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/clearMessages.sql");
	}

	public void clearHospitalAssignments() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hospitalAssignmentsReset.sql");
	}

	public void foreignKeyTest() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/foreignKeyTest.sql");
	}

	public void clearLoginFailures() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/loginFailures.sql");
	}

	public void clearTransactionLog() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/clearTransactionLog.sql");
	}

	public void setMode() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/setMode.sql");
	}
	
	
	public void drugInteractions() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/drugInteractions.sql");
	}

	public void drugInteractions2() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/drugInteractions2.sql");
	}

	public void drugInteractions3() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/drugInteractions3.sql");
	}

	public void drugInteractions4() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/drugInteractions4.sql");
	}

	public void fakeEmail() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/fakeemail.sql");
	}

	public void family() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/family.sql");
	}

	public void er4() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/er6.sql");
	}

	public void hcp0() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp0.sql");
	}

	public void hcp1() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp1.sql");
	}

	public void hcp2() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp2.sql");
	}

	public void hcp3() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp3.sql");
	}

	public void hcp4() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp4.sql");
	}

	public void hcp5() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp5.sql");
	}

	public void hcp7() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp7.sql");
	}

	/**
	 * Adds HCP Curious George for testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void hcp8() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp8.sql");
	}

	/**
	 * Adds HCP John Zoidberg for testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void hcp9() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp9.sql");
	} // NEW

	public void hcp10() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp10.sql");
	}

	/**
	 * Adds HCP Brooke Tran with Specialty Optometrist.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void hcp11() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp11.sql");
	}

	/**
	 * Adds HCP Lamar Bridges with Specialty Ophthalmologist.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void hcp12() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hcp12.sql");
	}

	public void hospitals() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hospitals0.sql");
	}

	public void hospitals1() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/hospitals1.sql");
	}

	public void messages() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/message.sql");
	}

	public void messages6() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/messageCase6.sql");
	}

	public void ndCodes() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndCodes.sql");
	}

	public void ORCodes() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ORCodes.sql");
	}

	public void ndCodes1() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndCodes1.sql");
	}

	public void ndCodes2() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndCodes2.sql");
	}

	public void ndCodes3() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndCodes3.sql");
	}

	public void ndCodes4() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndCodes4.sql");
	}

	/**
	 * Adds drugs Midichlominene and Midichlomaxene for UC10 and UC37 testing
	 * purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void ndCodes100() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndcodes100.sql");
	} // NEW

	public void ndCodes1000() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ndcodes1000.sql");
	} // NEW

	/* Retained for setting up Allergy info */
	public void officeVisit4() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ov4.sql");
	}

	public void officeVisit8() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ov8.sql");
	}

	public void operationalProfile() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/operationalProfile.sql");
	}

	public void patient1() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient1.sql");
	}

	public void clearProfilePhotos() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/clearphotos.sql");
	}

	public void patient2() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient2.sql");
	}

	public void patient3() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient3.sql");
	}

	public void patient4() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient4.sql");
	}

	public void patient5() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient5.sql");
	}

	public void patient6() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient6.sql");
	}

	public void patient7() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient7.sql");
	}

	public void patient8() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient8.sql");
	}

	public void patient9() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient9.sql");
	}

	public void patient10() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient10.sql");
	}

	public void patient11() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient11.sql");
	}

	public void patient12() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient12.sql");
	}

	public void patient13() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient13.sql");
	}

	public void patient14() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient14.sql");
	}

	public void patient15() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient15.sql");
	}

	public void UC32Acceptance() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/UC32Acceptance.sql");
	}

	public void patient20() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient20.sql");
	}

	public void patient21() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient21.sql");
	}

	public void patient22() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient22.sql");
	}

	/**
	 * Adds patient Dare Devil for testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient23() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient23.sql");
	}

	/**
	 * Adds patient Devils Advocate for testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient24() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient24.sql");
	}

	/**
	 * Adds patient Trend Setter for testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient25() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient25.sql");
	}

	/**
	 * Adds patient Philip Fry for testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient26() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient26.sql");
	} // NEW

	/**
	 * Adds patient Brody Franco, used in the testing of UC83.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient27() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient27.sql");
	}

	/**
	 * Adds patient Freya Chandler, used in the testing of UC83.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient28() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient28.sql");
	}

	/**
	 * Adds patient Brittany Franco, used in the testing of UC84.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient29() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient29.sql");
	}

	/**
	 * Adds patient James Franco, used in the testing of UC84.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient30() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient30.sql");
	}

	public void patient42() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient42.sql");
	}

	/**
	 * Adds patient Anakin Skywalker for UC10 and UC37 testing purposes.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void patient100() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/patient100.sql");
	} // NEW

	public void pendingAppointmentAlert() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/pendingAppointmentAlert.sql");
	}

	public void pendingAppointmentConflict() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/pendingAppointmentConflict.sql");
	}

	public void reportRequests() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/reportRequests.sql");
	}

	public void surveyResults() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/surveyResults.sql");
	}

	public void tester() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/tester.sql");
	}

	public void timeout() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/timeout.sql");
	}

	public void transactionLog() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/transactionLog.sql");
	}

	public void transactionLog2() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/transactionLog2.sql");
	}

	public void transactionLog3() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/transactionLog3.sql");
	}

	public void transactionLog4() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/transactionLog4.sql");
	}

	public void transactionLog5() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/transactionLog5.sql");
	}

	public void transactionLog6() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/transactionLog6.sql");
	}

	public void uap1() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uap1.sql");
	}

	public void remoteMonitoring1() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring1.sql");
	}

	public void remoteMonitoring2() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring2.sql");
	}

	public void remoteMonitoring3() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring3.sql");
	}

	public void remoteMonitoring4() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring4.sql");
	}

	public void remoteMonitoring5() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring5.sql");
	}

	public void remoteMonitoring6() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring6.sql");
	}

	public void remoteMonitoring7() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring7.sql");
	}

	public void remoteMonitoring8() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoring8.sql");
	}

	public void remoteMonitoringUAP() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoringUAP.sql");
	}

	public void remoteMonitoringAdditional() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoringAdditional.sql");
	}

	public void remoteMonitoringPresentation() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/remoteMonitoringPresentation.sql");
	}

	public void pha1() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/pha1.sql");
	}

	public void appointment() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/appointment.sql");
	}

	public void appointmentCase3() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/appointmentCase3.sql");
	}

	public void appointmentType() throws FileNotFoundException, IOException, SQLException {
		new DBBuilder(factory).executeSQLFile(DIR + "/appointmentType.sql");
	}

	public void admin3() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/admin3.sql");
	}
	
	public void labProcedure0() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/labProcedure0.sql");
	}
	
	public void labProcedure1() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/labProcedure1.sql");
	}
	
	public void labProcedure2() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/labProcedure2.sql");
	}
	
	public void labProcedure3() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/labProcedure3.sql");
	}
	
	public void labProcedure4() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/labProcedure4.sql");
	}
	
	public void labProcedure5() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/labProcedure5.sql");
	}
	
	public void loinc() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/loinc.sql");
	}

	public void ltData0() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/lt0.sql");
	}

	public void ltData1() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/lt1.sql");
	}

	public void ltData2() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/lt2.sql");
	}

	public void uc22() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/UC22.sql");
	}

	public void testExpertSearch() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/testExpertSearch.sql");
	}

	public void viewAccessLogTestData() throws SQLException, FileNotFoundException, IOException {
		// previously contained sql for referral_sort_testdata
		// create patients Dare Devil and Devils Advocate
		// Devils Advocate is Dare Devil's Personal Representative
		patient23();
		patient24();
	}

	/**
	 * Adds additional DLHCPs to certain patients.
	 * 
	 * MID DLHCPs --- ------ 1 9000000000, 9000000003
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void messagingCcs() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/messagingCcs.sql");
	}

	public void insertwards() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/wardmanagementdata.sql");

	}
	
	/**
	 * generate test data for uc15 acceptance scenarios
	 */
	public void uc11() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc11.sql");
	}
	
	/**
	 * generate test data for uc15 acceptance scenarios
	 */
	public void uc15() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc15.sql");
	}
	
	/**
	 * generate test data for uc19 acceptance scenarios
	 */
	public void uc19() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc19.sql");
	}

	/**
	 * Generate test data for uc21 acceptance scenarios
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
    public void uc21() throws FileNotFoundException, SQLException, IOException {
        new DBBuilder(factory).executeSQLFile(DIR + "/uc21.sql");
    }
    
   

	/**
	 * generate test data for uc26 acceptance scenarios
	 */
	public void uc26() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc26.sql");
	}
	
	/**
	 * Generate test data for uc51 acceptance scenarios
	 */
	public void uc51() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc51.sql");
	}
	
	public void uc52() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc52.sql");
	}

	public void uc47SetUp() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc47SetUp.sql");
	}

	public void zipCodes() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/zipCodes.sql");
	}

	public void uc47TearDown() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc47TearDown.sql");
	}

	public void uc53SetUp() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc53.sql");
	}

	public void uc55() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc55.sql");
	}

	/**
	 * 
	 * Generate records release data for uc56 acceptance scenarios Includes
	 * recordsrelease table data and UAP-HCP relations
	 */
	public void uc56() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc56.sql");
	}

	/**
	 * Generate dependency data for uc58 acceptance. Create a dependent user, a
	 * representative user, and establish representative relationship between
	 * the two.
	 */
	public void uc58() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc58.sql");
	}

	/**
	 * Generate dependency data for uc59 acceptance. Create a dependent user and
	 * a representative user
	 */
	public void uc59() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc59.sql");
	}

	/**
	 * Generate dependency data for uc60 acceptance. Create a Patient and a HCP
	 * to bill the patient. Create a billed office visit the patient can view.
	 */
	public void uc60() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc60.sql");
	}

	/**
	 * Sets up patients 314159 (deactivated),
	 */
	public void basicPatients() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/basicPatients_users-patients.sql");
	}

	/**
	 * Generate dependency data for uc63 acceptance. Create two HCPs who are
	 * OB/GYNs Create seven new patients Give one patient a past pregnancy
	 */
	public void uc63() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc63.sql");
	}

	/**
	 *Based on previous data generator for UC68 that included data on Patient Derek Morgan, Patient Jennifer Jareau,
	 *Patient Aaron Hotchner, and HCP Spencer Reid that was also used for patient representatives
	 *and other tests*/
	public void multiplePatients_old() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/multiplePatients_old.sql");
	}

	/**
	 * Inserts the user Emily Prentiss into the system and gives her one food
	 * diary entry.
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void uc71() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc71.sql");
	}

	/**
	 * Inserts the user Emily Prentiss into the system and gives her one food
	 * diary entry.
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void uc70() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/uc70.sql");
	}

	/**
	 * Generate dependency data for uc63 acceptance. NOTE: also executes the
	 * above method because the patients created above are also required for
	 * this use case. Give three patients initial obstetrics records.
	 */
	public void uc64() throws SQLException, FileNotFoundException, IOException {
		uc63();
		new DBBuilder(factory).executeSQLFile(DIR + "/uc64.sql");
	}

	/**
	 * Generate a list of reviews for 2 HCP's by 3 diff patients.
	 */
	public void uc61reviews() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/reviews.sql");
	}
	
	

	/**
	 * generate the dependency of Baby Programmer on Andy Programmer
	 */
	public void doBaby() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/programmerReps.sql");
	}

	/**
	 * generate the dependency of Baby Programmer on Andy Programmer
	 */
	public void reviews() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/reviews.sql");
	}


	/**
	 * Inserts a optometristVisit into the system
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void uc85() throws SQLException, FileNotFoundException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/ophthalmologydiagnosis.sql");
	}
	
	public void cptCode() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/cptCodes.sql");
	}
	
	public void testIcdCode() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/testicdcode.sql");
	}
	
	public void icdCode() throws FileNotFoundException, SQLException, IOException {
		new DBBuilder(factory).executeSQLFile(DIR + "/icdcode.sql");
	}

	public void standardData() throws FileNotFoundException, IOException, SQLException {
	
		ndCodes();
		ndCodes1();
		ndCodes2();
		ndCodes3();
		ndCodes4();
		ndCodes100(); // NEW
		ndCodes1000(); // NEW
		drugInteractions4();
		ORCodes();
		hospitals();
	
		basicPatients();
	
		hcp0();
	
		ltData0();
		ltData1();
		ltData2();
	
		hcp3();
		hcp7();
		er4();
		pha1();
		patient1();
		patient2();
		patient3();
		patient4();
		patient5();
		patient6();
		patient7();
		patient8();
		patient9();
		patient10();
		
		multiplePatients_old();
	
		// Added so that the black box test plans for Use Case 32 can be run
		// immediately after running TestDataGenerator
		hcp1();
		hcp2();
		patient11();
		patient12();
		patient13();
		patient14();
	
		patient20();
		patient21();
		patient22();
		patient25();
		patient26(); // NEW
		patient42();
		patient100(); // NEW
	
		admin1();
		admin2();
		admin3();
		uap1();
		messages();
		tester();
		fakeEmail();
		reportRequests();
		appointmentType();
		appointment();
	
		transactionLog();
		transactionLog2();
		transactionLog3();
		transactionLog4();
	
		hcp8();
		hcp9(); // NEW
		viewAccessLogTestData();
		insertwards();
		
		uc51();
		uc52();
		uc53SetUp();
		uc21();
		uc11();
		uc15();
		uc19();
		uc26();

		loinc();
		cptCode();
		icdCode();

		uc63(); // NEW
		uc55();
		uc56();
		if (!checkIfZipsExists()) {
			zipCodes();
		}
		// Added for UC83
		hcp11();
	
		// Added for UC 86
		hcp12();
	
		patient27();
		patient28();
		patient29();
		patient30();
		
		setMode();
	}

	/**
	 * Do we have zipcodes?
	 * 
	 * @return Whether or not we have zipcodes.
	 * @throws SQLException
	 */
	private boolean checkIfZipsExists() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM zipcodes WHERE zip='27614'");
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;

			}
		} catch (SQLException e) {
			return false;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					return false;
				}
		}

		return false;
	}

}
