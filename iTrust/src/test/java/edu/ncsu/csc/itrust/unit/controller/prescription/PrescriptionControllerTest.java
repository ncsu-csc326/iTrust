package edu.ncsu.csc.itrust.unit.controller.prescription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.prescription.PrescriptionController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureData;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.model.prescription.PrescriptionMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class PrescriptionControllerTest {

	@Mock
	private SessionUtils mockSessionUtils;
	@Mock
	private LabProcedureData mockData;
	@Spy
	private PrescriptionController controller;
	@Spy
	private DataSource ds;
	private TestDataGenerator gen;
	
	@Before
	public void setUp() throws FileNotFoundException, SQLException, IOException, DBException {
		ds = spy(ConverterDAO.getDataSource());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.uc21();
		gen.uc19();
		controller = spy(new PrescriptionController(ds));
		mockSessionUtils = mock(SessionUtils.class);
		controller.setSessionUtils(mockSessionUtils);
		mockData = mock(LabProcedureData.class);
	}

	@After
	public void tearDown() throws FileNotFoundException, SQLException, IOException {
		gen.clearAllTables();
	}
	
	@Test
	public void testGetPrescriptionByID() throws SQLException {
		assertNull(controller.getPrescriptionByID(null));
		verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_ERROR), anyString(), anyString(), anyString());
		assertNull(controller.getPrescriptionByID("-1"));
		verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_ERROR), anyString(), anyString(), anyString());
	}
	
	@Test
	public void testGetPrescriptionsForCurrentPatient() throws Exception {
		doReturn("201").when(mockSessionUtils).getCurrentPatientMID();
		assertEquals(3, controller.getPrescriptionsForCurrentPatient().size());
		
		doReturn("invalid id").when(mockSessionUtils).getCurrentPatientMID();
		assertEquals(0, controller.getPrescriptionsForCurrentPatient().size());
		verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_ERROR), anyString(), anyString(), anyString());
		
		doReturn("-1").when(mockSessionUtils).getCurrentPatientMID();
		assertEquals(0, controller.getPrescriptionsForCurrentPatient().size());
		verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_ERROR), anyString(), anyString(), anyString());
		
		doThrow(SQLException.class).when(ds).getConnection();
		doReturn("201").when(mockSessionUtils).getCurrentPatientMID();
		assertEquals(0, controller.getPrescriptionsForCurrentPatient().size());
	}
	
	@Test
	public void testGetListOfRepresentees() throws Exception {
		doReturn(null).when(mockSessionUtils).getRepresenteeList();
		doReturn(2L).when(mockSessionUtils).getSessionLoggedInMIDLong();
		doNothing().when(mockSessionUtils).setRepresenteeList(any());
		List<PatientBean> list = controller.getListOfRepresentees();
		assertNotNull(list);
	}
	
	@Test
	public void testExceptions() throws SQLException{
	    PrescriptionMySQL pSQL = spy(new PrescriptionMySQL(ds));
	    controller.setSql(pSQL);
	    
	    Prescription p = new Prescription();
	    p.setDrugCode(new MedicationBean("1234", "new code"));
	    p.setInstructions("instructions");
	    p.setDosage(50);
	    p.setStartDate(LocalDate.now());
	    p.setEndDate(LocalDate.now());
	    controller.add(p);
	    controller.add(p);
	    
	    when(pSQL.add(p)).thenThrow(new SQLException());
	    controller.add(p);
	    
	    //when(pSQL.update(p)).thenThrow(new SQLException());
	    controller.edit(p);
	    
        controller.remove(1);
	}
	
	@Test
	public void testLogViewPrescriptionReport() {
		when(mockSessionUtils.getCurrentPatientMID()).thenReturn("8"); // arbitrary
		doNothing().when(controller).logTransaction(any(), any());
		controller.logViewPrescriptionReport();
		verify(controller).logTransaction(TransactionType.PRESCRIPTION_REPORT_VIEW, null);
	}
	
	@Test
	public void testGetCodeName() {
		String codeName = "Midichlomaxene";
		String codeID = "48301-3420";
		assertEquals( codeName, controller.getCodeName(codeID) );
		
		
		/*codeID = "-1";
		controller.getCodeName(codeID);
		verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_ERROR), anyString(), anyString(), anyString());*/
	}
	
	@Test
	public void getRepParameter() {
		controller.getRepParameter();
	}
}
