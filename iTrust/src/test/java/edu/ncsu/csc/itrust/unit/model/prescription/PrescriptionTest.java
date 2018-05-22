package edu.ncsu.csc.itrust.unit.model.prescription;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.prescription.Prescription;

public class PrescriptionTest extends TestCase {
    
    @Test
    public void testEverything(){
        Prescription p = new Prescription();
        
        p.setDrugCode(new MedicationBean("code","name"));
        Assert.assertEquals("code", p.getCode());
        Assert.assertEquals("name", p.getName());
        
        LocalDate now = LocalDate.now();
        p.setEndDate(now);
        Assert.assertEquals(now, p.getEndDate());
        
        now = LocalDate.of(2012, 12, 12);
        p.setStartDate(now);
        Assert.assertEquals(now, p.getStartDate());
        
        p.setOfficeVisitId(155);
        Assert.assertEquals(155, p.getOfficeVisitId());
        
        p.setPatientMID(100);
        Assert.assertEquals(100, p.getPatientMID());
        
        p.setId(109);
        Assert.assertEquals(109, p.getId());
    }
}
