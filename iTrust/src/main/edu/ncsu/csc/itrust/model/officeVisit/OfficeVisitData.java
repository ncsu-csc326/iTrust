package edu.ncsu.csc.itrust.model.officeVisit;

import java.time.LocalDate;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface OfficeVisitData extends DataBean<OfficeVisit>{
	public List<OfficeVisit> getVisitsForPatient(Long patientID) throws DBException;
	
	/**
	 * Retrieves the patient's date of birth from database.
	 * 
	 * @param patientMID
	 * 			MID of the patient
	 * @return patient's date of birth
	 */
	public LocalDate getPatientDOB(Long patientID);
	
	/**
	 * Add office visit to the database and return the generated VisitID.
	 * 
	 * @param ov
	 * 			Office visit to add to the database
	 * @return VisitID generated from the database insertion, -1 if nothing was generated
	 * @throws DBException if error occurred in inserting office visit
	 */
	public long addReturnGeneratedId(OfficeVisit ov) throws DBException;
}
