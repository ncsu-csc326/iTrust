<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.GetVisitRemindersAction"%>
<%@page import="edu.ncsu.csc.itrust.action.GetVisitRemindersAction.ReminderType"%>
<%@page import="edu.ncsu.csc.itrust.beans.VisitFlag"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.VisitReminderReturnForm"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Visit Reminders";
%>

<%@include file="/header.jsp" %>

<%
loggingAction.logEvent(TransactionType.PATIENT_REMINDERS_VIEW, loggedInMID.longValue(), 0, "");

GetVisitRemindersAction action = new GetVisitRemindersAction(prodDAO, loggedInMID.longValue());
%>
<div align="center">
<h2>Patients Needing Visits</h2>

<form action="visitReminders.jsp" method="post" id="reminderForm">
<input type=hidden id="formIsFilled" value="true" />
<select id="ReminderType" name="ReminderType">
<%
	for (ReminderType rt : ReminderType.values()) {
%>
	<option	<%= StringEscapeUtils.escapeHtml("" + (rt.getTypeName().equals(request.getParameter("ReminderType")) ? " selected " : " ")) %> 
		value="<%=rt.getTypeName() %>"><%= StringEscapeUtils.escapeHtml("" + (rt.getTypeName() )) %></option>
<%
	}
%>
</select>
<br />
<br />
<input type="submit" id="getReminders" name="getReminders" value="Get Reminders" />
</form>
<br />

<%
if("Get Reminders".equals(request.getParameter("getReminders"))) {
	List<VisitReminderReturnForm> reminders = action.getVisitReminders(ReminderType.getReminderType(request.getParameter("ReminderType")));
	for (VisitReminderReturnForm reminder : reminders) {
%>
		<table class="fTable">
			<tr>
				<th colspan="2">Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td >
					<a href="sendEmailNotification.jsp?mid=<%= StringEscapeUtils.escapeHtml("" + (reminder.getPatientID())) %>">
					<%= StringEscapeUtils.escapeHtml("" + (reminder.getFirstName()+" "+reminder.getLastName())) %>
					</a>
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone Number:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (reminder.getPhoneNumber())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Reasons:</td>
				<td>
<%
			for(VisitFlag vf : reminder.getVisitFlags()) {
%>
					<%= StringEscapeUtils.escapeHtml("" + (vf.getType() )) %>: &nbsp;&nbsp; <%= StringEscapeUtils.escapeHtml("" + (vf.getValue() )) %><br />
<%
			}
%>
			 	</td>
			</tr>
		</table>
		
<!--  This is the original Table format which has the first and last name on separate lines.  In favor of making
	the link to send an email to a patient more friendly I've changed this to the above table which puts both names
	together.
		
		<table class="fTable">
			<tr>
				<th colspan="2">Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Last Name:</td>
				<td ><a href="sendEmailNotification.jsp?mid=<%= StringEscapeUtils.escapeHtml("" + (reminder.getPatientID())) %>"><%= StringEscapeUtils.escapeHtml("" + (reminder.getLastName())) %></a></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">First Name:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (reminder.getFirstName())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone Number:</td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (reminder.getPhoneNumber())) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Reasons:</td>
				<td>
<%
			for(VisitFlag vf : reminder.getVisitFlags()) {
%>
					<%= StringEscapeUtils.escapeHtml("" + (vf.getType() )) %>: &nbsp;&nbsp; <%= StringEscapeUtils.escapeHtml("" + (vf.getValue() )) %><br />
<%
			}
%>
			 	</td>
			</tr>
		</table>
-->
		<br />
<%
	} 
}
%>
</div>

<%@include file="/footer.jsp" %>
