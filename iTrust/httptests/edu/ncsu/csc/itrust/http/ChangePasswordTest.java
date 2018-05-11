package edu.ncsu.csc.itrust.http;

import org.apache.commons.codec.digest.DigestUtils;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.enums.TransactionType;

@SuppressWarnings("unused")
public class ChangePasswordTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testChangePassword_Acceptance_Short() throws Exception {
		//Patient1 logs into iTrust
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		//User goes to change password
		wr = wr.getLinkWith("Change Password").click();
		
		//User types in their current, new, and confirm passwords
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("oldPass", "pw");
		wf.setParameter("newPass", "pass1");
		wf.setParameter("confirmPass", "pass1");
		
		//User submits password change. Change logged
		wf.submit(wf.getSubmitButtons()[0]);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Password Changed"));
		assertLogged(TransactionType.PASSWORD_CHANGE, 1L, 0, "");
		
		//User logs out
		wr = wr.getLinkWith("Logout").click();
		
		//User can't log in with old password, but can with new one
		wc = login("1", "pw");
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
		wc = login("1", "pass1");
		assertEquals("iTrust - Patient Home", wc.getCurrentPage().getTitle());
	}

	public void testChangePassword_Acceptance_Long() throws Exception {
		//Patient1 logs into iTrust
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		//User goes to change password
		wr = wr.getLinkWith("Change Password").click();
		
		//User types in their current, new, and confirm passwords
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("oldPass", "pw");
		wf.setParameter("newPass", "pass12345abcde");
		wf.setParameter("confirmPass", "pass12345abcde");
		
		//User submits password change. Change logged
		wf.submit(wf.getSubmitButtons()[0]);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Password Changed"));
		assertLogged(TransactionType.PASSWORD_CHANGE, 1L, 0, "");
		
		//User logs out
		wr = wr.getLinkWith("Logout").click();
		
		//User can't log in with old password, but can with new one
		wc = login("1", "pw");
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
		wc = login("1", "pass12345abcde");
		assertEquals("iTrust - Patient Home", wc.getCurrentPage().getTitle());
	}
	
	public void testChangePassword_Invalid_Length() throws Exception {
		//Patient1 logs into iTrust
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		//User goes to change password
		wr = wr.getLinkWith("Change Password").click();
		
		//User types in their current, new, and confirm passwords
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("oldPass", "pw");
		wf.setParameter("newPass", "pas1");
		wf.setParameter("confirmPass", "pas1");
		
		//User submits password change. Change logged
		wf.submit(wf.getSubmitButtons()[0]);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Invalid password"));
		assertLogged(TransactionType.PASSWORD_CHANGE_FAILED, 1L, 0, "");
		
		//User logs out
		wr = wr.getLinkWith("Logout").click();
		
		//User can log in with old password, but can't with new one
		wc = login("1", "pas1");
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
		wc = login("1", "pw");
		assertEquals("iTrust - Patient Home", wc.getCurrentPage().getTitle());
	}
	
	public void testChangePassword_Invalid_Characters() throws Exception {
		//Patient1 logs into iTrust
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		//User goes to change password
		wr = wr.getLinkWith("Change Password").click();
		
		//User types in their current, new, and confirm passwords
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("oldPass", "pw");
		wf.setParameter("newPass", "password");
		wf.setParameter("confirmPass", "password");
		
		//User submits password change. Change logged
		wf.submit(wf.getSubmitButtons()[0]);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Invalid password"));
		assertLogged(TransactionType.PASSWORD_CHANGE_FAILED, 1L, 0, "");
		
		//User logs out
		wr = wr.getLinkWith("Logout").click();
		
		//User can log in with old password, but can't with new one
		wc = login("1", "password");
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
		wc = login("1", "pw");
		assertEquals("iTrust - Patient Home", wc.getCurrentPage().getTitle());
	}
	
	public void testChangePassword_Failed_Confirmation() throws Exception {
		//Patient1 logs into iTrust
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		//User goes to change password
		wr = wr.getLinkWith("Change Password").click();
		
		//User types in their current, new, and confirm passwords
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("oldPass", "pw");
		wf.setParameter("newPass", "pass1");
		wf.setParameter("confirmPass", "pass2");
		
		//User submits password change. Change logged
		wf.submit(wf.getSubmitButtons()[0]);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Invalid password"));
		assertLogged(TransactionType.PASSWORD_CHANGE_FAILED, 1L, 0, "");
		
		//User logs out
		wr = wr.getLinkWith("Logout").click();
		
		//User can log in with old password, but can't with new one
		wc = login("1", "pas1");
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
		wc = login("1", "pw");
		assertEquals("iTrust - Patient Home", wc.getCurrentPage().getTitle());
	}
	
	public void testChangePassword_Invalid_Password() throws Exception {
		//Patient1 logs into iTrust
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		//User goes to change password
		wr = wr.getLinkWith("Change Password").click();
		
		//User types in their current, new, and confirm passwords
		WebForm wf = wr.getFormWithID("mainForm");
		wf.setParameter("oldPass", "password");
		wf.setParameter("newPass", "pass1");
		wf.setParameter("confirmPass", "pass1");
		
		//User submits password change. Change logged
		wf.submit(wf.getSubmitButtons()[0]);
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Invalid password"));
		assertLogged(TransactionType.PASSWORD_CHANGE_FAILED, 1L, 0, "");
		
		//User logs out
		wr = wr.getLinkWith("Logout").click();
		
		//User can log in with old password, but can't with new one
		wc = login("1", "pass1");
		assertEquals("iTrust - Login", wc.getCurrentPage().getTitle());
		wc = login("1", "pw");
		assertEquals("iTrust - Patient Home", wc.getCurrentPage().getTitle());
	}
	
}
