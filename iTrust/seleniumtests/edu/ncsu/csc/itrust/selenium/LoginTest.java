package edu.ncsu.csc.itrust.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.meterware.httpunit.HttpUnitOptions;

public class LoginTest extends iTrustSeleniumTest {
	
	/*
	 * The URL for iTrust, change as needed
	 */
	/**ADDRESS*/
	public static final String ADDRESS = "http://localhost:8080/iTrust/";

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		// turn off htmlunit warnings
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testNonNumericLogin() {
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(ADDRESS);
		// log in using the given username and password
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("foo");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("1234");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		assertFalse(driver.getPageSource().contains("NumberFormatException"));
	}
}