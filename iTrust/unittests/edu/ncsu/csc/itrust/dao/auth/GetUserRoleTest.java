package edu.ncsu.csc.itrust.dao.auth;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class GetUserRoleTest extends TestCase{
	private TestDataGenerator gen = new TestDataGenerator(); 
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testHCPMeganHunt() throws Exception {
		gen.hcp0();
		assertEquals("HCP 90..0", "hcp", TestDAOFactory.getTestInstance().getAuthDAO().getUserRole(9000000000L).getUserRolesString());
	}
	
}
