package edu.ncsu.csc.itrust.unit.model.cptcode;

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
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class CPTCodeMySQLTest extends TestCase {
    
    private DataSource ds;
    TestDataGenerator gen;
    private CPTCodeMySQL sql;
    
    @Override
    public void setUp() throws DBException, FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        sql = new CPTCodeMySQL(ds);
        gen = new TestDataGenerator();
    }
    
    @Test
    public void testCPTCodeMySQL() throws SQLException, FormValidationException, FileNotFoundException, IOException{
        gen.clearAllTables();
        // check that db is empty
    	List<CPTCode> codes = sql.getAll();
    	Assert.assertEquals(0, codes.size());
    	
    	// add a code
    	CPTCode code1 = new CPTCode("11111", "test1");
        Assert.assertTrue(sql.add(code1));
        
        // make sure it was added right
        codes = sql.getAll();
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals("11111", codes.get(0).getCode());
        Assert.assertEquals("test1", codes.get(0).getName());
        
        // get it by id to make sure that works
        CPTCode justAdded = sql.getByCode("11111");
        Assert.assertNotNull(justAdded);
        Assert.assertEquals("11111", codes.get(0).getCode());
        Assert.assertEquals("test1", codes.get(0).getName());
        
        // add another code
        CPTCode code2 = new CPTCode("22222", "test2");
        Assert.assertTrue(sql.add(code2));
        
        // make sure it was added
        codes = sql.getAll();
        Assert.assertEquals(2, codes.size());

        // update the first record
        code1.setName("test3");
        Assert.assertTrue(sql.update(code1));
        
        // check that db is still correct
        codes = sql.getAll();
        Assert.assertEquals(2, codes.size());
        
        CPTCode changed = sql.getByCode("11111");
        Assert.assertNotNull(changed);
        Assert.assertEquals("11111", changed.getCode());
        Assert.assertEquals("test3", changed.getName());
        
        CPTCode notChanged = sql.getByCode("22222");
        Assert.assertNotNull(notChanged);
        Assert.assertEquals("22222", notChanged.getCode());
        Assert.assertEquals("test2", notChanged.getName());
        
        // delete second record
        Assert.assertTrue(sql.delete(code2));
        
        // check that db is still correct
        codes = sql.getAll();
        Assert.assertEquals(1, codes.size());
        
        CPTCode stillThere = sql.getByCode("11111");
        Assert.assertNotNull(stillThere);
        Assert.assertEquals("11111", stillThere.getCode());
        Assert.assertEquals("test3", stillThere.getName());
        
        // delete last record
        Assert.assertTrue(sql.delete(code1));
        
        // check db
        Assert.assertEquals(0, sql.getAll().size());
    }
    
    @Test
    public void testDiabolicals() throws FileNotFoundException, SQLException, IOException, FormValidationException{
        gen.clearAllTables();
        // check that db is empty
        List<CPTCode> codes = sql.getAll();
        Assert.assertEquals(0, codes.size());
        
        // add invalid code
        CPTCode bad = new CPTCode("a", "test1");
        try {
            sql.add(bad);
            fail();
        } catch (FormValidationException e) {
            Assert.assertEquals("CPTCode: Up to four digit integer plus a letter or digit", e.getErrorList().get(0));
        }
        
        // add invalid name
        bad = new CPTCode("22222", "%$#.<>?/'()");
        try {
            sql.add(bad);
            fail();
        } catch (FormValidationException e) {
            Assert.assertEquals("Name: Up to 30 alphanumeric, space and ()<>,.\\-?/'", e.getErrorList().get(0));
        }
        
        // add a code
        CPTCode good = new CPTCode("33333", "test3");
        Assert.assertTrue(sql.add(good));
        // try to add it again
        Assert.assertFalse(sql.add(good));
        // make sure db is still only size 1
        Assert.assertEquals(1, sql.getAll().size());
        
        // update nonexistent code
        bad = new CPTCode("44444", "Hello World");
        Assert.assertFalse(sql.update(bad));
        // make sure db is still only size 1
        Assert.assertEquals(1, sql.getAll().size());
        
        // delete nonexistent code
        Assert.assertFalse(sql.delete(bad));
        // make sure db is still only size 1
        Assert.assertEquals(1, sql.getAll().size());
        
        // get invalid code
        Assert.assertNull(sql.getByCode("44444"));
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
