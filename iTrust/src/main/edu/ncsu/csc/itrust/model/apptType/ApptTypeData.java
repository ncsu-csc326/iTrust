package edu.ncsu.csc.itrust.model.apptType;

import java.util.Map;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.apptType.ApptType;

public interface ApptTypeData extends DataBean<ApptType> {
	Map<Long, ApptType> getApptTypeIDs(String name) throws DBException;
	String getApptTypeName(Long id) throws DBException;
}
