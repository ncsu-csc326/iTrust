package edu.ncsu.csc.itrust.dao.labprocedure;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * 
 * 
 */
public class UpdateLabProcedureTest extends TestCase {
	private LabProcedureDAO lpDAO = TestDAOFactory.getTestInstance().getLabProcedureDAO();

	private TestDataGenerator gen;
	private LabProcedureBean l;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.labProcedures();
		l = new LabProcedureBean();
		l.setPid(1L);
		l.setOvID(905L);
		l.setLoinc("10763-1");
		l.statusPending();
		l.setCommentary("Awaiting results");
		l.setResults("");
	}

	public void testUpdateLabProcedure() throws Exception {
		long id = lpDAO.addLabProcedure(l);
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		assertEquals(1L, procedures.getPid());
		l.statusComplete();
		l.setResults("The results were good");
		l.setProcedureID(id);
		// the database cuts off milliseconds so we have to do that here
		long i = System.currentTimeMillis() / 1000 * 1000;
		lpDAO.updateLabProcedure(l);
		@SuppressWarnings("unused")
		java.sql.Timestamp t = new java.sql.Timestamp(i);
		LabProcedureBean updprocedures = lpDAO.getLabProcedure(id);
//		assertEquals(t.toString(), updprocedures.getTimestamp().toString());
		assertEquals(LabProcedureBean.Completed, updprocedures.getStatus());
		assertEquals("The results were good", updprocedures.getResults());
	}

	public void testFailLabProcedure() throws Exception {
		l.setPid(0);
		try {
			lpDAO.updateLabProcedure(l);
			fail();
		} catch (DBException e) {
			assertEquals("PatientMID cannot be null", e.getExtendedMessage());
		}
	}

}
