package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.ReportRequestBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ViewMyReportRequestsActionTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private DAOFactory evilFactory = EvilDAOFactory.getEvilInstance();
	private ViewMyReportRequestsAction action;
	//private FakeEmailDAO feDAO = factory.getFakeEmailDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.patient2();
		gen.hcp0();
		gen.admin1();
		gen.fakeEmail();
		gen.reportRequests();
	}


	public void testGetReportRequests3() throws Exception {
		action = new ViewMyReportRequestsAction(factory, 9000000000L);
		List<ReportRequestBean> list = action.getAllReportRequestsForRequester();
		assertEquals(6, list.size());
		assertEquals(ReportRequestBean.Requested, list.get(0).getStatus());
	}

	public void testGetEvilReportRequest() throws Exception {
		action = new ViewMyReportRequestsAction(evilFactory, 9000000000L);
		try {
			action.getReportRequest(1);
			fail("exception should have been thrown");
		} catch (ITrustException ex) {
			DBException dbe = (DBException) ex;
			assertEquals(EvilDAOFactory.MESSAGE, dbe.getSQLException().getMessage());
		}
	}

	public void testGetReportRequestForID3() throws Exception {
		action = new ViewMyReportRequestsAction(factory, 9000000000L);
		ReportRequestBean b = action.getReportRequest(3);
		assertEquals(3, b.getID());
		assertEquals(9000000000L, b.getRequesterMID());
		assertEquals(2, b.getPatientMID());
		assertEquals("01/03/2008 12:00", b.getRequestedDateString());
	}

	public void testGetReportRequestForID4() throws Exception {
		action = new ViewMyReportRequestsAction(factory, 9000000000L);
		ReportRequestBean b = action.getReportRequest(4);
		assertEquals(4, b.getID());
		assertEquals(9000000000L, b.getRequesterMID());
		assertEquals(2, b.getPatientMID());
		assertEquals("01/04/2008 12:00", b.getRequestedDateString());
		assertEquals("03/04/2008 12:00", b.getViewedDateString());
		assertEquals(ReportRequestBean.Viewed, b.getStatus());
	}

	public void testInsertReport1() throws Exception {
		action = new ViewMyReportRequestsAction(evilFactory, 9000000000L);
		try {
			action.addReportRequest(0);
			fail("Should have throw exception");
		} catch (ITrustException e) {
			DBException dbe = (DBException) e;
			assertEquals(EvilDAOFactory.MESSAGE, dbe.getSQLException().getMessage());
		}
	}

	public void testInsertReport2() throws Exception {
		action = new ViewMyReportRequestsAction(factory, 9000000000L);
		long id = action.addReportRequest(2);
		ReportRequestBean b2 = action.getReportRequest((int) id);
		assertEquals(9000000000L, b2.getRequesterMID());
		assertEquals(2, b2.getPatientMID());
		assertEquals(ReportRequestBean.Requested, b2.getStatus());
	}
	
	public void testSetViewedToZero() throws Exception {
		action = new ViewMyReportRequestsAction(factory, 9000000000L);
		try {
			action.setViewed(0);
			fail("Should have throw exception");
		} catch (ITrustException ex) {
			assertEquals(
					"A database exception has occurred. Please see the log in the console for stacktrace", ex
							.getMessage());
		}
	}

//	public void testSetViewed2() throws Exception {
//		action = new ViewMyReportRequestsAction(factory, 9000000000L);
//		long id = action.addReportRequest(2);
//		action = new ViewMyReportRequestsAction(factory, 9000000001L);
//		action.setViewed((int) id);
//		assertEquals(
//				"Dear Andy Programmer, \n The iTrust Health Care Provider (9000000000) has chosen to view your full medical report, which was approved by an iTrust administrator (9000000001).  This report was only viewable one time and is no longer available.",
//				list.get(0).getBody());
//		ReportRequestBean b2 = action.getReportRequest((int) id);
//		assertEquals(ReportRequestBean.Viewed, b2.getStatus());
//	}

	public void testGetLongStatus() throws Exception {
		ViewMyReportRequestsAction action = new ViewMyReportRequestsAction(factory, 2L);
		TestDataGenerator gen = new TestDataGenerator();
		gen.admin1();
		assertEquals("Request was requested on 01/01/2008 12:00 by Kelly Doctor", action.getLongStatus(1L));
		assertEquals("Request was requested on 01/04/2008 12:00 by Kelly Doctor, and viewed on 03/04/2008 12:00 by Kelly Doctor", action.getLongStatus(4L));
	}
}
