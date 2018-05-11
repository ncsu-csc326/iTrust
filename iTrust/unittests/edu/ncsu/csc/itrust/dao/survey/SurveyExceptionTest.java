package edu.ncsu.csc.itrust.dao.survey;

import java.util.Calendar;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.SurveyDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;

public class SurveyExceptionTest extends TestCase {

	private DAOFactory factory = EvilDAOFactory.getEvilInstance();
	private SurveyDAO surveyDAO = factory.getSurveyDAO();

	@Override
	protected void setUp() throws Exception {
	}

	public void testGetSurveyDataException() throws Exception {
		try {
			surveyDAO.getSurveyData(0L);
			fail("DBException should have been thrown");
		} catch (DBException ex) {
			assertEquals(EvilDAOFactory.MESSAGE, ex.getSQLException().getMessage());
		}
	}

	public void testAddSurveyDataException() throws Exception {
		try {
			surveyDAO.addCompletedSurvey(null, Calendar.getInstance().getTime());
			fail("DBException should have been thrown");
		} catch (DBException ex) {
			assertEquals(EvilDAOFactory.MESSAGE, ex.getSQLException().getMessage());
		}
	}

	public void testIsCompleted() throws Exception {
		try {
			surveyDAO.isSurveyCompleted(0L);
			fail("DBException should have been thrown");
		} catch (DBException ex) {
			assertEquals(EvilDAOFactory.MESSAGE, ex.getSQLException().getMessage());
		}
	}
}
