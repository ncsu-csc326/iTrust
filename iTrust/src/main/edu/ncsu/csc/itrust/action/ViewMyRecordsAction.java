package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.Email;
import edu.ncsu.csc.itrust.model.old.beans.FamilyMemberBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.ReportRequestBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.FakeEmailDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.FamilyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ReportRequestDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * Handles patients viewing their own records Used by viewMyRecords.jsp
 * 
 * 
 */
public class ViewMyRecordsAction {
	/** The number of months in a year */
	public static final int MONTHS_IN_YEAR = 12;
	
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private AllergyDAO allergyDAO;
	private FamilyDAO familyDAO;
	private FakeEmailDAO emailDAO;
	private ReportRequestDAO reportRequestDAO;
	private long loggedInMID;

	/**
	 * Set up
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the records.
	 */
	public ViewMyRecordsAction(DAOFactory factory, long loggedInMID) {
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.allergyDAO = factory.getAllergyDAO();
		this.familyDAO = factory.getFamilyDAO();
		this.emailDAO = factory.getFakeEmailDAO();
		this.reportRequestDAO = factory.getReportRequestDAO();
		this.loggedInMID = loggedInMID;
	}
	

	/**
	 * Takes the patient's representee as a param and returns it as a long if the patient represents the input
	 * param
	 * 
	 * @param input
	 *            the patient's representee mid
	 * @return representee's mid as a long
	 * @throws ITrustException
	 */
	public long representPatient(String input) throws ITrustException {

		try {
			long reppeeMID = Long.valueOf(input);
			if (patientDAO.represents(loggedInMID, reppeeMID)) {
				loggedInMID = reppeeMID;
				return reppeeMID;
			} else
				throw new ITrustException("You do not represent patient " + reppeeMID);
		} catch (NumberFormatException e) {
			throw new ITrustException("MID is not a number");
		}
	}

	/**
	 * Returns a PatientBean for the currently logged in patient
	 * 
	 * @return PatientBean for the currently logged in patient
	 * @throws ITrustException
	 */
	public PatientBean getPatient() throws ITrustException {
		return patientDAO.getPatient(loggedInMID);
	}
	
	/**
	 * Returns a PatientBean for the specified MID
	 * @param mid id of the requested bean
	 * @return PatientBean for the specified MID
	 * @throws ITrustException
	 */
	public PatientBean getPatient(long mid) throws ITrustException {
		return patientDAO.getPatient(mid);
	}

	/**
	 * Returns a PersonnelBean for the requested MID
	 * @param mid id of the requested bean
	 * @return a PersonnelBean for the requested MID
	 * @throws ITrustException
	 */
	public PersonnelBean getPersonnel(long mid) throws ITrustException {
		return personnelDAO.getPersonnel(mid);
	}

	/**
	 * Returns a PatientBean for the currently logged in patient
	 * 
	 * @return PatientBean for the currently logged in patient
	 * @throws ITrustException
	 */
	public List<Email> getEmailHistory() throws ITrustException {
		TransactionLogger.getInstance().logTransaction(TransactionType.EMAIL_HISTORY_VIEW, loggedInMID, (long)0, "");
		return emailDAO.getEmailsByPerson(getPatient().getEmail());
	}

	/**
	 * Returns a list of AllergyBeans for the currently logged in patient
	 * 
	 * @return a list of AllergyBeans for the currently logged in patient
	 * @throws ITrustException
	 */
	public List<AllergyBean> getAllergies() throws ITrustException {
		return allergyDAO.getAllergies(loggedInMID);
	}

	/**
	 * Returns a list of Parents, Siblings, and Children of the currently logged in patient
	 * 
	 * @return list of FamilyMemberBeans
	 */
	public List<FamilyMemberBean> getFamily() throws ITrustException {
		List<FamilyMemberBean> fam = new ArrayList<FamilyMemberBean>();
		List<FamilyMemberBean> parents = null;
		try {
			parents = familyDAO.getParents(loggedInMID);
			fam.addAll(parents);
			fam.addAll(familyDAO.getSiblings(loggedInMID));
			fam.addAll(familyDAO.getChildren(loggedInMID));
		} catch (DBException e) {
			throw new ITrustException(e.getMessage());
		}
		
		if(parents != null) {
			List<FamilyMemberBean> grandparents = new ArrayList<FamilyMemberBean>();
			for(FamilyMemberBean parent : parents) {
				try {
					grandparents.addAll(familyDAO.getParents(parent.getMid()));
				} catch (DBException e) {
					throw new ITrustException(e.getMessage());
				}
			}
			
			fam.addAll(grandparents);
			
			for(FamilyMemberBean gp : grandparents) {
				gp.setRelation("Grandparent");
			}
		}
		return fam;
	}
	
