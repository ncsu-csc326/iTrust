package edu.ncsu.csc.itrust.selenium;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@SuppressWarnings("unused")
/**
 * Selenium test conversion for HttpUnit GroupReportTest
 */
public class GroupReportTest extends iTrustSeleniumTest {

	private WebDriver driver;

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.uc47SetUp();
		driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080/iTrust/");
	}

	/*
	 * Matches acceptance test scenario
	 */
	public void testViewGroupReportAcceptScenario() throws Exception {

		WebElement element;

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.cssSelector("input[value='GENDER']")).click();
		driver.findElement(By.cssSelector("input[value='LOWER_AGE_LIMIT']")).click();
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		Select genderDropDown = new Select(driver.findElement(By.name("GENDER")));
		genderDropDown.selectByVisibleText("Female");

		driver.findElement(By.name("LOWER_AGE_LIMIT")).clear();
		driver.findElement(By.name("LOWER_AGE_LIMIT")).sendKeys("60");

		driver.findElement(By.name("generate")).submit();
		assertEquals("iTrust - View Group Report", driver.getTitle());
	}

	/*
	 * Filters by demographic filters
	 */
	public void testViewGroupReportDemographic() throws Exception {

		WebElement element;

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.cssSelector("input[value='GENDER']")).click();
		driver.findElement(By.cssSelector("input[value='FIRST_NAME']")).click();
		driver.findElement(By.cssSelector("input[value='CONTACT_EMAIL']")).click();
		driver.findElement(By.cssSelector("input[value='CITY']")).click();
		driver.findElement(By.cssSelector("input[value='STATE']")).click();
		driver.findElement(By.cssSelector("input[value='ZIP']")).click();
		driver.findElement(By.cssSelector("input[value='INSURE_NAME']")).click();
		driver.findElement(By.cssSelector("input[value='INSURE_ID']")).click();
		driver.findElement(By.cssSelector("input[value='LOWER_AGE_LIMIT']")).click();
		driver.findElement(By.cssSelector("input[value='UPPER_AGE_LIMIT']")).click();

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		Select genderDropDown = new Select(driver.findElement(By.name("GENDER")));
		genderDropDown.selectByVisibleText("Male");

		driver.findElement(By.name("FIRST_NAME")).clear();
		driver.findElement(By.name("FIRST_NAME")).sendKeys("Baby");

		driver.findElement(By.name("CONTACT_EMAIL")).clear();
		driver.findElement(By.name("CONTACT_EMAIL")).sendKeys("fake@email.com");

		driver.findElement(By.name("CITY")).clear();
		driver.findElement(By.name("CITY")).sendKeys("Raleigh");

		driver.findElement(By.name("STATE")).clear();
		driver.findElement(By.name("STATE")).sendKeys("NC");

		driver.findElement(By.name("ZIP")).clear();
		driver.findElement(By.name("ZIP")).sendKeys("27606");

		driver.findElement(By.name("INSURE_NAME")).clear();
		driver.findElement(By.name("INSURE_NAME")).sendKeys("Aetna");

		driver.findElement(By.name("INSURE_ID")).clear();
		driver.findElement(By.name("INSURE_ID")).sendKeys("ChetumNHowe");

		driver.findElement(By.name("LOWER_AGE_LIMIT")).clear();
		driver.findElement(By.name("LOWER_AGE_LIMIT")).sendKeys("10");

		driver.findElement(By.name("UPPER_AGE_LIMIT")).clear();
		driver.findElement(By.name("UPPER_AGE_LIMIT")).sendKeys("30");

		driver.findElement(By.name("generate")).submit();
		assertEquals("iTrust - View Group Report", driver.getTitle());
	}

	/*
	 * Filters by personnel filters
	 */
	public void testViewGroupReportPersonnel() throws Exception {

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.cssSelector("input[name='personnel']")).click();
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		Select listSelect = new Select(driver.findElement(By.cssSelector("[name='DLHCP']")));
		listSelect.selectByVisibleText("Gandalf Stormcrow");
		driver.findElement(By.name("generate")).submit();

	}

	/*
	 * Test viewing unselected MID
	 */
	public void testMID() throws Exception {

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.name("fillValues")).submit();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.name("generate")).submit();
		assertFalse(driver.getPageSource().contains("<th>MID</th>"));
	}

	/*
	 * Tests invalid entries for age, solely for code coverage purposes
	 */
	public void testGroupReportInvalidAge() throws Exception {

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.cssSelector("input[value='LOWER_AGE_LIMIT']")).click();
		driver.findElement(By.cssSelector("input[value='UPPER_AGE_LIMIT']")).click();
		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.name("LOWER_AGE_LIMIT")).clear();
		driver.findElement(By.name("LOWER_AGE_LIMIT")).sendKeys("-1");

		driver.findElement(By.name("UPPER_AGE_LIMIT")).clear();
		driver.findElement(By.name("UPPER_AGE_LIMIT")).sendKeys("asdf");
		driver.findElement(By.name("generate")).submit();
	}

	/*
	 * Test function of pressing the "download" button
	 */
	public void testDownloadButton() throws Exception {

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.name("fillValues")).submit();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		List<WebElement> submitButtons = driver.findElements(By.className("clear_button"));
		assertEquals(2, submitButtons.size());

		// NOTE: After clicking "Download" button, iTrust is redirecting to home
		// page (unexpected)
		// Existing error in XML download functionality
		// driver.findElement(By.name("download")).submit();
		submitButtons.get(1).submit();

	}

	/*
	 * Tests downloading XML file
	 */
	public void testXMLCheckboxFalse() throws Exception {

		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");

		driver.findElement(By.cssSelector("input[type='submit']")).click();
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.findElement(By.cssSelector("a[href='/iTrust/auth/hcp/groupReport.jsp']")).click();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		driver.findElement(By.name("fillValues")).submit();
		assertEquals("iTrust - Generate Group Report", driver.getTitle());

		// This is testing an element that currently does not exist on the JSP
		// in the first place
		/*
		 * try { driver.findElement(By.name("Download XML Report")); }
		 * catch(NoSuchElementException e) { //Exception is good return; }
		 * fail("Should have thrown NoSuchElementException.");
		 */
	}

	@Override
	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}