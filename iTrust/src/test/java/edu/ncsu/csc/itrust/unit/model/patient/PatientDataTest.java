package edu.ncsu.csc.itrust.unit.model.patient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.model.user.patient.PatientMySQLConverter;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class PatientDataTest extends TestCase {
	private DataBean<Patient> patientData;
	private TestDataGenerator gen;
	private DataSource ds;

	@Override
	protected void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		patientData = new PatientMySQLConverter(ds);
		gen = new TestDataGenerator();
	}
	
	@Test
	public void testGetHCPInstead() throws FileNotFoundException, SQLException, IOException{
		gen.hcp1();
		gen.patient1();

			Patient retVal;
			try {
				retVal = patientData.getByID(9900000000L);
				Assert.assertNull(retVal);
			} catch (DBException e) {
				Assert.assertTrue("error thrown",true);
			}

	}
	public void testGetPatient1() throws FileNotFoundException, SQLException, IOException{
		gen.patient1();

			Patient retVal;
			try {
				retVal = patientData.getByID(1L);
				Assert.assertEquals("Random", retVal.getFirstName());
				Assert.assertEquals("Person", retVal.getLastName());
				Assert.assertEquals(1L, retVal.getMID());
				Assert.assertEquals(Role.PATIENT, retVal.getRole());
			} catch (DBException e) {
				Assert.fail();
			}

	}

}
