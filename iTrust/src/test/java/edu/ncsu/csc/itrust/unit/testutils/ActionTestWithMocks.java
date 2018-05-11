package edu.ncsu.csc.itrust.unit.testutils;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;

import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AccessDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.FakeEmailDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.FamilyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.HospitalsDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.MessageDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.NDCodesDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ReportRequestDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO;

/**
 * This class is used to create some of the basic mock objects for iTrust. This
 * keeps one control for all unit tests, then resets it for each usage.
 * 
 * To use this class:
 * <ol>
 * <li>Extend this class instead of TestCase</li>
 * <li>Run initMocks in the setUp method</li>
 * <li>Use the mock objects as you wish. You don't need to worry about
 * factory.getDAO-type methods - those are set up to be expected a call any
 * number of times</li>
 * </ol>
 * 
 * By default, control is set up to a "nice" control.
 * 
 * Yes, everything in this class is protected static - which is not typically
 * desirable. However, this is a special test utility that takes advantage of
 * re-using mocks. Mock objects are created once and then reset every time
 * initMocks is used. This has a HUGE performance advantage as creating all of
 * these new Mocks takes 500ms, but resetting them takes 5ms.
 * 
 * Meneely
 * 
 */
abstract public class ActionTestWithMocks extends TestCase {

	protected static IMocksControl control;
	protected static DAOFactory factory;
	protected static AccessDAO accessDAO;
	protected static AllergyDAO allergyDAO;
	protected static AuthDAO authDAO;
	protected static FakeEmailDAO emailDAO;
	protected static FamilyDAO familyDAO;
	protected static HospitalsDAO hospitalDAO;
	protected static MessageDAO messageDAO;
	protected static NDCodesDAO ndDAO;
	protected static PatientDAO patientDAO;
	protected static PersonnelDAO personnelDAO;
	protected static ReportRequestDAO reportRequestDAO;
	protected static TransactionDAO transDAO;

	protected static void initMocks() {
		if (control == null)
			control = EasyMock.createNiceControl();
		else
			control.reset();
		createMocks();
		createFactoryExpectations();
	}

	private static void createMocks() {
		factory = control.createMock(DAOFactory.class);
		accessDAO = control.createMock(AccessDAO.class);
		allergyDAO = control.createMock(AllergyDAO.class);
		authDAO = control.createMock(AuthDAO.class);
		emailDAO = control.createMock(FakeEmailDAO.class);
		familyDAO = control.createMock(FamilyDAO.class);
		hospitalDAO = control.createMock(HospitalsDAO.class);
		messageDAO = control.createMock(MessageDAO.class);
		ndDAO = control.createMock(NDCodesDAO.class);
		patientDAO = control.createMock(PatientDAO.class);
		personnelDAO = control.createMock(PersonnelDAO.class);
		reportRequestDAO = control.createMock(ReportRequestDAO.class);
		transDAO = control.createMock(TransactionDAO.class);
	}

	private static void createFactoryExpectations() {
		expect(factory.getAccessDAO()).andReturn(accessDAO).anyTimes();
		expect(factory.getAllergyDAO()).andReturn(allergyDAO).anyTimes();
		expect(factory.getAuthDAO()).andReturn(authDAO).anyTimes();
		expect(factory.getFakeEmailDAO()).andReturn(emailDAO).anyTimes();
		expect(factory.getFamilyDAO()).andReturn(familyDAO).anyTimes();
		expect(factory.getHospitalsDAO()).andReturn(hospitalDAO).anyTimes();
		expect(factory.getMessageDAO()).andReturn(messageDAO).anyTimes();
		expect(factory.getNDCodesDAO()).andReturn(ndDAO).anyTimes();
		expect(factory.getPatientDAO()).andReturn(patientDAO).anyTimes();
		expect(factory.getPersonnelDAO()).andReturn(personnelDAO).anyTimes();
		expect(factory.getReportRequestDAO()).andReturn(reportRequestDAO).anyTimes();
		expect(factory.getTransactionDAO()).andReturn(transDAO).anyTimes();
	}
}
