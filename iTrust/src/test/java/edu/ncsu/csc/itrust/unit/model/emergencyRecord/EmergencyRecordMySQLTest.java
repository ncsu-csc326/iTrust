package edu.ncsu.csc.itrust.unit.model.emergencyRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecord;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecordMySQL;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(EmergencyRecordMySQL.class)
public class EmergencyRecordMySQLTest extends TestCase {

	private DataSource ds;

	private EmergencyRecordMySQL sql;
	
	@Spy
	private EmergencyRecordMySQL mockSql;

	@Override
	public void setUp() throws DBException, FileNotFoundException, SQLException, IOException {
		ds = ConverterDAO.getDataSource();
		AllergyDAO allergyData = TestDAOFactory.getTestInstance().getAllergyDAO();
		sql = new EmergencyRecordMySQL(ds, allergyData);
		mockSql = Mockito.spy(new EmergencyRecordMySQL(ds, allergyData));
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.ndCodes();
		gen.ndCodes1();
		gen.ndCodes100();
		gen.ndCodes2();
		gen.ndCodes3();
		gen.ndCodes4();
		gen.uc21();
	}

	@Test
	public void testLoadRecord() throws DBException {
		// loads the record for Sandy Sky
		EmergencyRecord r = sql.getEmergencyRecordForPatient(201);
		Assert.assertNotNull(r);

		Assert.assertEquals("Sandy Sky", r.getName());
		Assert.assertEquals(24, r.getAge());
		Assert.assertEquals("Male", r.getGender());
		Assert.assertEquals("Susan Sky-Walker", r.getContactName());
		Assert.assertEquals("444-332-4309", r.getContactPhone());
		Assert.assertEquals("O-", r.getBloodType());

		// test prescriptions
		List<Prescription> pList = r.getPrescriptions();
		Assert.assertEquals(2, pList.size());
		Assert.assertEquals("63739-291", pList.get(0).getDrugCode().getNDCode());
		Assert.assertEquals("48301-3420", pList.get(1).getDrugCode().getNDCode());

		// test allergies
		// the order in the list isn't specified so this gets gross, sorry
		List<AllergyBean> aList = r.getAllergies();
		Assert.assertEquals(2, aList.size());
		boolean found = false;
		for (int i = 0; i < aList.size(); i++) {
			if ("Pollen".equals(aList.get(i).getDescription())) {
				found = true;
				break;
			}
		}
		if (!found) {
			Assert.fail();
		}
		found = false;
		for (int i = 0; i < aList.size(); i++) {
			if ("Penicillin".equals(aList.get(i).getDescription())) {
				found = true;
				break;
			}
		}
		if (!found) {
			Assert.fail();
		}

		// test diagnoses
		List<Diagnosis> dList = r.getDiagnoses();
		Assert.assertEquals(2, dList.size());
		Assert.assertEquals("J00", dList.get(0).getCode());
		Assert.assertEquals("J45", dList.get(1).getCode());

		// test immunizations
		List<Immunization> iList = r.getImmunizations();
		Assert.assertEquals(1, iList.size());
		Assert.assertEquals("90715", iList.get(0).getCptCode().getCode());
	}

	@Test
	public void testNoDataSource() {
		try {
			new EmergencyRecordMySQL();
			Assert.fail();
		} catch (DBException e) {
			// yay, we passed
		}
	}

	class TestEmergencyRecordMySQL extends EmergencyRecordMySQL {
		public TestEmergencyRecordMySQL() throws DBException {
			super();
		}

		@Override
		public DataSource getDataSource() {
			return ds;
		}
	}
	
	@Test
	public void testMockDataSource() throws Exception {
		EmergencyRecordMySQL mysql = new TestEmergencyRecordMySQL();
		Assert.assertNotNull(mysql);
	}
	
	@Test
	public void testMockGetEmergencyRecordForPatient() throws Exception {
		Mockito.doThrow(SQLException.class).when(mockSql).loadRecord(Mockito.any());
		try {
			mockSql.getEmergencyRecordForPatient(1L);
			fail();
		} catch (DBException e) {
			// Do nothing
		}
	}

	@Test
	public void testInvalidPatient() throws DBException {
		Assert.assertNull(sql.getEmergencyRecordForPatient(-1));
	}
}
