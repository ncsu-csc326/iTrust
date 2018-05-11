package edu.ncsu.csc.itrust.unit.serverutils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import edu.ncsu.csc.itrust.server.SessionTimeoutListener;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class SessionTimeoutListenerTest extends TestCase {
	private TestDataGenerator gen;
	HttpSessionEvent mockSessionEvent;
	HttpSession mockSession;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.timeout();
		mockSessionEvent = mock(HttpSessionEvent.class);
		mockSession = mock(HttpSession.class);
	}

	// This uses a rudimentary mock object system - where we create these
	// objects that are
	// essentially stubs, except for keeping track of info passed to them.
	public void testListenerWorked() throws Exception {
		SessionTimeoutListener listener = new SessionTimeoutListener(TestDAOFactory.getTestInstance());
		HttpSessionEvent event = new MockHttpSessionEvent();
		listener.sessionCreated(event);
		assertEquals(1200, MockHttpSession.mins);
	}

	public void testSessionDestroyed() throws Exception {
		SessionTimeoutListener listener = new SessionTimeoutListener(TestDAOFactory.getTestInstance());
		when(mockSessionEvent.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute("loggedInMID")).thenReturn(1L);
		listener.sessionDestroyed(mockSessionEvent);
		verify(mockSession).getAttribute("loggedInMID");
		
	}

	public void testDBException() throws Exception {
		SessionTimeoutListener listener = new SessionTimeoutListener();
		listener.sessionCreated(new MockHttpSessionEvent());
		assertEquals(1200, MockHttpSession.mins);
	}

}
