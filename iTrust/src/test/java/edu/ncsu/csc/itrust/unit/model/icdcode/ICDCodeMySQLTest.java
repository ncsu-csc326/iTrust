package edu.ncsu.csc.itrust.unit.model.icdcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class ICDCodeMySQLTest extends TestCase {
	
	private ICDCodeMySQL mysql;

	private DataSource ds;
	
	@Override
	public void setUp() {
        ds = ConverterDAO.getDataSource();
		mysql = new ICDCodeMySQL(ds);
	}

	@Test
	public void testICDCodeMySQL() throws Exception {
	    TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        
        // ensure db is empty
        List<ICDCode> icdList = mysql.getAll();
        Assert.assertEquals(0, icdList.size());
        
        // add a code
        ICDCode code1 = new ICDCode("A11", "test1", true);
        Assert.assertTrue(mysql.add(code1));
        
        // make sure it was added right
        icdList = mysql.getAll();
        Assert.assertEquals(1, icdList.size());
        Assert.assertEquals("A11", icdList.get(0).getCode());
        Assert.assertEquals("test1", icdList.get(0).getName());
        Assert.assertTrue(icdList.get(0).isChronic());
        
        // get it with getByID() and check again
        ICDCode toCheck = mysql.getByCode("A11");
        Assert.assertNotNull(toCheck);
        Assert.assertEquals("A11", toCheck.getCode());
        Assert.assertEquals("test1", toCheck.getName());
        Assert.assertTrue(toCheck.isChronic());
        
        
        // add another code
        ICDCode code2 = new ICDCode("B22", "test2", false);
        Assert.assertTrue(mysql.add(code2));
        
        // check that it was added
        icdList = mysql.getAll();
        Assert.assertEquals(2, icdList.size());
        
        // update the first record
        code1.setName("test3");
        code1.setChronic(false);
        Assert.assertTrue(mysql.update(code1));
        
        // check that db is still the same size
        icdList = mysql.getAll();
        Assert.assertEquals(2, icdList.size());
        
        // delete the second record
        Assert.assertTrue(mysql.delete(code2));
        
        // check db size
        icdList = mysql.getAll();
        Assert.assertEquals(1, icdList.size());
        
        // check that code1 is still in there
        Assert.assertEquals("A11", icdList.get(0).getCode());
        Assert.assertEquals("test3", icdList.get(0).getName());
        Assert.assertFalse(icdList.get(0).isChronic());
        
        // delete last record
        Assert.assertTrue(mysql.delete(code1));
        
        // check db size
        icdList = mysql.getAll();
        Assert.assertEquals(0, icdList.size());
	}

	@Test
	public void testDiabolicals() throws FileNotFoundException, SQLException, IOException, DBException, FormValidationException{
	    TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        
        // ensure db is empty
        List<ICDCode> icdList = mysql.getAll();
        Assert.assertEquals(0, icdList.size());
        
        // add a bad code
        ICDCode code1 = new ICDCode("A", "test1", true);
        try {
            mysql.add(code1);
            fail();
        } catch (FormValidationException e){
            Assert.assertEquals("ICDCode: A capital letter, followed by a number, followed by a capital letter or number, optionally followed by 1-4 capital letters or numbers", e.getErrorList().get(0));
        }
        
        // ensure db is empty
        icdList = mysql.getAll();
        Assert.assertEquals(0, icdList.size());
        
        // add a good record
        code1.setCode("A11");
        Assert.assertTrue(mysql.add(code1));
        
        // check db
        icdList = mysql.getAll();
        Assert.assertEquals(1, icdList.size());
        
        // try to add it again
        Assert.assertFalse(mysql.add(code1));
        
        // make sure that failed
        Assert.assertEquals(1, icdList.size());
        
        // update with bad name
        code1.setName("*&?><:;");
        try {
            mysql.update(code1);
            fail();
        } catch (FormValidationException e){
            Assert.assertEquals("Name: Up to 30 alphanumeric, space, and ()<>,.\\-?/'", e.getErrorList().get(0));
        }
        
        // put name back, object is valid now
        code1.setName("test1");
        // change code
        code1.setCode("B22");
        // try to delete nonexistent code
        Assert.assertFalse(mysql.delete(code1));
        // try to edit nonexistent code
        Assert.assertFalse(mysql.update(code1));
	}

	@Test
	public void testProdConstructor(){
	    try {
            new ICDCodeMySQL();
            fail();
        } catch (DBException e) {
            // yay we passed
        }
	}
}
