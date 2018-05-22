package edu.ncsu.csc.itrust.model.labProcedure;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure.LabProcedureStatus;

public class LabProcedureValidator extends POJOValidator<LabProcedure> {

	private DataSource ds;

	public LabProcedureValidator() {

	}

	public LabProcedureValidator(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void validate(LabProcedure proc) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		// Commentary
		errorList.addIfNotNull(checkFormat("Commentary", proc.getCommentary(), 
				ValidationFormat.COMMENTS, true));
		
		// Confidence interval lower
		Integer confidenceLower = proc.getConfidenceIntervalLower();
		if(confidenceLower != null && (confidenceLower < 0 || confidenceLower > 100)) {
			errorList.addIfNotNull("Confidence Interval Lower: Confidence interval lower is invalid");
		}
		
		// Confidence interval upper
		Integer confidenceUpper = proc.getConfidenceIntervalUpper();
		if(confidenceUpper != null && (confidenceUpper < 0 || confidenceUpper > 100)) {
			errorList.addIfNotNull("Confidence Interval Upper: Confidence interval upper is invalid");
		}
		
		if(confidenceLower != null && confidenceUpper != null && confidenceUpper - confidenceLower < 0) {
			errorList.addIfNotNull("Confidence Interval: second number must be at least as big as the first number");
		}
		
		// Lab procedure code (LOINC code)
		errorList.addIfNotNull(checkFormat("Lab procedure code", proc.getLabProcedureCode(),
				ValidationFormat.LOINC, false));
		
		// Lab procedure ID, this variable is null on lab procedure creation
		if (proc.getLabProcedureID() != null && proc.getLabProcedureID() <= 0) {
			errorList.addIfNotNull("Lab Procedure ID: Invalid Lab Procedure ID");
		}
		
		// Office visit ID, required
		if (proc.getOfficeVisitID() == null || proc.getOfficeVisitID() <= 0) {
			errorList.addIfNotNull("Office Visit ID: Invalid Office Visit ID");
		}
		
		// Lab technician ID, required
		if (proc.getLabTechnicianID() == null || proc.getLabTechnicianID() <= 0) {
			errorList.addIfNotNull("Lab Technician ID: Invalid Lab Technician ID");
		}
		
		// Priority
		if(proc.getPriority() != null && (proc.getPriority() < 1 || proc.getPriority() > 3)) {
			errorList.addIfNotNull("Priority: invalid priority (null or out of bounds");
		}
		
		// Results
		errorList.addIfNotNull(checkFormat("Results", proc.getResults(), 
				ValidationFormat.COMMENTS, true));
		
		// Status
		boolean statusIsValid = false;
		LabProcedureStatus statusToValidate = proc.getStatus();
		if(statusToValidate != null) {
			for(LabProcedureStatus status : LabProcedureStatus.values()) {
				if(status.getID() == statusToValidate.getID()) {
					statusIsValid = true;
					break;
				}
			}
		}
		if(!statusIsValid) {
			errorList.addIfNotNull("Status: Invalid status");
		}
		
		// Updated date
		if(proc.getUpdatedDate() == null) {
			errorList.addIfNotNull("Updated date: null updated date");
		}
		
		// HCP MID
		if(proc.getHcpMID() == null) {
			errorList.addIfNotNull("HCP MID: Cannot be null");
		} else {
			errorList.addIfNotNull(checkFormat("HCP MID", proc.getHcpMID(), ValidationFormat.HCPMID, false));
		}
		
		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}

}
