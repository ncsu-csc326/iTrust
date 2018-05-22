package edu.ncsu.csc.itrust.unit.model.immunization;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.immunization.ImmunizationValidator;
import junit.framework.TestCase;

public class ImmunizationValidatorTest extends TestCase {
    
    private Immunization imm;
    private ImmunizationValidator iv;
    private DataSource ds;
    
    @Override
    public void setUp(){
    	ds = ConverterDAO.getDataSource();
    	CPTCode cptCode = new CPTCode("90650", "HPV, bivalent");
    	imm = new Immunization(1, 1, cptCode);
    	iv = new ImmunizationValidator(ds);
    }
    
    @Test
    public void testCorrectValidation(){
    	try{ 
    		iv.validate(imm);
    	} catch( FormValidationException e){
    		Assert.fail();
    	}
    	Assert.assertTrue(true);
    }
    
    @Test
    public void testValidationBadCodeLength(){
    	imm.getCptCode().setCode("123456");
    	
    	try{ 
    		iv.validate(imm);
    	} catch( FormValidationException e){
    		Assert.assertTrue(true);
    		return;
    	}
    	Assert.fail();
    }
    
    @Test
    public void testValidationBadCodeZeroLength(){
    	imm.getCptCode().setCode("");
    	
    	try{ 
    		iv.validate(imm);
    	} catch( FormValidationException e){
    		Assert.assertTrue(true);
    		return;
    	}
    	Assert.fail();
    }
    
}
