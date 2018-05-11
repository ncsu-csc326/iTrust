package edu.ncsu.csc.itrust.unit.dao.patient;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.Ethnicity;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class EditPatientTest extends TestCase {
	PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
	TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.patient2();
	}

	public void testGetPatient2() throws Exception {
		PatientBean p = patientDAO.getPatient(2);
		assertNotNull(p);
		assertIsPatient2(p);
	}

	public void testEditPatient2() throws Exception {
		PatientBean p = patientDAO.getPatient(2);
		p.setFirstName("Person1");
		p.setEmail("another email");
		p.setEmergencyName("another emergency person");
		p.setTopicalNotes("some topical notes");
		p.setDateOfBirthStr("05/20/1984");
		p.setDateOfDeactivationStr("05/21/1984");
		patientDAO.editPatient(p, 9000000003L);

		p = patientDAO.getPatient(2);
		assertEquals("Person1", p.getFirstName());
		assertEquals("Programmer", p.getLastName());
		assertEquals("another email", p.getEmail());
		assertEquals("another emergency person", p.getEmergencyName());
		assertEquals("some topical notes", p.getTopicalNotes());
		assertEquals("05/20/1984", p.getDateOfBirthStr());
		assertEquals("05/21/1984", p.getDateOfDeactivationStr());
		assertEquals("250.10", p.getCauseOfDeath());
		assertEquals("344 Bob Street", p.getStreetAddress1());
		assertEquals("", p.getStreetAddress2());
		assertEquals("Raleigh", p.getCity());
		assertEquals("NC", p.getState());
		assertEquals("27607", p.getZip());
		assertEquals("555-555-5555", p.getPhone());
		assertEquals("555-555-5551", p.getEmergencyPhone());
		assertEquals("IC", p.getIcName());
		assertEquals("Street1", p.getIcAddress1());
		assertEquals("Street2", p.getIcAddress2());
		assertEquals("City", p.getIcCity());
		assertEquals("PA", p.getIcState());
		assertEquals("19003-2715", p.getIcZip());
		assertEquals("555-555-5555", p.getIcPhone());
		assertEquals("1", p.getIcID());
		assertEquals("1", p.getMotherMID());
		assertEquals("0", p.getFatherMID());
		assertEquals("O-", p.getBloodType().getName());
		assertEquals(Ethnicity.Caucasian, p.getEthnicity());
		assertEquals(Gender.Male, p.getGender());
	}

	public void testGetEmpty() throws Exception {
		assertNull(patientDAO.getPatient(0L));
	}

	private void assertIsPatient2(PatientBean p) {
		assertEquals(2L, p.getMID());
		assertEquals("Andy", p.getFirstName());
		assertEquals("Programmer", p.getLastName());
		assertEquals("05/19/1984", p.getDateOfBirthStr());
		assertEquals("250.10", p.getCauseOfDeath());
		assertEquals("andy.programmer@gmail.com", p.getEmail());
		assertEquals("344 Bob Street", p.getStreetAddress1());
		assertEquals("", p.getStreetAddress2());
		assertEquals("Raleigh", p.getCity());
		assertEquals("NC", p.getState());
		assertEquals("27607", p.getZip());
		assertEquals("555-555-5555", p.getPhone());
		assertEquals("Mr Emergency", p.getEmergencyName());
		assertEquals("555-555-5551", p.getEmergencyPhone());
		assertEquals("IC", p.getIcName());
		assertEquals("Street1", p.getIcAddress1());
		assertEquals("Street2", p.getIcAddress2());
		assertEquals("City", p.getIcCity());
		assertEquals("PA", p.getIcState());
		assertEquals("19003-2715", p.getIcZip());
		assertEquals("555-555-5555", p.getIcPhone());
		assertEquals("1", p.getIcID());
		assertEquals("1", p.getMotherMID());
		assertEquals("0", p.getFatherMID());
		assertEquals("O-", p.getBloodType().getName());
		assertEquals(Ethnicity.Caucasian, p.getEthnicity());
		assertEquals(Gender.Male, p.getGender());
		assertEquals("This person is absolutely crazy. Do not touch them.", p.getTopicalNotes());
	}

	public void testRemoveAllRepresented() throws Exception {
		// 2 represents 1, but not 4
		gen.patient1();
		gen.patient4();

		// Add patient 4 to be represented by patient 2
		patientDAO.addRepresentative(2L, 4L);

		// Ensure the representatives were added correctly
		assertEquals(2, patientDAO.getRepresented(2L).size());

		// Remove all patient's from being represented by patient 2
		patientDAO.removeAllRepresented(2L);
		// Assert that no more patients are represented by patient 2
		assertTrue(patientDAO.getRepresented(2L).isEmpty());

		// Test with an evil factory
		patientDAO = new PatientDAO(EvilDAOFactory.getEvilInstance());

		try {
			patientDAO.removeAllRepresented(2L);
			fail("Exception should be caught");
		} catch (DBException e) {
			// Successful test
		}
	}
}
