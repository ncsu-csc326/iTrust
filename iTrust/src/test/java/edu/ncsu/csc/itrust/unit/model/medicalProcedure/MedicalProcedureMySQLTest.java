package edu.ncsu.csc.itrust.unit.model.medicalProcedure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedure;
import edu.ncsu.csc.itrust.model.medicalProcedure.MedicalProcedureMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class MedicalProcedureMySQLTest extends TestCase {
    private DataSource ds;
    private TestDataGenerator gen;
    private MedicalProcedureMySQL sql;
    private OfficeVisitMySQL ovSql;
    
    @Override
    public void setUp() throws FileNotFoundException, SQLException, IOException {
        ds = ConverterDAO.getDataSource();
        sql = new MedicalProcedureMySQL(ds);
        ovSql = new OfficeVisitMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc11();
    }
    
    @Test
    public void testValids() throws SQLException, DBException {
        long ovID = ovSql.getAll().get(0).getVisitID();
        MedicalProcedure p = new MedicalProcedure();
        p.setOfficeVisitId(ovID);
        p.setCptCode(new CPTCode("90717", "Typhoid Vaccine"));
        Assert.assertTrue(sql.add(p));
        List<MedicalProcedure> pList = sql.getMedicalProceduresForOfficeVisit(ovID);
        Assert.assertEquals(1, pList.size());
        Assert.assertEquals("90717", pList.get(0).getCode());
        
        long mpID = pList.get(0).getId();
        Assert.assertEquals("90717", sql.get(mpID).getCode());
        
        Assert.assertEquals("Typhoid Vaccine", sql.getCodeName("90717"));
        
        p.setCptCode(new CPTCode("99214", ""));
        p.setId(mpID);
        
        Assert.assertTrue(sql.update(p));
        pList = sql.getMedicalProceduresForOfficeVisit(ovID);
        Assert.assertEquals(1, pList.size());
        Assert.assertEquals("99214", pList.get(0).getCode());
        
        Assert.assertTrue(sql.remove(mpID));
        pList = sql.getMedicalProceduresForOfficeVisit(ovID);
        Assert.assertEquals(0, pList.size());
    }
}
