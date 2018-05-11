package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


/**
 * Tests the functionality of the new Food Diary portion of iTrust.
 * Tests that you can add new entries, you can view entries, HCPs with the role
 * of nutritionist can view entries, and error checks that the user enters the appropriate
 * data in the appropriate format.
 *
 */
public class FoodDiaryTest extends iTrustHTTPTest {
	
	/**
	 * Currently, the new data is not part of the main call (standardData), 
	 * so we have to make
	 * sure we call it ourselves.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.standardData();
		//gen.uc68(); //create the 3 patients and the one nutritionist hcp
		//uc68() is now a part of standardData
	}
	

	/**
	 * Tests that a patient can add an entry to an empty Food Diary.
	 * @throws Exception
	 */
	public void testAddFoodEntryToEmptyFoodDiary() throws Exception {
		WebConversation wc = login("333", "pw"); //login as Derek Morgan
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());

		wr = wr.getLinkWith("Add an entry to your Food Diary").click();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		WebForm form = wr.getForms()[0];
		
		form.setParameter("dateEatenStr", "02/04/2015");
		form.setParameter("mealType", "Dinner");
		form.setParameter("food", "Fruity Pebbles");
		form.setParameter("servings", "7");
		form.setParameter("calories", "110");
		form.setParameter("fat", "1");
		form.setParameter("sodium", "170");
		form.setParameter("carb", "24");
		form.setParameter("fiber", "0");
		form.setParameter("sugar", "11");
		form.setParameter("protein", "1");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		form = wr.getForms()[1];
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		assertEquals("02/04/2015", form.getParameterValue("Date:0"));
		assertEquals("Dinner", form.getParameterValue("MealType:0"));
		assertEquals("Fruity Pebbles", form.getParameterValue("FoodName:0"));
		assertEquals("7.0", form.getParameterValue("Servings:0"));
		assertEquals("110.0", form.getParameterValue("Calories:0"));
		assertEquals("1.0", form.getParameterValue("Fat:0"));
		assertEquals("170.0", form.getParameterValue("Sodium:0"));
		assertEquals("24.0", form.getParameterValue("Carb:0"));
		assertEquals("0.0", form.getParameterValue("Fiber:0"));
		assertEquals("11.0", form.getParameterValue("Sugar:0"));
		assertEquals("1.0", form.getParameterValue("Protein:0"));
		
	}
	
	/**
	 * Tests that a patient can add an entry to a non empty food diary.
	 * @throws Exception
	 */
	public void testAddFoodEntryToNonEmptyFoodDiary() throws Exception {
		WebConversation wc = login("334", "pw"); //login as Jennifer Jareau
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
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
		
		wr = wr.getLinkWith("Add an entry to your Food Diary").click();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		WebForm form = wr.getForms()[0];
		
		form.setParameter("dateEatenStr", "11/12/2014");
		form.setParameter("mealType", "Snack");
		form.setParameter("food", "Cookie Dough Ice Cream");
		form.setParameter("servings", ".5");
		form.setParameter("calories", "160");
		form.setParameter("fat", "8");
		form.setParameter("sodium", "45");
		form.setParameter("carb", "21");
		form.setParameter("fiber", "0");
		form.setParameter("sugar", "16");
		form.setParameter("protein", "2");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
		//assert she has the new entry and the totals are correct
		wf = wr.getFormWithID("beanForm");
		
		//new entry
		assertEquals("11/12/2014", wf.getParameterValue("Date:0"));
		assertEquals("Snack", wf.getParameterValue("MealType:0"));
		assertEquals("Cookie Dough Ice Cream", 
						wf.getParameterValue("FoodName:0"));
		assertEquals("0.5", wf.getParameterValue("Servings:0"));
		assertEquals("160.0", wf.getParameterValue("Calories:0"));
		assertEquals("8.0", wf.getParameterValue("Fat:0"));
		assertEquals("45.0", wf.getParameterValue("Sodium:0"));
		assertEquals("21.0", wf.getParameterValue("Carb:0"));
		assertEquals("0.0", wf.getParameterValue("Fiber:0"));
		assertEquals("16.0", wf.getParameterValue("Sugar:0"));
		assertEquals("2.0", wf.getParameterValue("Protein:0"));
		
		//old entries
		assertEquals("09/30/2012", wf.getParameterValue("Date:1"));
		assertEquals("Breakfast", wf.getParameterValue("MealType:1"));
		assertEquals("Hot dog", wf.getParameterValue("FoodName:1"));
		assertEquals("4.0", wf.getParameterValue("Servings:1"));
		assertEquals("80.0", wf.getParameterValue("Calories:1"));
		assertEquals("5.0", wf.getParameterValue("Fat:1"));
		assertEquals("480.0", wf.getParameterValue("Sodium:1"));
		assertEquals("2.0", wf.getParameterValue("Carb:1"));
		assertEquals("0.0", wf.getParameterValue("Fiber:1"));
		assertEquals("0.0", wf.getParameterValue("Sugar:1"));
		assertEquals("5.0", wf.getParameterValue("Protein:1"));
		
		assertEquals("09/30/2012", wf.getParameterValue("Date:2"));
		assertEquals("Lunch", wf.getParameterValue("MealType:2"));
		assertEquals("Mango Passionfruit Juice", 
					wf.getParameterValue("FoodName:2"));
		assertEquals("1.2", wf.getParameterValue("Servings:2"));
		assertEquals("130.0", wf.getParameterValue("Calories:2"));
		assertEquals("0.0", wf.getParameterValue("Fat:2"));
		assertEquals("25.0", wf.getParameterValue("Sodium:2"));
		assertEquals("32.0", wf.getParameterValue("Carb:2"));
		assertEquals("0.0", wf.getParameterValue("Fiber:2"));
		assertEquals("29.0", wf.getParameterValue("Sugar:2"));
		assertEquals("1.0", wf.getParameterValue("Protein:2"));
		
		//assert that the totals are correct
		wt2 = wr.getTables()[2];
		//first entry
		assertEquals("11/12/2014", wt2.getCellAsText(1, 0));
		assertEquals("80.0", wt2.getCellAsText(1, 1));
		assertEquals("4.0", wt2.getCellAsText(1, 2));
		assertEquals("22.5", wt2.getCellAsText(1, 3));
		assertEquals("10.5", wt2.getCellAsText(1, 4));
		assertEquals("0.0", wt2.getCellAsText(1, 5));
		assertEquals("8.0", wt2.getCellAsText(1, 6));
		assertEquals("1.0", wt2.getCellAsText(1, 7));
		
		//second entry
		assertEquals("09/30/2012", wt2.getCellAsText(2, 0));
		assertEquals("476.0", wt2.getCellAsText(2, 1));
		assertEquals("20.0", wt2.getCellAsText(2, 2));
		assertEquals("1950.0", wt2.getCellAsText(2, 3));
		assertEquals("46.4", wt2.getCellAsText(2, 4));
		assertEquals("0.0", wt2.getCellAsText(2, 5));
		assertEquals("34.8", wt2.getCellAsText(2, 6));
		assertEquals("21.2", wt2.getCellAsText(2, 7));
	}
	
	/**
	 * Tests that HCPs can view a patients food diary.
	 * @throws Exception
	 */
	public void testHCPViewPatientFoodDiary() throws Exception {
		WebConversation wc = login("9000000071", "pw"); 
		//login as HCP Spencer Reid
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Patient Food Diaries").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"335");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		
		assertEquals("iTrust - View Patient Food Diaries", wr.getTitle());

		

	}
	
	/**
	 * Tests to ensure that a patient has to enter something for the food name.
	 * @throws Exception
	 */
	public void testAddEmptyFoodType() throws Exception {
		WebConversation wc = login("333", "pw"); 
		//login as Derek Morgan
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
		wr = wr.getLinkWith("Add an entry to your Food Diary").click();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		WebForm form = wr.getForms()[0];
		
		form.setParameter("dateEatenStr", "02/04/2015");
		form.setParameter("mealType", "Lunch");
		form.setParameter("food", "");
		form.setParameter("servings", "1");
		form.setParameter("calories", "175");
		form.setParameter("fat", "4");
		form.setParameter("sodium", "430");
		form.setParameter("carb", "8");
		form.setParameter("fiber", "2");
		form.setParameter("sugar", "6");
		form.setParameter("protein", "2");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		assertTrue(wr.getText().contains("The following field are not"
				+ " properly filled in: [Must enter the Name of the Food]"));
	
	}
	
	/**
	 * Tests to ensures users have to enter dates in the correct format 
	 * (mm/dd/yy).
	 * @throws Exception
	 */
	public void testInvalidDateEntry() throws Exception {
		WebConversation wc = login("333", "pw"); //login as Derek Morgan
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
		wr = wr.getLinkWith("Add an entry to your Food Diary").click();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		WebForm form = wr.getForms()[0];
		
		form.setParameter("dateEatenStr", "2015/04/03");
		form.setParameter("mealType", "Dinner");
		form.setParameter("food", "Soup");
		form.setParameter("servings", "1");
		form.setParameter("calories", "175");
		form.setParameter("fat", "4");
		form.setParameter("sodium", "430");
		form.setParameter("carb", "8");
		form.setParameter("fiber", "2");
		form.setParameter("sugar", "6");
		form.setParameter("protein", "2");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertEquals("Add a Food Entry", wr.getTitle());
	
	}
	
	/**
	 * Tests to ensure that HCPs can view a Patient's Food Diary when it is 
	 * empty.
	 * @throws Exception
	 */
	public void testViewEmptyFoodDiaryAsHCP() throws Exception {
		WebConversation wc = login("9000000071", "pw"); 
		//login as HCP Spencer Reid
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Patient Food Diaries").click(); 
		//go to the Food Diary page
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", 
				"333");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		
		
		assertEquals("iTrust - View Patient Food Diaries", wr.getTitle());
		assertTrue(wr.getText().contains("The selected patient's food "
				+ "diary is empty. If you were expecting entries please "
				+ "try again later!"));

	}
	
	/**
	 * Tests that you are not allowed to set the number of servings to 0.
	 * @throws Exception
	 */
	public void testSetNumberOfServingsToZero() throws Exception {
		WebConversation wc = login("333", "pw");
		
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click();
		
		
		wr = wr.getLinkWith("Add an entry to your Food Diary").click();
		
		WebForm form = wr.getForms()[0];
		
		form.setParameter("dateEatenStr", "03/04/2014");
		form.setParameter("mealType", "Dinner");
		form.setParameter("food", "Soup");
		form.setParameter("servings", "0");
		form.setParameter("calories", "175");
		form.setParameter("fat", "4");
		form.setParameter("sodium", "430");
		form.setParameter("carb", "8");
		form.setParameter("fiber", "2");
		form.setParameter("sugar", "6");
		form.setParameter("protein", "2");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		assertEquals("Add a Food Entry", wr.getTitle());
		assertTrue(wr.getText().contains("The following field are not "
				+ "properly filled in: [Number of Servings must be greater "
				+ "than 0]"));
		
	}
	
	/**
	 * Tests to ensure that you cannot enter a negative number of calories.
	 * @throws Exception
	 */
	public void testNegativeEntry() throws Exception {
		WebConversation wc = login("333", "pw"); //login as Derek Morgan
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("My Food Diary").click(); 
		//go to the Food Diary page
		
		assertEquals("iTrust - View My Food Diary", wr.getTitle());
		
		wr = wr.getLinkWith("Add an entry to your Food Diary").click();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		WebForm form = wr.getForms()[0];
		
		form.setParameter("dateEatenStr", "03/04/2014");
		form.setParameter("mealType", "Dinner");
		form.setParameter("food", "Soup");
		form.setParameter("servings", "1");
		form.setParameter("calories", "-175");
		form.setParameter("fat", "4");
		form.setParameter("sodium", "430");
		form.setParameter("carb", "8");
		form.setParameter("fiber", "2");
		form.setParameter("sugar", "6");
		form.setParameter("protein", "2");
		form.getSubmitButtons()[0].click();
		
		wr = wc.getCurrentPage();
		
		assertEquals("Add a Food Entry", wr.getTitle());
		assertTrue(wr.getText().contains("The following field are not "
				+ "properly filled in: [Calories per Serving cannot be "
				+ "negative]"));
	}
}
