package edu.ncsu.csc.itrust.model.loinccode;

import org.apache.commons.lang3.StringUtils;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class LOINCCodeValidator extends POJOValidator<LOINCCode> {
	@Override
	public void validate(LOINCCode obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (StringUtils.isEmpty(obj.getCode())) {
			errorList.addIfNotNull("Code cannot be empty");
		} else {
			errorList.addIfNotNull(checkFormat("Code", obj.getCode(), ValidationFormat.LOINC, false));
		}
		if (StringUtils.isEmpty(obj.getComponent())) {
			errorList.addIfNotNull("Component cannot be empty");
		}
		if (StringUtils.isEmpty(obj.getKindOfProperty())) {
			errorList.addIfNotNull("Kind of Property cannot be empty");
		}
		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}
}
