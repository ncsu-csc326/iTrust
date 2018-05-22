package edu.ncsu.csc.itrust.controller.prescription;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ndcode.NDCCode;
import edu.ncsu.csc.itrust.model.ndcode.NDCCodeMySQL;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.prescription.Prescription;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "prescription_form")
@ViewScoped
public class PrescriptionForm {

	private Prescription prescription;
	private PrescriptionController controller;
	private SessionUtils sessionUtils;
	private NDCCodeMySQL ndcData;

	public PrescriptionForm() {
	    this(null, null, SessionUtils.getInstance(), null);
	}

	public PrescriptionForm(PrescriptionController pc, NDCCodeMySQL nData, SessionUtils sessionUtils, DataSource ds) {
	    this.sessionUtils = (sessionUtils == null) ? SessionUtils.getInstance() : sessionUtils;
		try {
			if (ds == null) {
				ndcData = (nData == null) ? new NDCCodeMySQL() : nData;
				controller = (pc == null) ? new PrescriptionController() : pc;
			} else {
				ndcData = (nData == null) ? new NDCCodeMySQL(ds) : nData;
				controller = (pc == null) ? new PrescriptionController(ds) : pc;
			}
			clearFields();
			
		} catch (Exception e) {
			this.sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Prescription Controller Error",
					"Prescription Procedure Controller Error", null);
		}
	}
	
	public void add(){
		controller.add(prescription);
		clearFields();
	}
	
	public void edit(){
		controller.edit(prescription);
		clearFields();
	}
	
	public void remove(String prescriptionID){
		controller.remove( Long.parseLong(prescriptionID) );
		clearFields();
	}
	
	public List<Prescription> getPrescriptionsByOfficeVisit(String visitID){
		List<Prescription> prescriptions = Collections.emptyList();
		try {
			prescriptions = controller.getPrescriptionsByOfficeVisit(visitID);
		} catch (DBException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "Prescription Controller Error", "Prescription Controller Error",
					null);
		}
		return prescriptions;
	}
	
	public List<Prescription> getPrescriptionsForCurrentPatient() {
		return controller.getPrescriptionsForCurrentPatient();
	}

	public List<Prescription> getPrescriptionsByPatientID(String patientID) {
		return controller.getPrescriptionsByPatientID(patientID);
	}

	public List<PatientBean> getListOfRepresentees() {
		return controller.getListOfRepresentees();
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}
	
	public String getCodeName(String codeString){
		return controller.getCodeName(codeString);
	}
	
	public List<NDCCode> getNDCCodes() {
		try {
			return ndcData.getAll();
		} catch (SQLException e) {
			sessionUtils.printFacesMessage(FacesMessage.SEVERITY_ERROR, "NDC Code retrival error", "NDC Code retrival error",
					null);
		}
		return Collections.emptyList();
	}
	
	public void fillInput(String prescriptionID, MedicationBean drug, long dosage, LocalDate startDate, LocalDate endDate, String instructions){
		prescription.setDrugCode(drug);
		prescription.setDosage(dosage);
		prescription.setStartDate(startDate);
		prescription.setEndDate(endDate);
		prescription.setInstructions(instructions);
		prescription.setId( Long.parseLong(prescriptionID) );
	}
	
	public void clearFields(){
		this.prescription = new Prescription();
		prescription.setPatientMID(sessionUtils.getCurrentPatientMIDLong());
		prescription.setOfficeVisitId(sessionUtils.getCurrentOfficeVisitId());
		prescription.setHcpMID(sessionUtils.getSessionLoggedInMIDLong());
		prescription.setStartDate(LocalDate.now());
	}
}
