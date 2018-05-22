package edu.ncsu.csc.itrust.unit.controller.loincCode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.loinccode.LoincCodeController;
import edu.ncsu.csc.itrust.controller.loinccode.LoincCodeForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class LOINCCodeFormTest extends TestCase{
    TestDataGenerator gen;
    DataSource ds;
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
    }
    
    @Test
    public void testForm(){
        // test constructor
        LoincCodeForm form = new LoincCodeForm();
        LoincCodeController controller = new LoincCodeController();
        controller.setSQLData(new LOINCCodeMySQL(ds));
        form = new LoincCodeForm(controller);
        Assert.assertEquals("", form.getSearch());
        Assert.assertFalse(form.getDisplayCodes());
        
        // test fields
        form.setCode("code");
        Assert.assertEquals("code", form.getCode());

        form.setComponent("comp");
        Assert.assertEquals("comp", form.getComponent());
        
        form.setDisplayCodes(true);
        Assert.assertTrue(form.getDisplayCodes());
        
        form.setKindOfProperty("kind");
        Assert.assertEquals("kind", form.getKindOfProperty());
        
        form.setLoincCode(new LOINCCode("code1", "comp1", "kind1"));
        Assert.assertEquals("code1", form.getLoincCode().getCode());
        
        form.setMethodType("method");
        Assert.assertEquals("method", form.getMethodType());
        
        form.setScaleType("scale");
        Assert.assertEquals("scale", form.getScaleType());
        
        form.setSearch("search");
        Assert.assertEquals("search", form.getSearch());
        
        form.setSystem("sys");
        Assert.assertEquals("sys", form.getSystem());
        
        form.setTimeAspect("time");
        Assert.assertEquals("time", form.getTimeAspect());
        
        // test add
        form.fillInput("1-2", "comp", "kind", "time", "sys", "scale", "method");
        form.add();
        form.setSearch("1-2");
        form.fillInput("1-2", "comp", "kind", "time", "sys", "scale", "method");
        List<LOINCCode> codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-2", codeList.get(0).getCode());
        Assert.assertEquals("comp", codeList.get(0).getComponent());
        
        // test update
        form.fillInput("1-2", "comp2", "kind", "time", "sys", "scale", "method");
        form.update();
        form.setSearch("1-2");
        form.fillInput("1-2", "comp2", "kind", "time", "sys", "scale", "method");
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-2", codeList.get(0).getCode());
        Assert.assertEquals("comp2", codeList.get(0).getComponent());
        
        // test delete
        form.delete();
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(0, codeList.size());
    }
}
