package edu.ncsu.csc.itrust.unit.model.prescription;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.model.prescription.PrescriptionMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class PrescriptionMySQLTest extends TestCase {
    PrescriptionMySQL sql;
    @Spy
    PrescriptionMySQL mockSql;
    private DataSource ds;
    
    @Override
    public void setUp() throws DBException, FileNotFoundException, SQLException, IOException{
        ds = ConverterDAO.getDataSource();
        sql = new PrescriptionMySQL(ds);
        mockSql = Mockito.spy(new PrescriptionMySQL(ds));
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.ndCodes();
        gen.ndCodes1();
        gen.ndCodes100();
        gen.ndCodes2();
        gen.ndCodes3();
        gen.ndCodes4();
        gen.uc21();
    }
    
    @Test
    public void testGetPrescriptionsForPatientEndingAfter() throws SQLException{
        List<Prescription> pList = sql.getPrescriptionsForPatientEndingAfter(201, LocalDate.now().minusDays(91));
        Assert.assertEquals(2, pList.size());
        Assert.assertEquals("63739-291", pList.get(0).getCode());
        Assert.assertEquals("48301-3420", pList.get(1).getCode());
    }

    
    @Test
    public void testNoDataSource(){
        try {
            new PrescriptionMySQL();
            Assert.fail();
        } catch (DBException e){
            // yay, we passed
        }
    }

	class TestPrescriptionMySQL extends PrescriptionMySQL {
		public TestPrescriptionMySQL() throws DBException {
			super();
		}

		@Override
		public DataSource getDataSource() {
			return ds;
		}
	}
	
	@Test
	public void testMockDataSource() throws Exception {
		TestPrescriptionMySQL mysql = new TestPrescriptionMySQL();
		Assert.assertNotNull(mysql);
	}
	
	@Test
	public void testMockGetPrescriptionsForPatientEndingAfter() throws Exception {
		Mockito.doThrow(SQLException.class).when(mockSql).loadRecords(Mockito.any());
		try {
			mockSql.getPrescriptionsForPatientEndingAfter(1L, LocalDate.now());
			fail();
		} catch (SQLException e) {
			// Do nothing
		}
	}
	
	@Test
	public void testGetPrescriptionsForOfficeVisit() throws DBException, SQLException{
	    OfficeVisitMySQL ovSQL = new OfficeVisitMySQL(ds);
	    List<OfficeVisit> ovList = ovSQL.getVisitsForPatient(201L);
	    Assert.assertEquals(3, ovList.size());
	    
	    long ovId = ovList.get(0).getVisitID();
	    Assert.assertEquals(0, sql.getPrescriptionsForOfficeVisit(ovId).size());
        
        ovId = ovList.get(1).getVisitID();
        Assert.assertEquals(2, sql.getPrescriptionsForOfficeVisit(ovId).size());
        
        ovId = ovList.get(2).getVisitID();
        Assert.assertEquals(1, sql.getPrescriptionsForOfficeVisit(ovId).size());
        
        ovId = -1;
        Assert.assertEquals(0, sql.getPrescriptionsForOfficeVisit(ovId).size());
	}
	
	@Test
	public void testGetPrescriptionsForMID() throws Exception {
		{
			List<Prescription> plist = sql.getPrescriptionsByMID(201L);
			Assert.assertEquals(3, plist.size());
		}
		{
			List<Prescription> plist = sql.getPrescriptionsByMID(202L);
			Assert.assertEquals(1, plist.size());
		}
		{
			List<Prescription> plist = sql.getPrescriptionsByMID(203L);
			Assert.assertEquals(0, plist.size());
		}
	}
	
	@Test
	public void testGetCodeName() throws Exception {
		String codeName = "Midichlomaxene";
		String codeID = "48301-3420";
		assertEquals( codeName, sql.getCodeName(codeID) );
	}
	
	@Test
	public void testGetPrescriptionByID() throws Exception {
		List<Prescription> plist = sql.getPrescriptionsByMID(202L);
		Prescription expected = plist.get(0);
		Assert.assertNotNull(expected);
		long actualId = expected.getId();
		Prescription actual = sql.get(actualId);
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected.getDrugCode().getNDCode(), actual.getDrugCode().getNDCode());
	}
	
	@Test
	public void testAddUpdateAndDelete() throws DBException, SQLException{
	    OfficeVisitMySQL ovSQL = new OfficeVisitMySQL(ds);
        List<OfficeVisit> ovList = ovSQL.getVisitsForPatient(201L);
        Assert.assertEquals(3, ovList.size());
        
        long ovId = ovList.get(1).getVisitID();
        List<Prescription> pList = sql.getPrescriptionsForOfficeVisit(ovId);
        Assert.assertEquals(2, pList.size());
        
        Prescription toRemove = pList.get(0);
        Assert.assertTrue(sql.remove(toRemove.getId()));
        
        pList = sql.getPrescriptionsForOfficeVisit(ovId);
        Assert.assertEquals(1, pList.size());
        
        Assert.assertTrue(sql.add(toRemove));
        
        pList = sql.getPrescriptionsForOfficeVisit(ovId);
        Assert.assertEquals(2, pList.size());
        Assert.assertTrue(toRemove.getCode().equals(pList.get(0).getCode()) || toRemove.getCode().equals(pList.get(1).getCode()));
        Assert.assertTrue(toRemove.getEndDate().equals(pList.get(0).getEndDate()) || toRemove.getEndDate().equals(pList.get(1).getEndDate()));
        Assert.assertTrue(toRemove.getStartDate().equals(pList.get(0).getStartDate()) || toRemove.getStartDate().equals(pList.get(1).getStartDate()));
        Assert.assertTrue(toRemove.getOfficeVisitId() == pList.get(0).getOfficeVisitId() || toRemove.getOfficeVisitId() == pList.get(1).getOfficeVisitId());
        Assert.assertTrue(toRemove.getPatientMID() == pList.get(0).getPatientMID() || toRemove.getPatientMID() == pList.get(1).getPatientMID());
        
        toRemove = pList.get(0);
        LocalDate oldDate = toRemove.getStartDate();
        toRemove.setStartDate(oldDate.minusDays(20));
        
        Assert.assertTrue(sql.update(toRemove));
        pList = sql.getPrescriptionsForOfficeVisit(ovId);
        Assert.assertEquals(2, pList.size());
        Assert.assertTrue(toRemove.getCode().equals(pList.get(0).getCode()) || toRemove.getCode().equals(pList.get(1).getCode()));
        Assert.assertTrue(toRemove.getEndDate().equals(pList.get(0).getEndDate()) || toRemove.getEndDate().equals(pList.get(1).getEndDate()));
        Assert.assertTrue(toRemove.getStartDate().equals(pList.get(0).getStartDate()) || toRemove.getStartDate().equals(pList.get(1).getStartDate()));
        Assert.assertTrue(toRemove.getOfficeVisitId() == pList.get(0).getOfficeVisitId() || toRemove.getOfficeVisitId() == pList.get(1).getOfficeVisitId());
        Assert.assertTrue(toRemove.getPatientMID() == pList.get(0).getPatientMID() || toRemove.getPatientMID() == pList.get(1).getPatientMID());
        

        toRemove.setStartDate(oldDate);
        
        Assert.assertTrue(sql.update(toRemove));
        pList = sql.getPrescriptionsForOfficeVisit(ovId);
        Assert.assertEquals(2, pList.size());
        Assert.assertTrue(toRemove.getCode().equals(pList.get(0).getCode()) || toRemove.getCode().equals(pList.get(1).getCode()));
        Assert.assertTrue(toRemove.getEndDate().equals(pList.get(0).getEndDate()) || toRemove.getEndDate().equals(pList.get(1).getEndDate()));
        Assert.assertTrue(toRemove.getStartDate().equals(pList.get(0).getStartDate()) || toRemove.getStartDate().equals(pList.get(1).getStartDate()));
        Assert.assertTrue(toRemove.getOfficeVisitId() == pList.get(0).getOfficeVisitId() || toRemove.getOfficeVisitId() == pList.get(1).getOfficeVisitId());
        Assert.assertTrue(toRemove.getPatientMID() == pList.get(0).getPatientMID() || toRemove.getPatientMID() == pList.get(1).getPatientMID());
	}
}
