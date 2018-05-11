package edu.ncsu.csc.itrust;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;

public class EmailUtilTest extends TestCase {

	public void testSendEmail() throws Exception {
		// Note: this test can be deleted once you switch to a "real" email util
		try {
			new EmailUtil(EvilDAOFactory.getEvilInstance()).sendEmail(null);
			fail("Exception should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getExtendedMessage());
		}
	}

}
