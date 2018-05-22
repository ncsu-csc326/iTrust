package edu.ncsu.csc.itrust.unit.model.ndcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class NDCCodeMySQLTest extends TestCase {
    
    private DataSource ds;
    
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
    }
    
    public void testNDCodeMySQL() throws FileNotFoundException, SQLException, IOException, FormValidationException{
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        NDCCodeMySQL sql = new NDCCodeMySQL(ds);
        
        // ensure there are no records to start
        Assert.assertEquals(0, sql.getAll().size());
        
        // test adding a valid record
        NDCCode newCode = new NDCCode();
        newCode.setCode("123");
        newCode.setDescription("test");
        Assert.assertTrue(sql.add(newCode));
        List<NDCCode> ndList = sql.getAll();
        Assert.assertEquals(1, ndList.size());
        Assert.assertEquals("123", ndList.get(0).getCode());
        Assert.assertEquals("test", ndList.get(0).getDescription());
        
        // make sure we can get it with getByID()
        NDCCode toCheck = sql.getByCode("123");
        Assert.assertNotNull(toCheck);
        Assert.assertEquals("123", ndList.get(0).getCode());
        Assert.assertEquals("test", ndList.get(0).getDescription());
        
        // make sure we can't get nonexistent codes
        toCheck = sql.getByCode("a");
        Assert.assertNull(toCheck);
        
        // make sure we can't add it again
        Assert.assertFalse(sql.add(newCode));
        ndList = sql.getAll();
        Assert.assertEquals(1, ndList.size());
        Assert.assertEquals("123", ndList.get(0).getCode());
        Assert.assertEquals("test", ndList.get(0).getDescription());
        
        // test adding a record with bad code
        newCode.setCode("a");
        newCode.setDescription("test2");
        try {
            sql.add(newCode);
            fail("Record should not have been added");
        } catch (FormValidationException e){
            List<String> errors = e.getErrorList();
            Assert.assertEquals(1, errors.size());
            Assert.assertEquals("NDCCode: Up to five digits, followed by an optional dash with 1-4 more digits", errors.get(0));
        }
        Assert.assertEquals(1, sql.getAll().size());
        
        // test adding a record with too long description
        newCode.setCode("321");
        newCode.setDescription("test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3test3");
        try {
            sql.add(newCode);
            fail("Record should not have been added");
        } catch (FormValidationException e){
            List<String> errors = e.getErrorList();
            Assert.assertEquals(1, errors.size());
            Assert.assertEquals("Description: Up to 100 alphanumeric characters plus space and ()<>,.-?/'", errors.get(0));
        }
        Assert.assertEquals(1, sql.getAll().size());
        
        // test deleting a record that doesn't exist
        newCode.setCode("4");
        newCode.setDescription("test4");
        Assert.assertFalse(sql.delete(newCode));
        Assert.assertEquals(1, sql.getAll().size());
        
        // test updating 123
        newCode.setCode("123");
        newCode.setDescription("blah");
        Assert.assertTrue(sql.update(newCode));
        toCheck = sql.getByCode("123");
        Assert.assertNotNull(toCheck);
        Assert.assertEquals("blah", toCheck.getDescription());
        
        // make sure we can't update with a bad record
        newCode.setDescription("fdsa$@$@$<>,.?/");
        try {
            sql.update(newCode);
            fail();
        } catch (FormValidationException e){
            // yay we passed
        }
        toCheck = sql.getByCode("123");
        Assert.assertNotNull(toCheck);
        Assert.assertEquals("blah", toCheck.getDescription());
        
        // test updating nonexistent record
        newCode.setDescription("blah2");
        newCode.setCode("54643");
        Assert.assertFalse(sql.update(newCode));
        
        // test deleting a record that does exist
        newCode.setCode("123");
        newCode.setDescription("test1");
        Assert.assertTrue(sql.delete(newCode));
        Assert.assertEquals(0, sql.getAll().size());
    }
    
    public void testProdConstructor(){
        try {
            new NDCCodeMySQL();
            fail();
        } catch (DBException e) {
            // yay, we passed
        }
    }
}
