package edu.ncsu.csc.itrust.unit.webutils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class SessionUtilsTest {
	
	private SessionUtils utils;
	private List<PatientBean> list;
	private PatientBean a;
	private PatientBean b;

	@Before
	public void setUp() throws Exception {
		utils = spy(SessionUtils.getInstance());

		list = new ArrayList<PatientBean>(2);
		a = new PatientBean();
		a.setFirstName("Foo");
		a.setLastName("Bar");
		b = new PatientBean();
		b.setFirstName("Baz");
		b.setLastName("Quack");
		list.add(a);
		list.add(b);
	}

	@Test
	public void testParseString() {
		Object foo = "foo";
		String out = utils.parseString(foo);
		assertEquals(foo, out);
	}

	@Test
	public void testParseLong() {
		Object num = new Long(3L);
		Long out = utils.parseLong(num);
		assertEquals(num, out);
	}

	@Test
	public void testGetSessionUserRole() {
		when(utils.getSessionVariable("userRole")).thenReturn("some role");
		String role = utils.getSessionUserRole();
		assertEquals("some role", role);
	}

	@Test
	public void testGetSessionPID() {
		when(utils.getSessionVariable("pid")).thenReturn("some pid");
		String pid = utils.getSessionPID();
		assertEquals("some pid", pid);
	}

	@Test
	public void testGetSessionLoggedInMID() {
		when(utils.getSessionVariable("loggedInMID")).thenReturn("123");
		String mid = utils.getSessionLoggedInMID();
		assertEquals("123", mid);
	}

	@Test
	public void testGetSessionLoggedInMIDLong() {
		when(utils.getSessionVariable("loggedInMID")).thenReturn(34L);
		Long mid = utils.getSessionLoggedInMIDLong();
		assertEquals(new Long(34L), mid);
	}

	@Test
	public void testGetCurrentPatientMID() {
		when(utils.getSessionVariable("pid")).thenReturn("123");
		when(utils.getSessionVariable("userRole")).thenReturn("patient");
		when(utils.getSessionVariable("loggedInMID")).thenReturn("456");
		String mid = utils.getCurrentPatientMID();
		assertEquals("456", mid);
		when(utils.getSessionVariable("userRole")).thenReturn("not a patient");
		mid = utils.getCurrentPatientMID();
		assertEquals("123", mid);
	}

	@Test
	public void testGetCurrentPatientMIDLong() {
		when(utils.getSessionVariable("pid")).thenReturn("123");
		when(utils.getSessionVariable("userRole")).thenReturn("patient");
		when(utils.getSessionVariable("loggedInMID")).thenReturn("456");
		Long mid = utils.getCurrentPatientMIDLong();
		assertEquals(new Long(456), mid);
		when(utils.getSessionVariable("userRole")).thenReturn("not a patient");
		mid = utils.getCurrentPatientMIDLong();
		assertEquals(new Long(123), mid);
	}

	@Test
	public void testGetCurrentOfficeVisitId() {
		when(utils.getSessionVariable("officeVisitId")).thenReturn(new Long(3));
		Long id = utils.getCurrentOfficeVisitId();
		assertEquals(new Long(3), id);
	}

	@Test
	public void testGetRepresenteeList() {
		when(utils.getSessionVariable("representees")).thenReturn(list);
		List<PatientBean> actual = utils.getRepresenteeList();
		assertEquals(2, actual.size());
		assertEquals(a, actual.get(0));
		assertEquals(b, actual.get(1));
	}

	@Test
	public void testSetRepresenteeList() {
		PatientBean a = new PatientBean();
		a.setFirstName("Foo");
		a.setLastName("Bar");
		PatientBean b = new PatientBean();
		b.setFirstName("Baz");
		b.setLastName("Quack");
		list.add(a);
		list.add(b);
		doNothing().when(utils).setSessionVariable(anyString(), any());
		utils.setRepresenteeList(list);
		verify(utils).setSessionVariable("representees", list);
	}

	@Test
	public void testGetInstance() {
		SessionUtils first = SessionUtils.getInstance();
		SessionUtils second = SessionUtils.getInstance();
		assertEquals(first, second); // since it's a singleton
	}

}
