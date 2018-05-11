<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.RequiredProceduresBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FoodEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.AuthDAO"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Patient Food Diaries";
%>

<%@include file="/header.jsp"%>

<itrust:patientNav thisTitle="Food Diaries" />

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-nutritionist/viewFoodDiaries.jsp");
		return;
	}
	/* Get a patient's respective food diary */
	long pid = Long.parseLong(pidString);
	String PatientName = authDAO.getUserName(pid);
	loggingAction.logEvent(TransactionType.NUTRITIONIST_VIEW_FOOD_DIARY, 
			loggedInMID.longValue(), pid, "");
	ViewFoodEntryAction action = new ViewFoodEntryAction(prodDAO,
	loggedInMID);
	LabelAction labelAction = new LabelAction(prodDAO, loggedInMID);
	LabelBean labelBean = new LabelBean();
		
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

	List<FoodEntryBean> foodBeans = new ArrayList<FoodEntryBean>();
	List<FoodEntryBean> foodBeansTotals = new ArrayList<FoodEntryBean>();
		if (dateRange) {
	foodBeans = action.getBoundedDiary(
	request.getParameter("startDate"),
	request.getParameter("endDate"), pid);
	foodBeansTotals = action.getBoundedDiaryTotals(
	request.getParameter("startDate"),
	request.getParameter("endDate"), pid);
		} else {
	foodBeans = action.getDiary(pid);
	foodBeansTotals = action.getDiaryTotals(pid);
		}
		loggingAction.logEvent(TransactionType.NUTRITIONIST_VIEW_FOOD_DIARY,
		loggedInMID.longValue(), pid, "");
%>

<div align="center">
<br />
<form id="filterForm" action="viewFoodDiaries.jsp" method="post">
	<table>
		<tr class="subHeader">
			<td>Start Date:</td>
			<td><input onchange="singleDate();" name="startDate" id="startDate"
				value="<%=StringEscapeUtils.escapeHtml((startDate == null ? "" : startDate))%>"
				size="10"> <input type="button" value="Select Date"
				onclick="displayDatePicker('startDate');"></td>
			<td>End Date:</td>
			<td><input onchange="singleDate();" name="endDate" id="endDate"
				value="<%=StringEscapeUtils.escapeHtml((endDate == null ? "" : endDate))%>">
				<input type="button" value="Select Date"
				onclick="displayDatePicker('endDate');"></td>
			<td><input onclick="singleDate();" type="submit" name="btn_filter"
				value="Filter Entries"></td>
		</tr>
	</table>
</form>
	<br>
	<h2><%=selectedPatientName + "'s Food Diary"%></h2>
	<%
		if (foodBeans.size() > 0 && foodBeansTotals.size() > 0) {
	%>
	<div style="margin-left: 5px;">
		<table class="fTable" border=1 align="center">
			<tr>
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
			</tr>
			<%
				int index = 0;
								for (FoodEntryBean b : foodBeans) {
									labelBean = labelAction.getLabel(b.getLabelID());
									String labelMessage = ""; //used in case label is null and we need to put "None"
									if (labelBean == null) {
										labelMessage = "None";
									} else {
										labelMessage = labelBean.getLabelName();
									}
									
			%>
			<td><%=StringEscapeUtils.escapeHtml(labelMessage) %></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getDateEatenStr()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getMealType().getName()))%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + (b.getFood()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getServings()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getCalories()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getFatGrams()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getMilligramsSodium())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getCarbGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getFiberGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getSugarGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getProteinGrams())%></td>

			</tr>
			<%
				index++;
			%>
			<%
				}
			%>
		</table>
		<br>
		<hr>
		<table class="fTable" border=1 align="center">
			<caption>
				<h2><%=selectedPatientName + "'s Daily Totals"%></h2>
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
								for (FoodEntryBean b : foodBeansTotals) {
			%>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getDateEatenStr()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getCalories()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ (b.getFatGrams()))%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getMilligramsSodium())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getCarbGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getFiberGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getSugarGrams())%></td>
			<td><%=StringEscapeUtils.escapeHtml(""
							+ b.getProteinGrams())%></td>
			</tr>
			<%
				counter++;
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
		<i>There are no entries in the specified range.</i>
	</div>
	<%
		} else {
	%>
	<div>
		<i>The selected patient's food diary is empty. If you were expecting entries please try again later!</i>
	</div>
	<%
		}
	%>
	<br /> <a
		href="/iTrust/auth/getPatientID.jsp?forward=
		/iTrust/auth/hcp-nutritionist/viewFoodDiaries.jsp">
		Select a different patient's Food Diary</a>
</div>

<script type="text/javascript">
function singleDate() {
	var start = document.getElementById("startDate");
	var end = document.getElementById("endDate");
	
	if (start.value != null && (end.value == null || end.value == "")) {
		end.value = start.value;
	} else if (end.value != null && (start.value == null || start.value == "")) {
		start.value = end.value;
	}
}
</script>

<%@include file="/footer.jsp"%>
