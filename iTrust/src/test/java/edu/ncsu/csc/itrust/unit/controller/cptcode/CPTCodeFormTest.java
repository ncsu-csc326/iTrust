package edu.ncsu.csc.itrust.unit.controller.cptcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.cptcode.CPTCodeController;
import edu.ncsu.csc.itrust.controller.cptcode.CPTCodeForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class CPTCodeFormTest extends TestCase {
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }

    @Test
    public void testCPTCodeForm(){
        // test constructor
        CPTCodeForm form = new CPTCodeForm();
        CPTCodeController controller = new CPTCodeController();
        controller.setMySQL(new CPTCodeMySQL(ds));
        form = new CPTCodeForm(controller);
        Assert.assertEquals("", form.getSearch());
        Assert.assertFalse(form.getDisplayCodes());
        
        // test fields
        form.setSearch("1");
        Assert.assertEquals("1", form.getSearch());
        
        form.setCptCode(new CPTCode("code2", "name2"));
        Assert.assertEquals("code2", form.getCptCode().getCode());
        Assert.assertEquals("name2", form.getCptCode().getName());
        
        form.setCode("code3");
        form.setDescription("name3");
        Assert.assertEquals("code3", form.getCode());
        Assert.assertEquals("name3", form.getDescription());
        
        form.setDisplayCodes(true);
        Assert.assertTrue(form.getDisplayCodes());
        
        form.fillInput("code4", "name4");
        Assert.assertEquals("code4", form.getCode());
        Assert.assertEquals("name4", form.getDescription());
        
        // test add
        form.fillInput("1234A", "desc");
        form.add();
        form.fillInput("1234A", "desc");
        form.setSearch("1234A");
        List<CPTCode> codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234A", codeList.get(0).getCode());
        Assert.assertEquals("desc", codeList.get(0).getName());
        
        // test update
        form.setDescription("newDesc");
        form.update();
        form.fillInput("1234A", "newDesc");
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1234A", codeList.get(0).getCode());
        Assert.assertEquals("newDesc", codeList.get(0).getName());
        
        // test delete
        form.delete();
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(0, codeList.size());
    }
}
