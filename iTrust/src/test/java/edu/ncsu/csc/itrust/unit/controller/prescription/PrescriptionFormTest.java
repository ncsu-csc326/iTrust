package edu.ncsu.csc.itrust.unit.controller.prescription;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.controller.prescription.PrescriptionController;
import edu.ncsu.csc.itrust.controller.prescription.PrescriptionForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;

public class PrescriptionFormTest extends TestCase {
    private TestDataGenerator gen;
    private DataSource ds;
    private PrescriptionForm form;
    private SessionUtils utils;
    private PrescriptionController pc;
    private NDCCodeMySQL nData;
    private OfficeVisitMySQL ovSql;
    
    
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException, DBException{
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        ovSql = new OfficeVisitMySQL(ds);
        gen.clearAllTables();
        gen.uc11();
        utils = spy(SessionUtils.getInstance());
        pc = spy(new PrescriptionController(ds));
        nData = spy(new NDCCodeMySQL(ds));
    }
    
    @Test
    public void testEverything() throws DBException, SQLException{
        OfficeVisit visit = ovSql.getAll().get(0);
        long ovID = visit.getVisitID();
        long patientMID = visit.getPatientMID();
        long loggedInMID = 9000000000L;
        when(utils.getCurrentOfficeVisitId()).thenReturn((Long) ovID);
        when(utils.getCurrentPatientMID()).thenReturn(Long.toString(patientMID));
        when(utils.getSessionLoggedInMIDLong()).thenReturn((Long) loggedInMID);
        form = new PrescriptionForm();
        form = new PrescriptionForm(pc, nData, utils, ds);
        
        form.fillInput("0", new MedicationBean("1234", "good meds"), 80, LocalDate.parse("2016-11-17"), LocalDate.parse("2017-11-17"), "take a bunch");
        form.add();
        List<Prescription> pList = form.getPrescriptionsByOfficeVisit(Long.toString(ovID));
        Assert.assertEquals(1, pList.size());
        Assert.assertEquals("1234", pList.get(0).getCode());
        
        long prescriptionID = pList.get(0).getId();
        
        form.fillInput(Long.toString(prescriptionID), new MedicationBean("1234", "good meds"), 80, LocalDate.parse("2016-11-17"), LocalDate.parse("2017-11-17"), "take a bunch MORE");
        form.edit();
        pList = form.getPrescriptionsByOfficeVisit(Long.toString(ovID));
        Assert.assertEquals(1, pList.size());
        Assert.assertEquals("take a bunch MORE", pList.get(0).getInstructions());
        
        Assert.assertEquals("good meds", form.getCodeName("1234"));
        Assert.assertEquals(1, form.getNDCCodes().size());
        Assert.assertEquals("1234", form.getNDCCodes().get(0).getCode());

        Assert.assertEquals(1, form.getPrescriptionsByPatientID(Long.toString(patientMID)).size());
        Assert.assertEquals(0, form.getPrescriptionsForCurrentPatient().size());
        
        form.remove(Long.toString(prescriptionID));
        Assert.assertEquals(0, form.getPrescriptionsByOfficeVisit(Long.toString(ovID)).size());
        
        Prescription p = new Prescription();
        form.setPrescription(p);
        Assert.assertEquals(p, form.getPrescription());
        
        when(pc.getPrescriptionsByOfficeVisit("1")).thenThrow(new DBException(new SQLException()));
        Assert.assertEquals(0, form.getPrescriptionsByOfficeVisit("1").size());
        
        when(nData.getAll()).thenThrow(new SQLException());
        Assert.assertEquals(0, form.getNDCCodes().size());
    }
}
