package edu.ncsu.csc.itrust.controller.labProcedure;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure.LabProcedureStatus;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeData;
import edu.ncsu.csc.itrust.model.loinccode.LOINCCodeMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "lab_procedure_form")
@ViewScoped
public class LabProcedureForm {
	private LabProcedureController controller;
	private LabProcedure labProcedure;
	private LOINCCodeData loincData;
	private SessionUtils sessionUtils;

	public LabProcedureForm() {
		this(null, null, SessionUtils.getInstance(), null);
	}

	public LabProcedureForm(LabProcedureController ovc, LOINCCodeData ldata, SessionUtils sessionUtils, DataSource ds) {
		this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		try {
			if (ds == null) {
				loincData = (ldata == null) ? new LOINCCodeMySQL() : ldata;
				controller = (ovc == null) ? new LabProcedureController() : ovc;
			} else {
				loincData = (ldata == null) ? new LOINCCodeMySQL(ds) : ldata;
				controller = (ovc == null) ? new LabProcedureController(ds) : ovc;
			}
			labProcedure = getSelectedLabProcedure();
			if (labProcedure == null) {
				labProcedure = new LabProcedure();
				Long ovid = sessionUtils.getCurrentOfficeVisitId();
				labProcedure.setOfficeVisitID(ovid);
				labProcedure.setStatus(LabProcedureStatus.IN_TRANSIT.getID());
			}
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Lab Procedure Controller Error",
					"Lab Procedure Controller Error", null);
		}
	}

	public LabProcedure getSelectedLabProcedure() {
		String id = sessionUtils.getRequestParameter("id");
		if (id == null) {
			return null;
		}

		return controller.getLabProcedureByID(id);
	}

	public void submitNewLabProcedure() {
		controller.add(labProcedure);
		try {
			NavigationController.officeVisitInfo();
		} catch (IOException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't redirect", "Couldn't redirect", null);
		}
	}

	/**
	 * Removes lab procedure with given ID. Logs the removal transaction. Prints error message if removal fails. 
	 * @param id ID of the lab procedure to remove
	 */
	public void removeLabProcedure(Long id) {
		if (id == null) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't remove lab procedure",
					"Invalid Lab Procedure ID specified", null);
			return;
		}
		LabProcedure toRemove = controller.getLabProcedureByID(id.toString());
		if(toRemove == null) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Couldn't remove lab procedure",
					"No lab procedure for that ID", null);
			return;
		}
		String code = toRemove.getLabProcedureCode();
		controller.remove(id.toString());
		controller.logTransaction(TransactionType.LAB_RESULTS_REMOVE, code);
	}

	/**
	 * Called when user clicks on the submit button in ReassignTechnician.xhtml.
	 * Takes data from form and sends to LabProcedureControllerLLoader.java for
	 * storage and validation
	 */
	public void submitReassignment() {
		controller.edit(labProcedure);
		controller.logTransaction(TransactionType.LAB_RESULTS_REASSIGN, labProcedure.getLabProcedureCode());
	}

	public void addCommentary(String labProcedureID) {
		String commentary = "Reviewed by HCP";
		if (sessionUtils.getCurrentFacesContext() != null) {
			Map<String, String> map = sessionUtils.getCurrentFacesContext().getExternalContext()
					.getRequestParameterMap();
			List<String> key = map.keySet().stream().filter(k -> {
				return k.matches("\\w+:\\w+:\\w+");
			}).collect(Collectors.toList());
			if (key.size() > 0) {
				commentary = map.get(key.get(0));
			}
		}
		LabProcedure proc = controller.getLabProcedureByID(labProcedureID);
		proc.setCommentary(commentary);
		long status = LabProcedure.LabProcedureStatus.COMPLETED.getID();
		proc.setStatus(status);
		controller.edit(proc);
		controller.logTransaction(TransactionType.LAB_RESULTS_ADD_COMMENTARY, proc.getLabProcedureCode());
	}

	public boolean isLabProcedureCreated() {
		Long labProcedureID = labProcedure.getLabProcedureID();
		return labProcedureID != null && labProcedureID > 0;
	}

	public boolean isReassignable(String idStr) {
		try {
			Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			return false;
		}

		LabProcedure proc = controller.getLabProcedureByID(idStr);

		LabProcedureStatus status = proc.getStatus();

		boolean isInTransit = status == LabProcedureStatus.IN_TRANSIT;
		boolean isReceived = status == LabProcedureStatus.RECEIVED;
		boolean result = isInTransit || isReceived;
		return result;
	}

	public boolean isRemovable(String idStr) {
		try {
			Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			return false;
		}

		LabProcedure proc = controller.getLabProcedureByID(idStr);

		LabProcedureStatus status = proc.getStatus();

		boolean isInTransit = status == LabProcedureStatus.IN_TRANSIT;
		boolean isReceived = status == LabProcedureStatus.RECEIVED;
		boolean result = isInTransit || isReceived;
		return result;
	}

	public boolean isCommentable(String idStr) {
		try {
			Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			return false;
		}

		LabProcedure proc = controller.getLabProcedureByID(idStr);
		LabProcedureStatus status = proc.getStatus();

		boolean result = status == LabProcedureStatus.PENDING;
		return result;
	}

	/**
	 * Upon recording results the lab procedure is updated to pending and
	 * results are submitted
	 */
	public void recordResults() {
		try {
			controller.recordResults(labProcedure);
		} catch (DBException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Lab Procedure Controller Error",
					"Lab Procedure Controller Error", null);
		}
		controller.logTransaction(TransactionType.LAB_RESULTS_RECORD, labProcedure.getLabProcedureCode());
	}

	public void updateToReceived(String labProcedureID) {
		controller.setLabProcedureToReceivedStatus(labProcedureID);
	}

	public LabProcedure getLabProcedure() {
		return labProcedure;
	}

	public List<LOINCCode> getLOINCCodes() {
		try {
			return loincData.getAll();
		} catch (DBException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "LOINC retrival error", "LOINC retrival error",
					null);
		}
		return Collections.emptyList();
	}
}
