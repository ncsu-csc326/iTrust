package edu.ncsu.csc.itrust.controller.officeVisit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitData;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "office_visit_controller")
@SessionScoped
public class OfficeVisitController extends iTrustController {

	/**
	 * The cut off age for being considered a baby.
	 */
	private static final int PATIENT_BABY_AGE = 3;

	/**
	 * The cut off age for being considered a child.
	 */
	private static final int PATIENT_CHILD_AGE = 12;

	/**
	 * Constant for the error message to be displayed if the Office Visit is
	 * invalid.
	 */
	private static final String OFFICE_VISIT_CANNOT_BE_UPDATED = "Invalid Office Visit";

	/**
	 * Constant for the message to be displayed if the office visit was
	 * unsuccessfully updated
	 */
	private static final String OFFICE_VISIT_CANNOT_BE_CREATED = "Office Visit Cannot Be Updated";

	/**
	 * Constant for the message to be displayed if the office visit was
	 * successfully created
	 */
	private static final String OFFICE_VISIT_SUCCESSFULLY_CREATED = "Office Visit Successfully Created";

	/**
	 * Constant for the message to be displayed if the office visit was
	 * successfully updated
	 */
	private static final String OFFICE_VISIT_SUCCESSFULLY_UPDATED = "Office Visit Successfully Updated";

	private OfficeVisitData officeVisitData;
	private SessionUtils sessionUtils;

	public OfficeVisitController() throws DBException {
		officeVisitData = new OfficeVisitMySQL();
		sessionUtils = SessionUtils.getInstance();
	}

	/**
	 * For testing purposes
	 * 
	 * @param ds
	 *            DataSource
	 * @param sessionUtils
	 *            SessionUtils instance
	 */
	public OfficeVisitController(DataSource ds, SessionUtils sessionUtils) {
		officeVisitData = new OfficeVisitMySQL(ds);
		this.sessionUtils = sessionUtils;
	}

	/**
	 * For testing purposes
	 * 
	 * @param ds
	 *            DataSource
	 */
	public OfficeVisitController(DataSource ds) {
		officeVisitData = new OfficeVisitMySQL(ds);
		sessionUtils = SessionUtils.getInstance();
	}

	/**
	 * Adding office visit used in testing.
	 * 
	 * @param ov
	 *            Office visit
	 * @return true if successfully added, false if otherwise
	 */
	public boolean addReturnResult(OfficeVisit ov) {
		boolean res = false;

		try {
			res = officeVisitData.add(ov);
		} catch (Exception e) {
			// do nothing
		}
		if (res) {
			logEditBasicHealthInformation();
		}

		return res;
	}

	/**
	 * Add office visit to the database and return the generated VisitID.
	 * 
	 * @param ov
	 *            Office visit to add to the database
	 * @return VisitID generated from the database insertion, -1 if nothing was
	 *         generated
	 * @throws DBException
	 *             if error occurred in inserting office visit
	 */
	public long addReturnGeneratedId(OfficeVisit ov) {
		long generatedId = -1;

		try {
			generatedId = officeVisitData.addReturnGeneratedId(ov);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_CANNOT_BE_CREATED,
					OFFICE_VISIT_CANNOT_BE_CREATED, null);
		}

