package edu.ncsu.csc.itrust.model.officeVisit;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;

public class OfficeVisitValidator extends POJOValidator<OfficeVisit> {

	private DataSource ds;
	public OfficeVisitValidator(DataSource ds){
		this.ds = ds;
		
	}
	
	/**
	 * Used to Validate an office visit object. If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * only performs checks on the values stored in the object (e.g. Patient MID)
	 * Does NOT validate the format of the visit date and other attributes that
	 * are NOT stored in the object itself
	 * 
	 * @param obj the Office Visit to be validated
	 */
	@Override
	public void validate(OfficeVisit obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		try{
				
				errorList.addIfNotNull(checkFormat("Patient MID", obj.getPatientMID(), ValidationFormat.NPMID, false));
				errorList.addIfNotNull(checkFormat("Location ID", obj.getLocationID(), ValidationFormat.HOSPITAL_ID, false));
				if(obj.getVisitID() != null){
					if(obj.getVisitID() <=0){
						errorList.addIfNotNull("Invalid Visit ID");
					}
				}
			Long apptTypeID = obj.getApptTypeID();
			ApptTypeData atData = new ApptTypeMySQLConverter(ds);
			String apptTypeName = "";
			try {
				apptTypeName = atData.getApptTypeName(apptTypeID);
			} catch (DBException e) {
				errorList.addIfNotNull("Invalid ApptType ID");
			}
			if(apptTypeName.isEmpty()) errorList.addIfNotNull("Invalid ApptType ID");
			HospitalData hData = new HospitalMySQLConverter(ds);
			Hospital temp = null;
			try {
				temp = hData.getHospitalByID(obj.getLocationID());
			} catch (DBException e) {
				errorList.addIfNotNull("Invalid Hospital ID");
			}
			if(temp == null) errorList.addIfNotNull("Invalid Hospital ID");
		}
		catch(NullPointerException np){
			errorList.addIfNotNull("A Required field is Null");
		}
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
		
	}

}
