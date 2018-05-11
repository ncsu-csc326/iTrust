package edu.ncsu.csc.itrust.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.WardRoomBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.WardDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 */
public class PatientRoomAssignmentActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private PatientRoomAssignmentAction action;
	private WardDAO wardDAO;
	private PatientBean patient = new PatientBean();
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		wardDAO = new WardDAO(factory);
	}
	
	/**
	 * testassignPatient
	 * @throws Exception
	 */
	public void testassignPatient() throws Exception {
		//WardBean wd = new WardBean(1,"testSpecialty",1);
		WardRoomBean rm = new WardRoomBean(1, 1, 1, "test", "clean");
		////wardDAO.addWard(wd);
		wardDAO.removeWardRoom(rm.getRoomID());
		//wardDAO.addWardRoom(rm);
		action = new PatientRoomAssignmentAction(factory);
		action.assignPatientToRoom(rm, 1);
		assertEquals(0, wardDAO.getAllWardRoomsByWardID(0).size());
		wardDAO.removeWardRoom(rm.getRoomID());
		//TODO add assert for patient in ward,
		   //getters in dao not there yet
		//TODO add assert for patient in ward
		//TODO check logging
		patient.setMID(1L);
		action.assignPatientToRoom(rm, patient);
	}
	public void testremovePatient() throws Exception {
		//WardBean wd = new WardBean(1,"testSpecialty",1);
		WardRoomBean rm = new WardRoomBean(1, 1, 1, "test", "clean");
		////wardDAO.addWard(wd);
		wardDAO.addWardRoom(rm);
		action = new PatientRoomAssignmentAction(factory);
		action.assignPatientToRoom(rm, 1);
		//TODO test patient is added
		//TODO test room is set to occupied -- getter not available
		action.removePatientFromRoom(rm, "Reason");
		assertEquals(0, wardDAO.getAllWardRoomsByWardID(0).size());
		wardDAO.removeWardRoom(rm.getRoomID());
	    //TODO check to see if patient was removed successfully
		//TODO make sure ward still exists
		//TODO check logging
	}
	
}
