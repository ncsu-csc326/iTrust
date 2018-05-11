package edu.ncsu.csc.itrust.unit.model.user;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.user.User;

public class UserTest extends TestCase {
	private User test;

	@Override
	protected void setUp() throws Exception {
		test = new User();
	}

	/*
	 * updated to reflect the new way addAllergy updates allergyDAO.
	 */
	@Test
	public void testMID() throws Exception {
		test.setMID(9000000001L);
		Assert.assertEquals(9000000001L, test.getMID());
	}
	@Test
	public void testBigMID() throws Exception {
		try{
			test.setMID(90000000000L);
			Assert.fail("No error thrown");
		}
		catch(ITrustException ie){
			Assert.assertTrue("Exception Thrown", true);
			
			
		}

		Assert.assertNotEquals(90000000000L, test.getMID());
	}
	@Test
	public void testSmallMID() throws Exception {
		try{
			test.setMID(-90000000L);
			Assert.fail("No error thrown");
		}
		catch(ITrustException ie){
			Assert.assertTrue("Exception Thrown", true);
			
			
		}

		Assert.assertNotEquals(-90000000L, test.getMID());
	}
	@Test
	public void testRole(){
		test.setRole(Role.HCP);
		Assert.assertEquals(Role.HCP, test.getRole());
	}
	@Test
	public void testLastName(){
		test.setLastName("testLN");
		Assert.assertEquals("testLN", test.getLastName());
		
	}
	@Test
	public void testFirstName(){
		test.setFirstName("testFN");
		Assert.assertEquals("testFN",test.getFirstName());
	}
}
