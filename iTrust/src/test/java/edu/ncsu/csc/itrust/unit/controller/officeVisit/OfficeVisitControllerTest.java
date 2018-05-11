package edu.ncsu.csc.itrust.unit.controller.officeVisit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitData;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;

public class OfficeVisitControllerTest extends TestCase {
	
	private static final long DEFAULT_PATIENT_MID = 1L;
	private static final long DEFAULT_HCP_MID = 900000000L;

	@Spy private OfficeVisitController ovc;
	@Spy private OfficeVisitController ovcWithNullDataSource;
	@Spy private SessionUtils sessionUtils;
	
	@Mock private HttpServletRequest mockHttpServletRequest;
	@Mock private HttpSession mockHttpSession;
	@Mock private SessionUtils mockSessionUtils;

	private ApptTypeData apptData;
	private OfficeVisitData ovData;
	private DataSource ds;
	private TestDataGenerator gen; // remove when ApptType, Patient, and other
									// files are finished
	private OfficeVisit testOV;

	private LocalDateTime birthDate;
	private LocalDateTime babyDate;
	private LocalDateTime childDate;
	private LocalDateTime adultDate;

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		ovc = Mockito.spy(new OfficeVisitController(ds, mockSessionUtils));
		Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(ovc).redirectToBaseOfficeVisit();
		apptData = new ApptTypeMySQLConverter(ds);
		ovData = new OfficeVisitMySQL(ds);
		// remove when these modules are built and can be called
		gen = new TestDataGenerator();
		gen.appointmentType();
		gen.hospitals();
		gen.patient1();
		gen.uc51();
		gen.uc52();
		gen.uc53SetUp();

		// Setup date
		birthDate = ovc.getPatientDOB(DEFAULT_PATIENT_MID).atTime(0, 0);
		babyDate = birthDate.plusYears(1);
		childDate = birthDate.plusYears(4);
		adultDate = birthDate.plusYears(13);

		// Setup test OfficeVisit
		testOV = new OfficeVisit();
		testOV.setPatientMID(DEFAULT_PATIENT_MID);
		List<ApptType> types = apptData.getAll();
		long apptTypeID = types.get((types.size() - 1)).getID();
		testOV.setApptTypeID(apptTypeID);
		HospitalData hospitalData = new HospitalMySQLConverter(ds);
		List<Hospital> hospitals = hospitalData.getAll();
		String locID = hospitals.get((hospitals.size() - 1)).getHospitalID();
		testOV.setLocationID(locID);
		testOV.setNotes("Hello World!");
		testOV.setSendBill(true);

		// Default test OV to baby date
		testOV.setDate(babyDate);

		// Initialize a office visit controller with null data source
		ovcWithNullDataSource = new OfficeVisitController(null, sessionUtils);

		// Mock HttpServletRequest
		mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

