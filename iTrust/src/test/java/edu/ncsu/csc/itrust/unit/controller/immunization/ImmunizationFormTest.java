package edu.ncsu.csc.itrust.unit.controller.immunization;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;

import edu.ncsu.csc.itrust.controller.immunization.ImmunizationController;
import edu.ncsu.csc.itrust.controller.immunization.ImmunizationForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.cptcode.CPTCodeMySQL;
import edu.ncsu.csc.itrust.model.immunization.Immunization;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;

public class ImmunizationFormTest extends TestCase {
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
        ImmunizationForm form = new ImmunizationForm();
        form = new ImmunizationForm(null, null, utils, null);
        form = new ImmunizationForm(null, null, utils, ds);
        
        // fill input
        form.fillInput("0", new CPTCode("90717", "Typhoid Vaccine"));
        Assert.assertEquals(0, form.getImmunization().getId());
        Assert.assertEquals("90717", form.getImmunization().getCode());
        Assert.assertEquals("Typhoid Vaccine", form.getImmunization().getName());
        Assert.assertEquals(ovID, form.getImmunization().getVisitId());
        
        form.add();
        List<Immunization> iList = form.getImmunizationsByOfficeVisit(Long.toString(ovID));
        Assert.assertEquals(1, iList.size());
        Assert.assertEquals("90717", iList.get(0).getCode());
        form.setImmunization(iList.get(0));
        form.getImmunization().setCptCode(new CPTCode("99214", ""));
        
        form.edit();
        iList = form.getImmunizationsByOfficeVisit(Long.toString(ovID));
        Assert.assertEquals(1, iList.size());
        Assert.assertEquals("99214", iList.get(0).getCode());
        
        Assert.assertEquals("Typhoid Vaccine", form.getCodeName("90717"));
        
        List<CPTCode> cptList = form.getCPTCodes();
        Assert.assertEquals(2, cptList.size());
        
        form.setImmunization(iList.get(0));
        form.remove(Long.toString(form.getImmunization().getId()));
        Assert.assertEquals(0, form.getImmunizationsByOfficeVisit(Long.toString(ovID)).size());
        
        ImmunizationController mockImmunizationController = spy(new ImmunizationController(ds));
        CPTCodeMySQL mockCPTCodeSQL = spy(new CPTCodeMySQL(ds));
        form = new ImmunizationForm(mockImmunizationController, mockCPTCodeSQL, utils, ds);
        when(mockImmunizationController.getImmunizationsByOfficeVisit(Long.toString(ovID))).thenThrow(new DBException(new SQLException()));
        when(mockCPTCodeSQL.getAll()).thenThrow(new SQLException());
        
        Assert.assertEquals(0, form.getCPTCodes().size());
        Assert.assertEquals(0, form.getImmunizationsByOfficeVisit(Long.toString(ovID)).size());
    }
}
