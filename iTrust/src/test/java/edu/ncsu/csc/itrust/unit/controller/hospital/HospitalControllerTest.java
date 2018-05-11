package edu.ncsu.csc.itrust.unit.controller.hospital;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.hospital.HospitalController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class HospitalControllerTest extends TestCase {
		private HospitalController hc;
		private DataSource ds;
		private TestDataGenerator gen; //remove when ApptType, Patient, and other files are finished
		@Override
		public void setUp() throws Exception {
			ds = ConverterDAO.getDataSource();
			hc = new HospitalController(ds);
			//remove when these modules are built and can be called
			gen = new TestDataGenerator();
			gen.hospitals();

		}
		
		@Test
		public void testHospitalInList() throws DBException{
			List<Hospital> all = hc.getHospitalList();
			String test = hc.HospitalNameForID("1");
			for(int i=0; i<all.size(); i++){
				if(all.get(i).getHospitalID() == "1"){
					Assert.assertEquals(test, all.get(i).getHospitalName());
				}
				
			}
			
		}

}
