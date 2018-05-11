package edu.ncsu.csc.itrust.enums;

import junit.framework.TestCase;

public class PrescriptionAlertTest extends TestCase {
	public void testGettersSetters()
	{
		PrescriptionAlerts p = PrescriptionAlerts.Prioglitazone;
		assertEquals("Prioglitazone", p.getName());
		assertEquals("647641512", p.getNDCode());
	}
}
