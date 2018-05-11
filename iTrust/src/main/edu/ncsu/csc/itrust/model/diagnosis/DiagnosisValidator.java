package edu.ncsu.csc.itrust.model.diagnosis;

import java.time.format.DateTimeFormatter;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

/**
 * Validates instance of Diagnosis.
 */
public class DiagnosisValidator extends POJOValidator<Diagnosis> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(Diagnosis obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (obj == null) {
			errorList.addIfNotNull("Invalid Diagnosis object");
			throw new FormValidationException(errorList);
		}
		
		if (obj.getIcdCode() == null) {
			errorList.addIfNotNull("Invalid ICDCode object");
		} else {
			errorList.addIfNotNull(checkFormat("ICD Code", obj.getIcdCode().getCode(), ValidationFormat.ICD10CM, false));
		}
		
		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}
}
