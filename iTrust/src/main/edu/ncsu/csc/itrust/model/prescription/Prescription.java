package edu.ncsu.csc.itrust.model.prescription;

import java.time.LocalDate;

import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;

public class Prescription {

	private long patientMID;
	private MedicationBean drugCode;
	private LocalDate startDate;
	private LocalDate endDate;
	private long officeVisitId;
	private long hcpMID;
	private long id;
	private String instructions;
	private long dosage;

	public Prescription() {
		drugCode = new MedicationBean();
	}

	public long getPatientMID() {
		return patientMID;
	}

	public void setPatientMID(long patientMID) {
		this.patientMID = patientMID;
	}

	public long getHcpMID() {
		return hcpMID;
	}

	public void setHcpMID(long hcpMID) {
		this.hcpMID = hcpMID;
	}

	public MedicationBean getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(MedicationBean drugCode) {
		this.drugCode = drugCode;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public long getOfficeVisitId() {
		return officeVisitId;
	}

	public void setOfficeVisitId(long officeVisitId) {
		this.officeVisitId = officeVisitId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return getDrugCode().getNDCode();
	}

	public String getName() {
		return getDrugCode().getDescription();
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public long getDosage() {
		return dosage;
	}

	public void setDosage(long dosage) {
		this.dosage = dosage;
	}
}
