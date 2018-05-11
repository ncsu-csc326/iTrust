package edu.ncsu.csc.itrust.unit.model.officeVisit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class OfficeVisitMySQLTest extends TestCase {
	
	private DataSource ds;
	private OfficeVisitMySQL ovsql;
	private TestDataGenerator gen;

	@Mock
	private DataSource mockDataSource;
	@Mock
	private Connection mockConnection;
	@Mock
	private PreparedStatement mockPreparedStatement;
	@Mock
	private ResultSet mockResultSet;
	private OfficeVisitMySQL mockOvsql;
	
	@Override
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		ovsql = new OfficeVisitMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.uc51();

		mockDataSource = Mockito.mock(DataSource.class);
		mockOvsql = new OfficeVisitMySQL(mockDataSource);
		
		mockConnection = Mockito.mock(Connection.class);
		mockResultSet = Mockito.mock(ResultSet.class);
		mockPreparedStatement = Mockito.mock(PreparedStatement.class);
	}
	
	@Test
	public void testGetVisitsForPatient() throws Exception {
		List<OfficeVisit> list101 = ovsql.getVisitsForPatient(101L);
		assertEquals(0, list101.size());
	}
	
	@Test
	public void testGetPatientDOB() throws Exception {
		LocalDate patient101DOB = ovsql.getPatientDOB(101L);
		// patient with MID 101 is initialized with birthday as '2013-05-01'
		LocalDate expectedPatientDOB = LocalDate.of(2013, 5, 1);
		assertEquals(expectedPatientDOB, patient101DOB);
		
		LocalDate invalidPatient106DOB = ovsql.getPatientDOB(106L);
		assertNull(invalidPatient106DOB);
		
		LocalDate invalidPatient107DOB = ovsql.getPatientDOB(107L);
		assertNull(invalidPatient107DOB);
	}
	
	@Test
	public void testGetPatientDOBWithInvalidDataSource() throws Exception {
		Mockito.doThrow(new SQLException("mock exception")).when(mockDataSource).getConnection();
		LocalDate patient101DOB = mockOvsql.getPatientDOB(101L);
		assertNull(patient101DOB);
		
		Mockito.reset(mockDataSource);
		
		Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
		Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
		Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
		Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
		LocalDate patient102DOB = mockOvsql.getPatientDOB(102L);
		assertNull(patient102DOB);
	}
}
