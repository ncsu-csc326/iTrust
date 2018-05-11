package edu.ncsu.csc.itrust.unit.model.cptcode;

import org.junit.Assert;
import org.junit.Test;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import junit.framework.TestCase;

public class CPTCodeTest extends TestCase {
    
    CPTCode cptCode;
    
    @Override
    public void setUp(){
    	cptCode = new CPTCode("90650", "HPV, bivalent");
    }
    
    @Test
    public void testCode(){
    	cptCode.setCode("90649");
    	Assert.assertEquals( "90649", cptCode.getCode() );
    }
    
    @Test
    public void testName(){
    	cptCode.setName("HPV, quadrivalent");
    	Assert.assertEquals( "HPV, quadrivalent", cptCode.getName() );
    }
        
}
