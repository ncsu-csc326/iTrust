package edu.ncsu.csc.itrust.model.medicalProcedure;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

public class MedicalProcedureValidator  extends POJOValidator<MedicalProcedure>{
    
    @Override
    public void validate(MedicalProcedure obj) throws FormValidationException {
        // nothing to validate
    }

}
