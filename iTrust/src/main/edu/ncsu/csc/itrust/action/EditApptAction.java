package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.ApptBeanValidator;

/**
 * EditApptAction
 */
public class EditApptAction extends ApptAction {
	
	private ApptBeanValidator validator = new ApptBeanValidator();
	private long loggedInMID;
	private long originalPatient;
	private long originalApptID;
	
	/**
	 * EditApptAction
	 * @param factory factory
	 * @param loggedInMID loggedInMID
	 */
	public EditApptAction(DAOFactory factory, long loggedInMID) {
		this(factory, loggedInMID, 0L, 0L);
	}
	
	public void setOriginalApptID(long id) {
		this.originalApptID = id;
	}
	
	public void setOriginalPatient(long patient) {
		this.originalPatient = patient;
	}
	
	public void logViewAction() {
		TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_VIEW, loggedInMID, originalPatient, "");
	}
	
	/**
	 * EditApptAction
	 * @param factory factory
	 * @param loggedInMID loggedInMID
	 */
	public EditApptAction(DAOFactory factory, long loggedInMID, long originalPatient, long originalApptID) {
		super(factory, loggedInMID);
		this.loggedInMID = loggedInMID;
		this.originalPatient = originalPatient;
		this.originalApptID = originalApptID;
	}
	
	/**
	 * Retrieves an appointment from the database, given its ID.
	 * Returns null if there is no match, or multiple matches.
	 * 
	 * @param apptID apptID
	 * @return ApptBean with matching ID
	 * @throws DBException 
	 * @throws SQLException 
	 */
	public ApptBean getAppt(int apptID) throws DBException, SQLException {
		try {
			List<ApptBean> apptBeans = apptDAO.getAppt(apptID);
			if (apptBeans.size() == 1){
				return apptBeans.get(0);
			}
			return null;
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * Updates an existing appointment
	 * 
	 * @param appt Appointment Bean containing the updated information
	 * @param ignoreConflicts ignoreConflicts
	 * @return Message to be displayed
	 * @throws FormValidationException
	 * @throws SQLException 
	 * @throws DBException 
	 */
	public String editAppt(ApptBean appt, boolean ignoreConflicts) throws FormValidationException, SQLException, DBException {
		validator.validate(appt);
		if(appt.getDate().before(new Timestamp(System.currentTimeMillis())))
			return "The scheduled date of this appointment ("+appt.getDate()+") has already passed.";
		
		if(!ignoreConflicts){
			if(getConflictsForAppt(appt.getHcp(), appt).size()>0){
				return "Warning! This appointment conflicts with other appointments";
			}
		}
		
		try {
			apptDAO.editAppt(appt);
			TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_EDIT, loggedInMID, originalPatient, ""+appt.getApptID());
			if(ignoreConflicts){
				TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_CONFLICT_OVERRIDE, loggedInMID, originalPatient, "");
			}
			return "Success: Appointment changed";
		} catch (DBException e) {
			
			return e.getMessage();
		} 
	}
	
	
	
	/**
	 * Removes an existing appointment
	 * 
	 * @param appt Appointment Bean containing the ID of the appointment to be removed.
	 * @return Message to be displayed
	 * @throws DBException 
	 */
	public String removeAppt(ApptBean appt) throws DBException, SQLException {
		try {
			apptDAO.removeAppt(appt);
			TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_REMOVE, loggedInMID, originalPatient, ""+originalApptID);
			return "Success: Appointment removed";
		} catch (SQLException e) {
			
			return e.getMessage();
		}
	}

}
