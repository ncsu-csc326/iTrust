package edu.ncsu.csc.itrust.unit.model.officeVisit;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class OfficeVisitTest extends TestCase {
	private OfficeVisit test;
	private ApptTypeData apptData;
	private TestDataGenerator gen;
	private DataSource ds;

	@Override
	protected void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		test = new OfficeVisit();
		apptData = new ApptTypeMySQLConverter(ds);
		gen = new TestDataGenerator();
		
	}
	@Test
	public void testApptTypeID() throws DBException, FileNotFoundException, IOException, SQLException{
		gen.appointmentType();
		List<ApptType> types = apptData.getAll();
		long apptTypeID = types.get((types.size()-1)).getID();
		test.setApptTypeID(apptTypeID);
		long testID = test.getApptTypeID();
		Assert.assertEquals(apptTypeID, testID);		
	}
	@Test
	public void testDate() {
		LocalDateTime testTime = LocalDateTime.now();
		test.setDate(testTime);
		Assert.assertTrue(ChronoUnit.MINUTES.between(testTime, test.getDate())<1);
	}
	@Test
	public void testLocationID() throws FileNotFoundException, SQLException, IOException, DBException{
		gen.hospitals();
		HospitalData hData = new HospitalMySQLConverter(ds);
		List<Hospital> all = hData.getAll();
		String id = all.get(0).getHospitalID();
		test.setLocationID(id);
		Assert.assertEquals(id, test.getLocationID());
	}
	@Test
	public void testNotes(){
		String note = "ABCDEF123><$%> ";
		test.setNotes(note);
		Assert.assertEquals(note, test.getNotes());
	}
	@Test
	public void testBill(){
		test.setSendBill(true);
		Assert.assertTrue(test.getSendBill());
		test.setSendBill(false);
		Assert.assertFalse(test.getSendBill());
	}
	@Test
	public void testPatient() throws FileNotFoundException, IOException, SQLException{
		gen.patient1();
		test.setPatientMID(1L);
		Assert.assertEquals(1L,test.getPatientMID());
	}
	@Test
	public void testID(){
		test.setVisitID(1L);
		long check = test.getVisitID();
		Assert.assertEquals(1L, check);
	}
	


}
