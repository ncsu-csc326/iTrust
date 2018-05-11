package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.Button;
import com.meterware.httpunit.DialogAdapter;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * Tests that patients can edit and delete entries in their
 * food diary.
 *
 */
public class EditAndDeleteFoodDiaryTest extends iTrustHTTPTest {

	/**
	 * Make sure we get the standard data for each call
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.standardData();
	}
	
	/**
	 * Test that a user can edit entries in their food diary.
	 */
	public void testEditFoodDiaryEntryValidValues() throws Exception {
		WebConversation wc = login("336", "pw"); //login as Emily Prentiss
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
		//assert that the has the original values
		WebForm wf = wr.getForms()[0];
		wf = wr.getFormWithID("beanForm");
		assertEquals("03/16/2014", wf.getParameterValue("Date:0"));
		assertEquals("Lunch", wf.getParameterValue("MealType:0"));
		assertEquals("Chocolate Shake", wf.getParameterValue("FoodName:0"));
		assertEquals("2.0", wf.getParameterValue("Servings:0"));
		assertEquals("500.0", wf.getParameterValue("Calories:0"));
		assertEquals("23.5", wf.getParameterValue("Fat:0"));
		assertEquals("259.0", wf.getParameterValue("Sodium:0"));
		assertEquals("66.5", wf.getParameterValue("Carb:0"));
		assertEquals("0.0", wf.getParameterValue("Fiber:0"));
		assertEquals("42.4", wf.getParameterValue("Sugar:0"));
		assertEquals("5.9", wf.getParameterValue("Protein:0"));
		
		//assert that the totals are correct
		WebTable wt2 = wr.getTables()[2];
		assertEquals("03/16/2014", wt2.getCellAsText(1, 0));
		assertEquals("1000.0", wt2.getCellAsText(1, 1));
		assertEquals("47.0", wt2.getCellAsText(1, 2));
		assertEquals("518.0", wt2.getCellAsText(1, 3));
		assertEquals("133.0", wt2.getCellAsText(1, 4));
		assertEquals("0.0", wt2.getCellAsText(1, 5));
		assertEquals("84.8", wt2.getCellAsText(1, 6));
		assertEquals("11.8", wt2.getCellAsText(1, 7));
		
		//click the edit entry button
		wf.getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		wf.setParameter("Servings:0", "3");
		wf.setParameter("Calories:0", "1327");
		wf.setParameter("Fat:0", "62.5");
		wf.setParameter("Sodium:0", "687");
		wf.setParameter("Carb:0", "176.4");
		wf.setParameter("Sugar:0", "112.4");
		wf.setParameter("Protein:0", "15.6");
		
		//click the submit button
		wf.getButtons()[0].click();

		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		assertTrue(wr.getText().contains("Congratulations!"));
		
		//now assert that everything is the new stuff
		assertEquals("03/16/2014", wf.getParameterValue("Date:0"));
		assertEquals("Lunch", wf.getParameterValue("MealType:0"));
		assertEquals("Chocolate Shake", wf.getParameterValue("FoodName:0"));
		assertEquals("3.0", wf.getParameterValue("Servings:0"));
		assertEquals("1327.0", wf.getParameterValue("Calories:0"));
		assertEquals("62.5", wf.getParameterValue("Fat:0"));
		assertEquals("687.0", wf.getParameterValue("Sodium:0"));
		assertEquals("176.4", wf.getParameterValue("Carb:0"));
		assertEquals("0.0", wf.getParameterValue("Fiber:0"));
		assertEquals("112.4", wf.getParameterValue("Sugar:0"));
		assertEquals("15.6", wf.getParameterValue("Protein:0"));
		
		//assert that the totals are correct
		wt2 = wr.getTables()[2];
		assertEquals("03/16/2014", wt2.getCellAsText(1, 0));
		assertEquals("3981.0", wt2.getCellAsText(1, 1));
		assertEquals("187.5", wt2.getCellAsText(1, 2));
		assertEquals("2061.0", wt2.getCellAsText(1, 3));
		assertEquals("529.2", wt2.getCellAsText(1, 4));
		assertEquals("0.0", wt2.getCellAsText(1, 5));
		assertEquals("337.2", wt2.getCellAsText(1, 6).substring(0, 5));
		assertEquals("46.8", wt2.getCellAsText(1, 7));

		
		
	}
	
