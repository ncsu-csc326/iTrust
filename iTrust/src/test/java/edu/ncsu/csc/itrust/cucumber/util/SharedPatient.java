package edu.ncsu.csc.itrust.cucumber.util;

import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class SharedPatient {
	private PatientBean patient;
	private PatientDAO patientDAO;
	public SharedPatient() {
		patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
	}
	public PatientDAO getPatientDAO() {
		return patientDAO;
	}
	public PatientBean getPatient() {
		return patient;
	}
	public void setPatient(PatientBean patient) {
		this.patient = patient;
	}
}