		if (generatedId >= 0) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_CREATED,
					OFFICE_VISIT_SUCCESSFULLY_CREATED, null);
			logEditBasicHealthInformation();
		}

		return generatedId;
	}

	/**
	 * Sends a FacesMessage for FacesContext to display.
	 * 
	 * @param severity
	 *            severity of the message
	 * @param summary
	 *            localized summary message text
	 * @param detail
	 *            localized detail message text
	 * @param clientId
	 *            The client identifier with which this message is associated
	 *            (if any)
	 */
	public void printFacesMessage(Severity severity, String summary, String detail, String clientId) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx == null) {
			return;
		}
		ctx.getExternalContext().getFlash().setKeepMessages(true);
		ctx.addMessage(clientId, new FacesMessage(severity, summary, detail));
	}

	public void redirectToBaseOfficeVisit() throws IOException {
		if (FacesContext.getCurrentInstance() != null) {
			NavigationController.baseOfficeVisit();
		}
	}

	public void add(OfficeVisit ov) {
		boolean res = false;

		try {
			res = officeVisitData.add(ov);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(),
					null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED,
					OFFICE_VISIT_CANNOT_BE_CREATED, null);
		}
		if (res) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_CREATED,
					OFFICE_VISIT_SUCCESSFULLY_CREATED, null);
			logEditBasicHealthInformation();
		}
	}

	/**
	 * @param pid
	 *            patient mid
	 * @return sorted list of office visit for the given patient
	 */
	public List<OfficeVisit> getOfficeVisitsForPatient(String pid) {
		List<OfficeVisit> ret = Collections.emptyList();
		long mid = -1;
		if ((pid != null) && ValidationFormat.NPMID.getRegex().matcher(pid).matches()) {
			mid = Long.parseLong(pid);
			try {
				ret = officeVisitData.getVisitsForPatient(mid).stream().sorted((o1, o2) -> {
					return o2.getDate().compareTo(o1.getDate());
				}).collect(Collectors.toList());
			} catch (Exception e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visits",
						"Unable to Retrieve Office Visits", null);
			}
		}
		return ret;
	}

	/**
	 * @return list of office visit sorted by date, empty list if no office
	 *         visit exists
	 */
	public List<OfficeVisit> getOfficeVisitsForCurrentPatient() {
		return getOfficeVisitsForPatient(sessionUtils.getCurrentPatientMID());
	}

	/**
	 * @param pid
	 *            patient mid
	 * @return list of office visits when given patient was a baby, empty list
	 *         if no office visit exists during that age range
	 */
	public List<OfficeVisit> getBabyOfficeVisitsForPatient(String pid) {
		return getOfficeVisitsForPatient(pid).stream().filter((o) -> {
			return isPatientABaby(o.getPatientMID(), o.getDate());
		}).collect(Collectors.toList());
	}

	/**
	 * @return list of office visits when current patient was a baby, empty list
	 *         if no office visit exists during that age range
	 */
	public List<OfficeVisit> getBabyOfficeVisitsForCurrentPatient() {
		return getBabyOfficeVisitsForPatient(sessionUtils.getCurrentPatientMID());
	}

	/**
	 * @param pid
	 *            patient mid
	 * @return list of office visits when given patient was a child, empty list
	 *         if no office visit exists during that age range
	 */
	public List<OfficeVisit> getChildOfficeVisitsForPatient(String pid) {
		return getOfficeVisitsForPatient(pid).stream().filter((o) -> {
			return isPatientAChild(o.getPatientMID(), o.getDate());
		}).collect(Collectors.toList());
	}

	/**
	 * @return list of office visits when current patient was a child, empty
	 *         list if no office visit exists during that age range
	 */
	public List<OfficeVisit> getChildOfficeVisitsForCurrentPatient() {
		return getChildOfficeVisitsForPatient(sessionUtils.getCurrentPatientMID());
	}

	/**
	 * @param pid
	 *            patient mid
	 * @return list of office visits when given patient was an adult, empty list
	 *         if no office visit exists during that age range
	 */
	public List<OfficeVisit> getAdultOfficeVisitsForPatient(String pid) {
		return getOfficeVisitsForPatient(pid).stream().filter((o) -> {
			return isPatientAnAdult(o.getPatientMID(), o.getDate());
		}).collect(Collectors.toList());
	}

	/**
	 * @return list of office visits when current patient was an adult, empty
	 *         list if no office visit exists during that age range
	 */
	public List<OfficeVisit> getAdultOfficeVisitsForCurrentPatient() {
		return getAdultOfficeVisitsForPatient(sessionUtils.getCurrentPatientMID());
	}

	public OfficeVisit getVisitByID(String VisitID) {
		long id = -1;
		try {
			id = Long.parseLong(VisitID);
		} catch (NumberFormatException ne) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
					"Unable to Retrieve Office Visit", null);
			return null;
		}
		try {
			return officeVisitData.getByID(id);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
					"Unable to Retrieve Office Visit", null);
			return null;
		}
	}

	/**
	 * @return Office Visit of the selected patient in the HCP session
	 */
	public OfficeVisit getSelectedVisit() {
		String visitID = sessionUtils.getRequestParameter("visitID");
		if (visitID == null || visitID.isEmpty()) {
			return null;
		}
		return getVisitByID(visitID);
	}

	/**
	 * @param patientID
	 *            Patient MID
	 * @return true if selected patient MID has at least 1 office visit, false
	 *         otherwise
	 */
	public boolean hasPatientVisited(String patientID) {
		boolean ret = false;
		if ((patientID != null) && (ValidationFormat.NPMID.getRegex().matcher(patientID).matches())) {
			if (getOfficeVisitsForPatient(patientID).size() > 0) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * @return true if patient selected in HCP session has at least 1 office
	 *         visit, false if otherwise
	 */
	public boolean CurrentPatientHasVisited() {
		return hasPatientVisited(sessionUtils.getCurrentPatientMID());
	}

	public void edit(OfficeVisit ov) {
		boolean res = false;

		try {
			res = officeVisitData.update(ov);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_UPDATED, e.getExtendedMessage(),
					null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_UPDATED,
					OFFICE_VISIT_CANNOT_BE_UPDATED, null);
		}
		if (res) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_UPDATED,
					OFFICE_VISIT_SUCCESSFULLY_UPDATED, null);
			logEditBasicHealthInformation();
		}
	}

	/**
	 * Calculate given patient's age given a day in the future.
	 * 
	 * @param patientMID
	 *            MID of the patient
	 * @param futureDate
	 *            Date in the future
	 * @return patient's age in calendar year, or -1 if error occurred
	 */
	public Long calculatePatientAge(final Long patientMID, final LocalDateTime futureDate) {
		Long ret = -1L;
		if (futureDate == null || patientMID == null) {
			return ret;
		}

		LocalDate patientDOB = getPatientDOB(patientMID);
		if (patientDOB == null) {
			return ret;
		}

		if (futureDate.toLocalDate().isBefore(patientDOB)) {
			return ret;
		}

		return ChronoUnit.YEARS.between(patientDOB, futureDate);
	}

	/**
	 * Check whether patient is under 3 years of age.
	 * 
	 * @param patientMID
	 *            MID of the patient
	 * @param officeVisitDate
	 *            date of the office visit
	 * @return true if patient is under 3 years old at the time of the office
	 *         visit, false if otherwise
	 */
	public boolean isPatientABaby(final Long patientMID, final LocalDateTime officeVisitDate) {
		Long age = calculatePatientAge(patientMID, officeVisitDate);
		return age < PATIENT_BABY_AGE && age >= 0;
	}

	/**
	 * Check whether patient is between 3 years (inclusive) and 12 years
	 * (exclusive) old.
	 * 
	 * @param patientMID
	 *            MID of the patient
	 * @param officeVisitDate
	 *            date of the office visit
	 * @return true if patient is is between 3 years (inclusive) and 12 years
	 *         (exclusive) old, false if otherwise
	 */
	public boolean isPatientAChild(final Long patientMID, final LocalDateTime officeVisitDate) {
		Long age = calculatePatientAge(patientMID, officeVisitDate);
		return age < PATIENT_CHILD_AGE && age >= PATIENT_BABY_AGE;
	}

	/**
	 * Check whether patient is between 12 years old or older.
	 * 
	 * @param patientMID
	 *            MID of the patient
	 * @param officeVisitDate
	 *            date of the office visit
	 * @return true if patient is is 12 years old or older, false if otherwise
	 */
	public boolean isPatientAnAdult(final Long patientMID, final LocalDateTime officeVisitDate) {
		Long age = calculatePatientAge(patientMID, officeVisitDate);
		return age >= PATIENT_CHILD_AGE;
	}

	/**
	 * @param patientMID
	 *            The patient's MID
	 * @return the patient's date of birth
	 */
	public LocalDate getPatientDOB(final Long patientMID) {
		return officeVisitData.getPatientDOB(patientMID);
	}

	/**
	 * Logs that the currently selected office visit has been viewed (if any
	 * office visit is selected)
	 */
	public void logViewOfficeVisit() {
		Long id = getSessionUtils().getCurrentOfficeVisitId();
		if (id != null) {
			logTransaction(TransactionType.OFFICE_VISIT_VIEW, id.toString());
			OfficeVisit ov = getVisitByID(Long.toString(id));
			long patientMID = ov.getPatientMID();
			LocalDateTime d = ov.getDate();
			logTransaction(TransactionType.VIEW_BASIC_HEALTH_METRICS, "Age: " + calculatePatientAge(patientMID, d));
		}
	}
	
	/**
	 * Logs that the current user viewed a patient's health metrics page
	 */
	public void logViewHealthMetrics(){
	    String role = sessionUtils.getSessionUserRole();
	    if ("hcp".equals(role)){
	        logTransaction(TransactionType.HCP_VIEW_BASIC_HEALTH_METRICS, "");
	    } else if ("patient".equals(role)){
	        logTransaction(TransactionType.PATIENT_VIEW_BASIC_HEALTH_METRICS, Long.parseLong(sessionUtils.getSessionLoggedInMID()), null, "");
	    }
	}
	
	public void logViewBasicHealthInformation() {
		logTransaction(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, "");
	}

	/**
	 * Editing basic health information is synonymous with editing or adding an
	 * office visit, so this method should be called whenever an OV is
	 * added/edited.
	 */
	private void logEditBasicHealthInformation() {
		logTransaction(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, "");
	}
}
