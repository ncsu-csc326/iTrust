package edu.ncsu.csc.itrust.serverutils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

@SuppressWarnings("serial")
public class MockHttpSessionEvent extends HttpSessionEvent {
	public MockHttpSessionEvent() {
		super(new MockHttpSession());
	}

	@Override
	public HttpSession getSession() {
		return new MockHttpSession();
	}
}
