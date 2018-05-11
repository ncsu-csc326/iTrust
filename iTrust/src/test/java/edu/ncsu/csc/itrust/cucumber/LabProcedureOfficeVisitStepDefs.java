 package edu.ncsu.csc.itrust.cucumber;
 
 import cucumber.api.java.en.Given;
 import cucumber.api.java.en.Then;
 import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.HospitalBean;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.HospitalsDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.model.prescription.PrescriptionMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
 
 
 public class LabProcedureOfficeVisitStepDefs {
	 
	 
		private PatientDAO patientController;
		private DataSource ds;
		private TestDataGenerator gen;
		private HospitalsDAO hospDAO;
		private PersonnelDAO persDAO;
		private OfficeVisitMySQL oVisSQL;
		private OfficeVisit oVis;
		private PrescriptionMySQL preSQL;
		private LabProcedureMySQL labSQL;
		
		private CPTCodeMySQL cptSQL;
		private ICDCodeMySQL icdSQL;
		private NDCCodeMySQL ndcSQL;
 
        public LabProcedureOfficeVisitStepDefs() {
        	this.ds = ConverterDAO.getDataSource();
    		this.patientController = new PatientDAO(TestDAOFactory.getTestInstance());
    		this.gen = new TestDataGenerator();
    		this.hospDAO = new HospitalsDAO(TestDAOFactory.getTestInstance());
    		this.persDAO = new PersonnelDAO(TestDAOFactory.getTestInstance());
    		this.oVisSQL = new OfficeVisitMySQL(ds);
    		this.oVis = new OfficeVisit();
    		this.preSQL = new PrescriptionMySQL(ds);	
    		this.labSQL = new LabProcedureMySQL(ds);
    		
    		this.cptSQL = new CPTCodeMySQL(ds);
    		this.icdSQL = new ICDCodeMySQL(ds);
    		this.ndcSQL = new NDCCodeMySQL(ds);
        }
        
        
        
        @Given("^I load uc11.sql$")
        public void loadData(){
        	try {
				gen.clearAllTables();
				gen.patient1();
	        	gen.hospitals();
	        	gen.appointmentType();
	        	gen.uc26();
	        	gen.uc11();
			} catch (SQLException | IOException e) {
				fail();
				e.printStackTrace();
			}
        }
			

        @When("^I add a new prescription with the following information: Medication: (.*) (.*), Dosage: (.*). Dates: (.*) to (.*), Instructions: (.*)")
        public void enterDiagnosisBlank(String medCode, String medName, String dosage, String date1, String date2, String instructions) {
        		List<OfficeVisit> oList;
				try {
					oList = oVisSQL.getAll();
					
					NDCCode nd = new NDCCode();
					nd.setCode(medCode);
					nd.setDescription(medName);
					
					Prescription pre = new Prescription();
	                MedicationBean medBean = new MedicationBean();
	                medBean.setNDCode(medCode);
	                pre.setOfficeVisitId(oList.get(oList.size() - 1).getVisitID());
	                pre.setPatientMID((long)1);
	                pre.setDrugCode(medBean);
	                pre.setInstructions(instructions);
	                pre.setDosage(Long.parseLong(dosage));
	                pre.setStartDate(LocalDate.parse(date1));
	                pre.setEndDate(LocalDate.parse(date2));
	                
					preSQL.add(pre);
					ndcSQL.add(nd);
				
				} catch (DBException | SQLException | FormValidationException e1) {
					System.out.println(e1.toString());
					fail();
					e1.printStackTrace();
				}
        	
        		
        }
        
        
       
        @When("^verify that it is there code: (.*), dosage (.*), start date (.*), end date (.*), and special instructions: (.*)$")
        public void newPrescrip(String code, String dosage, String start, String end, String instructions){
        	try {
    			List<Prescription> pList = preSQL.getPrescriptionsByMID((long)1);
    			Prescription prescrip = pList.get(pList.size() - 1);
    			Assert.assertEquals(code, prescrip.getCode() + "");
    			Assert.assertEquals(dosage, prescrip.getDosage() + ""); 
    	    	Assert.assertEquals(instructions, prescrip.getInstructions());
    			Assert.assertTrue(prescrip.getStartDate().toString().contains(start));
    			Assert.assertTrue(prescrip.getEndDate().toString().contains(end));
    		} catch (SQLException e) {
    			fail();
    			e.printStackTrace();
    		}
        }
        
        @When("^I add a prescription with backwards dates and the following information: Medication: (.*) (.*), Dosage: (.*). Dates: (.*) to (.*), Instructions: (.*)$")
        public void enterBackwardsDates(String medCode, String medName, String dosage, String date1, String date2, String instructions) {
        		List<OfficeVisit> oList;
				try {
					oList = oVisSQL.getAll();
					
					NDCCode nd = new NDCCode();
					nd.setCode(medCode);
					nd.setDescription(medName);
					
					Prescription pre = new Prescription();
	                MedicationBean medBean = new MedicationBean();
	                medBean.setNDCode(medCode);
	                pre.setOfficeVisitId(oList.get(oList.size() - 1).getVisitID());
	                pre.setPatientMID((long)1);
	                pre.setDrugCode(medBean);
	                pre.setInstructions(instructions);
	                pre.setDosage(Long.parseLong(dosage));
	                pre.setStartDate(LocalDate.parse(date1));
	                pre.setEndDate(LocalDate.parse(date2));
	                
					preSQL.add(pre);
					ndcSQL.add(nd);
				
				} catch (DBException | SQLException e1) {
					Assert.assertTrue(true);
					e1.printStackTrace();
				} catch (FormValidationException e) {
					Assert.assertTrue(true);
				}
        }
        
        
        @When("^delete the prescription you just made$")
        public void deletePrescrip(){
        	try {
				preSQL.remove(preSQL.getPrescriptionsForOfficeVisit((long)6).get(0).getId());
			} catch (SQLException e) {
				fail();
				e.printStackTrace();
			}    
        }
       
        @When("^I update notes to (.*), location to (.*), add CPT (.*) , (.*) to immunizations, delete (.*) from diagnosis$")
        public void updateHealthInfo(String newNotes, String newLocation, String newCPT, String newImmunization, String deleteThis ) {
        	List<OfficeVisit> oList;
        	
			try {
				oList = oVisSQL.getAll();
				OfficeVisit updateOV = oList.get(oList.size() - 1);
				
				updateOV.setNotes(newNotes);
				updateOV.setLocationID(newLocation);
				
				
				CPTCode cCode = new CPTCode(newCPT, newImmunization);
				cptSQL.add(cCode);
				icdSQL.delete(icdSQL.getByCode(deleteThis));
				
				oVisSQL.update(updateOV);
				
				
			} catch (DBException | SQLException | FormValidationException e) {
				System.out.println(e.toString());
				fail();
				//e.printStackTrace();
			}
			
        }
 
       
 
        @When("^For the Lab Procedures associated with the office visit, select LOINC (.*), (.*)with priority 1 and Lab Technician (.*)$")
        public void makeLabProc(String code, String description, String midLab) {
        	
        		List<OfficeVisit> oList;
				try {
					oList = oVisSQL.getAll();
					LabProcedure procedure = new LabProcedure();
	        		procedure.setOfficeVisitID(oList.get(oList.size() - 1).getVisitID());
	        		procedure.setStatus((long)1);
	        		procedure.setHcpMID(Long.parseLong("9000000000"));
	        		procedure.setLabProcedureCode(code);
	        		procedure.setPriority(1);
	        		procedure.setCommentary(description);
	        		procedure.setLabTechnicianID(Long.parseLong(midLab));//, status, hcpmid
					labSQL.add(procedure);
					
				} catch (DBException e) {
					e.printStackTrace();
					fail();
				}
        		
        		
				
        }
 
        @When("^Diagnosis: (.*), (.*), Medical Procedures: (.*), Immunizations: (.*), (.*)$")
        public void addDetails(String diagnosisCode, String diagnosisNotes, String medProcCode, String immunizationCode, String immunizationDescription) {
        		ICDCode icd = new ICDCode(diagnosisCode, diagnosisNotes, false);
                CPTCode cpt = new CPTCode(medProcCode, "notNamed");
                CPTCode cpt2 = new CPTCode(immunizationCode, immunizationDescription);
        		
                try {
					icdSQL.add(icd);
					cptSQL.add(cpt);
	                cptSQL.add(cpt2);
				} catch (SQLException | FormValidationException e) {
					System.out.println(e.toString());
					fail();
				}
        }
        
        @When("^User enters date: (.*), location: (.*), appointment type: (.*), notes: (.*), and select send a bill true, weight: (.*), height: (.*), blood pressure (.*), ldl: (.*), hdl: (.*), Triglycerides: (.*), house smoke: (.*), patient smoke: (.*)$")
        public void officeVisitBasics(String date, String location, String appType, String notes, String weight, String height, String blood, String ldl, String hdl, String trig, String house, String patient){
        	oVis.setDate(LocalDateTime.parse(date));
        	oVis.setLocationID(location);
        	oVis.setApptTypeID(Long.parseLong(appType));
        	oVis.setNotes(notes);
        	oVis.setSendBill(true);
        	oVis.setWeight(Float.parseFloat(weight));
            oVis.setHeight(Float.parseFloat(height));
            oVis.setBloodPressure(blood);
            oVis.setLDL(Integer.parseInt(ldl));
            oVis.setHDL(Integer.parseInt(hdl));
            oVis.setTriglyceride(Integer.parseInt(trig));
            oVis.setHouseholdSmokingStatus(Integer.parseInt(house));
            oVis.setPatientSmokingStatus(Integer.parseInt(patient));
            oVis.setPatientMID((long)1);
            try {
				oVisSQL.add(oVis);
			} catch (DBException e) {
				System.out.println(e.toString());
				fail();
				//e.printStackTrace();
			}
        }
        
        @Then("^After the office visit has been (.*) it does include the following basic health metrics: height: (.*) in, weight: (.*) lbs, blood pressure: (.*), LDL: (.*), HDL: (.*), Triglycerides: (.*), Household Smoking Status: (.*), Patient Smoking Status: (.*)$")
        public void successfulUpdate(String createOrUpdate, String height, String weight, String blood, String ldl, String hdl, String trig, String house, String patient) {
	        	List<OfficeVisit> oList;
	    		
				try {
					oList = oVisSQL.getAll();
		        	OfficeVisit visit = oList.get(oList.size() - 1);
		        	Assert.assertEquals(height, visit.getHeight() + "");
		        	Assert.assertEquals(weight, visit.getWeight() + "");
		        	Assert.assertEquals(blood, visit.getBloodPressure());
		        	Assert.assertEquals(ldl, visit.getLDL() + "");
		        	Assert.assertEquals(hdl, visit.getHDL() + "");
		        	Assert.assertEquals(trig, visit.getTriglyceride() + "");
		        	Assert.assertEquals(house, visit.getHouseholdSmokingStatus() + "");
		        	Assert.assertEquals(patient, visit.getPatientSmokingStatus() + "");
				} catch (DBException e) {
					e.printStackTrace();
					fail();
				}
        }
 
       

        @Then("^After the office visit has been created the Location: (.*), notes: (.*), sendbill: true$")
        public void locationNotesSendBill(String location, String notes) {
        	List<OfficeVisit> oList;
		
			try {
				oList = oVisSQL.getAll();
	        	OfficeVisit visit = oList.get(oList.size() - 1);
	        	Assert.assertEquals(location, visit.getLocationID());
	        	Assert.assertEquals(notes, visit.getNotes());
	        	Assert.assertEquals(true, visit.getSendBill());
			} catch (DBException e) {
				e.printStackTrace();
				fail();
			}
			
        }
        
        @Then("^After the office visit has been updated the type saved for the office visit is 1$")
        public void appointmentType() {
        	List<OfficeVisit> oList;
    		
			try {
				oList = oVisSQL.getAll();
	        	OfficeVisit visit = oList.get(oList.size() - 1);
	        	Assert.assertEquals("1", visit.getApptTypeID() + "");
			} catch (DBException e) {
				e.printStackTrace();
				fail();
			}
        }
        
        @Then("^After the office visit has been updated, it does NOT include the following Diagnosis of (.*), (.*)$")
        public void successDeletion(String diagnosis, String diagnosisNotes) {
        	List<OfficeVisit> oList;
    		
			try {
				oList = oVisSQL.getAll();
	        	OfficeVisit visit = oList.get(oList.size() - 1);
	        	List<ICDCode> icdList = icdSQL.getAll();
	        	
	        	for(int i = 0; i < icdList.size(); i++){
	        		if (icdList.get(i).getCode().equals(diagnosis)){
	        			fail();
	        			//otherwise it is not there and it passes
	        		}
	        	}
	        	
	        	
			} catch (DBException | SQLException e) {
				e.printStackTrace();
				fail();
			}
        }
 
        @Then("^After the office visit has been updated, it does include the following Immunization (.+), (.*)")
        public void successAddingImmunizaiont(String immuneCode, String immuneName) {
        	try {
				Assert.assertEquals(immuneCode, cptSQL.getByCode(immuneCode).getCode());
				Assert.assertEquals(immuneName, cptSQL.getByCode(immuneCode).getName());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
 
        @Then("^After the office visit has been created, its prescription info is: drugname: (.*), Code: (.*), Dosage: (.*), Start Date: (.*), End Date: (.*), Special instructions: (.*)$")
        public void prescripExistsInVisit(String name, String code, String dosage, String dateStart, String dateEnd, String spInstruct) {
        	
			try {
				List<Prescription> pList = preSQL.getPrescriptionsByMID((long)1);
				Prescription prescrip = pList.get(pList.size() - 1);
				Assert.assertEquals(code, prescrip.getCode() + "");
				Assert.assertEquals(dosage, prescrip.getDosage() + "");
		    	Assert.assertEquals(spInstruct, prescrip.getInstructions());
				Assert.assertTrue(prescrip.getStartDate().toString().contains(dateStart));
				Assert.assertTrue(prescrip.getEndDate().toString().contains(dateEnd));
			} catch (SQLException e) {
				fail();
				e.printStackTrace();
			}
        	
        }

        @Then("^After the office visit has been created, Diagnosis: (.*), (.*), Medical Procedures:  (.*), Immunizations: (.*), (.*)$")
        public void doubleChecking(String diagnosisCode, String diagnosisNotes, String medProcCode, String immunizationCode, String immunizationDescription) {
                
        	
        	try {
				Assert.assertEquals(medProcCode, cptSQL.getByCode(medProcCode).getCode());
				Assert.assertEquals(diagnosisCode, icdSQL.getByCode(diagnosisCode).getCode());
				Assert.assertEquals(diagnosisNotes, icdSQL.getByCode(diagnosisCode).getName());
				Assert.assertEquals(immunizationCode, cptSQL.getByCode(immunizationCode).getCode());
				Assert.assertEquals(immunizationDescription, cptSQL.getByCode(immunizationCode).getName());
			} catch (SQLException e) {
				fail();
				e.printStackTrace();
			}
        	
        	
        	
        }
 
        @Then("^After the office visit has been created, its Lab Procedures include (.*), (.*) with priority 1 and Lab Technician (.*)$")
        public void logEvent(String procCode, String procName, String labMid) {
        	List<OfficeVisit> oList;
			try {
				oList = oVisSQL.getAll();
				OfficeVisit oVis = oList.get(oList.size() - 1);
				
				List <LabProcedure> labProcs = labSQL.getAll();
				LabProcedure labProc = labProcs.get(labProcs.size() - 1);
				Assert.assertEquals(procName, labProc.getCommentary());
				Assert.assertEquals(procCode, labProc.getLabProcedureCode());
				Assert.assertEquals(labMid, labProc.getLabTechnicianID() + "");
			} catch (DBException e) {
				fail();
				e.printStackTrace();
			}
			
        }
 
        @Then("^None should be on record$")
        public void noPrescrips() {
        	try {
				Assert.assertEquals(0, preSQL.getPrescriptionsForOfficeVisit((long)6).size());
			} catch (SQLException e) {
				fail();
				e.printStackTrace();
			}
        }
 
        @Then("^No lab record are present when I check for baby Programmer id: (.*)$")
        public void noLabRecords(String id) {
        	try {
				Assert.assertEquals(0, preSQL.getPrescriptionsByMID(Long.parseLong(id)).size());
			} catch (NumberFormatException | SQLException e) {
				fail();
				e.printStackTrace();
			}
        }
 
 }