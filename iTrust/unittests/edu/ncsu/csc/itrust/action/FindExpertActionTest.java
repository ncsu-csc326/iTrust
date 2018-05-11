package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.HospitalBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class FindExpertActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private FindExpertAction fea;
		
	protected void setUp() throws Exception {
		fea = new FindExpertAction(factory);
		gen.clearAllTables();
	}
	
	public void testFindExperts(){
		List<HospitalBean> hospitals = new ArrayList<HospitalBean>();
		HospitalBean realHospital = new HospitalBean();
		hospitals.add(realHospital);
		
		//Test a single result
		assertTrue(fea.findExperts(hospitals, "ob/gyn").size() == 1);
	}
	
	public void testFilterHospitals(){
		HospitalBean hospitalZero = new HospitalBean();
		HospitalBean hospitalOne = new HospitalBean();
		HospitalBean blankHospital = new HospitalBean();
		HospitalBean nullHospital = new HospitalBean();
		List<HospitalBean> hospitals = new ArrayList<HospitalBean>();
		hospitalZero.setHospitalZip("00000");
		hospitalOne.setHospitalZip("11111");
		hospitalOne.setHospitalZip(null);
		
		hospitals.add(hospitalZero);
		hospitals.add(hospitalOne);
		hospitals.add(blankHospital);
		hospitals.add(nullHospital);
		
		assertTrue(fea.filterHospitals(hospitals, "00000", 5).size() == 1);
		assertTrue(fea.filterHospitals(hospitals, "00001", 4).size() == 1);
		assertTrue(fea.filterHospitals(hospitals, "00011", 3).size() == 1);
		assertTrue(fea.filterHospitals(hospitals, "00111", 2).size() == 1);
		assertTrue(fea.filterHospitals(hospitals, "01111", 1).size() == 1);
	}
	
	public void testFindHospitalsBySpecialty() {
		assertTrue(fea.findHospitalsBySpecialty("ob/gyn", "00000", 5).size() == 0);
	}
}
