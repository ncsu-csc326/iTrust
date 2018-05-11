<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.FoodEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.DeleteFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabelAction"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="edu.ncsu.csc.itrust.action.EditFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.enums.MealType"%>
<%@page import="java.lang.IllegalArgumentException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>


<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Food Diary";
%>

<%@include file="/header.jsp"%>

<div align=center>
	<h2>My Food Diary</h2>
	<%
		MealType[] mealTypes = MealType.values();
		loggingAction.logEvent(TransactionType.PATIENT_VIEW_FOOD_DIARY, 
		loggedInMID.longValue(), loggedInMID.longValue(), "");
	
		// Create a new action for viewing all of the food diary stuff
		ViewFoodEntryAction action = new ViewFoodEntryAction(prodDAO, loggedInMID.longValue());
		LabelAction labelAction = new LabelAction(prodDAO, loggedInMID);
		List<FoodEntryBean> foodBeans = new ArrayList<FoodEntryBean>();
		List<FoodEntryBean> foodBeansTotals = new ArrayList<FoodEntryBean>();
		List<LabelBean> labelBeans = new ArrayList<LabelBean>();
		labelBeans = labelAction.getLabels(loggedInMID);
	
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			
			// If they only gave us one date, use that for the range.
			if (startDate != null && (endDate == null || endDate.isEmpty())) {
				endDate = startDate;
			} else if (endDate != null && (startDate == null || startDate.isEmpty())) {
				startDate = endDate;
			}

			// Are we viewing within a specific range?
			boolean dateRange = startDate != null && endDate != null;
			
			boolean deleteEntry = request.getParameter("deleteEntry") != null
			&& request.getParameter("deleteEntry").equals("true");
			
			boolean editEntry = request.getParameter("editEntry") != null
			&& request.getParameter("editEntry").equals("true");
			
			String entryID = request.getParameter("entryID");
			long entryIDLong = -1; //initialize to invalid entryID
			if (entryID != null && !entryID.equals("")) {
		try {
			entryIDLong = Long.parseLong(entryID);
		} catch (NumberFormatException d) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
	</div>
	<%
		}
			}
			if (deleteEntry) {
		DeleteFoodEntryAction deleteAction = new DeleteFoodEntryAction(prodDAO, loggedInMID.longValue());
		try {
			int numDeleted = deleteAction.deleteEntry(entryIDLong);
			if (numDeleted == 1) {
		loggingAction.logEvent(TransactionType.DELETE_FOOD_ENTRY, 
			loggedInMID.longValue(), loggedInMID.longValue(), "");
			} else if (numDeleted == 0) {
	%>
	<div align=center>
		<span> <%=StringEscapeUtils.escapeHtml("No entries deleted")%></span>
	</div>
	<%
		}
		} catch (ITrustException d) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
	</div>
	<%
		}
			} else if (editEntry) {
		EditFoodEntryAction editAction = new EditFoodEntryAction(prodDAO, loggedInMID.longValue());
		FoodEntryBean newEntry = new FoodEntryBean();
		newEntry.setEntryID(entryIDLong);
		newEntry.setPatientID(loggedInMID.longValue());
		try {
		//can't use BeanBuilder.build because not all are strings
		//so we get all of them in order
		String labelEntry = request.getParameter("labelEntry");
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
		
		//now make sure none are null (need both checks here)
		if (labelEntry != null && !labelEntry.equals("")) {
			newEntry.setLabelID(Long.parseLong(labelEntry));
		}
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
		String message = "";
		int numUpdated = editAction.editEntry(newEntry);
		if (numUpdated == 1) {
			
			//if editing an entry is successful, change label for all entries on that day
			FoodEntryBean labelUpdaterBean = new FoodEntryBean();
			List<FoodEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<FoodEntryBean>();			
			listOfEntriesRequiringLabelUpdate = action.getBoundedDiary(dateEaten, 
					dateEaten, loggedInMID.longValue());
			for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
				labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(i);
				//just set it to the label of the entry (that way if it is "", we don't get an exception)
				labelUpdaterBean.setLabelID(newEntry.getLabelID());
				//labelUpdaterBean.setLabelID(Long.parseLong(labelEntry)); 
				editAction.editEntry(labelUpdaterBean);
			}
			
			loggingAction.logEvent(TransactionType.PATIENT_EDIT_FOOD_ENTRY,
			loggedInMID.longValue(), loggedInMID.longValue(), "");
			message = "Congratulations! You edited your food entry for "
			+ newEntry.getFood() + " from the date " 
			+ newEntry.getDateEatenStr() + ".";
		} else if (numUpdated == 0) {
			message = "Sorry. No food entries were updated.";
		} else if (numUpdated == -1) {
			message = "You cannot edit this food entry. It does not "
			+ "belong to you.";
		}
	%>
	<div align=center>
		<span><%=StringEscapeUtils.escapeHtml(message)%></span>
	</div>
	<%
		} catch (Exception e) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
	</div>
	<%
		}
		}

			try {
		if (dateRange) {
			foodBeans = action.getBoundedDiary(request.getParameter("startDate"), request.getParameter("endDate"), loggedInMID.longValue());
			foodBeansTotals = action.getBoundedDiaryTotals(request.getParameter("startDate"), request.getParameter("endDate"), loggedInMID.longValue());
		} else {
			foodBeans = action.getDiary(loggedInMID.longValue());
			foodBeansTotals = action.getDiaryTotals(loggedInMID.longValue());
		}
			} catch(Exception e) {
	%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
	</div>
	<%
		}
	%>
	<form id="filterForm" action="myFoodDiary.jsp" method="post">
		<table>
			<tr class="subHeader">
				<td>Start Date:</td>
				<td><input onchange="singleDate();" name="startDate" id="startDate"
					value="<%=StringEscapeUtils.escapeHtml((startDate == null ? "" : startDate))%>"
					size="10"> <input type="button" value="Select Date"
					onclick="displayDatePicker('startDate');"></td>
				<td>End Date:</td>
				<td><input onchange="singleDate();" name="endDate" id="endDate"
					value="<%=StringEscapeUtils.escapeHtml((endDate == null ? "" : endDate))%>"
					size="10">
					<input type="button" value="Select Date"
					onclick="displayDatePicker('endDate');"></td>
				<td><input onclick="singleDate();" type="submit" name="btn_filter"
					value="Filter Entries"></td>
			</tr>
		</table>
	</form>
	<br />
	<%
		if (foodBeans.size() > 0 && foodBeansTotals.size() > 0) {
	%>
	<div style="margin-left: 5px;">
		<form id="beanForm" action="myFoodDiary.jsp" method="post">
			<input type="hidden" id="beanID" name="entryID" value="-1"> <input
				type="hidden" id="deleteInput" name="deleteEntry" value="false">
			<input type="hidden" id="editInput" name="editEntry" value="false">
			<input type="hidden" id="labelEntryID" name="labelEntry" value="">
			<input type="hidden" id="labeColorID" name="labelColor" value="">
			<input type="hidden" id="dateEatenStrID" name="dateEatenStr" value="">
			<input type="hidden" id="mealTypeID" name="mealType" value="">
			<input type="hidden" id="foodID" name="food" value=""> <input
				type="hidden" id="servingsID" name="servings" value=""> <input
				type="hidden" id="caloriesID" name="calories" value=""> <input
				type="hidden" id="fatID" name="fat" value=""> <input
				type="hidden" id="sodiumID" name="sodium" value=""> <input
				type="hidden" id="carbID" name="carb" value=""> <input
				type="hidden" id="fiberID" name="fiber" value=""> <input
				type="hidden" id="sugarID" name="sugar" value=""> <input
				type="hidden" id="proteinID" name="protein" value="">
			<table class="fTable" border=1 align="center">
				<tr>
					<th></th>
					<th>Label</th>
					<th>Date</th>
					<th>Type of Meal</th>
					<th>Food Name</th>
					<th>Servings</th>
					<th>Calories</th>
					<th>Fat</th>
					<th>Sodium</th>
					<th>Carbs</th>
					<th>Fiber</th>
					<th>Sugar</th>
					<th>Protein</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
				<%
					int index = 0;
														for(FoodEntryBean b : foodBeans) {
				%>
				<tr id="<%=StringEscapeUtils.escapeHtml("tableRow" + index)%>">

					<td><input type="button" value="Submit"
						onclick="submitEdit(<%=StringEscapeUtils.escapeHtml("" + b.getEntryID())%>, 
								<%=StringEscapeUtils.escapeHtml("" + index)%>)"
						hidden id="<%=StringEscapeUtils.escapeHtml("SubmitID:" + index)%>">
					</td>
				<%
				String color = "";
				for (LabelBean label : labelBeans) {
					if (label.getEntryID() == b.getLabelID()) {
						color = label.getLabelColor();
					}
				}
				%>	
					<td bgcolor=<%=color %>><select disabled
						name="<%=StringEscapeUtils.escapeHtml("LabelEntry:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("LabelEntryID:" + index)%>">
							<option value="0">None</option>
							<%
								for (LabelBean label : labelBeans) {
							%>
							<option value="<%=label.getEntryID()%>"
								<%if (label.getEntryID() == b.getLabelID()) {%>
								selected <%}%>>
								<%=label.getLabelName()%>
							</option>
							<%
								}
							%>

					</select></td>
					
					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getDateEatenStr()))%>"
						disabled size="8"
						name="<%=StringEscapeUtils.escapeHtml("Date:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("DateID:" + index)%>">
					</td>

					<td><select disabled
						name="<%=StringEscapeUtils.escapeHtml("MealType:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("MealTypeID:" + index)%>">
							<option value="">Select:</option>
							<%
								for (MealType meal: mealTypes) {
							%>
							<option value="<%=StringEscapeUtils.escapeHtml(meal.getName())%>"
								<%if (meal.getName().equals(b.getMealType().getName())) {%>
								selected <%}%>>
								<%=meal.getName()%>
							</option>
							<%
								}
							%>

					</select></td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getFood()))%>"
						disabled size="20"
						name="<%=StringEscapeUtils.escapeHtml("FoodName:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("FoodNameID:" + index)%>"></td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getServings()))%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Servings:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("ServingsID:" + index)%>"></td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getCalories()))%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Calories:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("CaloriesID:" + index)%>"></td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + (b.getFatGrams()))%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Fat:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("FatID:" + index)%>">
					</td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + b.getMilligramsSodium())%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Sodium:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("SodiumID:" + index)%>">
					</td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + b.getCarbGrams())%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Carb:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("CarbID:" + index)%>">
					</td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + b.getFiberGrams())%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Fiber:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("FiberID:" + index)%>">
					</td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + b.getSugarGrams())%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Sugar:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("SugarID:" + index)%>">
					</td>

					<td><input type="text"
						value="<%=StringEscapeUtils.escapeHtml("" + b.getProteinGrams())%>"
						disabled size="3"
						name="<%=StringEscapeUtils.escapeHtml("Protein:" + index)%>"
						id="<%=StringEscapeUtils.escapeHtml("ProteinID:" + index)%>">
					</td>

					<td><input type="button" value="Edit Entry"
						onclick="editBean(<%=StringEscapeUtils.escapeHtml("" + index)%>)"></td>

					<td><input type="button" value="Delete Entry"
						onclick="deleteBean(<%=StringEscapeUtils.escapeHtml("" + b.getEntryID())%>,
									<%=StringEscapeUtils.escapeHtml("" + index)%>)"></td>


				</tr>
				<%
					index ++;
				%>
				<%
					}
				%>
			</table>
		</form>
		<br>
		<hr>
		<table class="fTable" border=1 align="center">
			<caption>
				<h2>Daily Totals</h2>
			</caption>
			<tr>
				<th>Date</th>
				<th>Total Calories</th>
				<th>Total Fat</th>
				<th>Total Sodium</th>
				<th>Total Carbs</th>
				<th>Total Fiber</th>
				<th>Total Sugar</th>
				<th>Total Protein</th>
			</tr>
			<%
				int counter = 0;
																	for(FoodEntryBean b : foodBeansTotals) {
			%>
			<td><%=StringEscapeUtils.escapeHtml("" + (b.getDateEatenStr()))%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + (b.getCalories()))%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + (b.getFatGrams()))%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + b.getMilligramsSodium())%>
			</td>
			<td><%=StringEscapeUtils.escapeHtml("" + b.getCarbGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + b.getFiberGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + b.getSugarGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + b.getProteinGrams())%>
			</td>
			</tr>
			<%
				counter ++;
			%>
			<%
				}
			%>
		</table>

	</div>
	<%
		} else if (dateRange) {
			// There are entries because the range parameters obscures them.
	%>
	<div>
		<i>You have no entries in the specified range.</i>
	</div>
	<%
		} else {
			// There are no entries at all.
	%>
	<div>
		<i>You have no Food Diary entries.</i>
	</div>
	<%
		}
	%>
	<br /> <br /> 
	<a href="/iTrust/auth/patient/addFoodEntry.jsp">Add an entry to your Food Diary.</a>
