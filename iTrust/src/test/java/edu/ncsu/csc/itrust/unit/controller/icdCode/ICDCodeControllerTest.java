package edu.ncsu.csc.itrust.unit.controller.icdCode;

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

import edu.ncsu.csc.itrust.controller.icdcode.ICDCodeController;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class ICDCodeControllerTest extends TestCase {
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }
    
    public void testConstructor(){
        ICDCodeController controller = new ICDCodeController(ds);
        Assert.assertNotNull(controller);
        
        controller = new ICDCodeController();
        controller.setSQLData(new ICDCodeMySQL(ds));
    }
    
    public void testAdd(){
        ICDCodeController controller = new ICDCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add an ICD code
        controller.add(new ICDCode("B11", "name1", true));
        // verify
        List<ICDCode> codeList = controller.getCodesWithFilter("B11");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("B11", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // try to add again
        controller.add(new ICDCode("B11", "name1", true));
        // verify it wasn't added
        codeList = controller.getCodesWithFilter("B11");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("B11", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // try to add garbage code
        controller.add(new ICDCode("fdsafdsafds", "name2", true));
        // verify it wasn't added
        Assert.assertNull(controller.getCodeByID("fdsafdsafds"));
        
        // cleanup
        controller.remove("B11");
    }
    
    public void testEdit(){
        ICDCodeController controller = new ICDCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add an ICD code
        controller.add(new ICDCode("C11", "name1", true));
        // verify
        List<ICDCode> codeList = controller.getCodesWithFilter("C11");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("C11", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // edit the code
        controller.edit(new ICDCode("C11", "name2", false));
        // verify
        Assert.assertEquals("name2", controller.getCodeByID("C11").getName());
        
        // edit with garbage
        controller.edit(new ICDCode("C11", "name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2", false));
        // verify
        Assert.assertEquals("name2", controller.getCodeByID("C11").getName());
        
        // edit a nonexistent code
        controller.edit(new ICDCode("C22", "name3", true));
        // verify not added
        Assert.assertNull(controller.getCodeByID("C22"));
        
        // cleanup
        controller.remove("C11");
    }
    
    public void testRemove(){
        ICDCodeController controller = new ICDCodeController(ds);
        Assert.assertNotNull(controller);
        
        // test deleting nonexistent code
        controller.remove("garbage");
    }
    
    public void testSQLErrors() throws SQLException, FormValidationException{
        DataSource mockDS = mock(DataSource.class);
        ICDCodeController controller = new ICDCodeController(mockDS);
        controller = spy(controller);
        ICDCodeMySQL mockData = mock(ICDCodeMySQL.class);
        controller.setSQLData(mockData);
        when(mockData.getByCode(Mockito.anyString())).thenThrow(new SQLException());
        controller.getCodeByID("b");
        when(mockData.add(Mockito.anyObject())).thenThrow(new SQLException());
        controller.add(new ICDCode("garbage", "garbage", true));
        when(mockData.update(Mockito.anyObject())).thenThrow(new SQLException());
        controller.edit(new ICDCode("garbage", "garbage", true));
        when(mockData.delete(Mockito.anyObject())).thenThrow(new SQLException());
        controller.remove("garbage");
        when(mockData.getCodesWithFilter(Mockito.anyObject())).thenThrow(new SQLException());
        controller.getCodesWithFilter("garbage");
    }
}
