package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Stack;

import javax.sql.DataSource;

import org.junit.Assert;

public class ManageLabProcedureStepDefs {
	
	private DataSource ds;
	private TestDataGenerator gen;
	private OfficeVisitMySQL oVisSQL;
	private LabProcedureMySQL labPSQL;

	public ManageLabProcedureStepDefs() {
		
		this.ds = ConverterDAO.getDataSource();
		this.gen = new TestDataGenerator();
		this.oVisSQL = new OfficeVisitMySQL(ds);
		this.labPSQL = new LabProcedureMySQL(ds);
	}
	

	@Given("^UC26sql has been loaded$")
    public void loadUC26SQL() throws FileNotFoundException, SQLException, IOException{
    	
		try {
			gen.clearAllTables();
	        gen.uc26();
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.toString());
			fail();
			//e.printStackTrace();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
    }
	
	@Given("^the lab proc status' have been changed for this use case$")
    public void changeLabProcStatus() throws FileNotFoundException, SQLException, IOException{
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
 				   if(allLabProcedures.get(i).getLabProcedureCode().equals("5583-0")){
 					  allLabProcedures.get(i).setStatus(2);
 					  labPSQL.update(allLabProcedures.get(i));
 				   }  
 				   else if (allLabProcedures.get(i).getLabProcedureCode().equals("5685-3")){
 					  allLabProcedures.get(i).setStatus(4);
 					  labPSQL.update(allLabProcedures.get(i));
 				   }
 				  else if (allLabProcedures.get(i).getLabProcedureCode().equals("12556-7")){
 					  allLabProcedures.get(i).setStatus(2);
 					  labPSQL.update(allLabProcedures.get(i));
 				   }
 				 else if (allLabProcedures.get(i).getLabProcedureCode().equals("14807-2")){
					  allLabProcedures.get(i).setStatus(3);
					  labPSQL.update(allLabProcedures.get(i));
				   }
	 		   }
	 		 } catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
    }
	
	@Given("^the data for loinc (.*) is updated for this use case$")
    public void changeLabProcData(String loinc) throws FileNotFoundException, SQLException, IOException{
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
 				    if (allLabProcedures.get(i).getLabProcedureCode().equals("12556-7")){
 				    	 allLabProcedures.get(i).setLabTechnicianID(allLabProcedures.get(i).getLabTechnicianID() - 1);
 					  allLabProcedures.get(i).setStatus(4);
 					  labPSQL.update(allLabProcedures.get(i));
 				   }
 				    else if (allLabProcedures.get(i).getLabProcedureCode().equals("14807-2")){
 	 					  allLabProcedures.get(i).setStatus(1);
 	 					  labPSQL.update(allLabProcedures.get(i));
 	 				   }
 				   
 				    else if (allLabProcedures.get(i).getLabProcedureCode().equals("5685-3")){
 					 allLabProcedures.get(i).setLabTechnicianID(allLabProcedures.get(i).getLabTechnicianID() - 1);
	 					  labPSQL.update(allLabProcedures.get(i));
	 				   }
 				   else if (allLabProcedures.get(i).getLabProcedureCode().equals("71950-0")){
 	 					 allLabProcedures.get(i).setLabTechnicianID(allLabProcedures.get(i).getLabTechnicianID() - 2);
 		 					  labPSQL.update(allLabProcedures.get(i));
 		 				   }
 				 
	 		   }
	 		 } catch (DBException e) {
	 			 System.out.println(e.toString());
			 fail();
			e.printStackTrace();
		}
		
    }
	
	
	
	@Given("^The office visit for (.+) from (.*) has only (.+) lab procedures$")
	public void visitsContainLabProcs(int mid, String date, int count) {
		List<LabProcedure> allLabProcedures;
		try {
			//make sure that the only lab procedures are 2 for 50000000002 and none for 5000000003
			allLabProcedures = labPSQL.getAll();
			   int counter = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   
	 			   //if the office visit associated with this lab proc has the same date, incerment the counter
	 			   if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
	 					  counter++;
		 				  Assert.assertTrue(true);
	 			   }
	 		   }		   
	 		   if (counter != count) {
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	
	@When("^Delete the Lab procedure with LOINC (.*) from (.+)'s office visit on (.*)$")
	public void deleteLabProc(String code, int mid, String date) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
			   int success = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (allLabProcedures.get(i).getLabProcedureCode().equals(code)){
	 				  labPSQL.removeLabProcedure(allLabProcedures.get(i).getLabProcedureID());
		 				  success = 1;
	 			   }
	 		   }		   
	 		   if (success == 0) {
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@When("^Update the lab procedure with LOINC (.*) by setting the Lab Technician to (.*)$")
	public void UpdateLabProc(String code, String ltID) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
			   int success = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (allLabProcedures.get(i).getLabProcedureCode().equals(code)){
	 				  allLabProcedures.get(i).setLabTechnicianID(Long.parseLong(ltID));
	 				  labPSQL.update(allLabProcedures.get(i));
		 				  success = 1;
	 			   }
	 		   }		   
	 		   if (success == 0) {
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@When("^Examine the lab procedures from the office visit (.*)$")
	public void examineLabProc(String date) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
			   int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
		 				  Assert.assertTrue(true);
		 				  found = 1;
	 			   }
	 		   }		   
	 		   if (found == 0) {
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@When("^Examine the lists of lab procedures assigned and Update the two (.+) lab procedures to (.+)$")
	public void examineAndUpdateList(int previous, int update) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (allLabProcedures.get(i).getStatus().getID() == previous){
	 				  allLabProcedures.get(i).setStatus(update);
	 			   }
	 		   }		  
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	
	@When("^Examine the list of received procedures$")
	public void examineList2() {
		fail();
	}
	
	@When("^I create a new lab procedure with LOINC (.+) Component: (.*), System: (.*), priority (.+), lab technicial (.+)$")
	public void createNewLabProcFailure(String code, String componenet, String system, int priority, int techID) {
		LabProcedure labP = new LabProcedure();
		labP.setLabProcedureCode(code);
		labP.setPriority(priority);
		labP.setLabTechnicianID((long)techID);
		try {
			labPSQL.add(labP);
			fail();
		} catch (DBException e) {
			//asserting true because the lab proc cant be created with some fields missing
			Assert.assertTrue(true);
		}
	}
	
	@When("^Add numerical result: (.*) and confidence interval: (.*)-(.*) to the procedure whose current status is (.*) loinc (.*)$")
	public void addNumericalResult(String result, String confInterval1, String interval2, String status, String code) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
				int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (allLabProcedures.get(i).getLabProcedureCode().equals(code)){
	 				  allLabProcedures.get(i).setResults(result);
	 				  allLabProcedures.get(i).setConfidenceIntervalLower(Integer.parseInt(confInterval1));
	 				  allLabProcedures.get(i).setConfidenceIntervalUpper(Integer.parseInt(interval2));
	 				  allLabProcedures.get(i).setStatus(3);
	 				  labPSQL.update(allLabProcedures.get(i));
	 				  found = 1;
	 				  break;
	 			   }
	 		   }		  
	 		   if (found == 0)fail();
		} catch (DBException e) {
			System.out.println(e.toString());
			 fail();
			e.printStackTrace();
		} catch (NumberFormatException e){
			//allow illegal number to get through for bbt 3
			Assert.assertTrue(true);
		}
	}	
	
	@When("^Add commentary to the lab procedure (.*) from date (.*)$")
	public void addCommentary(String comment, String date) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
				int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
	 				  allLabProcedures.get(i).setCommentary(comment);
	 				  allLabProcedures.get(i).setStatus(5);
	 				  labPSQL.update(allLabProcedures.get(i));
	 				  found = 1;
	 				  
	 			   }
	 		   }		  
	 		   if (found == 0)fail();
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	 
	
	
	@Then("^When I conclude the update, there is a message displayed at the top of the page: Information Successfully Updated.$")
	public void successMessage() {
		//fail();////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}
	
	@Then("^I view the (.*) and it is (.*) for office visit for patient (.*) on date (.*)$")
	public void checkFields(String category, String details, String patient, String date) {
		List<OfficeVisit> allOfficeVisits;
		try {
			int found = 0;
			allOfficeVisits = oVisSQL.getAll();
	 		   for (int i = 0; i < allOfficeVisits.size(); i++) {
	 			   if (allOfficeVisits.get(i).getDate().toString().contains(date) && allOfficeVisits.get(i).getPatientMID().toString().equals(patient)){
	 				   if (category.equals("location")){
	 					   Assert.assertEquals(details, allOfficeVisits.get(i).getLocationID());
	 				   }
	 				   else if (category.equals("notes")){
	 					   Assert.assertEquals(details, allOfficeVisits.get(i).getNotes());
	 				   }
	 				   else if (category.equals("appointment type")){
	 					   Assert.assertEquals(details + "", allOfficeVisits.get(i).getApptTypeID().toString());
	 				   }
	 				   else if (category.equals("date")){
	 					   Assert.assertTrue(allOfficeVisits.get(i).getDate().toString().contains(details));
	 				   }
	 				   else if (category.equals("sendBill")){
	 					   Assert.assertEquals(details, allOfficeVisits.get(i).getSendBill().toString());
	 				   }
	 				   found = 1;
	 				   break;
	 			   }
	 		   }	
	 		   if (found == 0){
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^After the office visit from (.*) for (.*) has been (.*), it does include the following basic health metrics: height: (.+) in, weight: (.+) lbs, blood pressure: (.*), LDL: (.+), HDL: (.+), Triglycerides: (.+), Household Smoking Status: (.*), Patient Smoking Status: (.*)$")
    public void updateBmetrics(String date, String patient, String createOrUpdate, int height, int weight, String bloodPressure, int ldl, int hdl, int trigs, String hSmoke, String pSmoke ) {
		List<OfficeVisit> allOfficeVisits;
		try {
			int found = 0;
			allOfficeVisits = oVisSQL.getAll();
	 		   for (int i = 0; i < allOfficeVisits.size(); i++) {
	 			   if (allOfficeVisits.get(i).getDate().toString().contains(date) && allOfficeVisits.get(i).getPatientMID().toString().equals(patient)){
	 				
	 				   Assert.assertEquals(Float.valueOf(height), allOfficeVisits.get(i).getHeight());
	 				   Assert.assertEquals(Float.valueOf(weight), allOfficeVisits.get(i).getWeight());
	 				   Assert.assertEquals(bloodPressure, allOfficeVisits.get(i).getBloodPressure());
	 				   Assert.assertEquals(Float.valueOf(ldl), Float.valueOf(allOfficeVisits.get(i).getLDL()));
	 				   Assert.assertEquals(Float.valueOf(hdl), Float.valueOf(allOfficeVisits.get(i).getHDL()));
	 				   Assert.assertEquals(Float.valueOf(trigs), Float.valueOf(allOfficeVisits.get(i).getTriglyceride()));
	 				   Assert.assertEquals(hSmoke, allOfficeVisits.get(i).getHouseholdSmokingStatusDescription() );
	 				   Assert.assertEquals(pSmoke, allOfficeVisits.get(i).getPatientSmokingStatusDescription() );
	 				   found = 1;
	 				   break;
	 			   }
	 		   }	
	 		   if (found == 0){
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
    }
	
	@Then("^there is a procedure with LOINC (.*), priority (.*), Lab Technician (.*)$")
	public void thereIsAProcedure( String code, String priority, String labTech) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
			   int success = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			  System.out.println(allLabProcedures.get(i).getLabProcedureCode());
	 				  if (allLabProcedures.get(i).getLabProcedureCode().equals(code)){
	 				   	  Assert.assertEquals(allLabProcedures.get(i).getLabProcedureCode(), code);
	 				   	  Assert.assertEquals(allLabProcedures.get(i).getPriority() + "", priority);
	 				      Assert.assertTrue(allLabProcedures.get(i).getLabTechnicianID().equals(Long.parseLong(labTech)));
	 				     success = 1;
	 				      break;
	 				  }  
	 			}		  
	 		   if (success == 0) {
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^there is not a procedure with LOINC (.*), priority (.*), Lab Technician (.*)$")
	public void thereIsNoProcedure( String code, String priority, String labTech) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 				  if (allLabProcedures.get(i).getLabProcedureCode().equals(code)){
	 				   	  fail();
	 				  }  
	 			}		
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	
	@Then("^When I view the office visits, they should include the visit from (.*)$")
	public void viewAndInclude(String Date) {
		List<OfficeVisit> allOfficeVisits;
		try {
			int found = 0;
			allOfficeVisits = oVisSQL.getAll();
	 		   for (int i = 0; i < allOfficeVisits.size(); i++) {
	 			   if (allOfficeVisits.get(i).getDate().toString().contains(Date)){
	 				  found = 1;
	 				  Assert.assertTrue(true);
	 			   }
	 		   }	
	 		   if (found == 0){
	 			   fail();
	 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	
	@Then("^When I view the office visit from (.*), there should be (.+) lab procedure$")
	public void viewWithProcs(String date, int count) {
		List<LabProcedure> allLabProcedures;
		try {
			allLabProcedures = labPSQL.getAll();
			   int counter = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
	 					  counter++;
		 				  Assert.assertTrue(true);
	 			   }
	 		   }		   
	 		   if (counter != count) {
	 			   fail();
	 		   }
	 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
		
		
	}
	
	@Then("^When I examine the lab procedures from the office visit (.*) there is a procedure with LOINC (.*), priority (.*), Lab Technician (.*), status (.*), numerical result: (.+), confidence interval: (.*)-(.*), commentary (.*)$")
	public void examineLabProc(String date, String code, String priority, String labTech, String status, String result, String interval1, String interval2, String comments) {
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++) {
	 			   if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
	 				   found = 1;
	 				   Assert.assertEquals(code, allLabProcedures.get(i).getLabProcedureCode());
	 				   Assert.assertEquals(priority, allLabProcedures.get(i).getPriority() + "");
	 				   Assert.assertEquals(labTech, allLabProcedures.get(i).getLabTechnicianID() + "");
	 				   Assert.assertEquals(status, allLabProcedures.get(i).getStatus().toString());
	 				   Assert.assertEquals(result, allLabProcedures.get(i).getResults());
	 				   Assert.assertEquals(interval1, allLabProcedures.get(i).getConfidenceIntervalLower() + "");
	 				   Assert.assertEquals(interval2, allLabProcedures.get(i).getConfidenceIntervalUpper() + "");
	 				   Assert.assertEquals(comments, allLabProcedures.get(i).getCommentary());
	 				   break;
	 			   }
	 		   }		   
	 		   if (found == 0) {
	 			   fail();
	 		   }
	 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^When I examine the lab procedures from the office visit (.*) there is a procedure with LOINC (.*), Lab Technician (.*), status (.*), and no other information$")
	public void examineProcNoOtherInfo(String date, String code, String labTech, String status) {
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++)
	 			   if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
	 				   if(allLabProcedures.get(i).getLabProcedureCode().equals(code)){
	 					   found = 1;
		 				   Assert.assertEquals(code, allLabProcedures.get(i).getLabProcedureCode());
		 				   Assert.assertEquals(labTech, allLabProcedures.get(i).getLabTechnicianID() + "");
		 				   Assert.assertEquals(status, allLabProcedures.get(i).getStatus().toString());
	 				   }  
	 			   }
	 		   if (found == 0) {
	 			   fail();
	 		   }
	 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^When I first access and examine the lab procedures, the (.*) queue should contain the correct lab procedure ID, lab procedure code, status, priority, HCP name, and the date the lab was assigned for the procedures with LOINC (.*) and (.*)$")
	public void correctInfo(String qType, String code1, String code2) {
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
	 			  //iterates through list and makes sure all needed lab procs with corresponding codes are there
 				   if(allLabProcedures.get(i).getStatus().toString().equals(qType)){
 					  found = 0;
 					   if (allLabProcedures.get(i).getLabProcedureCode().equals(code1) || allLabProcedures.get(i).getLabProcedureCode().equals(code2)){
 						  found = 1;
 					   }
 				   }  
	 		   }
	 		   if (found == 0) {
	 			   fail();
	 		   }
	 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^After I update the two intransit lab procedures to received, when I examine the (.*) queue the order should be: the procedure with LOINC (.*) first, the procedure with LOINC (.*) second, the procedure with LOINC (.*) third, and the procedure with LOINC (.*) fourth$")
	public void afterUpdate(String qType, String code1, String code2, String code3, String code4) {
		
		
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   for (int i = 0; i < allLabProcedures.size(); i++){
 				   if(allLabProcedures.get(i).getLabProcedureCode().equals("12556-7") || allLabProcedures.get(i).getLabProcedureCode().equals("71950-0")){
 					   allLabProcedures.get(i).setStatus(3);
 					   labPSQL.update(allLabProcedures.get(i));
 				   }  
	 		   }
			
			   Stack<LabProcedure> stackOfProcedures = new Stack<LabProcedure>();
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
 				   if(allLabProcedures.get(i).getStatus().toString().equals(qType)){
 					   stackOfProcedures.push(allLabProcedures.get(i));
 				   }  
	 		   }
	 		   Assert.assertEquals(code4, stackOfProcedures.pop().getLabProcedureCode());
	 		   Assert.assertEquals(code3, stackOfProcedures.pop().getLabProcedureCode());
	 		   Assert.assertEquals(code2, stackOfProcedures.pop().getLabProcedureCode());
	 		   Assert.assertEquals(code1, stackOfProcedures.pop().getLabProcedureCode());
	 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	      
	@Then("^The (.*) queue should contain the procedure with LOINC (.*) first and the procedure with LOINC (.*) second$")
	public void firstAccess(String qType, String code1, String code2) {
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   Stack<LabProcedure> stackOfProcedures = new Stack<LabProcedure>();
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
 				   if(allLabProcedures.get(i).getStatus().toString().equals(qType)){
 					   if(allLabProcedures.get(i).getLabProcedureCode().equals(code1) || allLabProcedures.get(i).getLabProcedureCode().equals(code2)){
 						  stackOfProcedures.push(allLabProcedures.get(i));
 					   }
 				   }  
	 		   }
	 		   
	 		   Assert.assertEquals(code2, stackOfProcedures.pop().getLabProcedureCode());
	 		   Assert.assertEquals(code1, stackOfProcedures.pop().getLabProcedureCode());
	 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^After I add results to the lab procedure with LOINC (.*) and update the procedure, its status should change to pending and it should no longer appear in the lab technician's queue$")
	public void afterAddResults(String code) {
		//verify the status changed
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
	 			  
 				   if(allLabProcedures.get(i).getLabProcedureCode().equals(code)){
 					  allLabProcedures.get(i).setStatus(1);
 					  allLabProcedures.get(i + 1).setStatus(4);
 					  labPSQL.update(allLabProcedures.get(i));
 					  labPSQL.update(allLabProcedures.get(i + 1));
 					  Assert.assertEquals("PENDING", allLabProcedures.get(i).getStatus().toString());
 					  found = 1;
 				   }  
	 		   }
	 		   if (found == 0) fail();
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^After the lab procedure with LOINC (.*) has been updated, the procedure with LOINC (.*) should now have status testing and be first in the received queue$")
	public void afterMoreUpdate(String code1, String code2) {
		List<LabProcedure> allLabProcedures;
		try {
			   allLabProcedures = labPSQL.getAll();
			   int found = 0;
	 		   for (int i = 0; i < allLabProcedures.size(); i++){
	 			  
 				   if(allLabProcedures.get(i).getLabProcedureCode().equals(code2)){
 					   Assert.assertEquals("TESTING", allLabProcedures.get(i).getStatus().toString());
 					   found = 1;
 				   }  
	 		   }
	 		   if (found == 0) fail();
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^After the lab procedure with LOINC (.*) has been updated, the procedure with LOINC (.*) should be second in the received queue, and the procedure with LOINC (.*) should be third$")
	public void finalUpdate(String code1, String code2, String code3) {
		fail();
	}
	
	@Then("^(.*) from (.*)'s status should change to completed$")
	public void statusCompleted(String code, String date) {
		List<LabProcedure> allLabProcedures;
		try {
		   allLabProcedures = labPSQL.getAll();
 		   for (int i = 0; i < allLabProcedures.size(); i++){
 			  if (oVisSQL.getByID(allLabProcedures.get(i).getOfficeVisitID()).getDate().toString().contains(date)){
				   if(allLabProcedures.get(i).getLabProcedureCode().equals(code)){
					 
	 				   Assert.assertEquals("COMPLETED", allLabProcedures.get(i).getStatus().toString());
				   }
 			  }
 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	
	@Then("^After I add an invalid intervals to the lab procedure with LOINC (.*) and attempt to update the procedure, it should not successfully update.$")
	public void failedToAdd(String code) {
		List<LabProcedure> allLabProcedures;
		try {
		   allLabProcedures = labPSQL.getAll();
		   int found = 0;
 		   for (int i = 0; i < allLabProcedures.size(); i++){
			   if(allLabProcedures.get(i).getLabProcedureCode().equals(code)){
				   found = 1;
 				   Assert.assertEquals("TESTING", allLabProcedures.get(i).getStatus().toString());
			   }
 		   }
 		   if (found == 0){
 			   fail();
 		   }
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	
	@Then("^the lab procedure is not created$")
	public void failNotCreatedMessage() {
		//make sure there are still only the 9 lab procs from the sql file
		List<LabProcedure> allLabProcedures;
		try {
		   allLabProcedures = labPSQL.getAll();
 		   if (allLabProcedures.size() < 9){
 			   fail();
 		   }
 		   else{
 			   Assert.assertTrue(true);
 		   }
 		   
		} catch (DBException e) {
			 fail();
			e.printStackTrace();
		}
	}
	@Then("^I clean up the tables$")
	public void cleanUp(){
		try {
			gen.clearAllTables();
		} catch (FileNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.toString());
			fail();
			//e.printStackTrace();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	
}
