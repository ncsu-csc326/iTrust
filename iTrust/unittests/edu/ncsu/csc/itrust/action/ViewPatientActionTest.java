package edu.ncsu.csc.itrust.action;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ViewPatientActionTest {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private ViewPatientAction action;
	private ViewPatientAction badAction;

	@Before
	public void setUp() throws Exception {
		gen.clearAllTables();
		gen.patient2();
		action = new ViewPatientAction(factory, 2L, "2");
		badAction = new ViewPatientAction(factory, -1L, "2");
	}

	@Test
	public void testGetViewablePatients() {
		List<PatientBean> list = null;
		try {
			list = action.getViewablePatients();
		} catch (ITrustException e) {
			fail();
		}
		if(list == null){
			fail();
		} else {
			assertEquals(1, list.size());
		}
	}

}
