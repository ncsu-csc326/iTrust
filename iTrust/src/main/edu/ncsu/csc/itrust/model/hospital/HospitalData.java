package edu.ncsu.csc.itrust.model.hospital;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.hospital.Hospital;

public interface HospitalData extends DataBean<Hospital>{
	public String getHospitalName(String id) throws DBException;

	public Hospital getHospitalByID(String locationID) throws DBException;
}
