package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.model.old.beans.loaders.AllergyBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.FamilyBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.HospitalBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.MedicationBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.OperationalProfileLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.RemoteMonitoringListsBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ReportRequestBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.TransactionBeanLoader;
import junit.framework.TestCase;

public class BeanLoaderTest extends TestCase {

	public void testLoadParameters() throws Exception {

		try {
			new AllergyBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new FamilyBeanLoader("self").loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new HospitalBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new MedicationBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new OperationalProfileLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new RemoteMonitoringListsBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new ReportRequestBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

		try {
			new TransactionBeanLoader().loadParameters(null, null);
			fail("Should have thrown Exception");
		} catch (IllegalStateException ex) {
			assertEquals("unimplemented!", ex.getMessage());
		}

	}

}
