package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.ApptBeanValidator;

public class AddApptAction extends ApptAction {
	private ApptBeanValidator validator = new ApptBeanValidator();
	private long loggedInMID;
	
	public AddApptAction(DAOFactory factory, long loggedInMID) {
		super(factory, loggedInMID);
		this.loggedInMID = loggedInMID;
	}
	
	public String addAppt(ApptBean appt, boolean ignoreConflicts) throws FormValidationException, SQLException, DBException {
		validator.validate(appt);
		if(appt.getDate().before(new Timestamp(System.currentTimeMillis()))) {
			return "The scheduled date of this Appointment ("+appt.getDate()+") has already passed.";
		}
		
		if(!ignoreConflicts){
			if(getConflictsForAppt(appt.getHcp(), appt).size()>0){
				return "Warning! This appointment conflicts with other appointments";
			}
		}
		
		try {
			apptDAO.scheduleAppt(appt);
			TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_ADD, loggedInMID, appt.getPatient(), "");
			if(ignoreConflicts){
				TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_CONFLICT_OVERRIDE, loggedInMID, appt.getPatient(), "");
			}
			return "Success: " + appt.getApptType() + " for " + appt.getDate() + " added";
		}
		catch (SQLException e) {
			
			return e.getMessage();
		} 
	}	
	
	

}
