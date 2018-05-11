package edu.ncsu.csc.itrust.selenium;

import java.util.List;
import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * There's nothing special about this class other than adding a few handy test utility methods and
 * variables. When extending this class, be sure to invoke super.setUp() first.
 */
abstract public class iTrustSeleniumTest extends TestCase {
	/*
	 * The URL for iTrust, change as needed
	 */
	/**ADDRESS*/
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	/**gen*/
	protected TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}

	/**
	 * Helper method for logging in to iTrust
	 * @param username username
	 * @param password password
	 * @return {@link WebConversation}
	 * @throws Exception
	 */
	public WebDriver login(String username, String password) throws Exception {
			// begin at the iTrust home page
			WebDriver wd = new HtmlUnitDriver();
			wd.get(ADDRESS);
			// log in using the given username and password
			WebElement user = wd.findElement(By.name("j_username"));
			WebElement pass = wd.findElement(By.name("j_password"));
			user.sendKeys(username);
			pass.sendKeys(password);
			pass.submit();
			
			if (wd.getTitle().equals("iTrust - Login")) {
				throw new IllegalArgumentException("Error logging in, user not in database?");
			}
			assertLogged(TransactionType.LOGIN_SUCCESS, Long.parseLong(username), Long.parseLong(username), "");
			return wd;
	}
	
	/**
	 * assertLogged
	 * @param code code
	 * @param loggedInMID loggedInMID
	 * @param secondaryMID secondaryMID
	 * @param addedInfo addedInfo
	 * @throws DBException
	 */
	public static void assertLogged(TransactionType code, long loggedInMID,
			long secondaryMID, String addedInfo)
			throws DBException {
		List<TransactionBean> transList = TestDAOFactory.getTestInstance().getTransactionDAO().getAllTransactions();
		for (TransactionBean t : transList)
		{	
			if( (t.getTransactionType() == code) &&
				(t.getLoggedInMID() == loggedInMID) &&
				(t.getSecondaryMID() == secondaryMID))
				{
					assertTrue(t.getTransactionType() == code);
					if(!t.getAddedInfo().trim().contains(addedInfo.trim()))
					{
						fail("Additional Information is not logged correctly.");
					}
					return;
				}
		}
		fail("Event not logged as specified.");
	}

	/**
	 * assertNotLogged
	 * @param code code
	 * @param loggedInMID loggedInMID
	 * @param secondaryMID secondaryMID
	 * @param addedInfo addedInfo
	 * @throws DBException
	 */
	public static void assertNotLogged(TransactionType code, long loggedInMID,
			long secondaryMID, String addedInfo)
			throws DBException {
		List<TransactionBean> transList = TestDAOFactory.getTestInstance().getTransactionDAO().getAllTransactions();
		for (TransactionBean t : transList)
		{	
			if( (t.getTransactionType() == code) &&
				(t.getLoggedInMID() == loggedInMID) &&
				(t.getSecondaryMID() == secondaryMID) &&
				(t.getAddedInfo().trim().contains(addedInfo)) )
				{
					fail("Event was logged, but should NOT have been logged");
					return;
				}
		}
	}
	
}
