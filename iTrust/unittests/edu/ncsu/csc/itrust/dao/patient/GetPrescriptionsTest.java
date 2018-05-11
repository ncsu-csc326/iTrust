package edu.ncsu.csc.itrust.dao.patient;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class GetPrescriptionsTest extends TestCase {
	private PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.ndCodes();
		gen.patient2();
	}

	public void testGetPatient2() throws Exception {
		List<PrescriptionBean> list = patientDAO.getCurrentPrescriptions(2l);
		PrescriptionBean first = list.get(0);
		assertEquals("Take twice daily", first.getInstructions());
		assertEquals("10/11/2020", first.getEndDateStr());
	}

	public void testNotPatient200() throws Exception {
		List<DiagnosisBean> diagnoses = patientDAO.getDiagnoses(200L);
		assertEquals(0, diagnoses.size());
	}
	
	public void testExpiredPrescription() throws Exception {
		List<PrescriptionBean> list;
		
		list = patientDAO.getExpiredPrescriptions(2);
		assertEquals(2, list.size());
		
		list = patientDAO.getExpiredPrescriptions(1);
		assertEquals(0, list.size());
	}
	
	public void testCurrentPrescriptions() throws Exception {
List<PrescriptionBean> list;
		
		list = patientDAO.getCurrentPrescriptions(2);
		assertEquals(1, list.size());
	}
}
