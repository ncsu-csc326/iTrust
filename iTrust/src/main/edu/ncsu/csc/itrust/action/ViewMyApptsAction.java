package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class ViewMyApptsAction extends ApptAction {
	private long loggedInMID;
	
	public ViewMyApptsAction(DAOFactory factory, long loggedInMID) {
		super(factory, loggedInMID);
		this.loggedInMID = loggedInMID;
	}
	
	public List<ApptBean> getMyAppointments() throws SQLException, DBException {
		return apptDAO.getApptsFor(loggedInMID);
	}
	
	public List<ApptBean> getAllMyAppointments() throws SQLException, DBException {
		return apptDAO.getAllApptsFor(loggedInMID);
	}
	
	/**
	 * Gets a user's appointments
	 * 
	 * @param mid the MID of the user
	 * @return a list of the user's appointments
	 * @throws SQLException
	 * @throws DBException 
	 */
	public List<ApptBean> getAppointments(long MID) throws SQLException, DBException {
		return apptDAO.getApptsFor(MID);
	}
}
