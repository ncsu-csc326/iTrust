package edu.ncsu.csc.itrust.unit.model.immunization;

import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.immunization.ImmunizationMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class ImmunizationMySQLTest extends TestCase {
    
    private DataSource ds;
	@Mock
	DataSource mockDS;
    TestDataGenerator gen;
    private ImmunizationMySQL sql;
    
    @Override
    public void setUp() throws DBException, FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
		mockDS = Mockito.mock(DataSource.class);
        sql = new ImmunizationMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc21();
    }
    
    @Test
    public void testGetAll() throws SQLException {
    	List<Immunization> immunizations = null;
    	try {
    		immunizations = sql.getAll();
		} catch (DBException e) {
			Assert.fail();
		}
    	
    	Assert.assertEquals(1, immunizations.size());

		// Now invoke the SQLException catch block via mocking
		sql = new ImmunizationMySQL(mockDS);
		Connection mockConnection = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getAll();
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
    }
       
    @Test
    public void testAdd() throws DBException, SQLException {
    	{
	    	CPTCode code = new CPTCode("90636", "Hep A-Hep B" );
	    	Immunization immunization = new Immunization(500 , 1, code);    	
	    	Assert.assertTrue(sql.add(immunization));
	    	
			// Now invoke the SQLException catch block via mocking
			sql = new ImmunizationMySQL(mockDS);
			Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
			try {
				sql.add(immunization);
				fail("Exception should be thrown");
			} catch (DBException e) {
				// Exception should throw
			}
    	}
    	{
	    	CPTCode code = new CPTCode("", "Hep A-Hep B" );
	    	Immunization immunization = new Immunization(501 , 1, code);
	    	try {
	    		sql.add(immunization);
	    		fail();
	    	} catch (DBException e) {
	    		// Do nothing
	    	}
    	}
    }
    
    @Test
    public void testGetByID() throws DBException, SQLException {
    	Immunization immunization = sql.getByID(-1L);
    	Assert.assertNull(immunization);
    	Immunization expected = sql.getAllImmunizations(201L).get(0);
    	Immunization actual = sql.getByID(expected.getId());
    	assertEquals(expected.getCptCode().getCode(), actual.getCptCode().getCode());

		// Now invoke the SQLException catch block via mocking
		sql = new ImmunizationMySQL(mockDS);
		Connection mockConnection = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getByID(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
    }
    
    @Test
    public void testGetAllImmunizations() throws DBException, SQLException {
    	List<Immunization> list = sql.getAllImmunizations(201L);
    	Assert.assertNotNull(list);
    	Assert.assertEquals(1, list.size());

		// Now invoke the SQLException catch block via mocking
		sql = new ImmunizationMySQL(mockDS);
		Connection mockConnection = Mockito.mock(Connection.class);
		when(mockDS.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());
		try {
			sql.getAllImmunizations(1L);
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
    }
    
    @Test
    public void testUpdate() throws DBException, SQLException{
    	CPTCode code = new CPTCode("90636", "Hep A-Hep B" );
    	Immunization immunization = new Immunization(1 , 1, code);
    	Assert.assertTrue(sql.update(immunization));
    	immunization.setId(-1L);
    	Assert.assertFalse(sql.update(immunization));
    	immunization.getCptCode().setCode("");
    	try {
    		sql.update(immunization);
			fail("Exception should be thrown");
    	} catch (DBException e) {
    		// Do nothing
    	}

		// Now invoke the SQLException catch block via mocking
		sql = new ImmunizationMySQL(mockDS);
		Mockito.doThrow(SQLException.class).when(mockDS).getConnection();
		try {
			sql.update(immunization);
			fail("Exception should be thrown");
		} catch (DBException e) {
			// Exception should throw
		}
    }
    
    @Test
    public void testNoDataSource(){
        try {
            new CPTCodeMySQL();
            Assert.fail();
        } catch (DBException e){
            Assert.assertTrue(true);
        }
    }
   
}
