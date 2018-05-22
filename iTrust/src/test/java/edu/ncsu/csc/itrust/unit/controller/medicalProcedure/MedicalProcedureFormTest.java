package edu.ncsu.csc.itrust.unit.controller.medicalProcedure;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.cptcode.CPTCodeController;
import edu.ncsu.csc.itrust.controller.medicalProcedure.MedicalProcedureController;
import edu.ncsu.csc.itrust.controller.medicalProcedure.MedicalProcedureForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedure;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;

public class MedicalProcedureFormTest extends TestCase {
    private TestDataGenerator gen;
    private DataSource ds;
    private OfficeVisitMySQL ovSql;
    private SessionUtils utils;
    
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        ovSql = new OfficeVisitMySQL(ds);
        utils = spy(SessionUtils.getInstance());
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc11();
    }
    
    public void testForm() throws DBException, SQLException{
        // set the office visit id in the session
        long ovID = ovSql.getAll().get(0).getVisitID();
        when(utils.getSessionVariable("officeVisitId")).thenReturn((Long) ovID);
        
        // make the form
        MedicalProcedureForm form = new MedicalProcedureForm();
        form = new MedicalProcedureForm(null, null, utils, null);
        form = new MedicalProcedureForm(null, null, utils, ds);
        
        // fill input
        form.fillInput("0", new CPTCode("90717", "Typhoid Vaccine"));
        Assert.assertEquals(0, form.getMedicalProcedure().getId());
        Assert.assertEquals("90717", form.getMedicalProcedure().getCode());
        Assert.assertEquals("Typhoid Vaccine", form.getMedicalProcedure().getName());
        Assert.assertEquals(ovID, form.getMedicalProcedure().getOfficeVisitId());
        
        form.add();
        List<MedicalProcedure> iList = form.getMedicalProceduresByOfficeVisit(Long.toString(ovID));
        Assert.assertEquals(1, iList.size());
        Assert.assertEquals("90717", iList.get(0).getCode());
        form.setMedicalProcedure(iList.get(0));
        form.getMedicalProcedure().setCptCode(new CPTCode("99214", ""));
        
        form.edit();
        iList = form.getMedicalProceduresByOfficeVisit(Long.toString(ovID));
        Assert.assertEquals(1, iList.size());
        Assert.assertEquals("99214", iList.get(0).getCode());
        
        Assert.assertEquals("Typhoid Vaccine", form.getCodeName("90717"));
        
        List<CPTCode> cptList = form.getCPTCodes();
        Assert.assertEquals(2, cptList.size());
        
        form.setMedicalProcedure(iList.get(0));
        form.remove(Long.toString(form.getMedicalProcedure().getId()));
        Assert.assertEquals(0, form.getMedicalProceduresByOfficeVisit(Long.toString(ovID)).size());
        
        MedicalProcedureController mockMedicalProcedureController = spy(new MedicalProcedureController(ds));
        CPTCodeMySQL mockCPTCodeSQL = spy(new CPTCodeMySQL(ds));
        form = new MedicalProcedureForm(mockMedicalProcedureController, mockCPTCodeSQL, utils, ds);
        when(mockCPTCodeSQL.getAll()).thenThrow(new SQLException());
        
        Assert.assertEquals(0, form.getCPTCodes().size());
    }
}
