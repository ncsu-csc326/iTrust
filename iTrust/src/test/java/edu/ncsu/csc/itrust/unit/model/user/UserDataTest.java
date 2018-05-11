package edu.ncsu.csc.itrust.unit.model.user;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.user.User;
import edu.ncsu.csc.itrust.model.user.UserMySQLConverter;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class UserDataTest extends TestCase {
	//private User test;
	private DataBean<User> testData;
	private DataSource ds;
	TestDataGenerator gen;
	
	@Override
	protected void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		testData = new UserMySQLConverter(ds);
		gen = new TestDataGenerator();
	}

	@Test
	public void testGetPatientUser() throws FileNotFoundException, IOException, SQLException, DBException{
		gen.patient1();
		User testUser = testData.getByID(1L);
		Assert.assertEquals(testUser.getFirstName(), "Random");
		Assert.assertEquals(testUser.getLastName(), "Person");
		Assert.assertEquals(testUser.getMID(), 1L);
		Assert.assertEquals(testUser.getRole(), Role.PATIENT);
	}
	
	@Test
	public void testGetHCPUser() throws FileNotFoundException, SQLException, IOException, DBException{
		gen.hcp1();
		User hcpTest = testData.getByID(9900000000L);
		Assert.assertEquals("Arehart",hcpTest.getLastName());
		Assert.assertEquals("Tester",hcpTest.getFirstName());
		Assert.assertEquals(9900000000L,hcpTest.getMID());
		Assert.assertEquals(Role.HCP, hcpTest.getRole());
	}
}
