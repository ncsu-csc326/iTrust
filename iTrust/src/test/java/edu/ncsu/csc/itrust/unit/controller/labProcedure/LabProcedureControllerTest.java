package edu.ncsu.csc.itrust.unit.controller.labProcedure;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.ncsu.csc.itrust.controller.labProcedure.LabProcedureController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure.LabProcedureStatus;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureData;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Unit tests for LabProcedureController.
 * 
 * @author mwreesjo
 */
public class LabProcedureControllerTest {

	@Mock
	private SessionUtils mockSessionUtils;
	@Mock
	private LabProcedureData mockData;

	private DataSource ds;
	private LabProcedureController controller;
	private TestDataGenerator gen;
	private LabProcedure procedure;

	@Before
	public void setUp() throws FileNotFoundException, SQLException, IOException {
		ds = ConverterDAO.getDataSource();
		gen = new TestDataGenerator();
		gen.clearAllTables();
		controller = new LabProcedureController(ds);

		mockSessionUtils = Mockito.mock(SessionUtils.class);
		mockData = Mockito.mock(LabProcedureData.class);

		procedure = new LabProcedure();
		procedure.setCommentary("commentary");
		procedure.setIsRestricted(true);
		procedure.setLabProcedureID(8L);
		procedure.setLabTechnicianID(9L);
		procedure.setOfficeVisitID(10L);
		procedure.setPriority(3);
		procedure.setResults("results!");
		procedure.setStatus(1L);
		procedure.setUpdatedDate(new Timestamp(100L));
	}

	@After
	public void tearDown() throws FileNotFoundException, SQLException, IOException {
		gen.clearAllTables();
	}

