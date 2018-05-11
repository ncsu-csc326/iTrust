package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

@SuppressWarnings("unused")
public class ExpertHospitalTest extends iTrustHTTPTest {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();	
	}
	
	public void testRefactor(){
		
	}
	/*
	public void testEmptyHospitalList() throws Exception {
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find Expert Hospital").click();
		wr = wr.getLinkWith("13495-7").click();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("There are no hospitals for this procedure"));		
	}
	
	public void testHospitalListMoreThan5() throws Exception {
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find Expert Hospital").click();
		wr = wr.getLinkWith("10640-1").click();
		
		wr = wc.getCurrentPage();
		
		//Only the first two can be tested because all the other hospitals have 3 procedures
		//and drift up and down the list randomly.
		assertEquals("Health Institute Dr. E", wr.getTableWithID("htable").getCellAsText(2, 0));
		assertEquals("Ninja Hospital", wr.getTableWithID("htable").getCellAsText(3, 0));
	}
	
	public void testHospitalOrderedWithEmpty() throws Exception {
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find Expert Hospital").click();
		wr = wr.getLinkWith("10666-6").click();
		
		wr = wc.getCurrentPage();
		assertEquals("Health Institute Dr. E", wr.getTableWithID("htable").getCellAsText(2, 0));
		assertEquals("Health Institute Mr. Barry", wr.getTableWithID("htable").getCellAsText(3, 0));
		assertEquals("Ninja Hospital", wr.getTableWithID("htable").getCellAsText(4, 0));
	}
	
	public void testHospitalOrderedDescending() throws Exception {
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Find Expert Hospital").click();
		wr = wr.getLinkWith("10763-1").click();
		
		wr = wc.getCurrentPage();
		assertEquals("Health Institute Dr. E", wr.getTableWithID("htable").getCellAsText(2, 0));
		assertEquals("Health Institute Mr. Barry", wr.getTableWithID("htable").getCellAsText(3, 0));
		assertEquals("Health Institute Mr. Donghoon", wr.getTableWithID("htable").getCellAsText(4, 0));
		assertEquals("Le Awesome Hospital", wr.getTableWithID("htable").getCellAsText(5, 0));
		assertEquals("Ninja Hospital", wr.getTableWithID("htable").getCellAsText(6, 0));
	}*/
}
