package edu.ncsu.csc.itrust.model.immunization;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

/**
 * Validates instance of Immunization.
 */
public class ImmunizationValidator extends POJOValidator<Immunization> {
	
	/** Data source for retrieving from database. */
	@SuppressWarnings("unused")
	private DataSource ds;

	/**
	 * Constructor for ImmunizationValidator. 
	 */
	public ImmunizationValidator(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(Immunization obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		String code = obj.getCode();
		
		if( code.isEmpty() || code.length() > 5 )
			errorList.addIfNotNull("Invalid code: code are 5 digit numbers");
		
		if ( errorList.hasErrors() )
			throw new FormValidationException(errorList);	
	}
	
}
