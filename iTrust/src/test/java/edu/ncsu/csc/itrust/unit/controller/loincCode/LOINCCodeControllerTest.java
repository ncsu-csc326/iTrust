package edu.ncsu.csc.itrust.unit.controller.loincCode;

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
import edu.ncsu.csc.itrust.controller.loinccode.LoincCodeController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class LOINCCodeControllerTest extends TestCase {
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }
    
    public void testConstructor(){
        LoincCodeController controller = new LoincCodeController(ds);
        Assert.assertNotNull(controller);
        
        controller = new LoincCodeController();
        controller.setSQLData(new LOINCCodeMySQL(ds));
    }
    
    public void testAdd(){
        LoincCodeController controller = new LoincCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add an ICD code
        controller.add(new LOINCCode("1-1", "name1", "kind"));
        // verify
        List<LOINCCode> codeList = controller.getCodesWithFilter("1-1");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-1", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getComponent());
        Assert.assertEquals("kind", codeList.get(0).getKindOfProperty());
        
        // try to add again
        controller.add(new LOINCCode("1-1", "name1", "kind"));
        // verify it wasn't added
        codeList = controller.getCodesWithFilter("1-1");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-1", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getComponent());
        Assert.assertEquals("kind", codeList.get(0).getKindOfProperty());
        
        // try to add garbage code
        controller.add(new LOINCCode("fda", "name1", "kind"));
        // verify it wasn't added
        Assert.assertNull(controller.getCodeByID("fda"));
        
        // cleanup
        controller.remove("1-1");
    }
    
    public void testEdit(){
        LoincCodeController controller = new LoincCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add an ICD code
        controller.add(new LOINCCode("2-2", "name1", "kind"));
        // verify
        List<LOINCCode> codeList = controller.getCodesWithFilter("2-2");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("2-2", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getComponent());
        
        // edit the code
        controller.edit(new LOINCCode("2-2", "name2", "kind"));
        // verify
        Assert.assertEquals("name2", controller.getCodeByID("2-2").getComponent());
        
        // edit with garbage
        controller.edit(new LOINCCode("2-2", "name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2", "kind"));
        // verify
        Assert.assertEquals("name2", controller.getCodeByID("2-2").getComponent());
        
        // edit a nonexistent code
        controller.edit(new LOINCCode("3-3", "name3", "kind"));
        // verify not added
        Assert.assertNull(controller.getCodeByID("3-3"));
        
        // cleanup
        controller.remove("2-2");
    }
    
    public void testRemove(){
        LoincCodeController controller = new LoincCodeController(ds);
        Assert.assertNotNull(controller);
        
        // test deleting nonexistent code
        controller.remove("garbage");
    }
    
    public void testSQLErrors() throws SQLException, FormValidationException, DBException{
        DataSource mockDS = mock(DataSource.class);
        LoincCodeController controller = new LoincCodeController(mockDS);
        controller = spy(controller);
        LOINCCodeMySQL mockData = mock(LOINCCodeMySQL.class);
        controller.setSQLData(mockData);
        when(mockData.getByCode(Mockito.anyString())).thenThrow(new DBException(null));
        controller.getCodeByID("b");
        when(mockData.add(Mockito.anyObject())).thenThrow(new DBException(null));
        controller.add(new LOINCCode("garbage", "garbage", "garbage"));
        when(mockData.update(Mockito.anyObject())).thenThrow(new DBException(null));
        controller.edit(new LOINCCode("garbage", "garbage", "garbage"));
        when(mockData.delete(Mockito.anyObject())).thenThrow(new SQLException());
        controller.remove("garbage");
        when(mockData.getCodesWithFilter(Mockito.anyObject())).thenThrow(new SQLException());
        controller.getCodesWithFilter("garbage");
    }
}
