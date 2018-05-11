package edu.ncsu.csc.itrust.unit.controller.icdCode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.icdcode.ICDCodeController;
import edu.ncsu.csc.itrust.controller.icdcode.ICDCodeForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.icdcode.ICDCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class ICDCodeFormTest extends TestCase {
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }
    
    @Test
    public void testICDCodeForm(){
        // test constructor
        ICDCodeForm form = new ICDCodeForm();
        ICDCodeController controller = new ICDCodeController();
        controller.setSQLData(new ICDCodeMySQL(ds));
        form = new ICDCodeForm(controller);
        Assert.assertEquals("", form.getSearch());
        Assert.assertFalse(form.getDisplayCodes());

        // test fields
        form.setCode("1");
        Assert.assertEquals("1", form.getCode());
        
        form.setDescription("2");
        Assert.assertEquals("2", form.getDescription());
        
        form.setDisplayCodes(true);
        Assert.assertTrue(form.getDisplayCodes());
        
        form.setIsChronic(true);
        Assert.assertTrue(form.getIsChronic());
        
        form.setSearch("3");
        Assert.assertEquals("3", form.getSearch());
        
        form.setIcdCode(new ICDCode("code", "desc", true));
        Assert.assertEquals("code", form.getIcdCode().getCode());
        Assert.assertEquals("desc", form.getIcdCode().getName());
        
        // test add
        form.fillInput("A11", "name1", false);
        form.add();
        form.setSearch("A11");
        form.fillInput("A11", "name1", false);
        List<ICDCode> codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("A11", codeList.get(0).getCode());
        Assert.assertEquals("name1", codeList.get(0).getName());
        
        // test update
        form.setDescription("newDesc");
        form.update();
        form.fillInput("A11", "newDesc", false);
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("A11", codeList.get(0).getCode());
        Assert.assertEquals("newDesc", codeList.get(0).getName());
        
        // test delete
        form.delete();
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(0, codeList.size());
    }
}
