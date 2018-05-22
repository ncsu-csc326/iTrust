package edu.ncsu.csc.itrust.unit.model.emergencyRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecord;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import junit.framework.TestCase;

public class EmergencyRecordTest extends TestCase {
    
    EmergencyRecord r;
    
    @Override
    public void setUp(){
        r = new EmergencyRecord();
    }
    
    @Test
    public void testName(){
        r.setName("testName");
        Assert.assertEquals("testName", r.getName());
    }
    
    @Test
    public void testAge(){
        r.setAge(50);
        Assert.assertEquals(50,  r.getAge());
    }
    
    @Test
    public void testGender(){
        r.setGender("Female");
        Assert.assertEquals("Female", r.getGender());
    }
    
    @Test
    public void testContactName(){
        r.setContactName("cName");
        Assert.assertEquals("cName", r.getContactName());
    }
    
    @Test
    public void testContactPhone(){
        r.setContactPhone("999-555-1111");
        Assert.assertEquals("999-555-1111", r.getContactPhone());
    }
    
    @Test
    public void testAllergies(){
    	List<AllergyBean> allergies = new ArrayList<AllergyBean>();
    	
    	AllergyBean a0 = new AllergyBean();
    	a0.setId(0L);
    	a0.setFirstFound(new Date(0L));
    	a0.setDescription("Test description");
    	a0.setNDCode("Test NDC");
    	a0.setPatientID(101L);
    	allergies.add(a0);
    	
        r.setAllergies(allergies);
        
        Assert.assertEquals(1, r.getAllergies().size());
        
        AllergyBean a1 = r.getAllergies().get(0);
        Assert.assertEquals(0L, a1.getId());
        Assert.assertEquals(new Date(0L), a1.getFirstFound());
        Assert.assertEquals("Test description", a1.getDescription());
        Assert.assertEquals("Test NDC", a1.getNDCode());
        Assert.assertEquals(101L, a1.getPatientID());
    }
    
    @Test
    public void testBloodType(){
        r.setBloodType("A-");
        Assert.assertEquals("A-", r.getBloodType());
    }
    
    @Test
    public void testDiagnoses(){
    	List<Diagnosis> diagnoses = new ArrayList<Diagnosis>();
    	diagnoses.add(new Diagnosis(0L, 1L, new ICDCode("TESTICD", "Test name", true)));
        r.setDiagnoses(diagnoses);
        Assert.assertEquals(1, r.getDiagnoses().size());
        Diagnosis d = r.getDiagnoses().get(0);
        Assert.assertEquals(0, d.getId());
        Assert.assertEquals(1, d.getVisitId());
        Assert.assertEquals("TESTICD", d.getIcdCode().getCode());
        Assert.assertEquals("Test name", d.getIcdCode().getName());
        Assert.assertTrue(d.getIcdCode().isChronic());
    }
    
    //TODO: fix this when prescription functionality is added
    @Test
    public void testPrescriptions(){
        r.setPrescriptions(null);
        Assert.assertNull(r.getPrescriptions());
    }
    
    //TODO: fix this when immunization functionality is added
    @Test
    public void testImmunizations(){
        r.setImmunizations(null);
        Assert.assertNull(r.getImmunizations());
    }
}
