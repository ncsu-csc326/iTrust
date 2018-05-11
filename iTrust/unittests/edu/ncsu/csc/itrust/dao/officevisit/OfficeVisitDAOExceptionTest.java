package edu.ncsu.csc.itrust.dao.officevisit;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;

/**
 * Test office visit with exception DAO
 * @throw exception
 */
public class OfficeVisitDAOExceptionTest extends TestCase {
	private OfficeVisitDAO evilDAO = EvilDAOFactory.getEvilInstance().getOfficeVisitDAO();
	//private DiagnosesDAO evilDAO = EvilDAOFactory.getEvilInstance().getDiagnosesDAO();

	@Override
	protected void setUp() throws Exception {
	}

	public void testAddException() throws Exception {
		try {
			evilDAO.add(new OfficeVisitBean());
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	/*public void testAddDiagToOVException() throws Exception {
		try {
			evilDAO.addDiagnosisToOfficeVisit(0.0, 0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testAddPresException() throws Exception {
		try {
			evilDAO.addPrescription(new PrescriptionBean());
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testAddProcToOVException() throws Exception {
		try {
			evilDAO.addProcedureToOfficeVisit("", 0L, "");
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	public void testCheckOVExistsException() throws Exception {
		try {
			evilDAO.checkOfficeVisitExists(0L, 0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testGetAllOVsException() throws Exception {
		try {
			evilDAO.getAllOfficeVisits(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	/*public void testGetDiagnosesException() throws Exception {
		try {
			evilDAO.getDiagnoses(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	public void testGetOVException() throws Exception {
		try {
			evilDAO.getOfficeVisit(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	/*public void testGetPrescriptionReportsException() throws Exception {
		try {
			evilDAO.getPrescriptionReports(new ArrayList<Long>(), 0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testGetPrescriptionsException() throws Exception {
		try {
			evilDAO.getPrescriptions(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testGetProceduresException() throws Exception {
		try {
			evilDAO.getProcedures(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testRemoveDiagnosisFromOVException() throws Exception {
		try {
			evilDAO.removeDiagnosisFromOfficeVisit(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testRemovePrescriptionException() throws Exception {
		try {
			evilDAO.removePrescription(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	/*public void testRemoveProcedureFromOVException() throws Exception {
		try {
			evilDAO.removeProcedureFromOfficeVisit(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}*/

	public void testUpdateOVException() throws Exception {
		try {
			evilDAO.update(new OfficeVisitBean());
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
}
