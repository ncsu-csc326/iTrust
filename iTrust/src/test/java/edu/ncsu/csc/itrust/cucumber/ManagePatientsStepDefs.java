package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.action.AddPatientAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import cucumber.api.java.en.Then;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

public class ManagePatientsStepDefs {
//	@Inject private long loggedInMID;
	private UserDataShared userData;
	private PatientDataShared patientData;
	
	public ManagePatientsStepDefs(UserDataShared currentUser, PatientDataShared currentPatient){
		this.userData = currentUser;
		this.patientData = currentPatient;
	}
	
	@When("^I enter a (.+), (.+), and (.+) for a new patient and submit the information$")
	public void HCP_enters_new_patient_name_and_email(String firstName, String lastName, String email) {
		if(this.userData.loginID>0){
			AddPatientAction testAction = new AddPatientAction(TestDAOFactory.getTestInstance(), this.userData.loginID);
			PatientBean newPatient = new PatientBean();
			newPatient.setFirstName(firstName);
			newPatient.setLastName(lastName);
			newPatient.setEmail(email);
			try {
				long patientMID = 0;
				patientMID = testAction.addPatient(newPatient, this.userData.loginID);
				this.patientData.patientID = patientMID;
			} catch (FormValidationException e) {
				Assert.fail("Unable to create Patient - FormValidationException");
			} catch (ITrustException e) {
				Assert.fail("Unable to create Patient ITrustException");
			}
		}else{
			Assert.fail("Not logged in");
		}
	}
	
	@Given("^a Patient has been created$")
	public void create_patient(){

		if(this.userData.loginID>0){
			AddPatientAction testAction = new AddPatientAction(TestDAOFactory.getTestInstance(), this.userData.loginID);
			PatientBean newPatient = new PatientBean();
			newPatient.setFirstName("Cucumber");
			DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("YYYYMMddhhmmss");
			String curTime = LocalDateTime.now().format(dtFormatter);
			newPatient.setLastName("Test");
			newPatient.setEmail("Cucumber_"+curTime+"@test.com");
			try {
				long patientMID = 0;
				patientMID = testAction.addPatient(newPatient, this.userData.loginID);
				this.patientData.patientID = patientMID;
			} catch (FormValidationException e) {
				Assert.fail("Unable to create Patient - FormValidationException");
			} catch (ITrustException e) {
				Assert.fail("Unable to create Patient ITrustException");
			}
		}else{
			Assert.fail("Not logged in");
		}
	
		
	}
		//ToDo: Add check to verify that patient was created recently?
		@Then("^the new patient is assigned an MID$")
		public void patient_assigned_MID(){
			Assert.assertTrue(this.patientData.patientID>0);
			
			
		}

