package edu.ncsu.csc.itrust.unit.model.labProcedure;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.sun.faces.context.RequestParameterMap;

import edu.ncsu.csc.itrust.controller.labProcedure.LabProcedureController;
import edu.ncsu.csc.itrust.controller.labProcedure.LabProcedureForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure.LabProcedureStatus;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureData;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeData;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class LabProcedureFormTest {

	@Mock
	private LabProcedureController mockController;
	@Mock
	private SessionUtils mockSessionUtils;
	@Mock
	private LabProcedureData mockData;
	@Mock
	private ExternalContext mockExternalContext;
	@Mock
	private RequestParameterMap mockReqParamMap;
	@Mock
	private LOINCCodeData mockCodeData;

	private LabProcedureForm form;
	private LabProcedure procedure;
	private LOINCCodeData codeData;
	private DataSource ds;
	private TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		form = new LabProcedureForm();
		procedure = new LabProcedure();
		codeData = new LOINCCodeMySQL(ds);
		ds = ConverterDAO.getDataSource();
		gen = new TestDataGenerator();
		gen.clearAllTables();
		mockData = mock(LabProcedureData.class);
		mockCodeData = mock(LOINCCodeData.class);
		mockController = mock(LabProcedureController.class);
		mockController.setLabProcedureData(mockData);
		mockSessionUtils = mock(SessionUtils.class);
		// mockFacesContext = mock(FacesContext.class);
		mockExternalContext = mock(ExternalContext.class);
		mockReqParamMap = mock(RequestParameterMap.class);

		procedure.setCommentary("commentary");
		procedure.setConfidenceIntervalLower(10);
		procedure.setConfidenceIntervalUpper(30);
		procedure.setLabProcedureCode("12345-6");
		procedure.setIsRestricted(true);
		procedure.setLabProcedureID(8L);
		procedure.setLabTechnicianID(9L);
		procedure.setOfficeVisitID(10L);
		procedure.setPriority(3);
		procedure.setResults("results");
		procedure.setStatus(1L);
		procedure.setUpdatedDate(new Timestamp(100L));
		procedure.setHcpMID(9000000001L);
	}

	/** TODO: test this better */
	@Test
	public void testLabProcedureForm() {
		form = new LabProcedureForm();
		Assert.assertNotNull(form);
	}

	@Test
	public void testLabProcedureFormLabProcedureController() {
		when(mockSessionUtils.getRequestParameter("id")).thenReturn(procedure.getLabProcedureID().toString());
		when(mockController.getLabProcedureByID(procedure.getLabProcedureID().toString())).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		Assert.assertNotNull(form);
		verify(mockSessionUtils, times(0)).printFacesMessage(any(), any(), any(), any());
		Assert.assertEquals(new Long(8L), form.getSelectedLabProcedure().getLabProcedureID());
	}

	@Test
	public void testLabProcedureFormLabProcedureControllerNullLabProcedure() {
		when(mockSessionUtils.getCurrentOfficeVisitId()).thenReturn(420L);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		Assert.assertNotNull(form);
		verify(mockSessionUtils, times(0)).printFacesMessage(any(), any(), any(), any());
		Assert.assertEquals(new Long(420), form.getLabProcedure().getOfficeVisitID());
		Assert.assertEquals(LabProcedureStatus.IN_TRANSIT, form.getLabProcedure().getStatus());
	}

	@Test
	public void testGetSelectedLabProcedure() {
		when(mockSessionUtils.getRequestParameter("id")).thenReturn("3");
		when(mockController.getLabProcedureByID("3")).thenReturn(procedure);
		
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);

		LabProcedure returnedProcedure = form.getSelectedLabProcedure();

		Assert.assertNotNull(returnedProcedure);
		Assert.assertEquals("commentary", returnedProcedure.getCommentary());
		Assert.assertEquals(new Integer(10), returnedProcedure.getConfidenceIntervalLower());
		Assert.assertEquals(new Integer(30), returnedProcedure.getConfidenceIntervalUpper());
		Assert.assertEquals("12345-6", returnedProcedure.getLabProcedureCode());
		Assert.assertTrue(returnedProcedure.isRestricted());
		Assert.assertEquals(new Long(9L), returnedProcedure.getLabTechnicianID());
		Assert.assertEquals(new Long(10L), returnedProcedure.getOfficeVisitID());
		Assert.assertEquals(new Integer(3), returnedProcedure.getPriority());
		Assert.assertEquals("results", returnedProcedure.getResults());
		Assert.assertEquals(1L, returnedProcedure.getStatus().getID());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		// Assert updatedDate is within 5 seconds
		Assert.assertTrue(now.compareTo(returnedProcedure.getUpdatedDate()) < 5000);
		Assert.assertEquals(new Long(9000000001L), returnedProcedure.getHcpMID());
	}

	@Test
	public void testAddCommentary() {
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		when(mockController.getLabProcedureByID("8")).thenReturn(procedure);
		when(mockSessionUtils.getCurrentFacesContext()).thenReturn(null);
		form.addCommentary("8");
		verify(mockController, times(1)).edit(procedure);
		verify(mockController, times(1)).logTransaction(TransactionType.LAB_RESULTS_ADD_COMMENTARY, procedure.getLabProcedureCode());
		Assert.assertEquals(LabProcedureStatus.COMPLETED, procedure.getStatus());
		Assert.assertEquals("Reviewed by HCP", procedure.getCommentary());
	}

	@Test
	public void testIsReassignable() {
		when(mockController.getLabProcedureByID("4")).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		procedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
		Assert.assertTrue(form.isReassignable("4"));
		procedure.setStatus(LabProcedureStatus.RECEIVED.getID());
		Assert.assertTrue(form.isReassignable("4"));
		procedure.setStatus(LabProcedureStatus.COMPLETED.getID());
		Assert.assertFalse(form.isReassignable("4"));
		procedure.setStatus(LabProcedureStatus.PENDING.getID());
		Assert.assertFalse(form.isReassignable("4"));
		procedure.setStatus(LabProcedureStatus.TESTING.getID());
		Assert.assertFalse(form.isReassignable("4"));
	}

	@Test
	public void testIsReassignableBadLong() {
		boolean isReassignable = form.isReassignable("uh oh");
		Assert.assertFalse(isReassignable);
	}

	@Test
	public void testIsRemovable() {
		when(mockController.getLabProcedureByID("4")).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		procedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
		Assert.assertTrue(form.isRemovable("4"));
		procedure.setStatus(LabProcedureStatus.RECEIVED.getID());
		Assert.assertTrue(form.isRemovable("4"));
		procedure.setStatus(LabProcedureStatus.COMPLETED.getID());
		Assert.assertFalse(form.isRemovable("4"));
		procedure.setStatus(LabProcedureStatus.PENDING.getID());
		Assert.assertFalse(form.isRemovable("4"));
		procedure.setStatus(LabProcedureStatus.TESTING.getID());
		Assert.assertFalse(form.isRemovable("4"));
	}

	@Test
	public void testIsRemovableBadLong() {
		boolean isRemovable = form.isRemovable("uh oh");
		Assert.assertFalse(isRemovable);
	}
	
	@Test
	public void testRemoveLabProcedure() {
		String procID = procedure.getLabProcedureID().toString();
		Mockito.doNothing().when(mockController).remove(procID);
		when(mockSessionUtils.getRequestParameter("id")).thenReturn(procID);
		when(mockController.getLabProcedureByID(procID)).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		form.removeLabProcedure(procedure.getLabProcedureID());
		verify(mockController).remove(procID);
		verify(mockController, times(1)).logTransaction(TransactionType.LAB_RESULTS_REMOVE, procedure.getLabProcedureCode());
		verify(mockController, times(0)).printFacesMessage(any(), any(), any(), any());
	}

	@Test
	public void testIsCommentable() {
		when(mockController.getLabProcedureByID("4")).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		procedure.setStatus(LabProcedureStatus.PENDING.getID());
		Assert.assertTrue(form.isCommentable("4"));
		procedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
		Assert.assertFalse(form.isCommentable("4"));
		procedure.setStatus(LabProcedureStatus.RECEIVED.getID());
		Assert.assertFalse(form.isCommentable("4"));
		procedure.setStatus(LabProcedureStatus.COMPLETED.getID());
		Assert.assertFalse(form.isCommentable("4"));
		procedure.setStatus(LabProcedureStatus.TESTING.getID());
		Assert.assertFalse(form.isCommentable("4"));
	}

	@Test
	public void testIsCommentableBadLong() {
		boolean isCommentable = form.isCommentable("uh oh");
		Assert.assertFalse(isCommentable);
	}

