package edu.ncsu.csc.itrust.unit.model.labProcedure;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Tests the LabProcedureMySQL class.
 * 
 * @author mwreesjo
 *
 */
public class LabProcedureMySQLTest {

	DataSource ds;
	@Mock
	DataSource mockDS;
	LabProcedureMySQL data;
	LabProcedure procedure;
	TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
		data = new LabProcedureMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();

		procedure = new LabProcedure();
		procedure.setCommentary("commentary");
		procedure.setConfidenceIntervalLower(10);
		procedure.setConfidenceIntervalUpper(30);
		procedure.setLabProcedureCode("12345-6");
		procedure.setIsRestricted(true);
		procedure.setLabProcedureID(8L);
		procedure.setLabTechnicianID(9L);
		procedure.setOfficeVisitID(10L);
		procedure.setPriority(3);
		procedure.setResults("results");
		procedure.setStatus(1L);
		procedure.setUpdatedDate(new Timestamp(100L));
		procedure.setHcpMID(9000000001L);
		
	}

	@After
	public void tearDown() throws FileNotFoundException, SQLException, IOException {
		gen.clearAllTables();
	}

	@Test
	public void testGetByID() throws FileNotFoundException, SQLException, IOException {
		try {
			gen.labProcedure0();
			gen.labProcedure0();
		} catch (SQLException | IOException e1) {
			fail("Couldn't set up test data");
			e1.printStackTrace();
		}
		try {
			LabProcedure proc = data.getByID(1L);
			Assert.assertNotNull(proc);
			Assert.assertTrue(proc.getLabProcedureID() == 1L);
			Assert.assertTrue(proc.getStatus().getID() == 1L);
			Assert.assertEquals("This is a lo pri lab procedure", proc.getCommentary());
			Assert.assertEquals("Foobar", proc.getResults());
		} catch (DBException e) {
			fail("Getting an existing lab procedure by ID shouldn't throw exception");
			e.printStackTrace();
		}
		// Now invoke the SQLException catch block via mocking
		data = new LabProcedureMySQL(mockDS);
		Connection mockConnection = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			data.getByID(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
	}
	
	@Test
	public void testGetLabProceduresByOfficeVisitSQLException() throws SQLException {
		// Now invoke the SQLException catch block via mocking
		data = new LabProcedureMySQL(mockDS);
		Connection mockConnection = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			data.getLabProceduresByOfficeVisit(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
	}

	/**
	 * Tests the getAll() method. At the time of writing of this test, the lab
	 * procedures returned are in insertion order. We use this assumption to
	 * validate the output.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testGetAll() throws SQLException {
		try {
			gen.labProcedure0();
			gen.labProcedure1();
			gen.labProcedure2();
			gen.labProcedure3();
			gen.labProcedure4();
			gen.labProcedure5();
		} catch (SQLException | IOException e1) {
			fail("Couldn't set up test data");
			e1.printStackTrace();
		}
		try {
			// Returned in insertion order
			List<LabProcedure> all = data.getAll();
			Assert.assertNotNull(all);
			Assert.assertTrue(all.size() == 6);
			Assert.assertEquals("This is a lo pri lab procedure", all.get(0).getCommentary());
			Assert.assertEquals("Foobar", all.get(0).getResults());
			Assert.assertEquals("This is for lab tech 5000000002", all.get(5).getCommentary());
		} catch (DBException e) {
			fail("Getting all lab procedures shouldn't throw exception");
			e.printStackTrace();
		}
		// Now invoke the SQLException catch block via mocking
		data = new LabProcedureMySQL(mockDS);
		Connection mockConnection = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			data.getAll();
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
	}

	@Test
	public void testAdd() {
		try {
			data.add(procedure);
		} catch (DBException e) {
			fail("Adding lab procedure should not throw error: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			List<LabProcedure> all = data.getAll();
			Assert.assertEquals(1, all.size());
			LabProcedure proc = all.get(0);
			Assert.assertNotNull(proc);
			Assert.assertEquals("commentary", proc.getCommentary());
			Assert.assertEquals(new Integer(10), proc.getConfidenceIntervalLower());
			Assert.assertEquals(new Integer(30), proc.getConfidenceIntervalUpper());
			Assert.assertEquals("12345-6", proc.getLabProcedureCode());
			Assert.assertTrue(proc.isRestricted());
			Assert.assertEquals(new Long(9L), proc.getLabTechnicianID());
			Assert.assertEquals(new Long(10L), proc.getOfficeVisitID());
			Assert.assertEquals(new Integer(3), proc.getPriority());
			Assert.assertEquals("results", proc.getResults());
			Assert.assertEquals(1L, proc.getStatus().getID());
			Timestamp now = new Timestamp(System.currentTimeMillis());
			// Assert updatedDate is within 5 seconds
			Assert.assertTrue(now.compareTo(proc.getUpdatedDate()) < 5000);
			Assert.assertEquals(new Long(9000000001L), proc.getHcpMID());
		} catch (DBException e) {
			fail("Couldn't get all lab procedures");
			e.printStackTrace();
		}
	}

	@Test
	public void testAddInvalid() throws SQLException {
		mockDS = Mockito.mock(DataSource.class);
		data = new LabProcedureMySQL(mockDS);
		procedure.setConfidenceIntervalLower(-1);
		try {
			data.add(procedure);
			fail("Adding invalid lab procedure should throw DBException");
		} catch (DBException e) {
			// Exception should be thrown
		}
		verify(mockDS, times(0)).getConnection();
	}

	@Test
	public void testUpdate() {
		try {
			gen.labProcedure0();
		} catch (Exception e) {
			fail("Couldn't set up test data");
		}
		procedure.setCommentary("updated comment");
		procedure.setLabProcedureID(1L);
		boolean success = false;
		try {
			success = data.update(procedure);
		} catch (DBException e) {
			fail("Shouldn't throw exception when updating lab procedure: " + e.getMessage());
		}
		if (!success) {
			fail("Update method should return true when updating is successful");
		}
		List<LabProcedure> all;
		try {
			all = data.getAll();
			if (all.size() != 1) {
				fail("Update method should not increase the number of lab procedures");
			}
			LabProcedure updatedProc = all.get(0);
			Assert.assertEquals(procedure.getCommentary(), updatedProc.getCommentary());
		} catch (DBException e) {
			fail("DBException should not be thrown");
		}
	}

	@Test
	public void testUpdateInvalid() {
		procedure.setLabProcedureCode("oopsie doopsie");
		boolean success = false;
		try {
			success = data.update(procedure);
			fail("Should throw exception when updating invalid lab procedure");
		} catch (DBException e) {
			// Exception should be thrown
		}
		if (success) {
			fail("Update method should return false when updating is unsuccessful");
		}
	}

	@Test
	public void testRemoveLabProcedure() {
		try {
			gen.labProcedure0();
		} catch (SQLException | IOException e1) {
			fail("Couldn't set up test data");
		}
		boolean success = false;
		try {
			success = data.removeLabProcedure(1L);
		} catch (DBException e) {
			fail("Shouldn't throw exception when removing existing lab procedure");
		}
		if (!success) {
			fail("removeLabProcedure should return true for successful removal");
		}
		try {
			List<LabProcedure> all = data.getAll();
			Assert.assertTrue(all.size() == 0);
		} catch (DBException e) {
			fail("Couldn't get all lab procedures");
		}
	}
	
	@Test
	public void testGetLabProcedure() throws Exception {
		gen.labProcedure0();
		LabProcedure proc = data.getByID(1L);
		Assert.assertNotNull(proc);
		Assert.assertEquals(5000000001L, proc.getLabTechnicianID().longValue());
		proc = data.getByID(0L);
		Assert.assertNull(proc);
	}
}
