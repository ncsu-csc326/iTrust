package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BasicHealthInfoStepDefs {
	@Given("^(.+) is an HCP with MID: (\\d+)$")
	public void is_an_HCP_with_MID(String hcpName, long MID) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new PendingException();
	}

	@Given("^(.+) is a patient with MID (\\d+) who is (\\d+) months old$")
	public void patient_with_MID_who_is_months_old(String patientName, long MID, int age) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^(.+) logs in$")
	public void logs_in(String loginName) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Chooses to document an office visit$")
	public void chooses_to_document_an_office_visit() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^selects patient (.+) with MID (\\d+)$")
	public void selects_patient_with_MID(String patientName, long patientMID) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Choose appointment type (.+)$")
	public void choose_appointment_type(String apptType) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new PendingException();
	}

	@When("^Chooses the date to be (\\d+)/(\\d+)/(\\d+)$")
	public void chooses_the_date_to_be(int month, int day, int year) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Select (.+) for location$")
	public void select_for_location(String loc) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^enters Notes (.+)$")
	public void enters_Notes(String note) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Creates a Basic Health History$")
	public void creates_a_Basic_Health_History() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Enters (\\d+) for weight$")
	public void enters_for_weight(int weight) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Enters (\\d+) for length$")
	public void enters_for_length(int weight) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^Enters (\\d+) for Head Circumference$")
	public void enters_for_Head_Circumference(int weight) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^selects (\\d+) (.+) for household smoking status$")
	public void selects_for_household_smoking_status(int smokingID, String smokingStatus) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^adds record$")
	public void adds_record() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Then("^a success message is displayed$")
	public void a_success_message_is_displayed() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
}
