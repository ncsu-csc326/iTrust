package edu.ncsu.csc.itrust.unit.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.action.ZipCodeAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ZipCodeActionTest extends TestCase {
	private ZipCodeAction zipCodeAction;
	private TestDataGenerator gen = new TestDataGenerator();
	private long loggedInMID = 2;

	@Override
	protected void setUp() throws FileNotFoundException, SQLException, IOException {
		gen = new TestDataGenerator();
		zipCodeAction = new ZipCodeAction(TestDAOFactory.getTestInstance(), loggedInMID);
		gen.clearAllTables();
		gen.standardData();
	}

	public void testGetExperts() throws DBException {
		System.out.println("Begin testGetExperts");
		List<PersonnelBean> physicians = zipCodeAction.getExperts("Surgeon", "10453", "250", 0l);
		assertEquals(0, physicians.size());
		System.out.println(physicians.size());
		for (PersonnelBean pb : physicians) {
			System.out.println(pb.getFullName());
		}
		physicians = zipCodeAction.getExperts("Surgeon", "10453", "500", 0l);
		System.out.println(physicians.size());
		for (PersonnelBean pb : physicians) {
			System.out.println(pb.getFullName());
		}
		System.out.println("End testGetExperts");
		physicians.get(0).getFullName().equals("Kelly Doctor");
	}
}
