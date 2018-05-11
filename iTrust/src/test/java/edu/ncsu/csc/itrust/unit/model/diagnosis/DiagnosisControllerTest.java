package edu.ncsu.csc.itrust.unit.model.diagnosis;

import javax.sql.DataSource;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.controller.diagnosis.DiagnosisController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;

public class DiagnosisControllerTest extends TestCase {
    DataSource ds;
    DiagnosisController controller;
    TestDataGenerator gen;
    DiagnosisMySQL sql;
    OfficeVisitMySQL ovSql;
    
    @Mock
    SessionUtils mockSessionUtils;

    @Override
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc11();
        
        mockSessionUtils = Mockito.mock(SessionUtils.class);
        Mockito.doReturn(1L).when(mockSessionUtils).getCurrentOfficeVisitId();
        sql = spy(new DiagnosisMySQL(ds));
        controller = new DiagnosisController(ds);
        controller.setSql(sql);
        ovSql = new OfficeVisitMySQL(ds);
    }
    
    public void testController() throws DBException{
        Diagnosis d = new Diagnosis();
        d.setIcdCode(new ICDCode("S108", "Skin injury neck", false));
        long ovID = ovSql.getAll().get(0).getVisitID();
        d.setVisitId(ovID);
        controller.add(d);
        List<Diagnosis> dList = controller.getDiagnosesByOfficeVisit(ovID);
        Assert.assertEquals(1, dList.size());
        d = dList.get(0);
        
        d.setIcdCode(new ICDCode("S107", "Skin injury hand", false));
        controller.edit(d);
        dList = controller.getDiagnosesByOfficeVisit(ovID);
        Assert.assertEquals(1, dList.size());
        
        controller.remove(d.getId());
        dList = controller.getDiagnosesByOfficeVisit(ovID);
        Assert.assertEquals(0, dList.size());
        
        d.setIcdCode(new ICDCode());
        controller.add(d);
        controller.edit(d);
        controller.remove(0);

        d.setIcdCode(new ICDCode("S107", "Skin injury hand", false));
        when(sql.add(d)).thenReturn(false);
        controller.add(d);
        
        when(sql.update(d)).thenReturn(false);
        controller.edit(d);
        
        when(sql.remove(0)).thenThrow(new DBException(new SQLException()));
        controller.remove(0);
        
        when(sql.getAllDiagnosisByOfficeVisit(0)).thenThrow(new DBException(new SQLException()));
        controller.getDiagnosesByOfficeVisit(0);
        
        when(sql.getAllDiagnosisByOfficeVisit(1)).thenThrow(new NullPointerException());
        controller.getDiagnosesByOfficeVisit(1);
    }
}
