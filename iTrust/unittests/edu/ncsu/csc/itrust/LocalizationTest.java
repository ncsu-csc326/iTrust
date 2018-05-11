package edu.ncsu.csc.itrust;

import java.util.Locale;
import junit.framework.TestCase;



public class LocalizationTest extends TestCase{

	public void testLocalization() {
		Locale l = Localization.instance().getCurrentLocale();
		assert(l != null);
	}

	
	
}