	/**
	 * Tests happy path retrieval of one lab procedure.
	 */
	@Test
	public void testGetLabProceduresByLabTechnicianSingleResult()
			throws FileNotFoundException, SQLException, IOException {
		gen.labProcedure0();
		gen.labProcedure5();
		List<LabProcedure> procs = null;
		try {
			procs = controller.getLabProceduresByLabTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(1, procs.size());
		LabProcedure procedure = procs.get(0);
		Assert.assertEquals("This is a lo pri lab procedure", procedure.getCommentary());
		Assert.assertEquals("Foobar", procedure.getResults());
		Assert.assertTrue(procedure.getLabTechnicianID() == 5000000001L);
		Assert.assertTrue(procedure.getOfficeVisitID() == 4);
		Assert.assertTrue(procedure.getPriority() == 3);
		Assert.assertEquals("Pending", procedure.getStatus().getName());
	}

	/**
	 * Tests that getLabProceduresByLabTechnician() returns the correct lab
	 * procedures in the correct order
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testGetLabProceduresByLabTechnicianMultipleResults()
			throws FileNotFoundException, SQLException, IOException {
		// Order should be lab procedure 1, 4, 3, 2, 0 based on the sort order
		// defined in S7 in UC26
		// (priority lo->hi, then date old->new)
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getLabProceduresByLabTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(5, procs.size());
		Assert.assertTrue(procs.get(0).getPriority() == 1);
		Assert.assertTrue(procs.get(1).getPriority() == 2);
		Assert.assertTrue(procs.get(2).getPriority() == 3);
		Assert.assertEquals("In testing status", procs.get(2).getCommentary());
		Assert.assertEquals("In received status", procs.get(3).getCommentary());
		Assert.assertEquals("This is a lo pri lab procedure", procs.get(4).getCommentary());
	}

	/**
	 * Tests that getLabProceduresByOfficeVisit() returns the correct lab
	 * procedures in the correct order
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testGetLabProceduresByOfficeVisit() throws FileNotFoundException, SQLException, IOException {
		// Should return lab procedures 4, 3, 2 in that order
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getLabProceduresByOfficeVisit("3");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertTrue(procs.size() == 3);
		Assert.assertTrue(procs.get(0).getPriority() == 2);
		Assert.assertTrue(procs.get(1).getPriority() == 3);
		Assert.assertTrue(procs.get(2).getPriority() == 3);
		Assert.assertEquals("In completed status", procs.get(0).getCommentary());
		Assert.assertEquals("In testing status", procs.get(1).getCommentary());
		Assert.assertEquals("In received status", procs.get(2).getCommentary());
	}

	@Test
	public void testGetCompletedLabProceduresByOfficeVisit() throws FileNotFoundException, SQLException, IOException {
		// Should only return labProcedure4
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getCompletedLabProceduresByOfficeVisit("3");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(1, procs.size());
		Assert.assertEquals("In completed status", procs.get(0).getCommentary());
		Assert.assertEquals(new Long(5L), procs.get(0).getLabProcedureID());
	}

	@Test
	public void testGetNonCompletedLabProceduresByOfficeVisit()
			throws FileNotFoundException, SQLException, IOException {
		// Should return labProcedure{2, 3}
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getNonCompletedLabProceduresByOfficeVisit("3");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(2, procs.size());
		Assert.assertEquals("In testing status", procs.get(0).getCommentary());
		Assert.assertEquals(new Long(4L), procs.get(0).getLabProcedureID());

		Assert.assertEquals("In received status", procs.get(1).getCommentary());
		Assert.assertEquals(new Long(3L), procs.get(1).getLabProcedureID());
	}

	/**
	 * Tests that getPendingLabProceduresByTechnician() returns only and all
	 * pending lab procedures.
	 */
	@Test
	public void testGetPendingLabProceduresByTechnician() throws FileNotFoundException, SQLException, IOException {
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getPendingLabProceduresByTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertTrue(procs.size() == 1);
		LabProcedure pending = procs.get(0);
		Assert.assertEquals("This is a lo pri lab procedure", pending.getCommentary());
		Assert.assertEquals("Foobar", pending.getResults());
		Assert.assertTrue(pending.getLabTechnicianID() == 5000000001L);
		Assert.assertTrue(pending.getOfficeVisitID() == 4);
		Assert.assertTrue(pending.getPriority() == 3);
		Assert.assertEquals("Pending", pending.getStatus().getName());
	}

	/**
	 * Tests that getInTransitLabProceduresByTechnician() returns only and all
	 * in transit lab procedures.
	 */
	@Test
	public void testGetInTransitLabProceduresByTechnician() throws FileNotFoundException, SQLException, IOException {
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getInTransitLabProceduresByTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(1, procs.size());
		LabProcedure inTransit = procs.get(0);
		Assert.assertEquals("A hi pri lab procedure", inTransit.getCommentary());
		Assert.assertEquals("Results are important", inTransit.getResults());
		Assert.assertTrue(inTransit.getLabTechnicianID() == 5000000001L);
		Assert.assertTrue(inTransit.getOfficeVisitID() == 4);
		Assert.assertTrue(inTransit.getPriority() == 1);
		Assert.assertEquals("In transit", inTransit.getStatus().getName());
	}

	/**
	 * Tests that getReceivedLabProceduresByTechnician() returns only and all
	 * received lab procedures.
	 */
	@Test
	public void testGetReceivedLabProceduresByTechnician() throws FileNotFoundException, SQLException, IOException {
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getReceivedLabProceduresByTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(1, procs.size());
		LabProcedure received = procs.get(0);
		Assert.assertEquals("Received", received.getStatus().getName());
	}

	/**
	 * Tests that getTestingLabProceduresByTechnician() returns only and all
	 * testing lab procedures.
	 */
	@Test
	public void testGetTestingLabProceduresByTechnician() throws FileNotFoundException, SQLException, IOException {
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getTestingLabProceduresByTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(1, procs.size());
		LabProcedure testing = procs.get(0);
		Assert.assertEquals("Testing", testing.getStatus().getName());
	}

	/**
	 * Tests that getCompletedLabProceduresByTechnician() returns only and all
	 * completed lab procedures.
	 */
	@Test
	public void getCompletedLabProceduresByTechnician() throws FileNotFoundException, SQLException, IOException {
		genLabProcedures0To5();

		List<LabProcedure> procs = null;
		try {
			procs = controller.getCompletedLabProceduresByTechnician("5000000001");
		} catch (DBException e) {
			fail("Shouldn't throw exception when getting lab procedures");
		}
		Assert.assertEquals(1, procs.size());
		LabProcedure completed = procs.get(0);
		Assert.assertEquals("Completed", completed.getStatus().getName());
	}

	/**
	 * Tests that setLabProcedureToReceivedStatus() correctly sets queries,
	 * updates, and persists the lab procedure with given ID.
	 * @throws FormValidationException 
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testSetLabProcedureToReceivedStatus() throws DBException, FormValidationException {
		DataSource mockDS = mock(DataSource.class);
		controller = new LabProcedureController(mockDS);
		controller = spy(controller);
		procedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
		LabProcedureData mockData = mock(LabProcedureData.class);
		controller.setLabProcedureData(mockData);
		when(mockData.getByID(Mockito.anyLong())).thenReturn(procedure);
		when(mockData.update(procedure)).thenReturn(true);
		controller.setLabProcedureToReceivedStatus("" + procedure.getLabProcedureID());
		verify(mockData, times(1)).update(procedure);
		verify(controller, times(1)).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Assert.assertEquals(LabProcedureStatus.RECEIVED, procedure.getStatus());
	}

	/**
	 * Tests that setLabProcedureToReceivedStatus() prints a faces message when
	 * a DBException occurs.
	 * @throws FormValidationException 
	 */
	@Test
	public void testSetLabProcedureToReceivedStatusDBException() throws DBException, FormValidationException {
		DataSource mockDS = mock(DataSource.class);
		controller = new LabProcedureController(mockDS);
		controller = spy(controller);
		procedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
		LabProcedureData mockData = mock(LabProcedureData.class);
		controller.setLabProcedureData(mockData);
		when(mockData.getByID(Mockito.anyLong())).thenThrow(new DBException(null));
		when(mockData.update(procedure)).thenReturn(false);
		controller.setLabProcedureToReceivedStatus("" + procedure.getLabProcedureID());
		verify(mockData, times(0)).update(procedure);
		verify(controller, times(1)).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Assert.assertEquals(LabProcedureStatus.IN_TRANSIT, procedure.getStatus());
	}

	/**
	 * Tests that setLabProcedureToReceivedStatus() prints a faces message when
	 * an Exception occurs.
	 * @throws FormValidationException 
	 */
	@Test
	public void testSetLabProcedureToReceivedStatusException() throws DBException, FormValidationException {
		DataSource mockDS = mock(DataSource.class);
		controller = new LabProcedureController(mockDS);
		controller = spy(controller);
		procedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
		LabProcedureData mockData = mock(LabProcedureData.class);
		controller.setLabProcedureData(mockData);
		// Any unchecked exception will do
		when(mockData.getByID(Mockito.anyLong())).thenThrow(new NullPointerException());
		when(mockData.update(procedure)).thenReturn(false);
		controller.setLabProcedureToReceivedStatus("" + procedure.getLabProcedureID());
		verify(mockData, times(0)).update(procedure);
		verify(controller, times(1)).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Assert.assertEquals(LabProcedureStatus.IN_TRANSIT, procedure.getStatus());
	}

	/**
	 * Tests that add() correctly adds a lab procedure.
	 * @throws FormValidationException 
	 */
	@Test
	public void testAdd() throws SQLException, DBException, FormValidationException {
		DataSource mockDS = mock(DataSource.class);
		LabProcedureData mockData = mock(LabProcedureData.class);
		when(mockData.add(Mockito.any(LabProcedure.class))).thenReturn(true);
		when(mockSessionUtils.getSessionUserRole()).thenReturn("hcp");
		when(mockSessionUtils.getSessionLoggedInMID()).thenReturn("9000000001");
		controller = spy(new LabProcedureController(mockDS));
		controller.setSessionUtils(mockSessionUtils);
		controller.setLabProcedureData(mockData);
		Mockito.doNothing().when(controller).logTransaction(any(), Mockito.anyString());

		controller.add(procedure);

		verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		verify(controller).logTransaction(TransactionType.LAB_RESULTS_CREATE, procedure.getLabProcedureCode());
	}

	/**
	 * Tests that add() catches Exceptions TODO: test this more thoroughly?
	 * @throws FormValidationException 
	 */
	@Test
	public void testAddWithDBException() throws SQLException, DBException, FormValidationException {
		DataSource mockDS = mock(DataSource.class);
		controller = new LabProcedureController(mockDS);
		controller.setSessionUtils(mockSessionUtils);
		controller = spy(controller);
		LabProcedureData mockData = mock(LabProcedureData.class);
		controller.setLabProcedureData(mockData);
		when(mockSessionUtils.getSessionUserRole()).thenReturn("hcp");
		when(mockData.add(Mockito.any(LabProcedure.class))).thenThrow(new DBException(null));
		controller.add(procedure);
		verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}

	/**
	 * Tests that edit() correctly edits a lab procedure. TODO: test this more
	 * thoroughly?
	 * @throws FormValidationException 
	 */
	@Test
	public void testEdit() throws SQLException, DBException, FormValidationException {
		DataSource mockDS = mock(DataSource.class);
		controller = new LabProcedureController(mockDS);
		controller = spy(controller);
		LabProcedureData mockData = mock(LabProcedureData.class);
		controller.setLabProcedureData(mockData);
		when(mockData.update(Mockito.any(LabProcedure.class))).thenReturn(true);
		controller.edit(procedure);
		verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}

	/**
	 * Tests that remove() correctly removes a lab procedure. TODO: test this
	 * more thoroughly?
	 */
	@Test
	public void testRemove() throws SQLException, DBException {
		DataSource mockDS = mock(DataSource.class);
		controller = new LabProcedureController(mockDS);
		controller = spy(controller);
		LabProcedureData mockData = mock(LabProcedureData.class);
		controller.setLabProcedureData(mockData);
		when(mockData.removeLabProcedure(1L)).thenReturn(true);
		controller.remove("1");
		verify(mockData, times(1)).removeLabProcedure(1L);
		verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}

	/**
	 * Tests that get() correctly retrieves a lab procedure.
	 */
	@Test
	public void testGet() throws Exception {
		gen.labProcedure0();
		controller = spy(controller);
		Mockito.doNothing().when(controller).printFacesMessage(any(), any(), any(), any());
		LabProcedure proc = controller.getLabProcedureByID("1");
		Assert.assertNotNull(proc);
		Assert.assertEquals(5000000001L, proc.getLabTechnicianID().longValue());
		proc = controller.getLabProcedureByID("-1");
		Assert.assertNull(proc);
		proc = controller.getLabProcedureByID("aa");
		Assert.assertNull(proc);
		verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_ERROR), any(), any(), any());
	}

	/**
	 * Tests that remove() throws correct exception for invalid ID argument
	 */
	@Test
	public void testRemoveInvalidID() throws SQLException, FileNotFoundException, IOException {
		genLabProcedures0To5();

		DataSource mockDS = Mockito.mock(DataSource.class);
		LabProcedureData mockData = Mockito.mock(LabProcedureMySQL.class);
		when(mockDS.getConnection()).thenReturn(ds.getConnection());
		controller = Mockito.spy(new LabProcedureController(mockDS));
		controller.remove("foo");
		Mockito.verify(controller, times(1)).printFacesMessage(Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
		Mockito.verifyZeroInteractions(mockDS);
		Mockito.verifyZeroInteractions(mockData);
	}

	/**
	 * Tests controller constructor. TODO: doesn't actually test much
	 */
	@Test
	public void testLabProcedure() {
		controller = new LabProcedureController();
		Assert.assertNotNull(controller);
	}

	@Test
	public void testRecordResults() {
		controller = spy(controller);
		procedure.setStatus(LabProcedureStatus.TESTING.getID());
		try {
			controller.recordResults(procedure);
		} catch (DBException e) {
			fail("Should not throw DBException when calling recordResults()");
			e.printStackTrace();
		}
		Assert.assertEquals(LabProcedureStatus.PENDING, procedure.getStatus());
		try {
			Mockito.verify(controller, times(1)).edit(procedure);
			Mockito.verify(controller, times(1)).updateStatusForReceivedList(Mockito.anyString());
		} catch (DBException e) {
			fail("Mockito couldn't verify controller method called, DBException thrown");
			e.printStackTrace();
		}
	}

	/**
	 * The controller should log that each lab procedure for the office visit in
	 * the session has been viewed by the loggedInMID.
	 */
	@Test
	public void testLogViewLabProcedure() throws DBException {
		List<LabProcedure> procs = new ArrayList<>(2);
		procs.add(procedure);
		LabProcedure second = new LabProcedure();
		second.setLabProcedureCode("33333-4");
		procs.add(second);

		controller = spy(controller);
		when(mockSessionUtils.getCurrentOfficeVisitId()).thenReturn(2L);
		Mockito.doNothing().when(controller).logTransaction(any(), any());
		when(controller.getLabProceduresByOfficeVisit("2")).thenReturn(procs);
		controller.setSessionUtils(mockSessionUtils);
		controller.setLabProcedureData(mockData);

		controller.logViewLabProcedure();
		verify(controller, times(2)).logTransaction(any(), Mockito.anyString());
	}
	
	@Test
	public void testLogViewLabProcedureNoOfficeVisitSelected() throws DBException {
		controller = spy(controller);
		when(mockSessionUtils.getCurrentOfficeVisitId()).thenReturn(null);
		Mockito.doNothing().when(controller).logTransaction(any(), any());
		controller.setSessionUtils(mockSessionUtils);
		controller.setLabProcedureData(mockData);
		
		controller.logViewLabProcedure();
		verify(controller, times(0)).logTransaction(any(), any());
	}

	/**
	 * The controller should log when a lab technician has viewed his/her queue
	 * of lab procedures.
	 */
	@Test
	public void testLogLabTechnicianViewLabProcedureQueue() {
		controller = spy(controller);
		when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(new Long(4L));
		Mockito.doNothing().when(controller).logTransaction(any(), any(), any(), any());
		controller.setSessionUtils(mockSessionUtils);
		controller.setLabProcedureData(mockData);

		controller.logLabTechnicianViewLabProcedureQueue();

		verify(controller, times(1)).logTransaction(TransactionType.LAB_RESULTS_VIEW_QUEUE, new Long(4), null, null);
	}

	/**
	 * Generates lab procedures 0, 1, 2, 3, 4, 5 with TestDataGenerator.
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	private void genLabProcedures0To5() throws FileNotFoundException, SQLException, IOException {
		gen.labProcedure0();
		gen.labProcedure1();
		gen.labProcedure2();
		gen.labProcedure3();
		gen.labProcedure4();
		gen.labProcedure5();
	}
}
