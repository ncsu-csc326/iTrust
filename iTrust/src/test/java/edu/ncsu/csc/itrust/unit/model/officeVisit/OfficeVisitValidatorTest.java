package edu.ncsu.csc.itrust.unit.model.officeVisit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitValidator;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class OfficeVisitValidatorTest extends TestCase {
	private ApptTypeData apptData;
	private TestDataGenerator gen;
	private DataSource ds;
	private HospitalData hData;
	private OfficeVisitValidator validator;

	@Override
	protected void setUp() throws Exception {

		ds = ConverterDAO.getDataSource();
		apptData = new ApptTypeMySQLConverter(ds);
		hData = new HospitalMySQLConverter(ds);
		gen = new TestDataGenerator();
		validator = new OfficeVisitValidator(ds);
		gen.clearAllTables();
		
	}
	@Test
	public void testValidOfficeVisit() throws FileNotFoundException, IOException, SQLException, DBException{
		gen.patient1();
		gen.appointmentType();
		OfficeVisit ov = new OfficeVisit();
		List<ApptType> apptList = apptData.getAll();
		if(apptList.size()>0){
			ov.setApptTypeID(apptList.get(0).getID());
		}
		ov.setDate(LocalDateTime.now());
		gen.hospitals();
		List<Hospital> hList = hData.getAll();
		ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
		ov.setNotes("Hello World!");
		ov.setSendBill(false);
		ov.setPatientMID(1L);
		try{
			validator.validate(ov);
			Assert.assertTrue(true);
		}
		catch(FormValidationException f){
			Assert.fail("Should be no error!");
			
		}
		
	}
		
		@Test
		public void testInvalidMID() throws FileNotFoundException, IOException, SQLException, DBException{
			gen.patient1();
			gen.appointmentType();
			OfficeVisit ov = new OfficeVisit();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setDate(LocalDateTime.now());
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(9000000000L);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		public void testInvalidApptID() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setApptTypeID(10L);
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		@Test
		public void testSetInvalidHospital() throws FileNotFoundException, IOException, SQLException, DBException {
			gen.clearAllTables();
			gen.hospitals();
			String hID = "-1";
			gen.patient1();
			gen.appointmentType();
			
			OfficeVisit ov = new OfficeVisit();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setDate(LocalDateTime.now());
			
			ov.setLocationID(hID);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}

		
		@Test
		public void testSetNullMID() throws FileNotFoundException, IOException, SQLException, DBException{

			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			String hID = hList.get(hList.size()-1).getHospitalID();
			gen.patient1();
			gen.appointmentType();
			OfficeVisit ov = new OfficeVisit();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setDate(LocalDateTime.now());
			
			ov.setLocationID(hID);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(null);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}

		public void testInvalidWeight() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.51F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidHeight() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.34F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidHSS() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(-1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}

		public void testInvalidPSS() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(-1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidBloodPressure() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("1400/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidHDL() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(100);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidTriglyceride() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(90);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidLDL() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.now());
			gen.patient1();
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(700);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testValidChild() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.of(1956, Month.MAY, 10, 0, 0));
			gen.patient1(); // b-day 1950
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setHeight(12.3F);
			ov.setBloodPressure("140/90");
			ov.setPatientSmokingStatus(1);
			ov.setHDL(45);
			ov.setTriglyceride(250);
			ov.setLDL(300);
			try{
				validator.validate(ov);
				Assert.assertTrue(true);
			}
			catch(FormValidationException f){
				Assert.fail("Should be no error!");
				
			}
			
		}
		
		public void testInvalidLength() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.of(1952, Month.MAY, 10, 0, 0));
			gen.patient1(); // b-day 1950
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setLength(12.33F);
			ov.setHeadCircumference(12.3F);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
		public void testInvalidHeadCircumference() throws FileNotFoundException, IOException, SQLException, DBException{
			OfficeVisit ov = new OfficeVisit();
			ov.setDate(LocalDateTime.of(1952, Month.MAY, 10, 0, 0));
			gen.patient1(); // b-day 1950
			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			ov.setLocationID(hList.get(hList.size()-1).getHospitalID());
			ov.setVisitID(1L);
			ov.setNotes("Hello World!");
			ov.setSendBill(false);
			ov.setPatientMID(1L);
			gen.appointmentType();
			List<ApptType> apptList = apptData.getAll();
			if(apptList.size()>0){
				ov.setApptTypeID(apptList.get(0).getID());
			}
			ov.setWeight(132.1F);
			ov.setHouseholdSmokingStatus(1);
			ov.setLength(12.3F);
			ov.setHeadCircumference(12.33F);
			try{
				validator.validate(ov);
				Assert.fail("No error thrown");
			}
			catch(FormValidationException f){
				Assert.assertTrue(true); //error thrown
				
			}
			
		}
		
}
