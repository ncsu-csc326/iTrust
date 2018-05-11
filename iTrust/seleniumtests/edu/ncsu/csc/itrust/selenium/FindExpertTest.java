package edu.ncsu.csc.itrust.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import edu.ncsu.csc.itrust.enums.TransactionType;

/*
 * This is the Selenium equivalent of FindExpertTest.java
 */
public class FindExpertTest extends iTrustSeleniumTest{
	@Override
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.clearAllTables();
		gen.standardData();
		gen.uc47SetUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		gen.uc47TearDown();
		gen.clearAllTables();
	}
	
	public void testEditAndFindExpert() throws Exception {
		// login admin
		HtmlUnitDriver driver = (HtmlUnitDriver)login("9000000001", "pw");
		String title = driver.getTitle();
		assertEquals("iTrust - Admin Home", title);
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on Edit Personnel
		driver.findElement(By.linkText("Edit Personnel")).click();
		// find hcp
		title = driver.getTitle();
		assertEquals("iTrust - Please Select a Personnel", title);
		driver.findElement(By.name("FIRST_NAME")).sendKeys("Kelly");
		driver.findElement(By.name("LAST_NAME")).sendKeys("Doctor");
		driver.findElement(By.name("LAST_NAME")).submit();
		
		//focus
		driver.findElement(By.xpath(".//*[@value='9000000000']")).click();
		//submit the form
		driver.findElement(By.xpath(".//*[@value='9000000000']")).submit();
		
		title = driver.getTitle();
		assertEquals("iTrust - Edit Personnel", title);
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("919-100-1000");
		driver.findElement(By.name("action")).click();
		
		assertTrue(driver.getPageSource().contains("Information Successfully Updated"));
		assertLogged(TransactionType.LHCP_EDIT, 9000000001L, 9000000000L, "");
		//logout admin
		WebElement link = driver.findElements(By.tagName("li")).get(1);
		link.findElement(By.tagName("a")).click();
		
		//login patient
		title = driver.getTitle();
		assertEquals("iTrust - Login", title);
		driver = (HtmlUnitDriver)login("1", "pw");
		title = driver.getTitle();
		assertEquals("iTrust - Patient Home", title);
		//click on Find an Expert
		driver.findElement(By.linkText("Find an Expert")).click();
		title = driver.getTitle();
		assertEquals("iTrust - Find an Expert", title);
		
		//Search for surgeon
	    Select oSelection = new Select(driver.findElement(By.name("specialty")));
        oSelection.selectByVisibleText("Surgeon");
		driver.findElement(By.name("findExpert")).click();
		title = driver.getTitle();
		assertEquals("iTrust - Find an Expert", title);
		assertTrue(driver.getPageSource().contains("Kelly Doctor"));
		
	}
}