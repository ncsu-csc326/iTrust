package edu.ncsu.csc.itrust.model.officeVisit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
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

	/**
	 * Default constructor for OfficeVisitValidator. 
	 */
	public OfficeVisitValidator(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * Used to Validate an office visit object. If the validation does not
	 * succeed, a {@link FormValidationException} is thrown. only performs
	 * checks on the values stored in the object (e.g. Patient MID) Does NOT
	 * validate the format of the visit date and other attributes that are NOT
	 * stored in the object itself
	 * 
	 * @param obj
	 *            the Office Visit to be validated
	 */
	@Override
	public void validate(OfficeVisit obj) throws FormValidationException {
		OfficeVisitController ovc = new OfficeVisitController(ds);
		ErrorList errorList = new ErrorList();
		
		LocalDateTime date = obj.getDate();
		Long patientMID = obj.getPatientMID();
		
		if (patientMID == null) {
			errorList.addIfNotNull("Cannot add office visit information: invalid patient MID");
			throw new FormValidationException(errorList);
		}
		
		LocalDate patientDOB = ovc.getPatientDOB(patientMID);
		if (patientDOB == null) {
			errorList.addIfNotNull("Cannot add office visit information: patient does not have a birthday");
			throw new FormValidationException(errorList);
		}
		
		if (date.toLocalDate().isBefore(patientDOB)) {
			errorList.addIfNotNull("Date: date cannot be earlier than patient's birthday at " + patientDOB.format(DateTimeFormatter.ISO_DATE));
		}
		
		errorList.addIfNotNull(checkFormat("Patient MID", patientMID, ValidationFormat.NPMID, false));
		
		errorList.addIfNotNull(checkFormat("Location ID", obj.getLocationID(), ValidationFormat.HOSPITAL_ID, false));
		if (obj.getVisitID() != null) {
			if (obj.getVisitID() <= 0) {
				errorList.addIfNotNull("Visit ID: Invalid Visit ID");
			}
		}
		Long apptTypeID = obj.getApptTypeID();
		ApptTypeData atData = new ApptTypeMySQLConverter(ds);
		String apptTypeName = "";
		try {
			apptTypeName = atData.getApptTypeName(apptTypeID);
		} catch (DBException e) {
			// Do nothing
		}
		if (apptTypeName.isEmpty()) {
			errorList.addIfNotNull("Appointment Type: Invalid ApptType ID");
		}
		
		HospitalData hData = new HospitalMySQLConverter(ds);
		Hospital temp = null;
		try {
			temp = hData.getHospitalByID(obj.getLocationID());
		} catch (DBException e) {
			// Do nothing
		}
		if (temp == null) {
			errorList.addIfNotNull("Location: Invalid Hospital ID");
		}

		errorList.addIfNotNull(checkFormat("Weight", obj.getWeight(), ValidationFormat.WEIGHT_OV, true));
		errorList.addIfNotNull(checkFormat("Household Smoking Status", obj.getHouseholdSmokingStatus(), ValidationFormat.HSS_OV, true));

		if (ovc.isPatientABaby(patientMID, date)) {
			errorList.addIfNotNull(checkFormat("Length", obj.getLength(), ValidationFormat.LENGTH_OV, true));
			errorList.addIfNotNull(checkFormat("Head Circumference", obj.getHeadCircumference(), ValidationFormat.HEAD_CIRCUMFERENCE_OV, true));
			
		}
		
		if (ovc.isPatientAnAdult(patientMID, date) || ovc.isPatientAChild(patientMID, date)) {
			errorList.addIfNotNull(checkFormat("Height", obj.getHeight(), ValidationFormat.HEIGHT_OV, true));
			errorList.addIfNotNull(checkFormat("Blood Pressure", obj.getBloodPressure(), ValidationFormat.BLOOD_PRESSURE_OV, true));
			
		}

		if (ovc.isPatientAnAdult(patientMID, date)) {
			errorList.addIfNotNull(checkFormat("Patient Smoking Status", obj.getPatientSmokingStatus(), ValidationFormat.PSS_OV, true));
			errorList.addIfNotNull(checkFormat("HDL", obj.getHDL(), ValidationFormat.HDL_OV, true));
			errorList.addIfNotNull(checkFormat("Triglyceride", obj.getTriglyceride(), ValidationFormat.TRIGLYCERIDE_OV, true));
			errorList.addIfNotNull(checkFormat("LDL", obj.getLDL(), ValidationFormat.LDL_OV, true));
		}

		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}
}
