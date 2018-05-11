package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * ViewPatientAction is just a class to help the edit demographics page get all the users that should
 * be displayed on the page.
 */
public class ViewPatientAction extends PatientBaseAction {

	/**patientDAO is the patientDAO that retrieves the users from the database*/
	private PatientDAO patientDAO;
	/**loggedInMID is the patient that is logged in.*/
	private long loggedInMID;
	/**Viewer is the patient bean for the person that is logged in*/ 
	private PatientBean viewer;

	/**
	 * ViewPateintAction is the constructor for this action class. It simply initializes the
	 * instance variables.
	 * @param factory The facory used to get the patientDAO.
	 * @param loggedInMID The MID of the logged in user.
	 * @param pidString The user ID patient we are viewing.
	 * @throws ITrustException When there is a bad user.
	 */
	public ViewPatientAction(DAOFactory factory, long loggedInMID, String pidString)
			throws ITrustException {
		super(factory, pidString);
		this.patientDAO = factory.getPatientDAO();
		this.loggedInMID = loggedInMID;
		this.viewer = patientDAO.getPatient(loggedInMID);
	}
	
	/**
	 * getViewablePateints returns a list of patient beans that should be viewed by this
	 * patient.
	 * @return The list of this users dependents and this user.
	 * @throws ITrustException When there is a bad user.
	 */
	public List<PatientBean> getViewablePatients() throws ITrustException{
		List<PatientBean> result;
		try {
			result = patientDAO.getDependents(loggedInMID);
			result.add(0, viewer);
		} catch (DBException e) {
			throw new ITrustException("Invalid User");
		}
		return result;
	}
}
