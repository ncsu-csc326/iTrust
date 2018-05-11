<%@page errorPage="auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.AddFoodEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.action.EditFoodEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.action.ViewFoodEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.beans.FoodEntryBean" %>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder" %>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>
<%@page import="java.lang.IllegalArgumentException" %>
<%@page import="java.lang.Double" %>
<%@page import="java.lang.NumberFormatException" %>
<%@page import="java.lang.IllegalArgumentException" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.ncsu.csc.itrust.enums.MealType" %>

<%@include file="/global.jsp" %>

<%
pageTitle="Add a Food Entry";
%>

<%@include file="/header.jsp"%>

<%
	FoodEntryBean newEntry = new FoodEntryBean();
	AddFoodEntryAction addAction = new AddFoodEntryAction(prodDAO, loggedInMID.longValue());
	Date date = new Date();
	String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(date);
	
	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");

	MealType[] mealTypes = MealType.values();
	
	if (formIsFilled) {
		try {
			//can't use BeanBuilder.build because not all are strings
			//so we get all of them in order
			String dateEaten = request.getParameter("dateEatenStr");
			String mealType = request.getParameter("mealType");
			String food = request.getParameter("food");
			String servings = request.getParameter("servings");
			String calories = request.getParameter("calories");
			String fat = request.getParameter("fat");
			String sodium = request.getParameter("sodium");
			String carb = request.getParameter("carb");
			String fiber = request.getParameter("fiber");
			String sugar = request.getParameter("sugar");
			String protein = request.getParameter("protein");
			Long labelEntry = null;
			
			//now make sure none are null (need both checks here)
			if (dateEaten != null && !dateEaten.equals("")) {
				newEntry.setDateEatenStr(dateEaten);
			}
			if (mealType != null && !mealType.equals("")) {
				newEntry.setMealType(mealType);
			}
			if (food != null && !food.equals("")) {
				newEntry.setFood(food);
			}
			if (servings != null && !servings.equals("")) {
				newEntry.setServings(Double.parseDouble(servings));
			}
			if (calories != null && !calories.equals("")) {
				newEntry.setCalories(Double.parseDouble(calories));
			}
			if (fat != null && !fat.equals("")) {
				newEntry.setFatGrams(Double.parseDouble(fat));
			}
			if (sodium != null && !sodium.equals("")) {
				newEntry.setMilligramsSodium(Double.parseDouble(sodium));
			}
			if (carb != null && !carb.equals("")) {
				newEntry.setCarbGrams(Double.parseDouble(carb));
			}
			if (fiber != null && !fiber.equals("")) {
				newEntry.setFiberGrams(Double.parseDouble(fiber));
			}
			if (sugar != null && !sugar.equals("")) {
				newEntry.setSugarGrams(Double.parseDouble(sugar));
			}
			if (protein != null && !protein.equals("")) {
				newEntry.setProteinGrams(Double.parseDouble(protein));
			}
			//Get label information for other entries on that date
			FoodEntryBean labelUpdaterBean = new FoodEntryBean();
			List<FoodEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<FoodEntryBean>();		
			ViewFoodEntryAction viewAction = new ViewFoodEntryAction(prodDAO, loggedInMID.longValue());
			EditFoodEntryAction editAction = new EditFoodEntryAction(prodDAO, loggedInMID.longValue());
			listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(dateEaten, 
					dateEaten, loggedInMID.longValue());
			if (listOfEntriesRequiringLabelUpdate.size() > 0) {
				labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(0);
				labelEntry = labelUpdaterBean.getLabelID();
			}
			
			//now add it to the food diary
			addAction.addEntry(newEntry);
			loggingAction.logEvent(TransactionType.FOOD_DIARY_ADD, 
					loggedInMID.longValue(), loggedInMID.longValue(), "");
			
			//if add was successful, then apply that day's label to all entries
			if (labelEntry != null) {
				listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(dateEaten, 
						dateEaten, loggedInMID.longValue());
				for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
					labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(i);
					labelUpdaterBean.setLabelID(labelEntry);
					editAction.editEntry(labelUpdaterBean);
				}
			}
			//iff adding was successful, then send them to their food diary
			response.sendRedirect("myFoodDiary.jsp");
			
		} catch (FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
			</div>
<%
		} catch (NumberFormatException d) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
			</div>
<%
		} catch (IllegalArgumentException b) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(b.getMessage())%></span>
			</div>
<%
		}
	}
%>

<div align=center>
	<h2>Add a Food Entry</h2>

	<form action="addFoodEntry.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<table class="fTable" cellspacing=0 align=center cellpadding=0>
			<tr>
				<td class="subHeaderVertical">Date Eaten:</td>
				<%//TODO make the default value equal to the current date %>
				<td><input name="dateEatenStr" type="text" 
				value="<%=StringEscapeUtils.escapeHtml(dateStr) %>"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Type of Meal:</td>
				<td><select name="mealType">
					<option value="">Select:</option>
<% 					for (MealType meal: mealTypes) {
%>
						<option value="<%= 
							StringEscapeUtils.escapeHtml(meal.getName()) %>">
						<%= meal.getName() %> </option>
<%
					}
%>
					
				</select>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name of Food:</td>
				<td><input name="food"  type="text"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Servings:</td>
				<td><input name="servings" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Calories per Serving:</td>
				<td><input name="calories" type="text" value="0.0"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Grams of Fat per Serving:</td>
				<td><input name="fat" type="text" value="0.0"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Milligrams of Sodium per Serving:</td>
				<td><input name="sodium" type="text" value="0.0"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Grams of Carbs per Serving:</td>
				<td><input name="carb" type="text" value="0.0"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Grams of Fiber per Serving:</td>
				<td><input name="fiber" type="text" value="0.0"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Grams of Sugars per Serving:</td>
				<td><input name="sugar" type="text" value="0.0"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Grams of Protein per Serving:</td>
				<td><input name="protein" type="text" value="0.0"></td>
			</tr>
		</table>
	<br>
		<div align="center">
			<input type="submit" style="font-weight:bold;"
				value="Add entry to Food Diary"> 
		</div>
	</form>
	<br>
	<div align="center">
		<a href="myFoodDiary.jsp">Cancel entry 
			and return to your Food Diary.</a>
	</div>


</div>



<%@include file="/footer.jsp"%>