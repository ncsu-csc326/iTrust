package edu.ncsu.csc.itrust;

import junit.framework.TestCase;

public class MessagesTest extends TestCase {

	public void testGetString() {
		assertEquals("Requested", Messages.getString("ReportRequestBean.requested"));
		assertEquals("!50000!", Messages.getString("50000"));
	}

}