		// Mock HttpSession
		mockHttpSession = Mockito.mock(HttpSession.class);
	}

	@Test
	public void testRetrieveOfficeVisit() throws DBException {
		Assert.assertTrue("Office visit should be added successfully", ovc.addReturnResult(testOV));

		// Get the visit ID from the DB
		List<OfficeVisit> all = ovData.getAll();
		long visitID = -1;
		for (int i = 0; i < all.size(); i++) {
			OfficeVisit ovI = all.get(i);
			boolean bApptType = ovI.getApptTypeID().equals(testOV.getApptTypeID());
			boolean bDate = false;
			long time = ChronoUnit.MINUTES.between(testOV.getDate(), ovI.getDate());
			bDate = (time < 1);
			boolean bLoc = (testOV.getLocationID().equals(ovI.getLocationID()));
			boolean bNotes = false;
			if (testOV.getNotes() == null) {
				if (ovI.getNotes() == null)
					bNotes = true;

			} else {
				bNotes = (testOV.getNotes().equals(ovI.getNotes()));
			}
			boolean bBill = (testOV.getSendBill() == ovI.getSendBill());

			if (bApptType && bDate && bLoc && bNotes && bBill) {
				visitID = ovI.getVisitID();
			}
		}
		Assert.assertNotEquals(-1L, visitID);
		OfficeVisit check = ovc.getVisitByID(Long.toString(visitID));
		Assert.assertEquals(testOV.getApptTypeID(), check.getApptTypeID());
		long dif = ChronoUnit.MINUTES.between(testOV.getDate(), check.getDate());
		Assert.assertTrue(dif < 1);
		Assert.assertEquals(testOV.getLocationID(), check.getLocationID());
		Assert.assertEquals(testOV.getNotes(), check.getNotes());
		Assert.assertEquals(testOV.getSendBill(), check.getSendBill());

		testOV.setVisitID(visitID);
		testOV.setNotes("testNotes");

		ovc.edit(testOV);

	}

	@Test
	public void testAddOfficeVisitWithInvalidDate() throws DBException {
		LocalDateTime date = birthDate.minusDays(1);
		testOV.setDate(date);
		Assert.assertFalse("Office Visit date cannot be set prior to patient birthday", ovc.addReturnResult(testOV));
	}

	@Test
	public void testAddOfficeVisitWithInvalidDateAndFacesContext() throws DBException {
		LocalDateTime date = birthDate.minusDays(1);
		testOV.setDate(date);
		ovc.add(testOV);
	}

	@Test
	public void testAddOfficeVisitWithFacesContext() throws DBException {
		ovc.add(testOV);
		Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testGetOfficeVisitsForPatient() {
		Assert.assertEquals(0, ovc.getOfficeVisitsForPatient(Long.toString(DEFAULT_PATIENT_MID)).size());
		Assert.assertEquals(0, ovc.getOfficeVisitsForPatient(Long.toString(101L)).size());
		Assert.assertEquals(5, ovc.getOfficeVisitsForPatient(Long.toString(102L)).size());
		Assert.assertEquals(3, ovc.getOfficeVisitsForPatient(Long.toString(103L)).size());
		Assert.assertEquals(3, ovc.getOfficeVisitsForPatient(Long.toString(104L)).size());
	}

	@Test
	public void testGetOfficeVisitsForPatientWithException() {
		Assert.assertEquals(0,
				ovcWithNullDataSource.getOfficeVisitsForPatient(Long.toString(DEFAULT_PATIENT_MID)).size());
	}

	@Test
	public void testGetOfficeVisitsForPatientWithNullPid() {
		Assert.assertEquals(0, ovc.getOfficeVisitsForPatient(null).size());
	}

	@Test
	public void testGetOfficeVisitsForPatientWithHCPPid() {
		Assert.assertEquals(0, ovc.getOfficeVisitsForPatient(Long.toString(DEFAULT_HCP_MID)).size());
	}

	@Test
	public void testGetOfficeVisitsForCurrentPatient() {
		Mockito.doReturn(Long.toString(DEFAULT_PATIENT_MID)).when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("101").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("102").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(5, ovc.getOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("103").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(3, ovc.getOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("104").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(3, ovc.getOfficeVisitsForCurrentPatient().size());
	}

	@Test
	public void testGetOfficeVisitsForCurrentPatientWithInvalidMID() {
		Mockito.doReturn(Long.toString(DEFAULT_HCP_MID)).when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("-1").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn(null).when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getOfficeVisitsForCurrentPatient().size());
	}

	@Test
	public void testGetAdultOfficeVisitsForCurrentPatient() {
		Mockito.doReturn("101").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getAdultOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("102").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getAdultOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("103").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getAdultOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("104").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(3, ovc.getAdultOfficeVisitsForCurrentPatient().size());
	}

	@Test
	public void testGetChildOfficeVisitsForCurrentPatient() {
		Mockito.doReturn("101").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getChildOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("102").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getChildOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("103").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(2, ovc.getChildOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("104").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getChildOfficeVisitsForCurrentPatient().size());
	}

	@Test
	public void testGetBabyOfficeVisitsForCurrentPatient() {
		Mockito.doReturn("101").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getBabyOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("102").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(5, ovc.getBabyOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("103").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(1, ovc.getBabyOfficeVisitsForCurrentPatient().size());
		Mockito.doReturn("104").when(mockSessionUtils).getCurrentPatientMID();
		Assert.assertEquals(0, ovc.getBabyOfficeVisitsForCurrentPatient().size());
	}

	@Test
	public void testGetVisitByIDWithInvalidID() {
		Assert.assertNull(ovc.getVisitByID("invalid id"));
		Assert.assertNull(ovc.getVisitByID("-1"));
	}

	@Test
	public void testCalculatePatientAge() {
		Assert.assertTrue(ovc.isPatientABaby(DEFAULT_PATIENT_MID, birthDate));
		Assert.assertFalse(ovc.isPatientAChild(DEFAULT_PATIENT_MID, birthDate));
		Assert.assertFalse(ovc.isPatientAnAdult(DEFAULT_PATIENT_MID, birthDate));

		Assert.assertTrue(ovc.isPatientABaby(DEFAULT_PATIENT_MID, babyDate));
		Assert.assertFalse(ovc.isPatientAChild(DEFAULT_PATIENT_MID, babyDate));
		Assert.assertFalse(ovc.isPatientAnAdult(DEFAULT_PATIENT_MID, babyDate));

		Assert.assertFalse(ovc.isPatientABaby(DEFAULT_PATIENT_MID, childDate));
		Assert.assertTrue(ovc.isPatientAChild(DEFAULT_PATIENT_MID, childDate));
		Assert.assertFalse(ovc.isPatientAnAdult(DEFAULT_PATIENT_MID, childDate));

		Assert.assertFalse(ovc.isPatientABaby(DEFAULT_PATIENT_MID, adultDate));
		Assert.assertFalse(ovc.isPatientAChild(DEFAULT_PATIENT_MID, adultDate));
		Assert.assertTrue(ovc.isPatientAnAdult(DEFAULT_PATIENT_MID, adultDate));
	}

	@Test
	public void testCalculatePatientAgeWithNulls() {
		Assert.assertFalse(ovc.isPatientABaby(null, birthDate));
		Assert.assertFalse(ovc.isPatientABaby(-1L, birthDate));
		Assert.assertFalse(ovc.isPatientAChild(null, birthDate));
		Assert.assertFalse(ovc.isPatientAChild(-1L, birthDate));
		Assert.assertFalse(ovc.isPatientAnAdult(null, birthDate));
		Assert.assertFalse(ovc.isPatientAnAdult(-1L, birthDate));
		Assert.assertFalse(ovc.isPatientAnAdult(1L, null));
	}

	@Test
	public void testGetSelectedVisit() throws DBException {
		// Add a test office visit
		ovc.add(testOV);
		List<OfficeVisit> officeVisitList = ovData.getAll();

		Assert.assertNotNull(officeVisitList);
		Assert.assertFalse(officeVisitList.isEmpty());

		// Return office visit id in mocked httpServletRequest
		OfficeVisit expected = officeVisitList.get(0);
		Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).parseString(Mockito.any());
		Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).getRequestParameter("visitID");
		
		OfficeVisit actual = ovc.getSelectedVisit();
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected.getPatientMID(), actual.getPatientMID());
		Assert.assertEquals(expected.getApptTypeID(), actual.getApptTypeID());
		long dif = ChronoUnit.MINUTES.between(expected.getDate(), actual.getDate());
		Assert.assertTrue(dif < 1);
		Assert.assertEquals(expected.getLocationID(), actual.getLocationID());
		Assert.assertEquals(expected.getNotes(), actual.getNotes());
		Assert.assertEquals(expected.getSendBill(), actual.getSendBill());
	}

	@Test
	public void testGetSelectedVisitWithNullRequest() {
		Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
		OfficeVisit ov = ovc.getSelectedVisit();
		Assert.assertNull(ov);
	}

	@Test
	public void testGetSelectedVisitWithNullVisitId() {
		Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
		OfficeVisit ov = ovc.getSelectedVisit();
		Assert.assertNull(ov);
	}

	@Test
	public void testHasPatientVisited() {
		Assert.assertFalse(ovc.hasPatientVisited("101"));
		Assert.assertTrue(ovc.hasPatientVisited("102"));
		Assert.assertTrue(ovc.hasPatientVisited("103"));
	}

	@Test
	public void testHasPatientVisitedWithNulls() {
		Assert.assertFalse(ovc.hasPatientVisited(null));
		Assert.assertFalse(ovc.hasPatientVisited("-1"));
	}

	@Test
	public void testCurrentPatientHasVisited() {
		final String MID = "101";
		final String PATIENT = "patient";

		Mockito.doReturn(PATIENT).when(mockSessionUtils).getSessionUserRole();
		Mockito.doReturn(MID).when(mockSessionUtils).getCurrentPatientMID();

		Assert.assertFalse(ovc.CurrentPatientHasVisited());
	}
	
	@Test
	public void testAddReturnGeneratedId() {
		long id = ovc.addReturnGeneratedId(testOV);
		Assert.assertTrue(id >= 0);
		Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void testLogViewOfficeVisit() {
		Mockito.when(mockSessionUtils.getCurrentOfficeVisitId()).thenReturn(2L);
		ovc.setSessionUtils(mockSessionUtils);
		Mockito.doNothing().when(ovc).logTransaction(Mockito.any(), Mockito.anyString());
		ovc.logViewOfficeVisit();
		Mockito.verify(ovc, Mockito.times(2)).logTransaction(Mockito.any(), Mockito.anyString());
	}
	
	@Test
	public void testLogViewOfficeVisitNoneSelected() {
		Mockito.when(mockSessionUtils.getCurrentOfficeVisitId()).thenReturn(null);
		ovc.setSessionUtils(mockSessionUtils);
		ovc.logViewOfficeVisit();
		Mockito.verify(ovc, Mockito.times(0)).logTransaction(TransactionType.OFFICE_VISIT_VIEW, new Long(2).toString());
	}
}
