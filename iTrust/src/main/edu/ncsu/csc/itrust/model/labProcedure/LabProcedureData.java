package edu.ncsu.csc.itrust.model.labProcedure;

import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface LabProcedureData extends DataBean<LabProcedure> {

	/**
	 * Returns all lab procedures with the given lab technician ID, or empty
	 * list if no lab procedures exist for the given lab technician ID.
	 * 
	 * @param technicianID
	 *            The ID of the lab technician to query by
	 * @return all lab procedures with given lab technician ID
	 * @throws DBException
	 */
	List<LabProcedure> getLabProceduresForLabTechnician(Long technicianID) throws DBException;

	/**
	 * Returns all lab procedures with the given office visit ID, or empty
	 * list if no lab procedures exist for the given office visit ID.
	 * 
	 * @param officeVisitID
	 *            The ID of the office visit to query by
	 * @return all lab procedures with given office visit ID
	 * @throws DBException
	 */
	List<LabProcedure> getLabProceduresByOfficeVisit(Long officeVisitID) throws DBException;

	/**
	 * Removes lab procedure with given ID.
	 * 
	 * @param labProcedureID
	 *            The ID of the lab procedure to remove
	 * @return true if the lab procedure was successfully removed from the
	 *         database
	 * @throws DBException
	 */
	boolean removeLabProcedure(Long labProcedureID) throws DBException;
}
