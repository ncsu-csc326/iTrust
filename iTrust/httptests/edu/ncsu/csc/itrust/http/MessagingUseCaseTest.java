package edu.ncsu.csc.itrust.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.DBBuilder;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * Use Case 30
 */
@SuppressWarnings("unused")
public class MessagingUseCaseTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		HttpUnitOptions.setScriptingEnabled(false);
		gen.clearAllTables();
		gen.standardData();
	}

	public void testHCPSendMessage() throws Exception {

		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Message Outbox").click();
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Compose a Message").click();
		
		// Select Patient
		WebForm wf = wr.getFormWithID("mainForm");

		wf.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		wr = wf.submit();

		// Submit message
		wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("subject", "Visit Request");
		wf.getScriptableObject().setParameterValue("messageBody", "We really need to have a visit.");
		wr = wf.submit();
		assertLogged(TransactionType.MESSAGE_SEND, 9000000000L, 2L, "");
		
		
		// Create timestamp
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		
		assertTrue(wr.getText().contains("My Sent Messages"));
		
		// Check message in outbox
		wr = wr.getLinkWith("Message Outbox").click();
		assertTrue(wr.getText().contains("Visit Request"));
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getTableWithID("mailbox").getText().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		
		// Check bolded message appears in patient
		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 9000000000L, 9000000000L, "");
		
		//wr = wr.getLinkWith("Log into iTrust").click();
		
		wc = login("2", "pw");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		
		assertEquals("font-weight: bold;", wr.getTableWithID("mailbox").getRows()[1].getAttribute("style"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Kelly Doctor"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Visit Request"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains(stamp));		
	}
	
	public void testPatientSendReply() throws Exception {

		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Read").click();
		assertLogged(TransactionType.MESSAGE_VIEW, 2L, 9000000000L, "");
		
		// Message List 
		wr = wr.getLinkWith("Reply").click();
		
		// Submit reply
		WebForm wf = wr.getFormWithID("mainForm");
		wf.getScriptableObject().setParameterValue("messageBody", "Which office visit did you update?");
		wr = wf.submit();
		assertLogged(TransactionType.MESSAGE_SEND, 2L, 9000000000L, "");
		
		// Create timestamp
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		
		// Check message in outbox
		wr = wr.getLinkWith("Message Outbox").click();
		assertTrue(wr.getText().contains("RE: Office Visit Updated"));
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getTableWithID("mailbox").getText().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 2L, 0L, "");
		
		// Check bolded message appears in hcp
		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 2L, 2L, "");
		
		
		wc = login("9000000000", "pw");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		
		assertEquals("font-weight: bold;", wr.getTableWithID("mailbox").getRows()[1].getAttribute("style"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Andy Programmer"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("RE: Office Visit Updated"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains(stamp));
	}
	
	public void testPatientSendMessageMultiRecipients() throws Exception {
		gen.messagingCcs();
		
		// Login
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		
		wr = wr.getLinkWith("Compose a Message").click();
		
		// Select Patient
		WebForm wf = wr.getFormWithID("mainForm");

		wf.getScriptableObject().setParameterValue("dlhcp", "9000000003");
		wr = wf.submit();

		// Submit message
		wf = wr.getFormWithID("mainForm");
		wf.setParameter("cc", "9000000000");
		wf.getScriptableObject().setParameterValue("subject", "This is a message to multiple recipients");
		wf.getScriptableObject().setParameterValue("messageBody", "We really need to have a visit!");
		wr = wf.submit();
		assertLogged(TransactionType.MESSAGE_SEND, 1L, 9000000003L, "9000000000");
		
		String entry = wr.getTableWithID("mailbox").getRows()[1].getText();
		
		assertTrue(entry.contains("Gandalf Stormcrow"));
		assertTrue(entry.contains("Kelly Doctor"));
		assertTrue(entry.contains("This is a message to multiple recipients"));
		
	}
	
	public void testPatientSendReplyMultipleRecipients() throws Exception {

		// Login
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("Read").click();
		assertLogged(TransactionType.MESSAGE_VIEW, 2L, 9000000000L, "");
		
		// Message List 
		wr = wr.getLinkWith("Reply").click();
		
		// Submit reply
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("cc", "9000000003");
		wf.getScriptableObject().setParameterValue("messageBody", "Which office visit did you update?");
		wr = wf.submit();
		assertLogged(TransactionType.MESSAGE_SEND, 2L, 9000000000L, "9000000003");
		
		// Create timestamp
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		
		// Check message in outbox
		wr = wr.getLinkWith("Message Outbox").click();
		assertTrue(wr.getText().contains("RE: Office Visit Updated"));
		assertTrue(wr.getText().contains("Kelly Doctor"));
		assertTrue(wr.getText().contains("Gandalf Stormcrow"));
		assertTrue(wr.getTableWithID("mailbox").getText().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 2L, 0L, "");
		
		// Check bolded message appears in hcp
		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 2L, 2L, "");
		
		
		wc = login("9000000000", "pw");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		
		assertEquals("font-weight: bold;", wr.getTableWithID("mailbox").getRows()[1].getAttribute("style"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Andy Programmer"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("RE: Office Visit Updated"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains(stamp));
		
		// Check bolded message appears in hcp
		wr = wr.getLinkWith("Logout").click();
		
		//wr = wr.getLinkWith("Log into iTrust").click();
		
		wc = login("9000000003", "pw");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000003L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000003L, 0L, "");
		
		assertEquals("font-weight: bold;", wr.getTableWithID("mailbox").getRows()[1].getAttribute("style"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Andy Programmer"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("RE: Office Visit Updated"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains(stamp));
	}
	
	public void testHCPSendReplySingleCCRecipient() throws Exception {
		
		gen.clearMessages();
		gen.messages6();

		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Read").click();
		assertLogged(TransactionType.MESSAGE_VIEW, 9000000000L, 22L, "Viewed Message: 3");
		
		// Message List 
		wr = wr.getLinkWith("Reply").click();
		
		// Submit reply
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("cc", "9000000007");
		wf.getScriptableObject().setParameterValue("messageBody", "I will not be able to make my next schedulded appointment.  Is there anyone who can book another time?");
		wr = wf.submit();
		assertLogged(TransactionType.MESSAGE_SEND, 9000000000L, 22L, "9000000007");
		
		// Create timestamp
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		
		// Check message in outbox
		wr = wr.getLinkWith("Message Outbox").click();
		assertTrue(wr.getText().contains("RE: Appointment rescheduling"));
		assertTrue(wr.getText().contains("Fozzie Bear"));
		assertTrue(wr.getText().contains("Beaker Beaker"));
		assertTrue(wr.getTableWithID("mailbox").getText().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		
		// Check bolded message appears in hcp
		wr = wr.getLinkWith("Logout").click();
		assertLogged(TransactionType.LOGOUT, 9000000000L, 9000000000L, "");
				
		wc = login("22", "pw");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 22L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 22L, 0L, "");
		
		assertEquals("font-weight: bold;", wr.getTableWithID("mailbox").getRows()[1].getAttribute("style"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Kelly Doctor"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("RE: Appointment rescheduling"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains(stamp));
		
		// Check bolded message appears in hcp
		wr = wr.getLinkWith("Logout").click();
		
		wc = login("9000000007", "pw");
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000007L, 0L, "");
		
		wr = wr.getLinkWith("Message Inbox").click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000007L, 0L, "");
		
		assertEquals("font-weight: bold;", wr.getTableWithID("mailbox").getRows()[1].getAttribute("style"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("Kelly Doctor"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains("RE: Appointment rescheduling"));
		assertTrue(wr.getTableWithID("mailbox").getRows()[1].getText().contains(stamp));
	}
	
	
}
