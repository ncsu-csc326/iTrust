package edu.ncsu.csc.itrust.unit.controller.ndcCode;

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

import edu.ncsu.csc.itrust.controller.ndcode.NDCCodeController;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class NDCCodeControllerTest extends TestCase {
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }
    
    public void testConstructor(){
        NDCCodeController controller = new NDCCodeController(ds);
        Assert.assertNotNull(controller);
        
        controller = new NDCCodeController();
        controller.setSQLData(new NDCCodeMySQL(ds));
    }
    
    public void testAdd(){
        NDCCodeController controller = new NDCCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add an NDC code
        controller.add(new NDCCode("1-1", "name1"));
        // verify
        List<NDCCode> codeList = controller.getCodesWithFilter("1-1");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-1", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getDescription());
        
        // try to add again
        controller.add(new NDCCode("1-2", "name1"));
        // verify it wasn't added
        codeList = controller.getCodesWithFilter("1-2");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-2", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getDescription());
        
        // try to add garbage code
        controller.add(new NDCCode("fdsafdsafds", "name2"));
        // verify it wasn't added
        Assert.assertNull(controller.getCodeByID("fdsafdsafds"));
        
        // cleanup
        controller.remove("1-2");
    }
    
    public void testEdit(){
        NDCCodeController controller = new NDCCodeController(ds);
        Assert.assertNotNull(controller);
        
        // add an NDC code
        controller.add(new NDCCode("2-1", "name1"));
        // verify
        List<NDCCode> codeList = controller.getCodesWithFilter("2-1");
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("2-1", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getDescription());
        
        // edit the code
        controller.edit(new NDCCode("2-1", "name2"));
        // verify
        Assert.assertEquals("name2", controller.getCodeByID("2-1").getDescription());
        
        // edit with garbage
        controller.edit(new NDCCode("C11", "name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2name2"));
        // verify
        Assert.assertEquals("name2", controller.getCodeByID("2-1").getDescription());
        
        // edit a nonexistent code
        controller.edit(new NDCCode("2-2", "name3"));
        // verify not added
        Assert.assertNull(controller.getCodeByID("2-2"));
        
        // cleanup
        controller.remove("2-2");
    }
    
    public void testRemove(){
        NDCCodeController controller = new NDCCodeController(ds);
        Assert.assertNotNull(controller);
        
        // test deleting nonexistent code
        controller.remove("garbage");
    }
    
    public void testSQLErrors() throws SQLException, FormValidationException{
        DataSource mockDS = mock(DataSource.class);
        NDCCodeController controller = new NDCCodeController(mockDS);
        controller = spy(controller);
        NDCCodeMySQL mockData = mock(NDCCodeMySQL.class);
        controller.setSQLData(mockData);
        when(mockData.getByCode(Mockito.anyString())).thenThrow(new SQLException());
        controller.getCodeByID("b");
        when(mockData.add(Mockito.anyObject())).thenThrow(new SQLException());
        controller.add(new NDCCode("garbage", "garbage"));
        when(mockData.update(Mockito.anyObject())).thenThrow(new SQLException());
        controller.edit(new NDCCode("garbage", "garbage"));
        when(mockData.delete(Mockito.anyObject())).thenThrow(new SQLException());
        controller.remove("garbage");
        when(mockData.getCodesWithFilter(Mockito.anyObject())).thenThrow(new SQLException());
        controller.getCodesWithFilter("garbage");
    }
}
