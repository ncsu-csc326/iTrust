package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.HospitalBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.HospitalsDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.HospitalBeanValidator;

/**
 * Handles updating the list of hospitals Used by hospitalListing.jsp
 * 
 * 
 */
public class UpdateHospitalListAction {
	private HospitalsDAO hospDAO;
	private long performerID;

	/**
	 * Set up
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param performerID The MID of the person updating the hospitals.
	 */
	public UpdateHospitalListAction(DAOFactory factory, long performerID) {
		this.hospDAO = factory.getHospitalsDAO();
		this.performerID = performerID;
	}
	
	public void logViewHospitalListing() {
		TransactionLogger.getInstance().logTransaction(TransactionType.HOSPITAL_LISTING_VIEW, performerID, null, "");
	}

	/**
	 * Adds a hospital using the HospitalBean passed as a parameter
	 * 
	 * @param hosp the new hospital listing
	 * @return Status message
	 * @throws FormValidationException
	 */
	public String addHospital(HospitalBean hosp) throws FormValidationException {
		new HospitalBeanValidator().validate(hosp);
		
		try {
			if (hospDAO.addHospital(hosp)) {
				TransactionLogger.getInstance().logTransaction(TransactionType.HOSPITAL_LISTING_ADD, performerID, null, hosp.getHospitalID());
				return "Success: " + hosp.getHospitalID() + " - " + hosp.getHospitalName() + " added";
			} else {
				return "The database has become corrupt. Please contact the system administrator for assistance.";
			}
		} catch (DBException e) {
			return e.getMessage();
		} catch (ITrustException e) {
			return e.getMessage();
		}
	}

	/**
	 * Updates a hospital (based on the hospital id) using new information from the HospitalBean passed as a
	 * parameter
	 * 
	 * @param hosp the new hospital information with the same hospital id
	 * @return Status message
	 * @throws FormValidationException
	 */
	public String updateInformation(HospitalBean hosp) throws FormValidationException {
		new HospitalBeanValidator().validate(hosp);
		
		try {
			int rows = 0;
			
			if (0 == (rows = updateHospital(hosp))) {
				return "Error: Hospital not found.";
			} else {
				TransactionLogger.getInstance().logTransaction(TransactionType.HOSPITAL_LISTING_EDIT, performerID, null, "" + hosp.getHospitalID());
				return "Success: " + rows + " row(s) updated";
			}
		} catch (DBException e) {	
			return e.getMessage();
		}
	}

	/**
	 * Updates hospital
	 * 
	 * @param hosp new information
	 * @return id for the updated hospital
	 * @throws DBException
	 */
	private int updateHospital(HospitalBean hosp) throws DBException {
		return hospDAO.updateHospital(hosp);
	}
}
