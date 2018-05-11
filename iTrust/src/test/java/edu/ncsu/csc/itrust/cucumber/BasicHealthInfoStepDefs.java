package edu.ncsu.csc.itrust.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.SharedOfficeVisit;
import edu.ncsu.csc.itrust.cucumber.util.SharedPatient;
import edu.ncsu.csc.itrust.cucumber.util.SharedPersonnel;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.enums.Role;

public class BasicHealthInfoStepDefs {

	private SharedPersonnel sharedPersonnel;
	private SharedPatient sharedPatient;
	private SharedOfficeVisit sharedOfficeVisit;

	public BasicHealthInfoStepDefs(SharedPersonnel sharedPersonnel, SharedPatient sharedPatient,
			SharedOfficeVisit sharedOfficeVisit) throws Exception {
		this.sharedPersonnel = sharedPersonnel;
		this.sharedPatient = sharedPatient;
		this.sharedOfficeVisit = sharedOfficeVisit;
	}

	@Given("^(?:.*) is an HCP with MID: (\\d+)$")
	public void hcp_is_an_HCP_with_MID(long MID) throws Throwable {
		PersonnelBean p = sharedPersonnel.getPersonnelDAO().getPersonnel(MID);
		assertNotNull(String.format("Personnel with MID: %d doesn't exist", MID), p);
		assertEquals(Role.HCP, p.getRole());
	}

	@Given("^(?:.*) is a patient with MID (\\d+) who is born on (\\S+)$")
	public void patient_is_a_patient_with_MID_who_is_months_old(long MID, Date birthday) throws Throwable {
		PatientBean p = sharedPatient.getPatientDAO().getPatient(MID);
		assertNotNull(String.format("Patient with MID: %d doesn't exist", MID), p);
		assertEquals("Patient birthday doesn't match", p.getDateOfBirth(), birthday);
	}

	@When("^(?:.*) logs in with MID: (\\d+)")
	public void hcp_logs_in(Long MID) throws Throwable {
		PersonnelBean p = sharedPersonnel.getPersonnelDAO().getPersonnel(MID);
		sharedPersonnel.setPersonnel(p);
	}

	@When("^Chooses to document an office visit$")
	public void chooses_to_document_an_office_visit() throws Throwable {
		sharedOfficeVisit.setOfficeVisit(new OfficeVisit());
	}

	@When("^selects patient (?:.*) with MID (\\d+)$")
	public void selects_patient_Brynn_McClain_with_MID(long MID) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setPatientMID(MID);
	}

	@When("^Choose appointment type (?:.*) with id: (\\d+)$")
	public void choose_appointment_type_General_checkup(long apptID) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setApptTypeID(apptID);
	}

	@When("^Chooses the date to be (\\S+)$")
	public void chooses_the_date_to_be(Date date) throws Throwable {
		sharedOfficeVisit.getOfficeVisit()
				.setDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC));
	}

	@When("^Select (?:.*) with id: (\\w+) for location$")
	public void select_Central_Hospital_for_location(String locationId) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setLocationID(locationId);
	}

	@When("^enters Note “(.+)”$")
	public void enters_Notes_Brynn_can_start_eating_rice_cereal_mixed_with_breast_milk_or_formula_once_a_day(
			String notes) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setNotes(notes);
	}

	@When("^Enters (\\S+) for weight$")
	public void enters_for_weight(String weightStr) throws Throwable {
		Float weight = null;
		try {
			weight = Float.parseFloat(weightStr);
		} catch (NumberFormatException e) {
			fail("Invalid floating point number format");
		}
		sharedOfficeVisit.getOfficeVisit().setWeight(weight);
	}

	@When("^Enters (\\S+) for length$")
	public void enters_for_length(String lengthStr) throws Throwable {
		Float length = null;
		try {
			length = Float.parseFloat(lengthStr);
			sharedOfficeVisit.getOfficeVisit().setLength(length);
		} catch (NumberFormatException e) {
			sharedOfficeVisit.setHasError(true);
		}
	}

	@When("^Enters (\\S+) for Head Circumference$")
	public void enters_for_Head_Circumference(String headCircumferenceString) throws Throwable {
		Float headCircumference = null;
		try {
			headCircumference = Float.parseFloat(headCircumferenceString);
		} catch (NumberFormatException e) {
			fail("Invalid floating point number format");
		}
		sharedOfficeVisit.getOfficeVisit().setHeadCircumference(headCircumference);
	}

	@When("^submits record$")
	public void submits_record() throws Throwable {
		sharedOfficeVisit.add();
	}

	@Then("^The record is saved successfully$")
	public void a_success_message_is_displayed() throws Throwable {
		Assert.assertTrue(sharedOfficeVisit.wasAddSuccessful() && !sharedOfficeVisit.hasError());
	}

	@When("^Enters (\\S+) for height$")
	public void enters_for_height(String heightStr) throws Throwable {
		Float height = Float.parseFloat(heightStr);
		sharedOfficeVisit.getOfficeVisit().setHeight(height);
	}

	@When("^selects (\\d+) for (?:.*) for household smoking status$")
	public void selects_for_indoors_smokers_for_household_smoking_status(int smokingStatus) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setHouseholdSmokingStatus(smokingStatus);
	}

	@When("^Enters (\\S+) for blood pressure$")
	public void enters_for_blood_pressure(String bloodPressure) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setBloodPressure(bloodPressure);
	}

	@When("^selects (\\d+) for (?:.*) for patient smoking status$")
	public void selects_for_former_smoker_for_patient_smoking_status(int smokingStatus) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setPatientSmokingStatus(smokingStatus);
	}

	@When("^enters (\\w+) for HDL$")
	public void enters_for_HDL(String hdlStr) throws Throwable {
		Integer hdl;
		try {
			hdl = Integer.parseInt(hdlStr);
			sharedOfficeVisit.getOfficeVisit().setHDL(hdl);
		} catch (NumberFormatException e) {
			sharedOfficeVisit.setHasError(true);
		}
	}

	@When("^enters (\\d+) for LDL$")
	public void enters_for_LDL(int ldl) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setLDL(ldl);
	}

	@When("^enters (\\d+) for Triglycerides$")
	public void enters_for_Triglycerides(int triglycerides) throws Throwable {
		sharedOfficeVisit.getOfficeVisit().setTriglyceride(triglycerides);
	}

	@Then("^the record is not successfully added$")
	public void an_error_message_is_displayed() throws Throwable {
		Assert.assertFalse(sharedOfficeVisit.wasAddSuccessful());
	}
	
	@Then("^the record is saved successfully$")
	public void the_record_is_saved_successfully() throws Throwable {
		Assert.assertTrue(sharedOfficeVisit.wasAddSuccessful());
	}
}
