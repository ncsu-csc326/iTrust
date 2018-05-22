package edu.ncsu.csc.itrust.model.icdcode;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class ICDCodeValidator  extends POJOValidator<ICDCode>{

    @Override
    public void validate(ICDCode obj) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        
        // code
        errorList.addIfNotNull(checkFormat("ICDCode", obj.getCode(), ValidationFormat.ICD10CM, false));

        // name
        errorList.addIfNotNull(checkFormat("Name", obj.getName(), ValidationFormat.ICD_CODE_DESCRIPTION, false));

        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }
    }

}
