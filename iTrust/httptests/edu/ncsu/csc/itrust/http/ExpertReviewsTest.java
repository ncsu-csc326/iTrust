package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;

/**
 * UC61 Tests
 */
public class ExpertReviewsTest extends iTrustHTTPTest {
	
	private TestDataGenerator gen;

	public void setUp() throws Exception{
		super.setUp();
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		gen.reviews();
	}
	
	public void testValidHCP() throws Exception{
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Find an Expert").click();
		WebForm form = wr.getForms()[0];
		form.setParameter("specialty", "Surgeon");
		form.setParameter("zipCode", "27607");
		form.setParameter("range", "250");
		wr = form.submit();
		assertTrue(wr.getText().contains("Surgeon"));
		assertTrue(wr.getText().contains("Physician Name:"));
		wr = wr.getLinkWithID("1").click();
		form = wr.getForms()[0];
		form.setParameter("title", "Too bored?");
		String[] rate = form.getOptions("rating");
		form.setParameter("rating", rate[2]);
		form.setParameter("description", "They seemed nice, but they asked how I was then started snoring.");
		wr = form.submit();
		assertTrue(wr.getText().contains("Too bored?"));
	}
	
	public void testInvalidHCP() throws Exception{
		WebConversation wc = login("109", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Find an Expert").click();
		WebForm form = wr.getForms()[0];
		form.setParameter("specialty", "Pediatrician");
		form.setParameter("zipCode", "27607");
		form.setParameter("range", "All");
		wr = form.submit();
		assertTrue(wr.getText().contains("Beaker Beaker"));
		wr = wr.getLinkWithID("1").click();
		assertTrue(!wr.getText().contains("Add a Review"));
	}
	
	public void testDirectRating() throws Exception{
		WebConversation wc = login("109", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Expert's Reviews").click();
		//The links are never on the webpage, so we need to manually do this, sorry.
		wr = wc.getResponse(ADDRESS + "auth/patient/reviewsPage.jsp?expertID=9000000000");
		assertEquals(ADDRESS + "auth/patient/reviewsPage.jsp", wr.getURL().toString());
		assertTrue(wr.getText().contains("Kelly Doctor is horrible!"));
		assertTrue(wr.getText().contains("Best doctor at this hospital!"));
		assertTrue(wr.getText().contains("So Bad."));
		assertTrue(wr.getText().contains("I am pretty happy"));
	}
	
	public void testOverallRating() throws Exception{
		WebConversation wc = login("22", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Expert's Reviews").click();
		//The links are never on the webpage, so we need to manually do this, sorry.
		wr = wc.getResponse(ADDRESS + "auth/patient/reviewsPage.jsp?expertID=9000000003");
		assertEquals(ADDRESS + "auth/patient/reviewsPage.jsp", wr.getURL().toString());
		assertTrue(wr.getText().contains("Gandalf Stormcrow"));
		assertTrue(wr.getText().contains("Pretty happy"));
		assertTrue(wr.getText().contains("Good service."));
		assertTrue(wr.getText().contains("Add a Review"));
	}
}
