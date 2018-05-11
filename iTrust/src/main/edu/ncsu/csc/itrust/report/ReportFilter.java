package edu.ncsu.csc.itrust.report;

import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.PatientBean;

/**
 * 
 *
 */
public abstract class ReportFilter {
	/**
	 * 
	 * @param patients
	 * @return
	 */
	public abstract List<PatientBean> filter(List<PatientBean> patients);
	/**
	 * 
	 */
	@Override
	public abstract String toString();
	public abstract String getFilterTypeString();
	public abstract String getFilterValue();
}
