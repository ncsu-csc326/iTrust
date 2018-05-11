package edu.ncsu.csc.itrust.unit.dao.patient;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class AddPatientTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}

	public void testAddEmptyPatient() throws Exception {
		long pid = patientDAO.addEmptyPatient();
		assertEquals(" ", patientDAO.getName(pid));
	}

	public void testGetEmptyPatient() throws Exception {
		try {
			patientDAO.getName(0L);
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("User does not exist", e.getMessage());
		}
	}

	public void testInsertDeath() throws Exception {
		gen.patient1();
		PatientBean p = patientDAO.getPatient(1l);
		assertEquals("Random", p.getFirstName());
		assertEquals("", p.getCauseOfDeath());
		assertEquals("", p.getDateOfDeathStr());
		p.setDateOfDeathStr("09/12/2007");
		p.setCauseOfDeath("79.3");
		patientDAO.editPatient(p, 9000000003L);
		PatientBean p2 = patientDAO.getPatient(1l);
		assertEquals("79.3", p2.getCauseOfDeath());
		assertEquals("09/12/2007", p2.getDateOfDeathStr());
	}

	public void testEmergencyContactInfo() throws Exception {
		long pid = patientDAO.addEmptyPatient();
		PatientBean p = patientDAO.getPatient(pid);
		p.setFirstName("Lola");
		p.setLastName("Schaefer");
		p.setEmail("l@cox.net");
		p.setCity("Raleigh");
		p.setState("NC");
		p.setZip("27602");
		p.setPhone("222-222-3333");
		p.setSecurityQuestion("What is the best team in the acc?");
		p.setSecurityAnswer("NCSU");
		p.setIcName("Blue Cross");
		p.setIcAddress1("222 Blue Rd");
		p.setIcCity("Raleigh");
		p.setIcState("NC");
		p.setIcZip("27607");
		p.setIcPhone("222-333-4444");
		p.setIcID("2343");
		p.setEmergencyName("Joy Jones");
		p.setEmergencyPhone("012-345-6789");
		patientDAO.editPatient(p, 9000000003L);
		assertEquals("Joy Jones", patientDAO.getPatient(pid).getEmergencyName());
		assertEquals("012-345-6789", patientDAO.getPatient(pid).getEmergencyPhone());
	}

}
