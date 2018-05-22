package edu.ncsu.csc.itrust.selenium;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.meterware.httpunit.HttpUnitOptions;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class MessagingUseCaseTest extends iTrustSeleniumTest {

	/*
	 * The URL for iTrust, change as needed
	 */
	/** ADDRESS */
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	private WebDriver driver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		HttpUnitOptions.setScriptingEnabled(false);
		// turn off htmlunit warnings
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	}

	public void testHCPSendMessage() throws Exception {
		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Outbox")).click();
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Compose a Message")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("2");
		driver.findElement(By.xpath("//input[@value='2']")).submit();
		driver.findElement(By.name("subject")).clear();
		driver.findElement(By.name("subject")).sendKeys("Visit Request");
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("We really need to have a visit.");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertLogged(TransactionType.MESSAGE_SEND, 9000000000L, 2L, "");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		assertTrue(driver.getPageSource().contains("My Sent Messages"));
		driver.findElement(By.linkText("Message Outbox")).click();
		assertTrue(driver.getPageSource().contains("Visit Request"));
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");

		driver = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains("Visit Request"));
		assertTrue(driver.getPageSource().contains(stamp));
	}

	public void testPatientSendReply() throws Exception {
		driver = login("2", "pw");
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 2L, 0L, "");
		driver.findElement(By.linkText("Read")).click();
	//	assertLogged(TransactionType.MESSAGE_VIEW, 2L, 9000000000L, "");
		driver.findElement(By.linkText("Reply")).click();
		driver.findElement(By.name("messageBody")).clear();
		driver.findElement(By.name("messageBody")).sendKeys("Which office visit did you update?");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertLogged(TransactionType.MESSAGE_SEND, 2L, 9000000000L, "");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		driver.findElement(By.linkText("Message Outbox")).click();
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		assertTrue(driver.getPageSource().contains(stamp));
		assertLogged(TransactionType.OUTBOX_VIEW, 2L, 0L, "");

		driver = login("9000000000", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 9000000000L, 0L, "");
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains("RE: Office Visit Updated"));
		assertTrue(driver.getPageSource().contains(stamp));
	}

}