	/**
	 * Returns a list of Parents, Siblings, and Grand Parents of the currently logged in patient
	 * 
	 * @return list of FamilyMemberBeans
	 */
	public List<FamilyMemberBean> getFamilyHistory() throws ITrustException {
		List<FamilyMemberBean> fam = new ArrayList<FamilyMemberBean>();
		List<FamilyMemberBean> parents = null;
		try {
			parents = familyDAO.getParents(loggedInMID);
			fam.addAll(parents);
			fam.addAll(familyDAO.getSiblings(loggedInMID));
		} catch (DBException e) {
			throw new ITrustException(e.getMessage());
		}
		
		if(parents != null) {
			List<FamilyMemberBean> grandparents = new ArrayList<FamilyMemberBean>();
			for(FamilyMemberBean parent : parents) {
				try {
					grandparents.addAll(familyDAO.getParents(parent.getMid()));
				} catch (DBException e) {
					throw new ITrustException(e.getMessage());
				}
			}
			
			fam.addAll(grandparents);
			
			for(FamilyMemberBean gp : grandparents) {
				gp.setRelation("Grandparent");
			}
		}
		return fam;
	}

	
	/**
	 * Returns a list of PatientBeans of all patients the currently logged in patient represents
	 * 
	 * @return a list of PatientBeans of all patients the currently logged in patient represents
	 * @throws ITrustException
	 */
	public List<PatientBean> getRepresented() throws ITrustException {
		return patientDAO.getRepresented(loggedInMID);
	}

	/**
	 * Returns a list of PatientBeans of all patients the currently logged in patient represents
	 * 
	 * @return a list of PatientBeans of all patients the currently logged in patient represents
	 * @throws ITrustException
	 */
	public List<PatientBean> getRepresenting() throws ITrustException {
		return patientDAO.getRepresenting(loggedInMID);
	}


	/**
	 * Returns all the report requests for the logged in patient
	 * @return the report requests for the logged in patient
	 * @throws ITrustException
	 */
	public List<ReportRequestBean> getReportRequests() throws ITrustException {
		return reportRequestDAO.getAllReportRequestsForPatient(loggedInMID);
	}

	/**
	 * Get patient's (logged in user's) age in months by taking the date of viewing the patient's records
	 * and comparing it with the patient's date of birth. 
	 * 
	 * @param viewDate The date of the patient's records are being viewed
	 * @return the patient's age in months
	 * @throws DBException
	 */
	public int getPatientAgeInMonths(Calendar viewDate) throws DBException {
		//Create int for patient's age in months
		int ageInMonths = 0;
		
		//Get the patient's birthdate
		Calendar birthDate = Calendar.getInstance();
		PatientBean patient = patientDAO.getPatient(loggedInMID);
		birthDate.setTime(patient.getDateOfBirth());
		
		//Split the patient's birthdate into day, month, and year
		int birthDay = birthDate.get(Calendar.DAY_OF_MONTH);
		int birthMonth = birthDate.get(Calendar.MONTH) + 1;
		int birthYear = birthDate.get(Calendar.YEAR);
		//Split the office visit date into day month and year
		int visitDay = viewDate.get(Calendar.DAY_OF_MONTH);
		int visitMonth = viewDate.get(Calendar.MONTH) + 1;
		int visitYear = viewDate.get(Calendar.YEAR);
		
		//Calculate the year, month, and day differences
		int yearDiff = visitYear - birthYear;
		int monthDiff = visitMonth - birthMonth;
		int dayDiff = visitDay - birthDay;
		
		//Get the patient's age in months by multiplying the year difference by 12
		//and adding the month difference
		ageInMonths = yearDiff * MONTHS_IN_YEAR + monthDiff;
		
		//If the day difference is negative, subtract a month from the age
		if (dayDiff < 0) {
			ageInMonths -= 1;
		}
		
		//Return the age in months
		return ageInMonths;
	}
	
	public void logViewMedicalRecords(Long mid, Long secondary) {
		TransactionLogger.getInstance().logTransaction(TransactionType.MEDICAL_RECORD_VIEW, mid, secondary, "");
	}
}