	/**
	 * Tests that a Patient cannot enter invalid values
	 * when editing a Food Diary entry.
	 */
	public void testEditFoodDiaryEntryInvalidValues() throws Exception {
		WebConversation wc = login("335", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click();
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		//assert that the has the original values
		WebForm wf = wr.getForms()[0];
		wf = wr.getFormWithID("beanForm");
		
		assertEquals("04/13/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Oreos", wf.getParameterValue("FoodName:0"));
		assertEquals("53.0", wf.getParameterValue("Servings:0"));
		assertEquals("140.0", wf.getParameterValue("Calories:0"));
		assertEquals("7.0", wf.getParameterValue("Fat:0"));
		assertEquals("90.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("1.0", wf.getParameterValue("Fiber:0"));
		assertEquals("13.0", wf.getParameterValue("Sugar:0"));
		assertEquals("0.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("05/21/2013", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Cheese and Bean Dip", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("0.75", wf.getParameterValue("Servings:1"));
		assertEquals("45.0", wf.getParameterValue("Calories:1"));
		assertEquals("2.0", wf.getParameterValue("Fat:1"));
		assertEquals("230.0", wf.getParameterValue("Sodium:1"));
		assertEquals("5.0", wf.getParameterValue("Carb:1"));
		assertEquals("2.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("2.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		WebTable wt2 = wr.getTables()[2];
		assertEquals("04/13/2014", wt2.getCellAsText(1, 0));
		assertEquals("7420.0", wt2.getCellAsText(1, 1));
		assertEquals("371.0", wt2.getCellAsText(1, 2));
		assertEquals("4770.0", wt2.getCellAsText(1, 3));
		assertEquals("1113.0", wt2.getCellAsText(1, 4));
		assertEquals("53.0", wt2.getCellAsText(1, 5));
		assertEquals("689.0", wt2.getCellAsText(1, 6));
		assertEquals("0.0", wt2.getCellAsText(1, 7));
		
		assertEquals("05/21/2013", wt2.getCellAsText(2, 0));
		assertEquals("33.75", wt2.getCellAsText(2, 1));
		assertEquals("1.5", wt2.getCellAsText(2, 2));
		assertEquals("172.5", wt2.getCellAsText(2, 3));
		assertEquals("3.75", wt2.getCellAsText(2, 4));
		assertEquals("1.5", wt2.getCellAsText(2, 5));
		assertEquals("0.0", wt2.getCellAsText(2, 6));
		assertEquals("1.5", wt2.getCellAsText(2, 7));
		
		//click the edit button on Oreos row (first row)
		wf.getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		wf.setParameter("Servings:0", "-17");
		wf.getButtons()[0].click();
		
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		//assert nothing changed and we got an error message
		assertTrue(wr.getText().contains("Number of Servings must be "
				+ "greater than 0"));
		//assert nothing changed
		
		assertEquals("04/13/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Oreos", wf.getParameterValue("FoodName:0"));
		assertEquals("53.0", wf.getParameterValue("Servings:0"));
		assertEquals("140.0", wf.getParameterValue("Calories:0"));
		assertEquals("7.0", wf.getParameterValue("Fat:0"));
		assertEquals("90.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("1.0", wf.getParameterValue("Fiber:0"));
		assertEquals("13.0", wf.getParameterValue("Sugar:0"));
		assertEquals("0.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("05/21/2013", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Cheese and Bean Dip", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("0.75", wf.getParameterValue("Servings:1"));
		assertEquals("45.0", wf.getParameterValue("Calories:1"));
		assertEquals("2.0", wf.getParameterValue("Fat:1"));
		assertEquals("230.0", wf.getParameterValue("Sodium:1"));
		assertEquals("5.0", wf.getParameterValue("Carb:1"));
		assertEquals("2.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("2.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		wt2 = wr.getTables()[2];
		assertEquals("04/13/2014", wt2.getCellAsText(1, 0));
		assertEquals("7420.0", wt2.getCellAsText(1, 1));
		assertEquals("371.0", wt2.getCellAsText(1, 2));
		assertEquals("4770.0", wt2.getCellAsText(1, 3));
		assertEquals("1113.0", wt2.getCellAsText(1, 4));
		assertEquals("53.0", wt2.getCellAsText(1, 5));
		assertEquals("689.0", wt2.getCellAsText(1, 6));
		assertEquals("0.0", wt2.getCellAsText(1, 7));
		
		assertEquals("05/21/2013", wt2.getCellAsText(2, 0));
		assertEquals("33.75", wt2.getCellAsText(2, 1));
		assertEquals("1.5", wt2.getCellAsText(2, 2));
		assertEquals("172.5", wt2.getCellAsText(2, 3));
		assertEquals("3.75", wt2.getCellAsText(2, 4));
		assertEquals("1.5", wt2.getCellAsText(2, 5));
		assertEquals("0.0", wt2.getCellAsText(2, 6));
		assertEquals("1.5", wt2.getCellAsText(2, 7));
		
	}
	
	/**
	 * Tests that a user will be presented with a popup before
	 * completely deleting an entry.
	 */
	public void testDeleteFoodDiaryEntry() throws Exception {
		WebConversation wc = login("334", "pw"); //login as Jennifer
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
		//assert that the has the original values
		WebForm wf = wr.getForms()[0];
		wf = wr.getFormWithID("beanForm");
		assertEquals("09/30/2012", wf.getParameterValue("Date:0"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:0"));
		assertEquals("Hot dog", wf.getParameterValue("FoodName:0"));
		assertEquals("4.0", wf.getParameterValue("Servings:0"));
		assertEquals("80.0", wf.getParameterValue("Calories:0"));
		assertEquals("5.0", wf.getParameterValue("Fat:0"));
		assertEquals("480.0", wf.getParameterValue("Sodium:0"));
		assertEquals("2.0", wf.getParameterValue("Carb:0"));
		assertEquals("0.0", wf.getParameterValue("Fiber:0"));
		assertEquals("0.0", wf.getParameterValue("Sugar:0"));
		assertEquals("5.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("09/30/2012", wf.getParameterValue("Date:1"));
		assertEquals("Lunch", wf.getParameterValue("MealType:1"));
		assertEquals("Mango Passionfruit Juice", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("1.2", wf.getParameterValue("Servings:1"));
		assertEquals("130.0", wf.getParameterValue("Calories:1"));
		assertEquals("0.0", wf.getParameterValue("Fat:1"));
		assertEquals("25.0", wf.getParameterValue("Sodium:1"));
		assertEquals("32.0", wf.getParameterValue("Carb:1"));
		assertEquals("0.0", wf.getParameterValue("Fiber:1"));
		assertEquals("29.0", wf.getParameterValue("Sugar:1"));
		assertEquals("1.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		WebTable wt2 = wr.getTables()[2];
		assertEquals("09/30/2012", wt2.getCellAsText(1, 0));
		assertEquals("476.0", wt2.getCellAsText(1, 1));
		assertEquals("20.0", wt2.getCellAsText(1, 2));
		assertEquals("1950.0", wt2.getCellAsText(1, 3));
		assertEquals("46.4", wt2.getCellAsText(1, 4));
		assertEquals("0.0", wt2.getCellAsText(1, 5));
		assertEquals("34.8", wt2.getCellAsText(1, 6));
		assertEquals("21.2", wt2.getCellAsText(1, 7));
		
		//now click to delete the first entry
		Button button = wf.getButtons()[2];
				
		//set it to OK the prompt
		DialogAdapter da = new DialogAdapter();
		wc.setDialogResponder(da);
		
		button.click();
		//now it should all have been updated
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		assertEquals("09/30/2012", wf.getParameterValue("Date:0"));
		assertEquals("Lunch", wf.getParameterValue("MealType:0"));
		assertEquals("Mango Passionfruit Juice", 
					wf.getParameterValue("FoodName:0"));
		assertEquals("1.2", wf.getParameterValue("Servings:0"));
		assertEquals("130.0", wf.getParameterValue("Calories:0"));
		assertEquals("0.0", wf.getParameterValue("Fat:0"));
		assertEquals("25.0", wf.getParameterValue("Sodium:0"));
		assertEquals("32.0", wf.getParameterValue("Carb:0"));
		assertEquals("0.0", wf.getParameterValue("Fiber:0"));
		assertEquals("29.0", wf.getParameterValue("Sugar:0"));
		assertEquals("1.0", wf.getParameterValue("Protein:0"));
		
		//assert that the totals are correct
		wt2 = wr.getTables()[2];
		assertEquals("09/30/2012", wt2.getCellAsText(1, 0));
		assertEquals("156.0", wt2.getCellAsText(1, 1));
		assertEquals("0.0", wt2.getCellAsText(1, 2));
		assertEquals("30.0", wt2.getCellAsText(1, 3));
		assertEquals("38.4", wt2.getCellAsText(1, 4));
		assertEquals("0.0", wt2.getCellAsText(1, 5));
		assertEquals("34.8", wt2.getCellAsText(1, 6));
		assertEquals("1.2", wt2.getCellAsText(1, 7));
		
	}
	
	/**
	 * Tests that a user can cancel deletion of an entry and that
	 * nothing will happen.
	 */
	public void testCancelDelete() throws Exception {
		WebConversation wc = login("335", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click();
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		//assert that the has the original values
		WebForm wf = wr.getForms()[0];
		wf = wr.getFormWithID("beanForm");
		
		assertEquals("04/13/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Oreos", wf.getParameterValue("FoodName:0"));
		assertEquals("53.0", wf.getParameterValue("Servings:0"));
		assertEquals("140.0", wf.getParameterValue("Calories:0"));
		assertEquals("7.0", wf.getParameterValue("Fat:0"));
		assertEquals("90.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("1.0", wf.getParameterValue("Fiber:0"));
		assertEquals("13.0", wf.getParameterValue("Sugar:0"));
		assertEquals("0.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("05/21/2013", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Cheese and Bean Dip", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("0.75", wf.getParameterValue("Servings:1"));
		assertEquals("45.0", wf.getParameterValue("Calories:1"));
		assertEquals("2.0", wf.getParameterValue("Fat:1"));
		assertEquals("230.0", wf.getParameterValue("Sodium:1"));
		assertEquals("5.0", wf.getParameterValue("Carb:1"));
		assertEquals("2.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("2.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		WebTable wt2 = wr.getTables()[2];
		assertEquals("04/13/2014", wt2.getCellAsText(1, 0));
		assertEquals("7420.0", wt2.getCellAsText(1, 1));
		assertEquals("371.0", wt2.getCellAsText(1, 2));
		assertEquals("4770.0", wt2.getCellAsText(1, 3));
		assertEquals("1113.0", wt2.getCellAsText(1, 4));
		assertEquals("53.0", wt2.getCellAsText(1, 5));
		assertEquals("689.0", wt2.getCellAsText(1, 6));
		assertEquals("0.0", wt2.getCellAsText(1, 7));
		
		assertEquals("05/21/2013", wt2.getCellAsText(2, 0));
		assertEquals("33.75", wt2.getCellAsText(2, 1));
		assertEquals("1.5", wt2.getCellAsText(2, 2));
		assertEquals("172.5", wt2.getCellAsText(2, 3));
		assertEquals("3.75", wt2.getCellAsText(2, 4));
		assertEquals("1.5", wt2.getCellAsText(2, 5));
		assertEquals("0.0", wt2.getCellAsText(2, 6));
		assertEquals("1.5", wt2.getCellAsText(2, 7));
		
		//set it to "Cancel" the popup confirmation window
		MyDialogAdapter da = new MyDialogAdapter();
		wc.setDialogResponder(da);
		
		//click the delete button on Oreos row (first row)
		wf.getButtons()[2].click();
		
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		
		//assert nothing changed
		
		assertEquals("04/13/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Oreos", wf.getParameterValue("FoodName:0"));
		assertEquals("53.0", wf.getParameterValue("Servings:0"));
		assertEquals("140.0", wf.getParameterValue("Calories:0"));
		assertEquals("7.0", wf.getParameterValue("Fat:0"));
		assertEquals("90.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("1.0", wf.getParameterValue("Fiber:0"));
		assertEquals("13.0", wf.getParameterValue("Sugar:0"));
		assertEquals("0.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("05/21/2013", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Cheese and Bean Dip", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("0.75", wf.getParameterValue("Servings:1"));
		assertEquals("45.0", wf.getParameterValue("Calories:1"));
		assertEquals("2.0", wf.getParameterValue("Fat:1"));
		assertEquals("230.0", wf.getParameterValue("Sodium:1"));
		assertEquals("5.0", wf.getParameterValue("Carb:1"));
		assertEquals("2.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("2.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		wt2 = wr.getTables()[2];
		assertEquals("04/13/2014", wt2.getCellAsText(1, 0));
		assertEquals("7420.0", wt2.getCellAsText(1, 1));
		assertEquals("371.0", wt2.getCellAsText(1, 2));
		assertEquals("4770.0", wt2.getCellAsText(1, 3));
		assertEquals("1113.0", wt2.getCellAsText(1, 4));
		assertEquals("53.0", wt2.getCellAsText(1, 5));
		assertEquals("689.0", wt2.getCellAsText(1, 6));
		assertEquals("0.0", wt2.getCellAsText(1, 7));
		
		assertEquals("05/21/2013", wt2.getCellAsText(2, 0));
		assertEquals("33.75", wt2.getCellAsText(2, 1));
		assertEquals("1.5", wt2.getCellAsText(2, 2));
		assertEquals("172.5", wt2.getCellAsText(2, 3));
		assertEquals("3.75", wt2.getCellAsText(2, 4));
		assertEquals("1.5", wt2.getCellAsText(2, 5));
		assertEquals("0.0", wt2.getCellAsText(2, 6));
		assertEquals("1.5", wt2.getCellAsText(2, 7));
	}
	
	/**
	 * Tests that a user cannot enter an invalid date
	 * when editing a food diary entry.
	 */
	public void testEditEntryWithInvalidDate() throws Exception {
		WebConversation wc = login("335", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click();
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		//assert that the has the original values
		WebForm wf = wr.getForms()[0];
		wf = wr.getFormWithID("beanForm");
		
		assertEquals("04/13/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Oreos", wf.getParameterValue("FoodName:0"));
		assertEquals("53.0", wf.getParameterValue("Servings:0"));
		assertEquals("140.0", wf.getParameterValue("Calories:0"));
		assertEquals("7.0", wf.getParameterValue("Fat:0"));
		assertEquals("90.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("1.0", wf.getParameterValue("Fiber:0"));
		assertEquals("13.0", wf.getParameterValue("Sugar:0"));
		assertEquals("0.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("05/21/2013", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Cheese and Bean Dip", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("0.75", wf.getParameterValue("Servings:1"));
		assertEquals("45.0", wf.getParameterValue("Calories:1"));
		assertEquals("2.0", wf.getParameterValue("Fat:1"));
		assertEquals("230.0", wf.getParameterValue("Sodium:1"));
		assertEquals("5.0", wf.getParameterValue("Carb:1"));
		assertEquals("2.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("2.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		WebTable wt2 = wr.getTables()[2];
		assertEquals("04/13/2014", wt2.getCellAsText(1, 0));
		assertEquals("7420.0", wt2.getCellAsText(1, 1));
		assertEquals("371.0", wt2.getCellAsText(1, 2));
		assertEquals("4770.0", wt2.getCellAsText(1, 3));
		assertEquals("1113.0", wt2.getCellAsText(1, 4));
		assertEquals("53.0", wt2.getCellAsText(1, 5));
		assertEquals("689.0", wt2.getCellAsText(1, 6));
		assertEquals("0.0", wt2.getCellAsText(1, 7));
		
		assertEquals("05/21/2013", wt2.getCellAsText(2, 0));
		assertEquals("33.75", wt2.getCellAsText(2, 1));
		assertEquals("1.5", wt2.getCellAsText(2, 2));
		assertEquals("172.5", wt2.getCellAsText(2, 3));
		assertEquals("3.75", wt2.getCellAsText(2, 4));
		assertEquals("1.5", wt2.getCellAsText(2, 5));
		assertEquals("0.0", wt2.getCellAsText(2, 6));
		assertEquals("1.5", wt2.getCellAsText(2, 7));
		
		//click the edit button on Oreos row (first row)
		wf.getButtons()[1].click();
		
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		wf.setParameter("Date:0", "2015/04/04");
		wf.getButtons()[0].click();
		
		wr = wc.getCurrentPage();
		wf = wr.getForms()[1];
		
		//assert nothing changed and we got an error message
		assertTrue(wr.getText().contains("Date Eaten: MM/DD/YYYY"));
		//assert nothing changed
		
		assertEquals("04/13/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Oreos", wf.getParameterValue("FoodName:0"));
		assertEquals("53.0", wf.getParameterValue("Servings:0"));
		assertEquals("140.0", wf.getParameterValue("Calories:0"));
		assertEquals("7.0", wf.getParameterValue("Fat:0"));
		assertEquals("90.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("1.0", wf.getParameterValue("Fiber:0"));
		assertEquals("13.0", wf.getParameterValue("Sugar:0"));
		assertEquals("0.0", wf.getParameterValue("Protein:0"));
		
		assertEquals("05/21/2013", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Cheese and Bean Dip", 
					wf.getParameterValue("FoodName:1"));
		assertEquals("0.75", wf.getParameterValue("Servings:1"));
		assertEquals("45.0", wf.getParameterValue("Calories:1"));
		assertEquals("2.0", wf.getParameterValue("Fat:1"));
		assertEquals("230.0", wf.getParameterValue("Sodium:1"));
		assertEquals("5.0", wf.getParameterValue("Carb:1"));
		assertEquals("2.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("2.0", wf.getParameterValue("Protein:1"));
		
		//assert that the totals are correct
		wt2 = wr.getTables()[2];
		assertEquals("04/13/2014", wt2.getCellAsText(1, 0));
		assertEquals("7420.0", wt2.getCellAsText(1, 1));
		assertEquals("371.0", wt2.getCellAsText(1, 2));
		assertEquals("4770.0", wt2.getCellAsText(1, 3));
		assertEquals("1113.0", wt2.getCellAsText(1, 4));
		assertEquals("53.0", wt2.getCellAsText(1, 5));
		assertEquals("689.0", wt2.getCellAsText(1, 6));
		assertEquals("0.0", wt2.getCellAsText(1, 7));
		
		assertEquals("05/21/2013", wt2.getCellAsText(2, 0));
		assertEquals("33.75", wt2.getCellAsText(2, 1));
		assertEquals("1.5", wt2.getCellAsText(2, 2));
		assertEquals("172.5", wt2.getCellAsText(2, 3));
		assertEquals("3.75", wt2.getCellAsText(2, 4));
		assertEquals("1.5", wt2.getCellAsText(2, 5));
		assertEquals("0.0", wt2.getCellAsText(2, 6));
		assertEquals("1.5", wt2.getCellAsText(2, 7));
	}
	
	/**
	 * Used to set the confirmation to false so that
	 * it can test cancelling an intended deletion of a
	 * food entry.
	 *
	 */
	private class MyDialogAdapter extends DialogAdapter {
		@Override
		public boolean getConfirmation(String confirmationPrompt) {
			// TODO Auto-generated method stub
			return false;
		}

		public MyDialogAdapter() {
			super();
		}
	}
	
}
