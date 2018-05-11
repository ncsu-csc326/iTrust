package edu.ncsu.csc.itrust.unit.dao.standards;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.NDCodesDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class NDCodeTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private NDCodesDAO ndDAO = factory.getNDCodesDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.ndCodes();
	}

	// order by code asc but field isn't numerical, so codes will NOT be
	// in NUMERICAL ascending order...
	// (unless codes are switched to fixed width)
	public void testGetAllNDCodes() throws Exception {
		List<MedicationBean> codes = ndDAO.getAllNDCodes();
		assertEquals(5, codes.size());
		assertEquals("00060-431", codes.get(0).getNDCode());
		assertEquals("Tetracycline", codes.get(1).getDescription());
	}

	public void testGetNDCode() throws DBException {
		MedicationBean proc = ndDAO.getNDCode("08109-6");
		assertEquals("08109-6", proc.getNDCode());
		assertEquals("Aspirin", proc.getDescription());
	}

	public void testGetAllFromEmptyTable() throws SQLException, DBException {
		clearNDCodes();
		assertEquals(0, ndDAO.getAllNDCodes().size());
	}

	public void testGetNDCodeFromEmptyTable() throws SQLException, DBException {
		clearNDCodes();
		assertEquals(null, ndDAO.getNDCode("00904-2407"));
	}

	public void testAddNDCode() throws DBException, ITrustException {
		final String code = "999999999";
		final String desc = "testAddNDCode description";
		genericAdd(code, desc);
		List<MedicationBean> allCodes = ndDAO.getAllNDCodes();
		assertEquals(code, allCodes.get(allCodes.size() - 1).getNDCode());
		assertEquals(desc, allCodes.get(allCodes.size() - 1).getDescription());
	}

	public void testAddDupe() throws SQLException, DBException, ITrustException {
		clearNDCodes();
		final String code = "000000000";
		final String descrip0 = "testAddDupe description";
		MedicationBean proc = genericAdd(code, descrip0);
		try {
			proc.setDescription("");
			ndDAO.addNDCode(proc);
			fail("NDCodeTest.testAddDupe failed to catch dupe");
		} catch (ITrustException e) {
			assertEquals("Error: Code already exists.", e.getMessage());
			proc = ndDAO.getNDCode(code);
			assertEquals(descrip0, proc.getDescription());
		}
	}

	private MedicationBean genericAdd(String code, String desc) throws DBException, ITrustException {
		MedicationBean proc = new MedicationBean(code, desc);
		assertTrue(ndDAO.addNDCode(proc));
		assertEquals(desc, ndDAO.getNDCode(code).getDescription());
		return proc;
	}

	public void testUpdateDescription() throws DBException, ITrustException {
		final String code = "777777777";
		final String desc = "testUpdateDescription NEW description";
		MedicationBean proc = genericAdd(code, "");
		proc.setDescription(desc);
		assertEquals(1, ndDAO.updateCode(proc));
		proc = ndDAO.getNDCode(code);
		assertEquals(desc, proc.getDescription());
	}

	public void testUpdateNonExistent() throws SQLException, DBException {
		clearNDCodes();
		final String code = "0000F";
		MedicationBean proc = new MedicationBean(code, "");
		assertEquals(0, ndDAO.updateCode(proc));
		assertEquals(0, ndDAO.getAllNDCodes().size());
	}

	private void clearNDCodes() throws SQLException {
		new DBBuilder().executeSQL(Arrays.asList("DELETE FROM ndcodes;"));
	}
}
