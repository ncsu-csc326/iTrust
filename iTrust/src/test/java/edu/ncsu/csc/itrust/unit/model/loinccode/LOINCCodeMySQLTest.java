package edu.ncsu.csc.itrust.unit.model.loinccode;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeData;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class LOINCCodeMySQLTest extends TestCase {
	
	LOINCCodeMySQL data;
	TestDataGenerator gen;
	DataSource ds;
	
	@Override
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		data = new LOINCCodeMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.uc26();
	}
	
	@Test
	public void testGetAll() throws Exception {
		List<LOINCCode> loincList = data.getAll();
		assertEquals("00000-1", loincList.get(0).getCode());
		assertEquals("00000-2", loincList.get(1).getCode());
	}
	
	@Test
	public void testGetByID() throws Exception {
		assertNull(data.getByID(0L));
	}
	
	@Test
	public void testGetByCode() throws Exception {
		LOINCCode c = data.getByCode("00000-1");
		assertNotNull(c);
		assertEquals("fluid", c.getKindOfProperty());
	}
	
	@Test
	public void testAdd() throws Exception {
		final String code = "99999-9";
		assertNull(data.getByCode(code));
		LOINCCode loincToAdd = new LOINCCode(code, "comp", "prop");
		data.add(loincToAdd);
		LOINCCode loinc = data.getByCode(code);
		assertNotNull(loinc);
		assertEquals("comp", loinc.getComponent());
	}
	
	@Test
	public void testEdit() throws Exception {
		final String code = "99999-9";
		assertNull(data.getByCode(code));
		LOINCCode loincToAdd = new LOINCCode(code, "comp", "prop");
		data.add(loincToAdd);
		LOINCCode loinc = data.getByCode(code);
		assertNotNull(loinc);
		assertEquals("comp", loinc.getComponent());
		loincToAdd.setComponent("newComp");
		data.update(loincToAdd);
		LOINCCode newLoinc = data.getByCode(code);
		assertNotNull(newLoinc);
		assertEquals("newComp", newLoinc.getComponent());
	}
	
	@Test
	public void testAddInvalid() throws Exception {
		final String code = "99999-99999";
		assertNull(data.getByCode(code));
		LOINCCode loincToAdd = new LOINCCode(code, "comp", "prop");
		try {
			data.add(loincToAdd);
			fail("LOINC with invalid code should not have been added");
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	@Test
	public void testDelete() throws DBException, FormValidationException, SQLException{
	    data.add(new LOINCCode("12345-6", "a", "b"));
	    data.delete(new LOINCCode("12345-6", "a", "b"));
	}
}
