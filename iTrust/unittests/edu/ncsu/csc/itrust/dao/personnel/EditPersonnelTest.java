package edu.ncsu.csc.itrust.dao.personnel;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 */
public class EditPersonnelTest extends TestCase {
	PersonnelDAO personnelDAO = TestDAOFactory.getTestInstance().getPersonnelDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.uap1();
	}

	/**
	 * testGetPersonnel2
	 * @throws Exception
	 */
	public void testGetPersonnel2() throws Exception {
		PersonnelBean p = personnelDAO.getPersonnel(8000000009L);
		assertNotNull(p);
		assertIsPersonnel2(p);
	}

	/**
	 * testEditPersonnel2
	 * @throws Exception
	 */
	public void testEditPersonnel2() throws Exception {
		PersonnelBean p = personnelDAO.getPersonnel(8000000009L);
		p.setFirstName("Person1");
		p.setEmail("blah@blah.com");
		personnelDAO.editPersonnel(p);
		p = personnelDAO.getPersonnel(8000000009L);
		assertEquals("Person1", p.getFirstName());
		assertEquals("LastUAP", p.getLastName());
		assertEquals("blah@blah.com", p.getEmail());
	}

	/**
	 * testGetNonExistentPersonnel
	 * @throws Exception
	 */
	public void testGetNonExistentPersonnel() throws Exception {
		assertNull(personnelDAO.getPersonnel(0L));
	}

	private void assertIsPersonnel2(PersonnelBean p) {
		assertEquals(8000000009L, p.getMID());
		assertEquals("FirstUAP", p.getFirstName());
		assertEquals("LastUAP", p.getLastName());
//		assertEquals("opposite of yin", p.getSecurityQuestion());
//		assertEquals("yang", p.getSecurityAnswer());
		assertEquals("100 Ave", p.getStreetAddress1());
		assertEquals("", p.getStreetAddress2());
		assertEquals("Raleigh", p.getCity());
		assertEquals("NC", p.getState());
		assertEquals("27607", p.getZip());
		assertEquals("111-111-1111", p.getPhone());
	}
	
	/**
	 * testEditPersonallZipCode
	 * @throws Exception
	 */
	public void testEditPersonnelZipCode() throws Exception {
		PersonnelBean p = personnelDAO.getPersonnel(8000000009L);
		p.setZip("55555-6666");
		personnelDAO.editPersonnel(p);
		assertEquals("55555-6666", p.getZip());
	}
	
	/**
	 * testEditPersonnelSpeciality
	 * @throws Exception
	 */
	public void testEditPersonnelSpecialty() throws Exception {
		PersonnelBean p = personnelDAO.getPersonnel(8000000009L);
		p.setSpecialty("chocolate");
		personnelDAO.editPersonnel(p);
		assertEquals("chocolate", p.getSpecialty());
	}
}
