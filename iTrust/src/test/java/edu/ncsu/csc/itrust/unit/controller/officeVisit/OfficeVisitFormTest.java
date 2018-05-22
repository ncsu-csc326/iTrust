package edu.ncsu.csc.itrust.unit.controller.officeVisit;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class OfficeVisitFormTest extends TestCase {

	private OfficeVisitForm ovf;
	@Spy
	private OfficeVisitForm spyovf;
	@Spy
	private OfficeVisitController ovc;
	private ApptTypeData apptData;
	private TestDataGenerator gen;
	private DataSource ds;
	private HospitalData hData;
	private OfficeVisit ovBaby;
	private OfficeVisit ovChild;
	private OfficeVisit ovAdult;
	@Spy
	private OfficeVisitController mockovc;
	
	private static final Float FLOAT_VALUE = 12.3F;
	private static final String BP_VALUE = "140/90";
	private static final Integer INTEGER_VALUE = 1;
	private static final Integer HDL = 45;
	private static final Integer TRI = 350;
	private static final Integer LDL = 300;
	
	private static final Float FLOAT_TEST = 11.1F;
	private static final String BP_TEST = "190/80";
	private static final Integer INTEGER_TEST = 3;
	private static final Integer HDL_TEST = 60;
	private static final Integer TRI_TEST = 200;
	private static final Integer LDL_TEST = 500;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		apptData = new ApptTypeMySQLConverter(ds);
		hData = new HospitalMySQLConverter(ds);
		gen = new TestDataGenerator();
		ovc = Mockito.spy(new OfficeVisitController(ds));
		mockovc = Mockito.mock(OfficeVisitController.class);
		
		// Set up office visits
		createOVBaby();
		createOVChild();
		createOVAdult();
		
		// Generate data
		gen = new TestDataGenerator();
		gen.appointmentType();
		gen.hospitals();
		gen.patient1();
		gen.uc51();
		gen.uc52();
		gen.uc53SetUp();
	}
	
	private void createOVBaby() {
		ovBaby = new OfficeVisit();
		ovBaby.setPatientMID(1L);
		ovBaby.setLength(FLOAT_VALUE);
		ovBaby.setWeight(FLOAT_VALUE);
		ovBaby.setHeadCircumference(FLOAT_VALUE);
		ovBaby.setHouseholdSmokingStatus(INTEGER_VALUE);
	}
	
	private void createOVChild() {
		ovChild = new OfficeVisit();
		ovChild.setPatientMID(1L);
		ovChild.setHeight(FLOAT_VALUE);
		ovChild.setWeight(FLOAT_VALUE);
		ovChild.setBloodPressure(BP_VALUE);
		ovChild.setHouseholdSmokingStatus(INTEGER_VALUE);
	}

	private void createOVAdult() throws SQLException, FileNotFoundException, IOException, DBException {
		ovAdult = new OfficeVisit();
		gen.appointmentType();
		List<ApptType> apptList = apptData.getAll();
		if(apptList.size()>0){
			ovAdult.setApptTypeID(apptList.get(0).getID());
		}
		ovAdult.setDate(LocalDateTime.now());
		ovAdult.setPatientMID(1L);
		ovAdult.setVisitID(1L);
		ovAdult.setSendBill(true);
		ovAdult.setHeight(FLOAT_VALUE);
		ovAdult.setWeight(FLOAT_VALUE);
		ovAdult.setBloodPressure(BP_VALUE);
		ovAdult.setHouseholdSmokingStatus(INTEGER_VALUE);
		ovAdult.setPatientSmokingStatus(INTEGER_VALUE);
		ovAdult.setHDL(HDL);
		ovAdult.setTriglyceride(TRI);
		ovAdult.setLDL(LDL);
	}

	@Test
	public void testGetVisitID() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		ovf.setVisitID(1L);
		Assert.assertTrue(1L == ovf.getVisitID());
	}

	@Test
	public void testGetPatientMID() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getPatientMID().equals(1L));
		ovf.setPatientMID(2L);
		Assert.assertTrue(ovf.getPatientMID().equals(2L));
	}

	@Test
	public void testGetDate() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		LocalDateTime test = LocalDateTime.of(2010, Month.JANUARY, 1, 0, 0);
		ovf.setDate(test);
		Assert.assertTrue(ovf.getDate().equals(test));
	}

	@Test
	public void testGetLocationID() {
		final String locationId = "1";
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		ovf.setLocationID(locationId);
		Assert.assertEquals(locationId, ovf.getLocationID());
	}

	@Test
	public void testGetApptTypeID() {
		final Long apptTypeId = 1L;
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		ovf.setApptTypeID(apptTypeId);
		Assert.assertEquals(apptTypeId, ovf.getApptTypeID());
	}

	@Test
	public void testGetNotes() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		ovf.setNotes("abc");
		Assert.assertTrue(ovf.getNotes().equals("abc"));
	}

	@Test
	public void testGetSendBill() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getSendBill());
		ovf.setSendBill(false);
		Assert.assertFalse(ovf.getSendBill());
	}

	@Test
	public void testGetHeight() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getHeight().equals(FLOAT_VALUE));
		ovf.setHeight(FLOAT_TEST);
		Assert.assertTrue(ovf.getHeight().equals(FLOAT_TEST));
	}

	@Test
	public void testGetLength() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovBaby);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getLength().equals(FLOAT_VALUE));
		ovf.setLength(FLOAT_TEST);
		Assert.assertTrue(ovf.getLength().equals(FLOAT_TEST));
	}

	@Test
	public void testGetWeight() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getWeight().equals(FLOAT_VALUE));
		ovf.setWeight(FLOAT_TEST);
		Assert.assertTrue(ovf.getWeight().equals(FLOAT_TEST));
	}

	@Test
	public void testGetHeadCircumference() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovBaby);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getHeadCircumference().equals(FLOAT_VALUE));
		ovf.setHeadCircumference(FLOAT_TEST);
		Assert.assertTrue(ovf.getHeadCircumference().equals(FLOAT_TEST));
	}

	@Test
	public void testGetBloodPressure() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovChild);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getBloodPressure().equals(BP_VALUE));
		ovf.setBloodPressure(BP_TEST);
		Assert.assertTrue(ovf.getBloodPressure().equals(BP_TEST));
	}

	@Test
	public void testGetHDL() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getHDL().equals(HDL));
		ovf.setHDL(HDL_TEST);
		Assert.assertTrue(ovf.getHDL().equals(HDL_TEST));
	}

	@Test
	public void testGetTriglyceride() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getTriglyceride().equals(TRI));
		ovf.setTriglyceride(TRI_TEST);
		Assert.assertTrue(ovf.getTriglyceride().equals(TRI_TEST));
	}

	@Test
	public void testGetLDL() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getLDL().equals(LDL));
		ovf.setLDL(LDL_TEST);
		Assert.assertTrue(ovf.getLDL().equals(LDL_TEST));
	}

	@Test
	public void testGetHouseholdSmokingStatus() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getHouseholdSmokingStatus().equals(INTEGER_VALUE));
		ovf.setHouseholdSmokingStatus(INTEGER_TEST);
		Assert.assertTrue(ovf.getHouseholdSmokingStatus().equals(INTEGER_TEST));
	}

	@Test
	public void testGetPatientSmokingStatus() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		Assert.assertTrue(ovf.getPatientSmokingStatus().equals(INTEGER_VALUE));
		ovf.setPatientSmokingStatus(INTEGER_TEST);
		Assert.assertTrue(ovf.getPatientSmokingStatus().equals(INTEGER_TEST));
	}

	@Test
	public void testSubmit() {
		Mockito.when(mockovc.getSelectedVisit()).thenReturn(ovAdult);
		ovf = new OfficeVisitForm(mockovc);
		ovf.submit();
		
		Assert.assertTrue(ovf.getHeight().equals(FLOAT_VALUE));
		Assert.assertTrue(ovf.getWeight().equals(FLOAT_VALUE));
		Assert.assertTrue(ovf.getBloodPressure().equals(BP_VALUE));
		Assert.assertTrue(ovf.getHouseholdSmokingStatus().equals(INTEGER_VALUE));
		Assert.assertTrue(ovf.getPatientSmokingStatus().equals(INTEGER_VALUE));
		Assert.assertTrue(ovf.getHDL().equals(HDL));
		Assert.assertTrue(ovf.getTriglyceride().equals(TRI));
		Assert.assertTrue(ovf.getLDL().equals(LDL));
	}

	@Test
	public void testIsPatientABaby() {
		OfficeVisit ov = new OfficeVisit();
		ov.setPatientMID(101L);
		ov.setDate(LocalDateTime.of(2013, 6, 1, 0, 0));
		Mockito.doReturn(ov).when(ovc).getSelectedVisit();
		spyovf = Mockito.spy(new OfficeVisitForm(ovc));
		Assert.assertTrue(spyovf.isPatientABaby());
	}

	@Test
	public void testIsPatientAChild() {
		OfficeVisit ov = new OfficeVisit();
		ov.setPatientMID(101L);
		ov.setDate(LocalDateTime.of(2017, 6, 1, 0, 0));
		Mockito.doReturn(ov).when(ovc).getSelectedVisit();
		spyovf = Mockito.spy(new OfficeVisitForm(ovc));
		Assert.assertTrue(spyovf.isPatientAChild());
	}

	@Test
	public void testIsPatientAnAdult() {
		OfficeVisit ov = new OfficeVisit();
		ov.setPatientMID(101L);
		ov.setDate(LocalDateTime.of(2026, 6, 1, 0, 0));
		Mockito.doReturn(ov).when(ovc).getSelectedVisit();
		spyovf = Mockito.spy(new OfficeVisitForm(ovc));
		Assert.assertTrue(spyovf.isPatientAnAdult());
	}
}