		//ToDo: Add check to verify that patient was created recently?
		@Then("^all initial values except MID default to null$")
		public void new_patient_values_default_null(){
			PatientDAO testPatientDAO = new PatientDAO(TestDAOFactory.getTestInstance());
			try {
				PatientBean newPatient = testPatientDAO.getPatient(this.patientData.patientID);
				boolean b = true;
				//Check that age is 0
				if((newPatient.getAge() !=0) || (newPatient.getAgeInDays() !=0) || (newPatient.getAgeInWeeks() !=0)){
					Assert.fail("Age not equal to 0");
					b = false;
				}
				if(!newPatient.getAlternateName().isEmpty() && (newPatient.getAlternateName() != null)){
					Assert.fail("Alternate Name not null");
					b = false;
				}
				if((!newPatient.getBloodType().getName().contains("N")) ||(!newPatient.getBloodType().getName().contains("S")) ){
					Assert.fail("Blood Type not null");
					b = false;
				}
				if(!newPatient.getCauseOfDeath().isEmpty() && (newPatient.getCauseOfDeath() != null)){
					Assert.fail("Cause of Death not null");
					b = false;
				}
				if(!newPatient.getCity().isEmpty() && (newPatient.getCity() != null)){
					Assert.fail("City not null");
					b = false;
				}
				if(!newPatient.getCreditCardNumber().isEmpty() && (newPatient.getCreditCardNumber() != null)){
					Assert.fail("Credit Card Number not null");
					b = false;
				}
				if(!newPatient.getCreditCardType().isEmpty() && (newPatient.getCreditCardType() != null)){
					Assert.fail("Credit Card Type not null");
					b = false;
				}
				if(!newPatient.getCreditCardType().isEmpty() && (newPatient.getCreditCardType() != null)){
					Assert.fail("Credit Card Type not null");
					b = false;
				}
				//date of birth
				if(!DateUtils.isSameDay(newPatient.getDateOfBirth(), java.util.Date.from(Instant.now()))){
					Assert.fail("Date of Birth not correct");
					b = false;
				}
				if(!newPatient.getDateOfDeactivationStr().isEmpty() && (newPatient.getDateOfDeactivationStr() != null)){
					Assert.fail("Date of Deactivation not null");
					b = false;
				}
				if(!(newPatient.getDateOfDeath() == null) ){
					Assert.fail("Date of Death not null");
					b = false;
				}
				if(!newPatient.getDirectionsToHome().isEmpty() && (newPatient.getDirectionsToHome() != null)){
					Assert.fail("Directions to Home not null");
					b = false;
				}
				if(!newPatient.getEmergencyName().isEmpty() && (newPatient.getEmergencyName() != null)){
					Assert.fail("Emergency Name not null");
					b = false;
				}
				if(!newPatient.getEmergencyPhone().isEmpty() && (newPatient.getEmergencyPhone() != null)){
					Assert.fail("Emergency Phone not null");
					b = false;
				}
				
				if(!(newPatient.getEthnicity().getName().contains("Not Specified"))){
					Assert.fail("Ethnicity not null");	
					b = false;
				}
				if(!(newPatient.getFatherMID().contains("0")) && !(newPatient.getFatherMID() == null)){
					Assert.fail("Father MID not null");
					b = false;
				}
				if(!(newPatient.getGender().getName().contains("Not Specified"))){
					Assert.fail("Gender not null");	
					b = false;
				}
				
				if(!newPatient.getIcAddress1().isEmpty() && (newPatient.getIcAddress1() != null)){
					Assert.fail("IC Address 1 not null");
					b = false;
				}
				if(!newPatient.getIcAddress2().isEmpty() && (newPatient.getIcAddress2() != null)){
					Assert.fail("IC Address 2 not null");
					b = false;
				}
				if(!newPatient.getIcCity().isEmpty() && (newPatient.getIcCity() != null)){
					Assert.fail("IC City not null");
					b = false;
				}
				if(!newPatient.getIcID().isEmpty() && (newPatient.getIcID() != null)){
					Assert.fail("IC ID not null");
					b = false;
				}
				if(!newPatient.getIcName().isEmpty() && (newPatient.getIcName() != null)){
					Assert.fail("IC Name not null");
					b = false;
				}
				if(!newPatient.getIcPhone().isEmpty() && (newPatient.getIcPhone() != null)){
					Assert.fail("IC Phone not null");
					b = false;
				}
				// States cannot be null - no verification for ICState
				if(!newPatient.getIcZip().isEmpty() && (newPatient.getIcZip() != null)){
					Assert.fail("IC Zip not null");
					b = false;
				}
				if(!newPatient.getLanguage().isEmpty() && (newPatient.getLanguage() != null)){
					Assert.fail("Language not null");
					b = false;
				}

				if(!(newPatient.getMotherMID().contains("0")) && (newPatient.getMotherMID() != null)){
					Assert.fail("Mother MID not null");
					b = false;
				}
				if(!newPatient.getPhone().isEmpty() && (newPatient.getPhone() != null)){
					Assert.fail("Phone not null");
					b = false;
				}
				
				if(!newPatient.getReligion().isEmpty() && (newPatient.getReligion() != null)){
					Assert.fail("Religion not null");
					b = false;
				}
				if(!newPatient.getSpiritualPractices().isEmpty() && (newPatient.getSpiritualPractices() != null)){
					Assert.fail("Spiritual Practices not null");
					b = false;
				}
				//States cannot be null, no verification for state
				if(!newPatient.getStreetAddress1().isEmpty() && (newPatient.getStreetAddress1() != null)){
					Assert.fail("Street Address 1 not null");
					b = false;
				}
				if(!newPatient.getStreetAddress2().isEmpty() && (newPatient.getStreetAddress2() != null)){
					Assert.fail("Street Address 2 not null");
					b = false;
				}

				if(!newPatient.getTopicalNotes().isEmpty() && (newPatient.getTopicalNotes() != null)){
					Assert.fail("Topical Notes not null");
					b = false;
				}
				if(!newPatient.getZip().isEmpty() && (newPatient.getZip() != null)){
					Assert.fail("Zip Code not null");
					b = false;
				}

				Assert.assertTrue(b);
			} catch (DBException e) {
				Assert.fail("Error in retrieving PatientName");
			}
			
		}

		//ToDo: Add check to verify that patient was created recently?
		@Then("^(.+) (.+) is created$")
		public void patient_name_created(String fName, String lName){
			PatientDAO testPatientDAO = new PatientDAO(TestDAOFactory.getTestInstance());
			try {
				List<PatientBean> matchingPatientBeans = testPatientDAO.fuzzySearchForPatientsWithName(fName, lName);
				Assert.assertTrue(matchingPatientBeans.size()>0);
				boolean b = false;
				for(PatientBean pb : matchingPatientBeans){
					String f = pb.getFirstName();
					String l = pb.getLastName();
					if((f.compareToIgnoreCase(fName) ==0) && (l.compareToIgnoreCase(lName) ==0)){
						b = true;
						break;
						
					}
				}
				Assert.assertTrue(b);
			} catch (DBException e) {
				Assert.fail("Error in retrieving PatientName");
			}
			
		}


}
