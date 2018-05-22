package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import cucumber.api.java.en.Then;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;

public class MaintainStandardsListStepDefs {

	

	private DataSource ds;
	private TestDataGenerator gen;
	private PersonnelDAO persDAO;
	private CPTCodeMySQL cptSQL;
	private ICDCodeMySQL icdSQL;
	private NDCCodeMySQL ndcSQL;
	private LOINCCodeMySQL loincSQL;
	
	public MaintainStandardsListStepDefs(){
		
		
		this.ds = ConverterDAO.getDataSource();
		this.gen = new TestDataGenerator();
		this.persDAO = new PersonnelDAO(TestDAOFactory.getTestInstance());
		this.cptSQL = new CPTCodeMySQL(ds);
		this.icdSQL = new ICDCodeMySQL(ds);
		this.ndcSQL = new NDCCodeMySQL(ds);
		this.loincSQL = new LOINCCodeMySQL(ds);
	}
	
	
	@Given("^I load uc15.sql$")
	public void loadSql(){
		try {
			gen.clearAllTables();
			gen.uc15();
			gen.admin1();
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
	
	@Given("^Admin user MID: (.*) PW: (.*) exists$")
	public void userExists(String id, String pw){
		PersonnelBean p;
		try {
			p = persDAO.getPersonnel(Long.parseLong(id));
			assertEquals(Role.ADMIN, p.getRole());
		} catch (NumberFormatException e) {
			fail();
			e.printStackTrace();
		} catch (DBException e) {
			fail();
			System.out.println(e.toString());
		}
		
	}
	
	@When("^Go to the Add CPT/Vaccine code functionality, enter (.*) as the Code, enter (.*) as the name then add the code$")
	public void addCPT(String code, String name){
		CPTCode cpt = new CPTCode(code, name);
		try {
			cptSQL.add(cpt);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	
	@When("^Go to the Update CPT/Vaccine code functionality, update (.*), enter (.*) as the Name and update the code.$")
	public void updateCPT(String code, String name){
		
		try {
			CPTCode cpt = cptSQL.getByCode(code);
			cpt.setName(name);
			cptSQL.update(cpt);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@When("^Go to the Add ICD Code functionality, input (.*) as the code, input (.*) as the description field, select (.*), and add code.$")
	public void addICD(String code, String description, String chronic){
		ICDCode icd = new ICDCode(code, description, Boolean.parseBoolean(chronic));
		try {
			icdSQL.add(icd);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			System.out.println(e.toString());
			System.out.println(code);
			fail();
			
		}
	}
	
	@When("^Select Edit ICD Codes from the sidebar, select (.*) from the list, select (.*), and update Code$")
	public void updateICD(String code, String chronic){
		try {
			ICDCode icd = icdSQL.getByCode(code);
			icd.setChronic(Boolean.parseBoolean(chronic));
			icdSQL.update(icd);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@When("^Go to the Add NDC functionality, enter (.*) as the code, enter (.*) as the Name, and Add Code$")
	public void addNDC(String code, String name){
		NDCCode ndc = new NDCCode();
		ndc.setCode(code);
		ndc.setDescription(name);
		try {
			ndcSQL.add(ndc);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			System.out.println(code);
			System.out.println(e.toString());
			fail();
			
		}
	}
	
	@When("^Go to the Update NDC functionality, Select NDC (.*), name to (.*), Update Code$")
	public void updateNDC(String code, String name){
		try {
			NDCCode ndc = ndcSQL.getByCode(code);
			ndc.setDescription(name);
			ndcSQL.update(ndc);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@When("^Enter (.*) as the code, (.*) as the Component, (.*) as the property, (.*) as the timing aspect, (.*) as the system, (.*) as the scale type, (.*) as the method type, and Add LOINC$")
	public void addLOINC(String code, String component, String property, String timing, String system, String scale, String method) throws FormValidationException{
		LOINCCode loinc = new LOINCCode(code, component, property);
		loinc.setTimeAspect(timing);
		loinc.setSystem(system);
		loinc.setScaleType(scale);
		loinc.setMethodType(method);
		try {
			loincSQL.add(loinc);
		} catch (DBException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@When("^Go to the Update LOINC functionality, Select LOINC (.*), update the method type to (.*), and Update LOINC$")
	public void updateLOINC(String code, String method) throws FormValidationException{
		try {
			LOINCCode loinc = loincSQL.getByCode(code);
			loinc.setMethodType(method);
			loincSQL.update(loinc);
		} catch (DBException e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	@When("^I enter (.*) as an icd code$")
	public void failureICDCode(String code){
		ICDCode icd = new ICDCode(code, "bland description", true);
		try {
			icdSQL.add(icd);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			//allow it to pass since the code is invalid on purpose
		}
	}
	
	@When("^I add NDC code (.*) with name (.*)$")
	public void addNDCCode(String code, String name){
		NDCCode ndc = new NDCCode();
		ndc.setCode(code);
		ndc.setDescription(name);
		try {
			ndcSQL.add(ndc);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		} catch (FormValidationException e) {
			fail();
			
		}
	}
	
	@Then("^There are 0 cpt codes present when I attempt to search for (.*)$")
	public void noCPTCodes(String code){
		try {
			CPTCode cpt = cptSQL.getByCode(code);
			
			Assert.assertEquals(null, cpt);
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Then("^After the user sees a message - Success: (.*) - (.*) and its in the system$")
	public void messageAndInSystem(String code, String name) throws SQLException{
		//now way to assert message cause it is front end
		Assert.assertEquals(code, cptSQL.getByCode(code).getCode().toString());
	}
	
	@Then("^After adding the information the user sees a message - Success: (.*) - (.*) added. it is also (.*)$")
	public void chronic(String code, String name, String chronicYesNo) throws SQLException{
		ICDCode iCode = icdSQL.getByCode(code);
		
		Assert.assertEquals(name, iCode.getName());
		Assert.assertEquals(Boolean.parseBoolean(chronicYesNo), iCode.isChronic());
		
	}
	
	@Then("^After the update, message displays - Success 1 row updated and (.*) has (.*) selected$")
	public void rowsAndChronic(String code, String chronicYesNo) throws SQLException {
		ICDCode icd = null;
		System.out.println(code);
		icd = icdSQL.getByCode(code);
		Assert.assertEquals(Boolean.parseBoolean(chronicYesNo), icd.isChronic());
		Assert.assertEquals(code, icd.getCode());
		
		
	}
	
	@Then("^After adding the code the user sees a message - Success: (.*): (.*) added and is in the system$")
	public void addEggplantExtract(String code, String name) throws SQLException{
		NDCCode nCode = ndcSQL.getByCode(code);
		Assert.assertEquals(name, nCode.getDescription());
		Assert.assertEquals(code, nCode.getCode());
	}
	
	@Then("^After the update, the user sees a message - Success! Code (.*): (.*) updated and is in the system$")
	public void updateAcid(String code, String name) throws SQLException{
		NDCCode nCode = ndcSQL.getByCode(code);
		Assert.assertEquals(name, nCode.getDescription());
		Assert.assertEquals(code, nCode.getCode());
	}
	
	@Then("^After adding the code the user sees a message - Success: 66554-3 added and in the system, after adding the code LOINC (.*) has Component (.*), property (.*), timing aspect (.*), system (.*), scale (.*), type (.*)$")
	public void checkLOINCUpdate(String code, String component, String property, String timing, String system, String scale, String type) throws DBException{
		LOINCCode loinc = loincSQL.getByCode(code);
		Assert.assertEquals(component, loinc.getComponent());
		Assert.assertEquals(property, loinc.getKindOfProperty());
		Assert.assertEquals(timing, loinc.getTimeAspect());
		Assert.assertEquals(system, loinc.getSystem());
		Assert.assertEquals(scale, loinc.getScaleType());
		Assert.assertEquals(type, loinc.getMethodType());
		
	}
	
	@Then("^After the update, the user sees a message - Success! Code 66554-4 updated and in the system, after adding the code LOINC (.*) has Component (.*), property (.*), timing aspect (.*), system (.*), scale (.*), type (.*)$")
	public void checkLOINCOtherUpdate(String code, String component, String property, String timing, String system, String scale, String type) throws DBException{
		LOINCCode loinc = loincSQL.getByCode(code);
		System.out.println(code);
		Assert.assertEquals(component, loinc.getComponent());
		Assert.assertEquals(property, loinc.getKindOfProperty());
		Assert.assertEquals(timing, loinc.getTimeAspect());
		Assert.assertEquals(system, loinc.getSystem());
		Assert.assertEquals(scale, loinc.getScaleType());
		Assert.assertEquals(type, loinc.getMethodType());
	}
	
	@Then("^I get an error because the code (.*) is too long and the code is not added$")
	public void errorCodeTooLong(String code){
		try {
			Assert.assertEquals(null, icdSQL.getByCode(code));
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Then("^The code already exists and is not added$")
	public void alreadyExistsNotAdded() throws SQLException{
		Assert.assertEquals(1,  ndcSQL.getAll().size());
	}
	
	@Then("^I clean up all of my data$")
	public void cleanTables(){
		try {
			gen.clearAllTables();
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
}