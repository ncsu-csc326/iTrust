package edu.ncsu.csc.itrust.unit.model.officeVisit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
		public void testSetInvalidHospital() throws FileNotFoundException, IOException, SQLException, DBException{

			gen.hospitals();
			List<Hospital> hList = hData.getAll();
			String hID = hList.get(hList.size()-1).getHospitalID();
			gen.clearAllTables();
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



}
