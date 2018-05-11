package edu.ncsu.csc.itrust.model.prescription;

import org.apache.commons.lang3.StringUtils;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class PrescriptionValidator extends POJOValidator<Prescription> {
	@Override
	public void validate(Prescription obj) throws FormValidationException {

		ErrorList errorList = new ErrorList();
		
		if (obj.getDrugCode() == null) {
			errorList.addIfNotNull("Drug code cannot be empty");
		} else {
			errorList.addIfNotNull(checkFormat("Drug Code", obj.getDrugCode().getNDCode(), ValidationFormat.ND, true));
			errorList.addIfNotNull(checkFormat("Drug Code Description", obj.getDrugCode().getDescription(), ValidationFormat.ND_CODE_DESCRIPTION, true));
		}
		
		if (StringUtils.isEmpty(obj.getInstructions())) {
			errorList.addIfNotNull("Instruction cannot be empty");
		}
		
		if (obj.getDosage() <= 0) {
			errorList.addIfNotNull("Dosage cannot be 0 or negative");
		}
		
		errorList.addIfNotNull(checkFormat("Special Instructions", obj.getInstructions(), ValidationFormat.ND_CODE_DESCRIPTION, true));
		
		if (obj.getStartDate() == null) {
			errorList.addIfNotNull("Start date cannot be empty");
		} else if (obj.getEndDate() == null) {
			errorList.addIfNotNull("End date cannot be empty");
		} else if (obj.getStartDate().isAfter(obj.getEndDate())) {
			errorList.addIfNotNull("Start date must precede end date");
		}
		
		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}
}