//	 @Test
//	 public void testSubmitNewLabProcedure() {
//		 form = new LabProcedureForm(mockController, codeData, mockSessionUtils);
//		 form.submitNewLabProcedure();
//		 verify(mockController, times(1)).add(any());
//		 
//	 }
	
	@Test
	public void testSubmitReassignment() {
		Mockito.doNothing().when(mockController).edit(procedure);
		when(mockSessionUtils.getRequestParameter("id")).thenReturn("someID");
		when(mockController.getLabProcedureByID("someID")).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		form.submitReassignment();
		verify(mockController).edit(procedure);
		verify(mockController).logTransaction(TransactionType.LAB_RESULTS_REASSIGN, procedure.getLabProcedureCode());
	}

	@Test
	public void testIsLabProcedureCreated() {
		when(mockSessionUtils.getRequestParameter("id")).thenReturn("8");
		when(mockController.getLabProcedureByID(procedure.getLabProcedureID().toString())).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		Assert.assertTrue(form.isLabProcedureCreated());
	}

	@Test
	public void testRecordResults() throws DBException {
		String procID = procedure.getLabProcedureID().toString();
		Mockito.doNothing().when(mockController).recordResults(procedure);
		when(mockSessionUtils.getRequestParameter("id")).thenReturn(procID);
		when(mockController.getLabProcedureByID(procID)).thenReturn(procedure);
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		form.recordResults();
		verify(mockController, times(1)).recordResults(any());
		verify(mockSessionUtils, times(0)).printFacesMessage(any(), any(), any(), any());
		verify(mockController, times(1)).logTransaction(TransactionType.LAB_RESULTS_RECORD, procedure.getLabProcedureCode());
	}

	@Test
	public void testRecordResultsDBException() throws DBException, FormValidationException {
		when(mockData.update(procedure)).thenThrow(new DBException(null));
		form = new LabProcedureForm(mockController, codeData, mockSessionUtils, ds);
		form.recordResults();
		verify(mockController, times(1)).recordResults(any());
		// TODO: verify sessionUtils.printFacesMessage() was called (limited by current mocking capabilities)
	}
	
	@Test
	public void testGetLoincCodes() throws DBException {
		List<LOINCCode> testCodes = new ArrayList<LOINCCode>(1);
		when(mockCodeData.getAll()).thenReturn(testCodes);
		form = new LabProcedureForm(mockController, mockCodeData, mockSessionUtils, ds);
		testCodes.add(new LOINCCode("12345-6", "component", "kind of property"));
		List<LOINCCode> returnedCodes = form.getLOINCCodes();
		Assert.assertNotNull(returnedCodes);
		Assert.assertEquals(1, returnedCodes.size());
	}
	
	@Test
	public void testGetLoincCodesEmpty() throws DBException {
		when(mockCodeData.getAll()).thenReturn(Collections.emptyList());
		form = new LabProcedureForm(mockController, mockCodeData, mockSessionUtils, ds);
		List<LOINCCode> codes = form.getLOINCCodes();
		Assert.assertNotNull(codes);
		Assert.assertEquals(0, codes.size());
	}
	
	@Test
	public void testGetLoincCodesDBException() throws DBException {
		when(mockCodeData.getAll()).thenThrow(new DBException(null));
		form = new LabProcedureForm(mockController, mockCodeData, mockSessionUtils, ds);
		List<LOINCCode> codes = form.getLOINCCodes();
		Assert.assertNotNull(codes);
		Assert.assertEquals(0, codes.size());
	}
}
