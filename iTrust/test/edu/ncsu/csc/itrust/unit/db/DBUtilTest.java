package edu.ncsu.csc.itrust.unit.db;

import java.sql.Connection;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class DBUtilTest extends TestCase {

	public void testNoFailCloseConnection() throws Exception {
		DAOFactory df = TestDAOFactory.getTestInstance();
		Connection c = df.getConnection();
		c.close();
		DBUtil.closeConnection(c, null);
	}
	
	public void testCloseConnection() throws Exception
	{
		DAOFactory df = TestDAOFactory.getTestInstance();
		Connection c = df.getConnection();
		DBUtil.closeConnection(c, null);
	}
}
