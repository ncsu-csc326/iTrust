package edu.ncsu.csc.itrust.unit.serverutils;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("all")
public class MockHttpSession implements HttpSession {
	static int mins = 0;

	@Override
	@SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
	public void setMaxInactiveInterval(int arg0) {
		mins = arg0;
	}

	@Override
	public Object getAttribute(String arg0) {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public Enumeration getAttributeNames() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public long getCreationTime() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public String getId() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public long getLastAccessedTime() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public int getMaxInactiveInterval() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public ServletContext getServletContext() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	@Deprecated
	public HttpSessionContext getSessionContext() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public Object getValue(String arg0) {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public String[] getValueNames() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public void invalidate() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public boolean isNew() {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public void removeAttribute(String arg0) {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public void removeValue(String arg0) {
		throw new IllegalStateException("should not be hit!");
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		throw new IllegalStateException("should not be hit!");
	}
}
