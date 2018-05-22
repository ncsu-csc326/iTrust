package edu.ncsu.csc.itrust.unit.model.loinccode;

import org.junit.Test;

import edu.ncsu.csc.itrust.model.loinccode.LOINCCode;
import junit.framework.TestCase;

public class LOINCCodeTest extends TestCase {
	private LOINCCode loinc;

	@Override
	public void setUp() {
		loinc = new LOINCCode("code", "comp", "prop", "time", "system", "scale", "method");
	}

	@Test
	public void testConstructors() {
		assertEquals("code", loinc.getCode());
		assertEquals("comp", loinc.getComponent());
		assertEquals("prop", loinc.getKindOfProperty());
		assertEquals("time", loinc.getTimeAspect());
		assertEquals("system", loinc.getSystem());
		assertEquals("scale", loinc.getScaleType());
		assertEquals("method", loinc.getMethodType());
	}

	@Test
	public void testGettersAndSetters() {
		loinc.setComponent("newComponent");
		loinc.setKindOfProperty("newProp");
		loinc.setTimeAspect("newTime");
		loinc.setSystem("newSystem");
		loinc.setScaleType("newScale");
		loinc.setMethodType("newMethod");
		assertEquals("newComponent", loinc.getComponent());
		assertEquals("newProp", loinc.getKindOfProperty());
		assertEquals("newTime", loinc.getTimeAspect());
		assertEquals("newSystem", loinc.getSystem());
		assertEquals("newScale", loinc.getScaleType());
		assertEquals("newMethod", loinc.getMethodType());
	}
}
