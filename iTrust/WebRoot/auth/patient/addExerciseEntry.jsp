<%@page errorPage="auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.AddExerciseEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.action.EditExerciseEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.action.ViewExerciseEntryAction"  %>
<%@page import="edu.ncsu.csc.itrust.beans.ExerciseEntryBean" %>
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
<%@page import="edu.ncsu.csc.itrust.enums.ExerciseType" %>

<%@include file="/global.jsp" %>

<%
pageTitle="Add an Exercise Entry";
%>

<%@include file="/header.jsp"%>

<%
	ExerciseEntryBean newEntry = new ExerciseEntryBean();
	AddExerciseEntryAction addAction = new AddExerciseEntryAction(prodDAO, loggedInMID.longValue());
	Date date = new Date();
	String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(date);
	
	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");

	ExerciseType[] ExerciseTypes = ExerciseType.values();
	
	if (formIsFilled) {
		try {
			//can't use BeanBuilder.build because not all are strings
			//so we get all of them in order
			String strDate = request.getParameter("strDate");
			String strType = request.getParameter("strType");
			String strName = request.getParameter("strName");
			String strHours = request.getParameter("strHours");
			String strCalories = request.getParameter("strCalories");
			Long labelEntry = null;
			
			//now make sure none are null (need both checks here)
			if (strDate != null && !strDate.equals("")) {
				newEntry.setStrDate(strDate);
			}
			if (strType != null && !strType.equals("")) {
				newEntry.setExerciseType(strType);
			}
			if (strName != null && !strName.equals("")) {
				newEntry.setStrName(strName);
			}
			if (strHours != null && !strHours.equals("")) {
				newEntry.setHoursWorked(Double.parseDouble(strHours));
			}
			if (strCalories != null && !strCalories.equals("")) {
				newEntry.setCaloriesBurned(Integer.parseInt(strCalories));
			}
			
			// Weight Training only.
			String strSets = request.getParameter("strSets");
			String strReps = request.getParameter("strReps");
			if (strSets != null && !strSets.equals("") && strReps != null && !strReps.equals("")) {
				newEntry.setNumSets(Integer.parseInt(strSets));
				newEntry.setNumReps(Integer.parseInt(strReps));
			}
			//Get label information for other entries on that date
			ExerciseEntryBean labelUpdaterBean = new ExerciseEntryBean();
			List<ExerciseEntryBean> listOfEntriesRequiringLabelUpdate = new ArrayList<ExerciseEntryBean>();		
			ViewExerciseEntryAction viewAction = new ViewExerciseEntryAction(prodDAO, loggedInMID.longValue());
			EditExerciseEntryAction editAction = new EditExerciseEntryAction(prodDAO, loggedInMID.longValue());
			listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(strDate, 
					strDate, loggedInMID.longValue());
			if (listOfEntriesRequiringLabelUpdate.size() > 0) {
				labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(0);
				labelEntry = labelUpdaterBean.getLabelID();
			}
			
			//now add it to the exercise diary
			addAction.addEntry(newEntry);
			loggingAction.logEvent(TransactionType.EXERCISE_DIARY_ADD, 
					loggedInMID.longValue(), loggedInMID.longValue(), "");
			
			//if add was successful, then apply that day's label to all entries
			if (labelEntry != null) {
				listOfEntriesRequiringLabelUpdate = viewAction.getBoundedDiary(strDate, 
						strDate, loggedInMID.longValue());
				for(int i = 0; i < listOfEntriesRequiringLabelUpdate.size(); i++) {
					labelUpdaterBean = listOfEntriesRequiringLabelUpdate.get(i);
					labelUpdaterBean.setLabelID(labelEntry);
					editAction.editEntry(labelUpdaterBean);
				}
			}
			//iff adding was successful, then send them to their exercise diary
			response.sendRedirect("myExerciseDiary.jsp");
			
		} catch (Exception e) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
			</div>
<%
		}
	}
%>

<div align=center>
	<h2>Add an Exercise Entry</h2>

	<form action="addExerciseEntry.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<table class="fTable" cellspacing=0 align=center cellpadding=0>
			<tr>
				<td class="subHeaderVertical">Date Performed:</td>
				<%//TODO make the default value equal to the current date %>
				<td><input id="selectDate" name="strDate" type="text" 
				value="<%=StringEscapeUtils.escapeHtml(dateStr) %>"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Type of Exercise:</td>
				<td><select id="selectType" name="strType" onchange="changeExerciseType();">
<% 					for (ExerciseType xt: ExerciseTypes) {
%>
						<option value="<%= 
							StringEscapeUtils.escapeHtml(xt.getName()) %>">
						<%= xt.getName() %> </option>
<%
					}
%>
					
				</select>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name of Exercise:</td>
				<td><input name="strName" type="text"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Hours Performed:</td>
				<td><input name="strHours" type="text"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Calories Burned:</td>
				<td><input name="strCalories" type="text"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Sets:</td>
				<td><input id="txtSets" name="strSets" type="text" disabled="disabled"> </td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Number of Reps per Set:</td>
				<td><input id="txtReps" name="strReps" type="text" disabled="disabled"> </td>
			</tr>
		</table>
		<br>
		<div align="center">
			<input type="submit" style="font-weight:bold;"
				value="Add entry to Exercise Diary"> 
		</div>
	</form>
	<br>
	<div align="center">
		<a href="myExerciseDiary.jsp">Cancel entry 
			and return to your Exercise Diary.</a>
	</div>


</div>

<script type="text/javascript">

function changeExerciseType() {
    var selectType = document.getElementById("selectType");
    var txtSets = document.getElementById("txtSets");
    var txtReps = document.getElementById("txtReps");
    
    if (selectType.options[selectType.selectedIndex].value == "Weight Training") {
    	txtSets.disabled = false;
    	txtReps.disabled = false;
    } else {
    	txtSets.value = "";
    	txtReps.value = "";
    	txtSets.disabled = true;
    	txtReps.disabled = true;
    }
}

</script>

<%@include file="/footer.jsp"%>