</div>
<script type="text/javascript">
function deleteBean(entry, rowIndex) {
	var row = "tableRow";
	row = row.concat(rowIndex);
	var tableRow = document.getElementById(row);
	var background = tableRow.style.backgroundColor;
	var msg = "Are you sure you want to delete this entry?";
	tableRow.style.backgroundColor = "red";
	var r = confirm(msg);
	if (r == true) { //only submit the form if they confirm
		document.getElementById("deleteInput").value="true";
		document.getElementById("editInput").value="false";
		document.getElementById("beanID").value=entry;
		document.getElementById("beanForm").submit();
	} else {
		tableRow.style.backgroundColor = background;
	}
}

function singleDate() {
	var start = document.getElementById("startDate");
	var end = document.getElementById("endDate");
	
	if (start.value != null && (end.value == null || end.value == "")) {
		end.value = start.value;
	} else if (end.value != null && (start.value == null || start.value == "")) {
		start.value = end.value;
	}
}

function editBean(rowIndex) {
	var ids = ["LabelEntryID:", "DateID:", "MealTypeID:", "FoodNameID:", 
	           "ServingsID:", "CaloriesID:", "FatID:", "SodiumID:",
	           "CarbID:", "FiberID:", "SugarID:", "ProteinID:"]
	var buttonName = "SubmitID:".concat(rowIndex);
	document.getElementById(buttonName).hidden=false;
	for (i = 0; i < 12; i++) {
		var inputID = ids[i].concat(rowIndex);
		document.getElementById(inputID).disabled=false;
	}
	document.getElementById("editInput").value="true";
}

function submitEdit(entry, rowIndex) {
	//have an array of all the different ids
	//(not the best solution at all, but I can't think
	//of anything better at this time)
	var formIDs = ["labelEntryID", "dateEatenStrID", "mealTypeID", "foodID", "servingsID",
	                 "caloriesID", "fatID", "sodiumID", "carbID", "fiberID", 
	                 "sugarID", "proteinID"];
	
	var ids = ["LabelEntryID:", "DateID:", "MealTypeID:", "FoodNameID:", 
	           "ServingsID:", "CaloriesID:", "FatID:", "SodiumID:",
	           "CarbID:", "FiberID:", "SugarID:", "ProteinID:"]
	
	for (i = 0; i < 12; i++) {
		var inputID = ids[i].concat(rowIndex);
		var formParam = document.getElementById(formIDs[i]);
		formParam.value = document.getElementById(inputID).value;
	}
	document.getElementById("deleteInput").value = "false";
	document.getElementById("editInput").value = "true";
	document.getElementById("beanID").value = entry;
	document.getElementById("beanForm").submit();
}

</script>

<%@include file="/footer.jsp"%>
