package edu.ncsu.csc.itrust.selenium;

import org.junit.*;


import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;


import edu.ncsu.csc.itrust.enums.TransactionType;

public class EditLOINCDataHTTPTest extends iTrustSeleniumTest{
  private HtmlUnitDriver driver;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	super.setUp();
	gen.standardData();
    driver = new HtmlUnitDriver();
  }

  @Test
  public void testAddLOINCFile() throws Exception {
	  driver = (HtmlUnitDriver)login("9000000001", "pw");
	  assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
	  driver.findElement(By.linkText("Edit LOINC Codes")).click();
	  assertEquals("iTrust - Maintain LOINC Codes", driver.getTitle());
	  assertLogged(TransactionType.LOINC_CODE_VIEW, 9000000001L, 0L, "");
	  driver.setJavascriptEnabled(true);
	  driver.findElement(By.id("import")).click();
	  
	  
	  // Import the LOINC database file
	  driver.findElement(By.name("loincFile")).clear();
	  driver.findElement(By.name("loincFile")).sendKeys("testing-files/sample_loinc/sampleLoinc.txt");
	  driver.findElement(By.name("ignoreDupData")).clear();
	  driver.findElement(By.name("ignoreDupData")).sendKeys("1");
	  driver.findElement(By.id("sendFile")).click();
	  assertLogged(TransactionType.LOINC_CODE_FILE_ADD, 9000000001L, 0L, "");

	  // Check the Edit LOINC page for the updated codes
	  driver.findElement(By.linkText("Return to LOINC Codes List")).click();
	  WebElement table = driver.findElements(By.tagName("table")).get(1);
	  assertEquals(35, table.findElements(By.tagName("tr")).size());

	  assertFalse(driver.getPageSource().contains("THIS ONE IS DIFFERENT"));
  }
  
  @Test
  public void testAddLOINCFileNoIgnore() throws Exception {
	  driver = (HtmlUnitDriver)login("9000000001", "pw");
	  driver.setJavascriptEnabled(true);
	  driver.findElement(By.linkText("Edit LOINC Codes")).click();
	  driver.findElement(By.id("import")).click();
    
	  assertEquals("iTrust - Upload LOINC Codes", driver.getTitle());
	  driver.findElement(By.name("loincFile")).clear();
	  driver.findElement(By.name("loincFile")).sendKeys("testing-files/sample_loinc/sampleLoinc.txt");
	  new Select(driver.findElement(By.name("ignoreDupData"))).selectByVisibleText("Replace Duplicates");
	  driver.findElement(By.id("sendFile")).click();
	  assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains("Upload Successful"));
	  driver.findElement(By.linkText("Return to LOINC Codes List")).click();
	  // Warning: assertTextNotPresent may require manual changes
	  assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains("THIS ONE IS DIFFERENT"));
  }
  
  @Test
  public void testUploadLOINCFileInvalidLines() throws Exception {
	  driver = (HtmlUnitDriver)login("9000000001", "pw");
	  driver.setJavascriptEnabled(true);
	  driver.findElement(By.linkText("Edit LOINC Codes")).click();
	  driver.findElement(By.id("import")).click();
	  assertEquals("iTrust - Upload LOINC Codes", driver.getTitle());
	  driver.findElement(By.name("loincFile")).clear();
	  driver.findElement(By.name("loincFile")).sendKeys("testing-files/sample_loinc/invalidLine.txt");
	  new Select(driver.findElement(By.name("ignoreDupData"))).selectByVisibleText("Replace Duplicates");
	  driver.findElement(By.id("sendFile")).click();
	  assertTrue(driver.findElement(By.xpath("//div[@id='iTrustContent']/div/table/tbody/tr[2]/td/div")).getText().contains("ERROR, LINE 2: \"10054-5\" \"I skip rest of fields\" This form has not been validated correctly. The following field are not properly filled in: [You must have a Lab Procedure Code, Component and Kind Of Property]"));
	  assertTrue(driver.findElement(By.xpath("//div[@id='iTrustContent']/div/table/tbody/tr[2]/td/div")).getText().contains("Successfully added 2 lines of new LOINC data. Updated 0 lines of existing LOINC data."));
  }
  
  @Test
  public void testUploadBadLOINCFile() throws Exception {
	  driver = (HtmlUnitDriver)login("9000000001", "pw");
	  driver.setJavascriptEnabled(true);
	  driver.findElement(By.linkText("Edit LOINC Codes")).click();
	  driver.findElement(By.id("import")).click();

	  assertEquals("iTrust - Upload LOINC Codes", driver.getTitle());
	  driver.findElement(By.name("loincFile")).clear();
	  driver.findElement(By.name("loincFile")).sendKeys("testing-files/sample_loinc/badLoincFile.txt");
	  new Select(driver.findElement(By.name("ignoreDupData"))).selectByVisibleText("Ignore Duplicates");
	  driver.findElement(By.id("sendFile")).click();
	  assertTrue(driver.findElement(By.xpath("//div[@id='iTrustContent']/div/table/tbody/tr[2]/td/div")).getText().contains("IGNORED LINE 1: This file contains no LOINC data and should fail the LOINC file verification process."));
	  assertTrue(driver.findElement(By.xpath("//div[@id='iTrustContent']/div/table/tbody/tr[2]/td/div")).getText().contains("File invalid. No LOINC data added."));
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
}