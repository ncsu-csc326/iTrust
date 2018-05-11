package edu.ncsu.csc.itrust.unit.controller.ndcCode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.ndcode.NDCCodeController;
import edu.ncsu.csc.itrust.controller.ndcode.NDCCodeForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class NDCCodeFormTest extends TestCase {
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
        NDCCodeForm form = new NDCCodeForm();
        NDCCodeController controller = new NDCCodeController();
        controller.setSQLData(new NDCCodeMySQL(ds));
        form = new NDCCodeForm(controller);
        Assert.assertEquals("", form.getSearch());
        Assert.assertFalse(form.getDisplayCodes());
        
        // test fields
        form.setCode("code");
        Assert.assertEquals("code", form.getCode());
        
        form.setDescription("desc");
        Assert.assertEquals("desc", form.getDescription());
        
        form.setDisplayCodes(true);
        Assert.assertTrue(form.getDisplayCodes());
        
        form.setNDCCode(new NDCCode("code2", "desc2"));
        Assert.assertEquals("code2", form.getNDCCode().getCode());
        Assert.assertEquals("desc2", form.getNDCCode().getDescription());
        
        form.setSearch("search");
        Assert.assertEquals("search", form.getSearch());
        
        // test add
        form.fillInput("1-1", "description");
        form.add();
        form.setSearch("1-1");
        form.fillInput("1-1", "description");
        List<NDCCode> codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-1", codeList.get(0).getCode());
        Assert.assertEquals("description", codeList.get(0).getDescription());
        
        // test update
        form.setDescription("newDesc");
        form.update();
        form.fillInput("1-1", "newDesc");
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(1, codeList.size());
        Assert.assertEquals("1-1", codeList.get(0).getCode());
        Assert.assertEquals("newDesc", codeList.get(0).getDescription());

        // test delete
        form.delete();
        codeList = form.getCodesWithFilter();
        Assert.assertEquals(0, codeList.size());
    }
}
