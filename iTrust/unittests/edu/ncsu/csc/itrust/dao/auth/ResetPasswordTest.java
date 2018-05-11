package edu.ncsu.csc.itrust.dao.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.action.ResetPasswordAction;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * ResetPasswordTest
 */
public class ResetPasswordTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	AuthDAO authDAO = factory.getAuthDAO();
	private TestDataGenerator gen = new TestDataGenerator();
    
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.patient2();
	}

	/**
	 * testResetPassword
	 * @throws Exception
	 */
	public void testResetPassword() throws Exception {
		/**
		assertEquals(DigestUtils.sha256Hex("pw"), getPassword(2L));
		authDAO.resetPassword(2L, "password");
		assertEquals(DigestUtils.sha256Hex("password"), getPassword(2L));
		*/
		//sanity check of the current user password 
		assertTrue(authDAO.authenticatePassword(2L, "pw"));
		//reset the password
		authDAO.resetPassword(2L, "password");
		//check that the password is truly reset
		assertTrue(authDAO.authenticatePassword(2L, "password"));
	}

	
	/**
	 * testResetPasswordNonExistent
	 * @throws Exception
	 */
	public void testResetPasswordNonExistent() throws Exception {
		// Still runs with no exception - that's the expected behavior
		authDAO.resetPassword(500L, "password");
	}

	private String getPassword(long mid) throws SQLException {
		Connection conn = null; 
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT password from users WHERE MID=" + mid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString("password");
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * testResetSecurityQuestionAnswer
	 * @throws DBException
	 */
	public void testResetSecurityQuestionAnswer() throws DBException {
		authDAO.setSecurityQuestionAnswer("how you doin?", "good", 2L);
	}

	/**
	 * testGetNoSecurityAnswer
	 * @throws Exception
	 */
	public void testGetNoSecurityAnswer() throws Exception {
		long mid = factory.getPatientDAO().addEmptyPatient();
		try {
			authDAO.getSecurityAnswer(mid);
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("No security answer set for MID " + mid, e.getMessage());
		}
	}
	
	/**
	 * testGetNoSecurityQuestion
	 * @throws Exception
	 */
	public void testGetNoSecurityQuestion() throws Exception {
		long mid = factory.getPatientDAO().addEmptyPatient();
		try {
			authDAO.getSecurityQuestion(mid);
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("No security question set for MID: " + mid, e.getMessage());
		}
	}
}
