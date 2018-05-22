package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecordMySQL;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;


public class EmergencyHealthRecordsStepDefs {

	private PatientDAO patientController;
	private DataSource ds;
	private TestDataGenerator gen;	
	private OfficeVisitMySQL oVisSQL;	
	private EmergencyRecordMySQL erSQL;
	private AllergyDAO allergyDAO;
	
	public EmergencyHealthRecordsStepDefs(){
		
		this.ds = ConverterDAO.getDataSource();
		this.patientController = new PatientDAO(TestDAOFactory.getTestInstance());
		this.gen = new TestDataGenerator();
		this.oVisSQL = new OfficeVisitMySQL(ds);
	}

	@Given("^I load uc21.sql$")
	public void loadTables(){
		try {
			gen.clearAllTables();
	        gen.ndCodes();
	        gen.ndCodes1();
	        gen.ndCodes100();
	        gen.ndCodes2();
	        gen.ndCodes3();
	        gen.ndCodes4();
	        gen.uc21();
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Given("^No office visits exist for patient (.+)$")
	public void noVisits(int pid){
		try {
			Assert.assertTrue(oVisSQL.getVisitsForPatient((long)pid).isEmpty());
		} catch (DBException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@When("^Go to the Emergency Patient Report.$")
	public void goToEmerReport(){
		this.allergyDAO = new AllergyDAO(TestDAOFactory.getTestInstance());
		this.erSQL = new EmergencyRecordMySQL(ds, allergyDAO);
	}
	
	@Then("^mid: (.+) name: (.*), age: (.+), gender: (.*), emergency contact: (.*), phone: (.*), allergies: (.*), blood type: (.*), diagnoses: (.*), and (.*), prescriptions: (.*), and (.*), immunizaitons: (.*)$")
	public void checkResults(int mid, String name, int age, String gender, String contactName, String contactPhone, String allergies, String bloodType, String diagnoses1, String diagnoses2, String prescrip1, String prescrip2, String immunization ){
		try {
			
			Assert.assertEquals(name, erSQL.getEmergencyRecordForPatient((long)mid).getName());
			Assert.assertEquals(age, erSQL.getEmergencyRecordForPatient((long)mid).getAge());
			Assert.assertEquals(gender, erSQL.getEmergencyRecordForPatient((long)mid).getGender());
			Assert.assertEquals(contactName, erSQL.getEmergencyRecordForPatient((long)mid).getContactName());
			Assert.assertEquals(contactPhone, erSQL.getEmergencyRecordForPatient((long)mid).getContactPhone());
			Assert.assertEquals(allergies, erSQL.getEmergencyRecordForPatient((long)mid).getAllergies().toString());
			Assert.assertEquals(bloodType, erSQL.getEmergencyRecordForPatient((long)mid).getBloodType());
			
			
			List<Immunization> immuneList = erSQL.getEmergencyRecordForPatient((long)mid).getImmunizations();
			Assert.assertEquals(immunization, immuneList.get(0).getCode());
			
			List<Diagnosis> diagnosesList = erSQL.getEmergencyRecordForPatient((long)mid).getDiagnoses();
			Assert.assertEquals(diagnoses1, diagnosesList.get(0).getCode());
			Assert.assertEquals(diagnoses2, diagnosesList.get(1).getCode());
			
			List<Prescription> prescriptionList = erSQL.getEmergencyRecordForPatient((long)mid).getPrescriptions();
			Assert.assertEquals(prescrip1, prescriptionList.get(0).getCode());
			Assert.assertEquals(prescrip2, prescriptionList.get(1).getCode());
			
		} catch (DBException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Then("^mid: (.+), name: (.*), age: (.+), gender: (.*), emergency contact: (.*), phone: (.*), no allergies, blood type: (.*), diagnosis: (.*), prescriptions: (.*), no immunizations$")
	public void checkResults2(int mid, String name, int age, String gender, String contactName, String contactPhone, String bloodType, String diagnoses1, String prescrip1){
		try {
					
			Assert.assertEquals(name, erSQL.getEmergencyRecordForPatient((long)mid).getName());
			Assert.assertEquals(age, erSQL.getEmergencyRecordForPatient((long)mid).getAge());
			Assert.assertEquals(gender, erSQL.getEmergencyRecordForPatient((long)mid).getGender());
			Assert.assertEquals(contactName, erSQL.getEmergencyRecordForPatient((long)mid).getContactName());
			Assert.assertEquals(contactPhone, erSQL.getEmergencyRecordForPatient((long)mid).getContactPhone());
			Assert.assertEquals(bloodType, erSQL.getEmergencyRecordForPatient((long)mid).getBloodType());
			
			List<Diagnosis> diagnosesList = erSQL.getEmergencyRecordForPatient((long)mid).getDiagnoses();
			Assert.assertEquals(diagnoses1, diagnosesList.get(0).getCode());
			
			List<Prescription> prescriptionList = erSQL.getEmergencyRecordForPatient((long)mid).getPrescriptions();
			Assert.assertEquals(prescrip1, prescriptionList.get(0).getCode());
			
			Assert.assertTrue(erSQL.getEmergencyRecordForPatient((long)mid).getAllergies().size() == 0);
			Assert.assertTrue(erSQL.getEmergencyRecordForPatient((long)mid).getImmunizations().size() == 0);
		
		} catch (DBException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Then("^Error: The patient (.*) does not exist$")
	public void noPatient(String mid){
		 try {
				Assert.assertFalse(patientController.checkPatientExists(Long.parseLong(mid)));
			} catch (NumberFormatException e) {
				fail();
				e.printStackTrace();
			} catch (DBException e) {
				fail();
				e.printStackTrace();
			}
	}
	
	@Then("^Office visit info missing for (.+) so no prescriptions, diagnoses, allergies, or immunizations$")
	public void noOfficeVisitForPatient(int mid){
		try {
			
			Assert.assertTrue(erSQL.getEmergencyRecordForPatient((long)mid).getPrescriptions().size() == 0);
			Assert.assertTrue(erSQL.getEmergencyRecordForPatient((long)mid).getDiagnoses().size() == 0);
			Assert.assertTrue(erSQL.getEmergencyRecordForPatient((long)mid).getAllergies().size() == 0);
			Assert.assertTrue(erSQL.getEmergencyRecordForPatient((long)mid).getImmunizations().size() == 0);
			
		} catch (DBException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	
}
