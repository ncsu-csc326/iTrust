package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.model.old.beans.OverrideReasonBean;
import junit.framework.TestCase;

public class OverrideReasonBeanTest extends TestCase {

	private OverrideReasonBean orb;

	public void testPrescriptionEquals() throws Exception {
		orb = new OverrideReasonBean();
		orb.setDescription("blah");
		orb.setORCode("00001");
		orb.setID(1);
		orb.setPresID(2);

		OverrideReasonBean orb2 = new OverrideReasonBean();
		orb2.setDescription("blah");
		orb2.setORCode("00001");
		orb2.setID(1);
		orb2.setPresID(2);
		assertTrue(orb2.equals(orb));
	}
}
