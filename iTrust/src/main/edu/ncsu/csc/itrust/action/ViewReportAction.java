package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * 
 * Action class for ViewReport.jsp
 *
 */
public class ViewReportAction {
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	
	public ViewReportAction(DAOFactory factory, long loggedInMID, long patientMID) {
		patientDAO = factory.getPatientDAO();
		personnelDAO = factory.getPersonnelDAO();
		TransactionLogger.getInstance().logTransaction(TransactionType.COMPREHENSIVE_REPORT_VIEW, loggedInMID, patientMID, "");
	}

	/**
	 * Set up defaults
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the report.
	 */
	public ViewReportAction(DAOFactory factory, long loggedInMID) {
		this(factory, loggedInMID, 0L);
	}

	/**
	 *  Get declared HCPs list for the given patient
	 * @param pid the patient of interest
	 * @return list of declared HCPs
	 * @throws ITrustException
	 */
	public List<PersonnelBean> getDeclaredHCPs(long pid) throws ITrustException {
		return patientDAO.getDeclaredHCPs(pid);
	}
	
	/**
	 * Returns a PersonnelBean when given an MID
	 * @param mid HCP of interest
	 * @return PersonnelBean of the given HCP
	 * @throws ITrustException
	 */
	public PersonnelBean getPersonnel(long mid) throws ITrustException {
		return personnelDAO.getPersonnel(mid);
	}
	
	/**
	 * Returns a PaitentBean when given an MID
	 * @param mid patient of interest
	 * @return PatientBean of the given HCP
	 * @throws ITrustException
	 */
	public PatientBean getPatient(long mid) throws ITrustException {
		return patientDAO.getPatient(mid);
	}
}
