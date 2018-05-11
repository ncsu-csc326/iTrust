package edu.ncsu.csc.itrust.unit.model.immunization;

import org.junit.Assert;
import org.junit.Test;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import junit.framework.TestCase;

public class ImmunizationTest extends TestCase {
    
    Immunization imm;
    
    @Override
    public void setUp(){
    	CPTCode cptCode = new CPTCode("90650", "HPV, bivalent");
    	imm = new Immunization(1, 1, cptCode);
    }
    
    @Test
    public void testCPTCode(){
    	CPTCode cptCode = new CPTCode("90649", "HPV, quadrivalent");
    	imm.setCptCode(cptCode);
    	Assert.assertEquals( "90649", imm.getCode() );
    	Assert.assertEquals( "HPV, quadrivalent", imm.getName() );
    }
    
    @Test
    public void testId(){
    	imm.setId(0);
    	Assert.assertEquals( 0, imm.getId() );
    }
    
    @Test
    public void testVisitId(){
    	imm.setVisitId(0);
    	Assert.assertEquals( 0, imm.getVisitId() );
    }
    
    
}
