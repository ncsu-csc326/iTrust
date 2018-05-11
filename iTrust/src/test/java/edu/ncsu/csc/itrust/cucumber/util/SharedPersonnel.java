package edu.ncsu.csc.itrust.cucumber.util;

import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class SharedPersonnel {
	private PersonnelBean personnel;
	private PersonnelDAO personnelDAO;
	public PersonnelDAO getPersonnelDAO() {
		return personnelDAO;
	}
	public PersonnelBean getPersonnel() {
		return personnel;
	}
	public void setPersonnel(PersonnelBean personnel) {
		this.personnel = personnel;
	}
	public SharedPersonnel() {
		this.personnelDAO = TestDAOFactory.getTestInstance().getPersonnelDAO();
	}
}