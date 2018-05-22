package edu.ncsu.csc.itrust.unit.model.diagnosis;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import junit.framework.TestCase;

public class DiagnosisTest extends TestCase {
    
    @Test
    public void testGettersAndSetters(){
        Diagnosis d = new Diagnosis(0, 0, null);
        Assert.assertEquals(0, d.getId());
        Assert.assertEquals(0, d.getVisitId());
        Assert.assertEquals(null, d.getIcdCode());
        Assert.assertEquals("", d.getCode());
        Assert.assertEquals("", d.getName());
        
        d.setVisitId(2);
        Assert.assertEquals(2, d.getVisitId());
        
        ICDCode code = new ICDCode("code", "name", true);
        d.setIcdCode(code);
        Assert.assertEquals(code, d.getIcdCode());
        Assert.assertEquals("code", d.getCode());
        Assert.assertEquals("name", d.getName());
    }
}
