package edu.ncsu.csc.itrust.enums;

import junit.framework.TestCase;

public class BloodTypeTest extends TestCase {
	public void testParse() throws Exception {
		assertEquals(BloodType.ABNeg, BloodType.parse("AB-"));
		assertEquals(BloodType.ONeg, BloodType.parse("O-"));
		assertEquals(BloodType.NS, BloodType.parse("N/S"));
		assertEquals(BloodType.NS, BloodType.parse("non-existent ethnicity"));
	}
}
