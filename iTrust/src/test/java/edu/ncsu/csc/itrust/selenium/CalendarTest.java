package edu.ncsu.csc.itrust.selenium;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;

public class CalendarTest extends iTrustSeleniumTest {

	private HtmlUnitDriver driver = null;

	@Override
	protected void setUp() throws Exception {
		// Create a new instance of the driver
		driver = new HtmlUnitDriver();

		super.setUp(); // clear tables is called in super
		gen.clearAllTables();
		gen.standardData();
		// gen.officeVisit5();
	}

	@Override
	protected void tearDown() throws Exception {
		gen.clearAllTables();
		// gen.standardData();
	}

	public void testHCPViewAppointmentCalendar() throws Exception {
		// Login
		driver = (HtmlUnitDriver) login("9000000000", "pw");
		assertTrue(driver.getTitle().contains("iTrust - HCP Home"));

		// Click Calendar
		driver.findElement(By.linkText("Appointment Calendar")).click();

		// check title
		assertTrue(driver.getTitle().contains("Appointment Calendar"));
		assertLogged(TransactionType.CALENDAR_VIEW, 9000000000L, 0L, "");

		// check for the right appointments
		WebElement tableElem = driver.findElement(By.id("calendarTable"));
		List<WebElement> tableData = tableElem.findElements(By.tagName("tr"));
		Iterator<WebElement> rowsOnTable = tableData.iterator();

		while (rowsOnTable.hasNext()) {
			WebElement row = rowsOnTable.next();
			List<WebElement> j = row.findElements(By.tagName("td"));
			Iterator<WebElement> columnsOnTable = j.iterator();

			while (columnsOnTable.hasNext()) {
				WebElement column = columnsOnTable.next();

				if (column.getText().startsWith("5")) {
					// On the 5th: 1:30PM - General Checkup
					assertTrue(column.getText().contains("General Checkup"));
				} else if (column.getText().startsWith("18")) {
					// On the 18th: 8:00AM - Colonoscopy
					assertTrue(column.getText().contains("Colonoscopy"));
				} else if (column.getText().startsWith("28")) {
					// On the 28th: 9:00AM - Physical
					assertTrue(column.getText().contains("Physical"));
				}
			}
		}
	}

	public void testHCPViewAppointmentCalendarDetails() throws Exception {
		// Login
		driver = (HtmlUnitDriver) login("9000000000", "pw");
		assertTrue(driver.getTitle().contains("iTrust - HCP Home"));

		// Click Calendar
		driver.findElement(By.linkText("Appointment Calendar")).click();

		// check title
		assertTrue(driver.getTitle().contains("Appointment Calendar"));
		assertLogged(TransactionType.CALENDAR_VIEW, 9000000000L, 0L, "");

		List<WebElement> links = driver.findElements(By.tagName("a"));

		int count = 0;
		// get the second link with General Checkup-5
		for (int i = 0; i < links.size(); i++) {
			String name = links.get(i).getAttribute("name");

			if (name != null && name.contains("General Checkup-5")) {
				count++;
				if (count == 2) {
					links.get(i).click();
					break;
				}

			}
		}

		// ensure proper data is showing up
		assertTrue(driver.getPageSource().contains("Andy Programmer"));
		assertTrue(driver.getPageSource().contains("General Checkup"));

		assertTrue(driver.getPageSource().contains("45 minutes"));
		assertTrue(driver.getPageSource().contains("No Comment"));

		// get the current month and year
		Calendar cal = Calendar.getInstance();
		int month1 = cal.get(Calendar.MONTH) + 1;
		int day1 = 5;
		int year1 = cal.get(Calendar.YEAR);
		assertTrue(driver.getPageSource().contains(month1 + "/0" + day1 + "/" + year1 + " 09:10 AM"));

	}

}