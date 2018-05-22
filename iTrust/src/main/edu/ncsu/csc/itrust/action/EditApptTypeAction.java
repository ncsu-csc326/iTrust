package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.ApptTypeBeanValidator;

public class EditApptTypeAction {
	private ApptTypeDAO apptTypeDAO;
	private long loggedInMID;
	private ApptTypeBeanValidator validator = new ApptTypeBeanValidator();
	
	public EditApptTypeAction(DAOFactory factory, long loggedInMID) {
		this.apptTypeDAO = factory.getApptTypeDAO();
		this.loggedInMID = loggedInMID;
		TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_TYPE_VIEW, loggedInMID, 0L, "");
	}
	
	public List<ApptTypeBean> getApptTypes() throws SQLException, DBException {
		return apptTypeDAO.getApptTypes();
	}
	
	public String addApptType(ApptTypeBean apptType) throws SQLException, FormValidationException, DBException {
		validator.validate(apptType);
		
		List<ApptTypeBean> list = this.getApptTypes();
		for(ApptTypeBean a : list) {
			if(a.getName().equals(apptType.getName()))
				return "Appointment Type: "+apptType.getName()+" already exists.";
		}
		
		try {
			if (apptTypeDAO.addApptType(apptType)) {
				TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_TYPE_ADD, loggedInMID, 0L, "");
				return "Success: " + apptType.getName() + " - Duration: " + apptType.getDuration() + " added";
			} else
				return "The database has become corrupt. Please contact the system administrator for assistance.";
		} catch (SQLException e) {
			
			return e.getMessage();
		} 
	}
	
	public String editApptType(ApptTypeBean apptType) throws SQLException, FormValidationException, DBException {
		validator.validate(apptType);
		
		List<ApptTypeBean> list = this.getApptTypes();
		int flag = 0;
		for(ApptTypeBean a : list) {
			if(a.getName().equals(apptType.getName())) {
				flag = 1;
				if(a.getDuration() == apptType.getDuration())
					return "Appointment Type: "+apptType.getName()+" already has a duration of "+apptType.getDuration()+" minutes.";
				break;
			}
		}
		if(flag == 0){
			return "Appointment Type: "+apptType.getName()+" you are trying to update does not exist.";
		}
		
		try {
			if (apptTypeDAO.editApptType(apptType)) {
				TransactionLogger.getInstance().logTransaction(TransactionType.APPOINTMENT_TYPE_EDIT, loggedInMID, 0L, "");
				return "Success: " + apptType.getName() + " - Duration: " + apptType.getDuration() + " updated";
			} else
				return "The database has become corrupt. Please contact the system administrator for assistance.";
		} catch (DBException e) {
			
			return e.getMessage();
		}
	}
}
