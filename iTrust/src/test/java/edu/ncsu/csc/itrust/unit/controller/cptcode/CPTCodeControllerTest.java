package edu.ncsu.csc.itrust.unit.controller.cptcode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.cptcode.CPTCodeController;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class CPTCodeControllerTest extends TestCase {
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }
    
    public void testConstructor(){
        // test testing constructor
        CPTCodeController controller = new CPTCodeController(ds);
        Assert.assertNotNull(controller);
        
        controller = new CPTCodeController();
        controller.setMySQL(new CPTCodeMySQL(ds));
    }
    
    public void testAdd(){
        CPTCodeController controller = new CPTCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add a CPTCode
        controller.add(new CPTCode("1234B", "name1"));
        // verify
        List<CPTCode> codeList = controller.getCodesWithFilter("1234B");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234B", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // try to add again
        controller.add(new CPTCode("1234B", "name2"));
        // verify it wasn't added
        codeList = controller.getCodesWithFilter("1234B");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234B", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // try to add a garbage code
        controller.add(new CPTCode("fkhdskh432432", "name2"));
        // verify it wasn't added
        codeList = controller.getCodesWithFilter("fkhdskh432432");
        Assert.assertEquals(0, codeList.size());
        
        // cleanup
        controller.remove("1234B");
    }
    
    public void testEdit(){
        CPTCodeController controller = new CPTCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add a CPTCode
        controller.add(new CPTCode("1234C", "name1"));
        // verify
        List<CPTCode> codeList = controller.getCodesWithFilter("1234C");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234C", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // edit the code
        controller.edit(new CPTCode("1234C", "name2"));
        // verify
        codeList = controller.getCodesWithFilter("1234C");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234C", codeList.get(0).getCode());
        Assert.assertEquals("name2", codeList.get(0).getName());
        
        // edit with garbage
        controller.edit(new CPTCode("1234C", "name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2"));
        // verify that nothing changed
        codeList = controller.getCodesWithFilter("1234C");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234C", codeList.get(0).getCode());
        Assert.assertEquals("name2", codeList.get(0).getName());
        
        // edit a nonexistent code
        controller.edit(new CPTCode("1234D", "name2"));
        // verify that code wasn't added
        codeList = controller.getCodesWithFilter("1234D");
        Assert.assertEquals(0, codeList.size());
        
        // cleanup
        controller.remove("1234C");
    }
    
    public void testRemove(){
        CPTCodeController controller = new CPTCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add a CPTCode
        controller.add(new CPTCode("1234E", "name1"));
        // verify
        List<CPTCode> codeList = controller.getCodesWithFilter("1234E");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234E", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // remove it
        controller.remove("1234E");
        // verify
        codeList = controller.getCodesWithFilter("1234E");
        Assert.assertEquals(0, codeList.size());
        
        // remove nonexistent code
        controller.remove("1234E");
        // make sure it's still gone
        codeList = controller.getCodesWithFilter("1234E");
        Assert.assertEquals(0, codeList.size());
    }
    
    public void testGetters(){
        CPTCodeController controller = new CPTCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add a CPTCode
        controller.add(new CPTCode("1234F", "name1"));
        // verify
        List<CPTCode> codeList = controller.getCodesWithFilter("1234F");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234F", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        CPTCode code = controller.getCodeByID("1234F");
        Assert.assertEquals("1234F", code.getCode());
        Assert.assertEquals("name1", code.getName());
    }
    
    public void testSQLErrors() throws SQLException, FormValidationException{
        DataSource mockDS = mock(DataSource.class);
        CPTCodeController controller = new CPTCodeController(mockDS);
        controller = spy(controller);
        CPTCodeMySQL mockData = mock(CPTCodeMySQL.class);
        controller.setMySQL(mockData);
        when(mockData.getByCode(Mockito.anyString())).thenThrow(new SQLException());
        controller.getCodeByID("b");
        when(mockData.add(Mockito.anyObject())).thenThrow(new SQLException());
        controller.add(new CPTCode("garbage", "garbage"));
        when(mockData.update(Mockito.anyObject())).thenThrow(new SQLException());
        controller.edit(new CPTCode("garbage", "garbage"));
        when(mockData.delete(Mockito.anyObject())).thenThrow(new SQLException());
        controller.remove("garbage");
        when(mockData.getCodesWithFilter(Mockito.anyObject())).thenThrow(new SQLException());
        controller.getCodesWithFilter("garbage");
    }
}
