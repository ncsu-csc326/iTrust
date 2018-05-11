package edu.ncsu.csc.itrust.dao.visitreminders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.DateUtil;
import edu.ncsu.csc.itrust.beans.VisitFlag;
import edu.ncsu.csc.itrust.beans.forms.VisitReminderReturnForm;
import edu.ncsu.csc.itrust.beans.loaders.VisitReminderReturnFormLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.VisitRemindersDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * Currently only tests missed-last-year's-flu-shot half of flu shot methods (only includes static data)
 * 
 * Testing for month decision is in DateUtil tests
 */
public class GetVisitRemindersTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private VisitRemindersDAO visRemDAO = factory.getVisitRemindersDAO();
	private TestDataGenerator gen = new TestDataGenerator();
	private long LHCP = 9000000003l;

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();

	}

	private int getCurrentMonth() {
		Calendar cal = new GregorianCalendar();
		return cal.get(Calendar.MONTH);
	}

	public void testGetFluShotDelinquents() throws DBException, IOException, FileNotFoundException,
			SQLException {
		gen.patient1();
		gen.patient2();
		gen.patient3();
		List<VisitReminderReturnForm> visRems = visRemDAO.getFluShotDelinquents(LHCP);

		assertEquals(2, visRems.size());
		VisitReminderReturnForm reminder = visRems.get(0);
		assertEquals(reminder.getLastName(), "Needs");
		assertEquals(reminder.getPatientID(), 3);
		/*
		 * We need to account for "today" changing. Note that tests should NOT normally have if-statements in
		 * them.
		 */
		if (getCurrentMonth() >= 8 && getCurrentMonth() <= 11) // if we are in [Sep,Dec]
			assertEquals(VisitFlag.MISSING_MEDICATION, reminder.getVisitFlags()[0].getType());
		else
			// otherwise we are in [Jan,Aug]
			assertEquals(VisitFlag.MISSED_MEDICATION, reminder.getVisitFlags()[0].getType());
		assertEquals(reminder.getVisitFlags()[0].getValue(), "Flu Shot");
		reminder = visRems.get(1);
		assertEquals(reminder.getLastName(), "Person");
		assertEquals(reminder.getPatientID(), 1);
		if (getCurrentMonth() >= 8 && getCurrentMonth() <= 11) // if we are in [Sep,Dec]
			assertEquals(reminder.getVisitFlags()[0].getType(), VisitFlag.MISSING_MEDICATION);
		else
			// otherwise we are in [Jan,Aug]
			assertEquals(reminder.getVisitFlags()[0].getType(), VisitFlag.MISSED_MEDICATION);
		assertEquals(reminder.getVisitFlags()[0].getValue(), "Flu Shot");
	}

	public void testGetDiagnosedVisitNeeders() throws DBException, IOException, FileNotFoundException,
			SQLException {
		gen.patient1();
		gen.patient2();
		gen.patient3();
		List<VisitReminderReturnForm> visRems = visRemDAO.getDiagnosedVisitNeeders(LHCP);
		// assertEquals(2, visRems.size());
		VisitReminderReturnForm reminder = visRems.get(0);
		/**assertEquals(reminder.getLastName(), "Needs");
		assertEquals(reminder.getPatientID(), 3);
		assertEquals(reminder.getVisitFlags()[0].getType(), VisitFlag.DIAGNOSED);
		assertEquals(reminder.getVisitFlags()[0].getValue(), "459.99");
		reminder = visRems.get(1);**/
		assertEquals(reminder.getLastName(), "Person");
		assertEquals(reminder.getPatientID(), 1);
		assertEquals(reminder.getVisitFlags()[0].getType(), VisitFlag.DIAGNOSED);
		assertEquals(reminder.getVisitFlags()[0].getValue(), "250.00");
	}

	public void testGetFluShotDelinquentsEmptyList() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		gen.patient1();
		gen.patient2();
		gen.patient3();
		boolean thisYear = DateUtil.currentlyInMonthRange(9, 12);

		java.sql.Date september = new java.sql.Date(0l), december = new java.sql.Date(0l);
		DateUtil.setSQLMonthRange(september, 8, thisYear ? 0 : 1, december, 11, thisYear ? 0 : 1);
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT DISTINCT "
					+ "? as hid, ov.patientid, p.lastname, p.firstname, " + "p.phone "
					+ "FROM officevisits ov, patients p " + "WHERE ov.patientid=p.mid "
					+ "AND p.dateofdeath IS NULL "
					+ "AND p.dateofbirth < DATE_SUB(CURDATE(), INTERVAL 50 YEAR) " + "AND patientid NOT IN "
					+ "(SELECT patientid FROM officevisits ov, ovmedication om " + "WHERE ov.id=om.visitid "
					+ "AND NDCode IN (90656, 90658, 90660) " + "AND ((StartDate BETWEEN ? AND ?) "
					+ "OR (EndDate BETWEEN ? AND ?))) " + "ORDER BY lastname, firstname, ov.patientid");
			ps.setLong(1, this.LHCP);
			ps.setDate(2, september);
			ps.setDate(3, december);
			ps.setDate(4, september);
			ps.setDate(5, december);
			rs = ps.executeQuery();
			VisitReminderReturnFormLoader loader = new VisitReminderReturnFormLoader();
			List<VisitReminderReturnForm> patients = loader.loadList(rs);
			assertEquals(2, patients.size());
			VisitReminderReturnForm patient1 = patients.get(0);
			assertEquals(9000000003l, patient1.getHcpID());
			assertEquals(3l, patient1.getPatientID());
			assertEquals("Care", patient1.getFirstName());
			assertEquals("919-971-0000", patient1.getPhoneNumber());
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	public void testGetFluShotDelinquentsEmpty() throws DBException, IOException, FileNotFoundException,
			SQLException {
		List<VisitReminderReturnForm> visRems = visRemDAO.getFluShotDelinquents(LHCP);
		assertEquals(0, visRems.size());
	}

	public void testGetDiagnosedVisitNeedersEmpty() throws DBException, IOException, FileNotFoundException,
			SQLException {
		List<VisitReminderReturnForm> visRems = visRemDAO.getDiagnosedVisitNeeders(LHCP);
		assertEquals(0, visRems.size());
	}
}
