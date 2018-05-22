package edu.ncsu.csc.itrust.controller.labProcedure;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure.LabProcedureStatus;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureData;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "lab_procedure_controller")
@SessionScoped
public class LabProcedureController extends iTrustController {

	private static final String INVALID_LAB_PROCEDURE = "Invalid lab procedure";
	private LabProcedureData labProcedureData;

	public LabProcedureController() {
		super();
		try {
			labProcedureData = new LabProcedureMySQL();
		} catch (DBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public LabProcedureController(DataSource ds) {
		super();
		labProcedureData = new LabProcedureMySQL(ds);
	}

	/**
	 * Setter injection for lab procedure data. ONLY use for unit testing
	 * purposes.
	 */
	public void setLabProcedureData(LabProcedureData data) {
		this.labProcedureData = data;
	}

	/**
	 * Adds a lab procedure.
	 * 
	 * @param procedure
	 *            The lab procedure to add
	 */
	public void add(LabProcedure procedure) {
		boolean successfullyAdded = false;
		// Only the HCP role can add LabProcedures
		String role = getSessionUtils().getSessionUserRole();
		if (role == null || !role.equals("hcp")) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid user authentication",
					"Only HCPs can add Lab Procedures", null);
			return;
		}
		try {
			procedure.setHcpMID(Long.parseLong(getSessionUtils().getSessionLoggedInMID()));
			successfullyAdded = labProcedureData.add(procedure);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_LAB_PROCEDURE, e.getExtendedMessage(), null);
		} catch (NumberFormatException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't add lab procedure", "Couldn't parse HCP MID",
					null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_LAB_PROCEDURE, INVALID_LAB_PROCEDURE, null);
		}
		if (successfullyAdded) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, "Lab Procedure Successfully Updated",
					"Lab Procedure Successfully Updated", null);
			if (procedure != null) {
				logTransaction(TransactionType.LAB_RESULTS_CREATE, procedure.getLabProcedureCode());
				Long ovid = getSessionUtils().getCurrentOfficeVisitId();
				logTransaction(TransactionType.LAB_PROCEDURE_ADD, ovid == null ? null : ovid.toString());
			}
		}
	}

	/**
	 * Updates a lab procedure. Prints FacesContext info message when
	 * successfully updated, error message when the update fails.
	 * 
	 * @param procedure
	 *            The lab procedure to update
	 */
	public void edit(LabProcedure procedure) {
		boolean successfullyUpdated = false;

		try {
			successfullyUpdated = labProcedureData.update(procedure);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_LAB_PROCEDURE, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_LAB_PROCEDURE, INVALID_LAB_PROCEDURE, null);
		}
		if (successfullyUpdated) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, "Lab Procedure Successfully Updated",
					"Lab Procedure Successfully Updated", null);
			Long ovid = getSessionUtils().getCurrentOfficeVisitId();
			logTransaction(TransactionType.LAB_PROCEDURE_EDIT, ovid == null ? null : ovid.toString());
		}
	}

	/**
	 * Removes lab procedure from the system. Prints FacesContext info message
	 * when successfully removed, error message when the removal fails.
	 * 
	 * @param labProcedureID
	 *            The ID of the lab procedure to remove.
	 */
	public void remove(String labProcedureID) {
		boolean successfullyRemoved = false;

		long id = -1;
		if (labProcedureID != null) {
			try {
				id = Long.parseLong(labProcedureID);
				successfullyRemoved = labProcedureData.removeLabProcedure(id);
			} catch (NumberFormatException e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Could not remove lab procedure",
						"Failed to parse lab procedure ID", null);
			} catch (Exception e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Could not remove lab procedure",
						"Could not remove lab procedure", null);
			}
		}
		if (successfullyRemoved) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, "Lab procedure successfully removed",
					"Lab procedure successfully removed", null);
			Long ovid = getSessionUtils().getCurrentOfficeVisitId();
			logTransaction(TransactionType.LAB_PROCEDURE_REMOVE, ovid == null ? null : ovid.toString());
		}
	}

	public LabProcedure getLabProcedureByID(String labProcedureID) {
		long id = -1;
		try {
			id = Long.parseLong(labProcedureID);
			return labProcedureData.getByID(id);
		} catch (NumberFormatException ne) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Lab Procedure",
					"Unable to Retrieve Lab Procedure", null);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Lab Procedure",
					"Unable to Retrieve Lab Procedure", null);
		}
		return null;
	}

	/**
	 * Returns lab procedures with given office visit ID, or empty list if no
	 * such procedures exist.
	 */
	public List<LabProcedure> getLabProceduresByOfficeVisit(String officeVisitID) throws DBException {
		List<LabProcedure> procedures = Collections.emptyList();
		long mid = -1;
		if ((officeVisitID != null) && ValidationFormat.NPMID.getRegex().matcher(officeVisitID).matches()) {
			mid = Long.parseLong(officeVisitID);
			try {
				procedures = labProcedureData.getLabProceduresByOfficeVisit(mid).stream().sorted((o1, o2) -> {
					return (o1.getPriority() == o2.getPriority()) ? o1.getUpdatedDate().compareTo(o2.getUpdatedDate())
							: o1.getPriority() - o2.getPriority();
				}).collect(Collectors.toList());
			} catch (Exception e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Lab Procedures",
						"Unable to Retrieve Lab Procedures", null);
			}
		}
		return procedures;
	}

	public List<LabProcedure> getLabProceduresByLabTechnician(String technicianID) throws DBException {
		List<LabProcedure> procedures = Collections.emptyList();
		long mid = -1;
		if ((technicianID != null) && ValidationFormat.NPMID.getRegex().matcher(technicianID).matches()) {
			mid = Long.parseLong(technicianID);
			try {
				procedures = labProcedureData.getLabProceduresForLabTechnician(mid).stream().sorted((o1, o2) -> {
					return (o1.getPriority() == o2.getPriority()) ? o1.getUpdatedDate().compareTo(o2.getUpdatedDate())
							: o1.getPriority() - o2.getPriority();
				}).collect(Collectors.toList());
			} catch (Exception e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Lab Procedures",
						"Unable to Retrieve Lab Procedures", null);
			}
		}
		return procedures;
	}

	public List<LabProcedure> getPendingLabProceduresByTechnician(String technicianID) throws DBException {
		return getLabProceduresByLabTechnician(technicianID).stream().filter((o) -> {
			return o.getStatus().name().equals(LabProcedureStatus.PENDING.name());
		}).collect(Collectors.toList());
	}

	public List<LabProcedure> getInTransitLabProceduresByTechnician(String technicianID) throws DBException {
		return getLabProceduresByLabTechnician(technicianID).stream().filter((o) -> {
			return o.getStatus().name().equals(LabProcedureStatus.IN_TRANSIT.name());
		}).collect(Collectors.toList());
	}

	public List<LabProcedure> getReceivedLabProceduresByTechnician(String technicianID) throws DBException {
		return getReceivedLabProceduresStreamByTechnician(technicianID).collect(Collectors.toList());
	}

	public Stream<LabProcedure> getReceivedLabProceduresStreamByTechnician(String technicianID) throws DBException {
		return getLabProceduresByLabTechnician(technicianID).stream().filter((o) -> {
			return o.getStatus().name().equals(LabProcedureStatus.RECEIVED.name());
		});
	}

	public List<LabProcedure> getTestingLabProceduresByTechnician(String technicianID) throws DBException {
		return getTestingLabProceduresStreamsByTechnician(technicianID).collect(Collectors.toList());
	}

	public Stream<LabProcedure> getTestingLabProceduresStreamsByTechnician(String technicianID) throws DBException {
		return getLabProceduresByLabTechnician(technicianID).stream().filter((o) -> {
			return o.getStatus().name().equals(LabProcedureStatus.TESTING.name());
		});
	}

	public List<LabProcedure> getCompletedLabProceduresByTechnician(String technicianID) throws DBException {
		return getLabProceduresByLabTechnician(technicianID).stream().filter((o) -> {
			return o.getStatus().name().equals(LabProcedureStatus.COMPLETED.name());
		}).collect(Collectors.toList());
	}

	public List<LabProcedure> getCompletedLabProceduresByOfficeVisit(String officeVisitID) throws DBException {
		return getLabProceduresByOfficeVisit(officeVisitID).stream().filter((o) -> {
			return o.getStatus().name().equals(LabProcedureStatus.COMPLETED.name());
		}).collect(Collectors.toList());
	}

	public List<LabProcedure> getTestingAndReceivedLabProceduresByTechnician(String technicianID) throws DBException {
	    // TODO
		Stream<LabProcedure> testing = getTestingLabProceduresStreamsByTechnician(technicianID);
		Stream<LabProcedure> received = getReceivedLabProceduresStreamByTechnician(technicianID);
		return Stream.concat(testing, received).collect(Collectors.toList());
	}

	/**
	 * Returns all lab procedures for the given office visit that are not in
	 * completed state.
	 * 
	 * @param officeVisitID
	 *            ID of the office visit to query by
	 * @return Lab procedures with the given office visit ID that aren't in
	 *         completed state
	 * @throws DBException
	 */
	public List<LabProcedure> getNonCompletedLabProceduresByOfficeVisit(String officeVisitID) throws DBException {
		return getLabProceduresByOfficeVisit(officeVisitID).stream().filter((o) -> {
			return !o.getStatus().name().equals(LabProcedureStatus.COMPLETED.name());
		}).collect(Collectors.toList());
	}

	public void setLabProcedureToReceivedStatus(String labProcedureID) {
		boolean successfullyUpdated = false;
		try {
			Long id = Long.parseLong(labProcedureID);
			LabProcedure proc = labProcedureData.getByID(id);
			proc.setStatus(LabProcedureStatus.RECEIVED.getID());
			successfullyUpdated = labProcedureData.update(proc);
			updateStatusForReceivedList(proc.getLabTechnicianID().toString());
			if (successfullyUpdated) {
				logTransaction(TransactionType.LAB_RESULTS_RECEIVED, proc.getLabProcedureCode());
				printFacesMessage(FacesMessage.SEVERITY_INFO, "Lab Procedure Successfully Updated to Received Status",
						"Lab Procedure Successfully Updated to Received Status", null);
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, INVALID_LAB_PROCEDURE, e.getExtendedMessage(), null);
		} catch (Exception e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, GENERIC_ERROR, GENERIC_ERROR, null);
		}
	}

	/**
	 * 
	 * @param technicianID
	 * @throws DBException
	 */
	public void updateStatusForReceivedList(String technicianID) throws DBException {
		List<LabProcedure> received = getReceivedLabProceduresByTechnician(technicianID);
		List<LabProcedure> testing = getTestingLabProceduresByTechnician(technicianID);

		if (testing.size() == 0 && received.size() > 0) {
			received.get(0).setStatus(LabProcedureStatus.TESTING.getID());
			edit(received.get(0));
		}
	}
	
	public void updateReceivedQueue(){
	    
	}

	/**
	 * Updates the status of the given lab procedure to pending and sets the
	 * next received lab procedure to testing status.
	 * 
	 * @param labProcedure
	 *            The lab procedure to update to pending
	 * @throws DBException
	 */
	public void recordResults(LabProcedure labProcedure) throws DBException {
		labProcedure.setStatus(LabProcedureStatus.PENDING.getID());
		edit(labProcedure);
		updateStatusForReceivedList(labProcedure.getLabTechnicianID().toString());
	}

	/**
	 * Logs that each lab procedure for the given office visit was viewed by the
	 * logged in MID.
	 */
	public void logViewLabProcedure() {
		try {
			Long ovID = getSessionUtils().getCurrentOfficeVisitId();
			if (ovID == null) { // Only log if an office visit has been selected
				return;
			}
			List<LabProcedure> procsByOfficeVisit = getLabProceduresByOfficeVisit(ovID.toString());
			for (LabProcedure proc : procsByOfficeVisit) {
				logTransaction(TransactionType.LAB_RESULTS_VIEW, proc.getLabProcedureCode());
			}
		} catch (DBException e) {
		}
	}

	public void logLabTechnicianViewLabProcedureQueue() {
		logTransaction(TransactionType.LAB_RESULTS_VIEW_QUEUE, getSessionUtils().getSessionLoggedInMIDLong(), null,
				null);
	}
}
