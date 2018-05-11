package edu.ncsu.csc.itrust.unit.model.diagnosis;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class DiagnosisMySQLTest extends TestCase {

	DataSource ds;
	DiagnosisMySQL sql;
	@Spy
	DiagnosisMySQL mockSql;
	TestDataGenerator gen;

	@Override
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		sql = new DiagnosisMySQL(ds);
		mockSql = Mockito.spy(new DiagnosisMySQL(ds));
		gen = new TestDataGenerator();
		gen.clearAllTables();
	}

	@Test
	public void testGetAllEmergencyDiagnosis() throws Exception {
		gen.uc21();
		List<Diagnosis> dList = sql.getAllEmergencyDiagnosis(201);
		assertEquals(2, dList.size());
		assertEquals("J00", dList.get(0).getCode());
		assertEquals("J45", dList.get(1).getCode());
	}

	@Test
    public void testGetAll() throws Exception {
		gen.uc21();
		List<Diagnosis> list = sql.getAll();
        assertNotNull(list);
        assertEquals(4, list.size());
    }

	// TODO actually do this, this is just for coverage
	@Test
	public void testGetByID() throws Exception {
		assertNull(sql.getByID(-1));
	}

	@Test
	public void testAdd() throws Exception {
		try {
			sql.add(new Diagnosis(0, 0, null));
			fail("Cannot add null ICDCode");
		} catch (DBException e) {
			// do nothing
		}
		gen.testIcdCode();
		Diagnosis expected1 = new Diagnosis(0, 0, new ICDCode("T000", null, true));
		Diagnosis expected2 = new Diagnosis(1, 0, new ICDCode("T001", "", true));
		assertTrue(sql.add(expected1));
		assertTrue(sql.add(expected2));
		List<Diagnosis> list = sql.getAllDiagnosisByOfficeVisit(0);
		assertEquals(2, list.size());
		assertEquals("T000", list.get(0).getCode());
		assertEquals("Test code 1", list.get(1).getName());
	}

	// TODO actually do this, this is just for coverage
	@Test
	public void testUpdate() throws Exception {
		try {
			sql.update(new Diagnosis(0, 0, null));
			fail("Cannot update null ICDCode");
		} catch (DBException e) {
			// do nothing
		}
		gen.testIcdCode();
		Diagnosis expected = new Diagnosis(0, 0, new ICDCode("T000", null, true));
		assertTrue(sql.add(expected));
		assertEquals(1, sql.getAllDiagnosisByOfficeVisit(0).size());
		assertEquals(0, sql.getAllDiagnosisByOfficeVisit(1).size());
		expected = sql.getAllDiagnosisByOfficeVisit(0).get(0);
		assertEquals(0, expected.getVisitId());
		expected.setVisitId(1);
		assertTrue(sql.update(expected));
		assertEquals(0, sql.getAllDiagnosisByOfficeVisit(0).size());
		assertEquals(1, sql.getAllDiagnosisByOfficeVisit(1).size());
	}

	@Test
	public void testProdConstructor() {
		try {
			new DiagnosisMySQL();
			Assert.fail();
		} catch (DBException e) {
			// yay, we passed
		}
	}

	public class TestDiagnosisMySQL extends DiagnosisMySQL {
		public TestDiagnosisMySQL() throws DBException {
			super();
		}

		public TestDiagnosisMySQL(DataSource ds) {
			super(ds);
		}

		@Override
		public DataSource getDataSource() {
			return ds;
		}
	}

	@Test
	public void testMockDataSource() throws Exception {
		DiagnosisMySQL mysql = new TestDiagnosisMySQL();
		assertNotNull(mysql);
	}
	
	@Test
	public void testRemove() throws Exception {
		gen.testIcdCode();
		sql.add(new Diagnosis(0, 0, new ICDCode("T000", null, true)));
		assertEquals(1, sql.getAllDiagnosisByOfficeVisit(0).size());
		Diagnosis expected = sql.getAllDiagnosisByOfficeVisit(0).get(0);
		assertNotNull(expected);
		sql.remove(expected.getId());
		assertEquals(0, sql.getAllDiagnosisByOfficeVisit(0).size());
	}
}
