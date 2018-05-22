package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitValidator;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ViewHealthInfoStepDefs {
	
	private AuthDAO authController;
	private PatientDAO patientController;
	private PatientDataShared sharedPatient;
	private OfficeVisitValidator ovValidator;
	private DataSource ds;
	private OfficeVisitController ovController;
	private OfficeVisit sharedVisit;
	private UserDataShared sharedUser;
	private TestDataGenerator gen;
	private List<OfficeVisit> healthMetricsTable;
	private int sharedVisitIndex;
	
	public static final Long DEFAULT_APPT_TYPE_ID = 1L;
	
	public ViewHealthInfoStepDefs(PatientDataShared sharedPatient, OfficeVisit sharedVisit, UserDataShared sharedUser){
		this.ds = ConverterDAO.getDataSource();
		this.ovController = new OfficeVisitController(ds);
		this.ovValidator = new OfficeVisitValidator(ds);
		this.authController = new AuthDAO(TestDAOFactory.getTestInstance());
		this.patientController = new PatientDAO(TestDAOFactory.getTestInstance());
		this.sharedPatient = sharedPatient;
		this.sharedVisit = sharedVisit;
		this.sharedUser = sharedUser;
		this.gen = new TestDataGenerator();
		this.healthMetricsTable = Collections.emptyList();
	}
	
	@Given("^standard data is loaded$")
	public void standard_data_is_loaded() throws Throwable {
	    gen.clearAllTables();
	    gen.standardData();
	}
	
	@Given("^UC51 data is loaded$")
	public void uc51_data_is_loaded() throws Throwable {
		gen.uc51();
	}

	@Given("^UC52 data is loaded$")
	public void uc52_data_is_loaded() throws Throwable {
		gen.uc52();
	}
	
	@Given("^user logs in as (?:.*) with MID: (\\d+) and Password: (\\S+)$")
	public void user_logs_in_with_MID_and_Password(long mid, String password) throws Throwable {
		try {
			if(authController.authenticatePassword(mid, password)){
				sharedUser.loginID = mid;
			}
		} catch (DBException e) {
			fail("Unable to authenticate Password");
		}
	}

	@When("^user enters \"(\\d+)\" in Search by name or MID field$")
	public void user_enters_in_Search_by_name_or_MID_field(Long mid) throws Throwable {
		assertTrue(patientController.checkPatientExists(mid));
	}

	@When("^user selects patient with MID (\\d+)$")
	public void user_selects_patient_with_MID(Long mid) throws Throwable {
		sharedPatient.patientID = mid;
	}

	@When("^user clicks on Click Here to Create a New Office Visit Button$")
	public void user_clicks_on_Click_Here_to_Create_a_New_Office_Visit_Button() throws Throwable {
		sharedVisit = new OfficeVisit();
		sharedVisit.setPatientMID(sharedPatient.patientID);
	}

	@When("^user enters \"([^\"]*)\" as the Office Visit Date$")
	public void user_enters_as_the_Office_Visit_Date(String dateStr) throws Throwable {
		LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yyyy h:mm a"));
		sharedVisit.setDate(date);
		sharedVisit.setApptTypeID(DEFAULT_APPT_TYPE_ID);
	}

	@When("^user selects (?:.*) with id \"(\\d+)\" as the Location$")
	public void user_selects_with_id_as_the_Location(int locationId) throws Throwable {
		sharedVisit.setLocationID(Integer.toString(locationId));
	}

	@When("^user enters \"([^\"]*)\" as Notes$")
	public void user_enters_as_Notes(String notes) throws Throwable {
		sharedVisit.setNotes(notes);
	}

	@When("^user clicks the \"([^\"]*)\" button$")
	public void user_clicks_the_button(String arg1) throws Throwable {
		ovValidator.validate(sharedVisit);
	}

	@When("^user enters \"([^\"]*)\" as the weight$")
	public void user_enters_as_the_weight(String weight) throws Throwable {
		sharedVisit.setWeight(Float.parseFloat(weight));
	}

	@When("^user enters \"([^\"]*)\" as the length$")
	public void user_enters_as_the_length(String length) throws Throwable {
		sharedVisit.setLength(Float.parseFloat(length));
	}

	@When("^user enters \"([^\"]*)\" as the head circumference$")
	public void user_enters_as_the_head_circumference(String headCircumference) throws Throwable {
		sharedVisit.setHeadCircumference(Float.parseFloat(headCircumference));
	}

	@When("^user selects \"(\\d+)\" as the household smoking status$")
	public void user_selects_as_the_household_smoking_status(int hss) throws Throwable {
		sharedVisit.setHouseholdSmokingStatus(hss);
	}

	@When("^user clicks on the Add Record button$")
	public void user_clicks_on_the_Add_Record_button() throws Throwable {
		ovController.add(sharedVisit);
	}

	@When("^user navigates to view basic health info page for HCP$")
	public void user_navigates_to_view_basic_health_info_page_for_HCP() throws Throwable {
		healthMetricsTable = Collections.emptyList();
	}

	@Then("^the baby records table is visible$")
	public void the_baby_records_table_is_visible() throws Throwable {
		healthMetricsTable = ovController.getBabyOfficeVisitsForPatient(Long.toString(sharedPatient.patientID));
		assertTrue(healthMetricsTable.size() > 0);
	}

	@Then("^the above records table \\(if visible\\) contains (\\d+) entry\\(ies\\) \\(if visible\\)$")
	public void the_above_records_table_if_visible_contains_entry_ies_if_visible(int numEntries) throws Throwable {
		if (healthMetricsTable.size() == 0) {
			return;
		}
		assertEquals(numEntries, healthMetricsTable.size());
	}

	@Then("^the office visit at (.*) exists in the above records table \\(if visible\\)$")
	public void the_office_visit_at_PM_exists_in_the_above_records_table_if_visible(String dateStr) throws Throwable {
		LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yyyy h:mm a"));
		for (int i=0; i<healthMetricsTable.size(); i++) {
			if (healthMetricsTable.get(i).getDate().equals(date)) {
				sharedVisitIndex = i;
				break;
			}
		}
		assertTrue(sharedVisitIndex > -1);
		sharedVisit = (sharedVisitIndex > 0) ? healthMetricsTable.get(sharedVisitIndex) : null;
	}

	@Then("^the above office visit \\(if exists\\) is the (\\d+)-th entry of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_is_the_th_entry_of_the_above_records_table_if_visible(int index)
			throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(index-1, sharedVisitIndex);
	}

	@Then("^the above office visit \\(if exists\\) contains (\\S+) as the weight of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_weight_of_the_above_records_table_if_visible(String weightStr) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertTrue(Float.parseFloat(weightStr) == sharedVisit.getWeight());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\S+) as the length of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_length_of_the_above_records_table_if_visible(String lengthStr) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertTrue(Float.parseFloat(lengthStr) == sharedVisit.getLength());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\S+) as the head circumference of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_head_circumference_of_the_above_records_table_if_visible(String hsStr) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertTrue(Float.parseFloat(hsStr) == sharedVisit.getHeadCircumference());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\d+) as the household smoking the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_household_smoking_the_above_records_table_if_visible(
			int id) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(id, sharedVisit.getHouseholdSmokingStatus().intValue());
	}

	@Then("^the child records table is invisible$")
	public void the_child_records_table_is_invisible() throws Throwable {
		healthMetricsTable = ovController.getChildOfficeVisitsForPatient(Long.toString(sharedPatient.patientID));
		assertEquals(0, healthMetricsTable.size());
	}

	@Then("^the adult records table is invisible$")
	public void the_adult_records_table_is_invisible() throws Throwable {
		healthMetricsTable = ovController.getAdultOfficeVisitsForPatient(Long.toString(sharedPatient.patientID));
		assertEquals(0, healthMetricsTable.size());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\S+) as the height of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_height_of_the_above_records_table_if_visible(String height) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(height, sharedVisit.getHeight().toString());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\S+) as the blood_pressure of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_blood_pressure_of_the_above_records_table_if_visible(String bloodPressure) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(bloodPressure, sharedVisit.getBloodPressure());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\d+) as the hdl of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_hdl_of_the_above_records_table_if_visible(int hdl)
			throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(hdl, sharedVisit.getHDL().intValue());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\d+) as the ldl of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_ldl_of_the_above_records_table_if_visible(int ldl)
			throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(ldl, sharedVisit.getLDL().intValue());
	}

	@Then("^the above office visit \\(if exists\\) contains (\\d+) as the triglycerides of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_triglycerides_of_the_above_records_table_if_visible(
			int tryg) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		assertEquals(tryg, sharedVisit.getTriglyceride().intValue());
	}

	@Then("^the child records table is visible$")
	public void the_child_records_table_is_visible() throws Throwable {
		healthMetricsTable = ovController.getChildOfficeVisitsForPatient(Long.toString(sharedPatient.patientID));
		assertTrue(healthMetricsTable.size() > 0);
	}

	@Then("^the baby records table is invisible$")
	public void the_baby_records_table_is_invisible() throws Throwable {
		healthMetricsTable = ovController.getBabyOfficeVisitsForPatient(Long.toString(sharedPatient.patientID));
		assertEquals(0, healthMetricsTable.size());
	}

	@Then("^the adult records table is visible$")
	public void the_adult_records_table_is_visible() throws Throwable {
		healthMetricsTable = ovController.getAdultOfficeVisitsForPatient(Long.toString(sharedPatient.patientID));
		assertTrue(healthMetricsTable.size() > 0);
	}

	@When("^user navigates to view basic health info page for Patient$")
	public void user_navigates_to_view_basic_health_info_page_for_Patient() throws Throwable {
		sharedPatient.patientID = sharedUser.loginID;
	}

	@Then("^the page displays message \"([^\"]*)\"$")
	public void the_page_displays_message(String arg1) throws Throwable {
		assertEquals(0, healthMetricsTable.size());
	}

	@When("^user enters \"([^\"]*)\" as the height$")
	public void user_enters_as_the_height(String height) throws Throwable {
		sharedVisit.setHeight(Float.parseFloat(height));
	}

	@When("^user enters \"([^\"]*)\" as the blood pressure$")
	public void user_enters_as_the_blood_pressure(String bloodPressure) throws Throwable {
		sharedVisit.setBloodPressure(bloodPressure);
	}

	@When("^user navigates to edit basic health info page$")
	public void user_navigates_to_edit_basic_health_info_page() throws Throwable {
		sharedVisit = null;
		sharedVisitIndex = -1;
	}
	
	@Then("^the above office visit \\(if exists\\) contains (\\S+) as the BMI of the above records table \\(if visible\\)$")
	public void the_above_office_visit_if_exists_contains_as_the_BMI_of_the_above_records_table_if_visible(String bmiStr) throws Throwable {
		if (sharedVisit == null) {
			return;
		}
		if ("N/A".equals(bmiStr)) {
			bmiStr = "";
		}
	    assertEquals(bmiStr, sharedVisit.getAdultBMI());
	}



}
