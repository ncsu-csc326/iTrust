package edu.ncsu.csc.itrust.model.labtechnician;

import javax.sql.DataSource;

import org.junit.Assert;

import edu.ncsu.csc.itrust.controller.labtechnician.LabTechnicianController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class LabTechnicianControllerTest extends TestCase {

	LabTechnicianController controller;
	TestDataGenerator gen;
	
	@Override
	public void setUp() throws Exception {
		DataSource ds = ConverterDAO.getDataSource();
		PersonnelDAO pdao = TestDAOFactory.getTestInstance().getPersonnelDAO();
		controller = new LabTechnicianController(ds, pdao);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testGetLabTechnicianStatusMID() throws DBException {
		Assert.assertFalse(controller.getLabTechnicianStatusMID().isEmpty());
	}
}
