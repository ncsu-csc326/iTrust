package edu.ncsu.csc.itrust.unit.controller.officeVisit;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitData;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class OfficeVisitControllerTest extends TestCase{
	private OfficeVisitController ovc;
	private ApptTypeData apptData;
	private OfficeVisitData ovData;
	private DataSource ds;
	private TestDataGenerator gen; //remove when ApptType, Patient, and other files are finished
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		ovc = new OfficeVisitController(ds);
		apptData= new ApptTypeMySQLConverter(ds);
		ovData = new OfficeVisitMySQL(ds);
		//remove when these modules are built and can be called
		gen = new TestDataGenerator();
		gen.appointmentType();
		gen.hospitals();
		gen.patient1();

	}

	@Test
	public void testRetrieveOfficeVisit() throws DBException {
		OfficeVisit testOV = new OfficeVisit();
		List<ApptType> types = apptData.getAll();
		long apptTypeID = types.get((types.size()-1)).getID();
		testOV.setApptTypeID(apptTypeID);
		
		LocalDateTime date = LocalDateTime.now();
		testOV.setDate(date);
		
		HospitalData hospitalData = new HospitalMySQLConverter(ds);
		List<Hospital> hospitals = hospitalData.getAll();
		String locID = hospitals.get((hospitals.size()-1)).getHospitalID();
		testOV.setLocationID(locID);
		
		testOV.setNotes("Hello World!");
		
		
		testOV.setPatientMID(1L);
		testOV.setSendBill(true);
		ovData.add(testOV);
		
		
		//Get the visit ID from the DB
		List<OfficeVisit> all = ovData.getAll();
		long visitID = -1;
		for(int i=0; i<all.size(); i++){
			OfficeVisit ovI = all.get(i);
			boolean bApptType = (testOV.getApptTypeID() == ovI.getApptTypeID());
			boolean bDate = false;
			long time = ChronoUnit.MINUTES.between(testOV.getDate(), ovI.getDate());
			bDate = (time<1);
			boolean bLoc = (testOV.getLocationID().equals(ovI.getLocationID()));
			boolean bNotes = false;
			if(testOV.getNotes() == null){
				if(ovI.getNotes() == null) bNotes = true;
				
			}else{
			 	bNotes = (testOV.getNotes().equals(ovI.getNotes()));
			}
			boolean bBill = (testOV.getSendBill() == ovI.getSendBill());
			
			if(bApptType && bDate && bLoc && bNotes && bBill){
				visitID = ovI.getVisitID();
				
			}
		}
		OfficeVisit check = ovc.getVisitByID(Long.toString(visitID));
		Assert.assertEquals(testOV.getApptTypeID(), check.getApptTypeID());
		long dif = ChronoUnit.MINUTES.between(testOV.getDate(), check.getDate());
		Assert.assertTrue(dif<1);
		Assert.assertEquals(testOV.getLocationID(), check.getLocationID());
		Assert.assertEquals(testOV.getNotes(), check.getNotes());
		Assert.assertEquals(testOV.getSendBill(), check.getSendBill());
	}

}
