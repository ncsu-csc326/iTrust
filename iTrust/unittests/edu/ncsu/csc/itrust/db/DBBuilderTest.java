package edu.ncsu.csc.itrust.db;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.DBBuilder;

public class DBBuilderTest extends TestCase {

	// Make sure that the actual database can be rebuilt
	// This is run twice so that we check the "drop tables" script
	public void testRebuildNoException() throws Exception {
		DBBuilder.rebuildAll();
		DBBuilder.rebuildAll();
	}
}
