package edu.ncsu.csc.itrust.unit.model.icdcode;

import org.junit.Test;

import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import junit.framework.TestCase;

public class ICDCodeTest extends TestCase {
	@Test
	public void testICDCode() {
		ICDCode icdCode = new ICDCode("initId", "initName", false);
		icdCode.setChronic(true);
		icdCode.setCode("testCode");
		icdCode.setName("testName");
		assertEquals("testCode", icdCode.getCode());
		assertEquals("testName", icdCode.getName());
		assertTrue(icdCode.isChronic());
	}
}
