package edu.ncsu.csc.itrust.enums;

import junit.framework.TestCase;

public class EthnicityTest extends TestCase {
	public void testParse() throws Exception {
		assertEquals(Ethnicity.Caucasian, Ethnicity.parse("Caucasian"));
		assertEquals(Ethnicity.AfricanAmerican, Ethnicity.parse("African American"));
		assertEquals(Ethnicity.NotSpecified, Ethnicity.parse("Not Specified"));
		assertEquals(Ethnicity.NotSpecified, Ethnicity.parse("non-existent ethnicity"));
	}
}
