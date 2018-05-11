package edu.ncsu.csc.itrust.unit.model.medicalProcedure;

import org.junit.Assert;

import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedure;
import junit.framework.TestCase;

public class MedicalProcedureTest extends TestCase{
    public void testFields(){
        MedicalProcedure p = new MedicalProcedure();
        
        p.setId(90);
        Assert.assertEquals(90, p.getId());
        
        p.setOfficeVisitId(80);
        Assert.assertEquals(80, p.getOfficeVisitId());
        
        p.setCptCode(new CPTCode("blah", "other"));
        Assert.assertEquals("blah", p.getCode());
        Assert.assertEquals("other", p.getName());
    }
}
