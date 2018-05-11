package edu.ncsu.csc.itrust.unit.bean;

import java.text.SimpleDateFormat;

import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import junit.framework.TestCase;

public class AllergyBeanTest extends TestCase {
	public void testFirstFound() throws Exception {
		AllergyBean allergy = new AllergyBean();
		allergy.setFirstFound(new SimpleDateFormat("MM/dd/yyyy").parse("05/19/1984"));
		assertEquals("05/19/1984", allergy.getFirstFoundStr());
	}

	public void testGetID() {
		AllergyBean allergy = new AllergyBean();
		allergy.setId(7l);
		assertEquals(7l, allergy.getId());
		allergy.setDescription("testing");
		assertEquals("testing", allergy.toString());
	}

	public void testInvalidFirstFound() {
		AllergyBean allergy = new AllergyBean();
		allergy.setFirstFound(null);
		assertEquals("", allergy.getFirstFoundStr());
	}
}
