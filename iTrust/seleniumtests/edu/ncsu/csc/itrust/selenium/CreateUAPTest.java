package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

public class CreateUAPTest extends iTrustSeleniumTest {

	public void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
		gen.hcp0();
		gen.cptCodes();
	}
	
	public void testCreateUAP1() throws Exception {
		WebDriver driver = new HtmlUnitDriver();
		driver = login("9000000000", "pw");
		driver.findElement(By.linkText("UAP")).click();
		assertEquals("iTrust - Add UAP", driver.getTitle());
		
		WebElement firstName = driver.findElement(By.name("firstName"));
		firstName.sendKeys("Drake");
		WebElement lastName = driver.findElement(By.name("lastName"));
		lastName.sendKeys("Ramoray");
		WebElement email = driver.findElement(By.name("email"));
		email.sendKeys("drake@drake.com");
		WebElement form = driver.findElement(By.name("formIsFilled"));
		form.submit();

	}
}
