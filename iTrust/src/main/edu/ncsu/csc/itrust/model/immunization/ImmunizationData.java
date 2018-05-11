package edu.ncsu.csc.itrust.model.immunization;

import java.util.List;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;


public interface ImmunizationData extends DataBean<Immunization> {
	
	/**
	 * Gets a list of immunizations that are either chronic diagnoses or short term 
	 * 
	 * @param mid patient MID
	 * @return a list of immunizations
	 * @throws DBException when error occurs when accessing database
	 */
	public List<Immunization> getAllImmunizations(long mid) throws DBException;
}
