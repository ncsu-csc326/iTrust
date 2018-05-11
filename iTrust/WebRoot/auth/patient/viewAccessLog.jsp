<%@page import="edu.ncsu.csc.itrust.action.ViewMyAccessLogAction"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.GetUserNameAction"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Access Log";
%>

<%@include file="/header.jsp"%>

<%
session.removeAttribute("personnelList");
	
	
	ViewMyAccessLogAction action = new ViewMyAccessLogAction(DAOFactory.getProductionInstance(), loggedInMID);
	List<TransactionBean> accesses; //stores entries in the access log
	String mid = ""; //stores the mid of the user whose log is being fetched
	
	mid = request.getParameter("logMID"); //try to get the id from a request
	if (mid == null) {
		mid = ""+loggedInMID; //if no mid was set in the request, default to the logged in user
		session.setAttribute("currentLogMID", mid); //used to check when the user has been changed for log views.
	}
	
	String logMIDSess = (String) session.getAttribute("currentLogMID");
	if (logMIDSess != null && !logMIDSess.equals(mid)) { //If the requested user is different than the last log, reset date filter
		accesses = action.getAccesses(null,null,mid,false);
	} else { //otherwise, run the action with the requested dates
		try{
			accesses = action.getAccesses(request.getParameter("startDate"), request.getParameter("endDate"), mid, "role".equals(request.getParameter("sortBy")));
		} catch(FormValidationException e){
			e.printHTML(pageContext.getOut());
			accesses = action.getAccesses(null,null,mid,false); 
		}	
	}
	session.setAttribute("currentLogMID", mid); //set the currently viewed MID so it can be seen later in the session

	List<PatientBean> patientRelatives = action.getRepresented(loggedInMID); //get list of medical dependents for user

	String logUser = new GetUserNameAction(DAOFactory.getProductionInstance()).getUserName(mid); //get the name of the user whose logs you're viewing
%>
<h1>Viewing Log For: <%= logUser %></h1>
<br />
<table class="fTable" align='center'>
	<tr>
		<th><a href="#" onClick="javascript:sortBy('date');">Date</a></th>
		<th>Accessor</th>
		<th><a href = "#" onClick="javascript:sortBy('role');">Role</a></th>
		<th>Description</th>
	</tr>
<%
	boolean hasData = false;
	List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
	int index = 0;
	loggingAction.logEvent(TransactionType.ACCESS_LOG_VIEW, loggedInMID, 0, "");
	for(TransactionBean t : accesses){ 
		PersonnelBean hcp = new PersonnelDAO(DAOFactory.getProductionInstance()).getPersonnel(t.getLoggedInMID());
		if (hcp != null) {
			hasData = true;

	%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (t.getTimeLogged())) %></td>
				<td ><a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%= StringEscapeUtils.escapeHtml("" + (index)) %>"><%= StringEscapeUtils.escapeHtml("" + (hcp.getFullName())) %></a></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (t.getRole() )) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getDescription())) %></td>		
			</tr>
	<%
		PersonnelBean personnel = new PersonnelDAO(prodDAO).getPersonnel(t.getLoggedInMID());
		personnelList.add(personnel);
		index++;
		
			}
			else if("Personal Health Representative".equals(t.getRole())) {
		PatientBean p = new PatientDAO(DAOFactory.getProductionInstance()).getPatient(t.getLoggedInMID());
	%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (t.getTimeLogged())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (p.getFullName())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (t.getRole())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (t.getAddedInfo())) %> (<%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getCode())) %>)</td>		
			</tr>
	<%

		}
	}
	session.setAttribute("personnelList", personnelList);
	if(!hasData) {
%>
	<tr>
		<td colspan=3 align="center">No Data</td>
	</tr>
<%
	}
	
	String startDate = action.getDefaultStart(accesses);
	String endDate = action.getDefaultEnd(accesses);
	if("role".equals(request.getParameter("sortBy"))) {
		startDate = request.getParameter("startDate");
		endDate = request.getParameter("endDate");
	}
%>
</table>
<br />
<br />

<form action="viewAccessLog.jsp" id="logMIDSelectionForm" method="post">

<input type="hidden" name="sortBy" value=""></input>

<div align=center>
<table class="fTable" align="center">
	<tr class="subHeader">
	<td>View log for: </td>
	<td>
		<select name="logMID" id="logMIDSelectMenu">
		<option value="<%= loggedInMID %>"><%= StringEscapeUtils.escapeHtml("" + userName) %></option>
<% 
	for(PatientBean represented : patientRelatives) {
		if (mid.equals(""+represented.getMID())) {
%>						
			<option selected="selected" value="<%= StringEscapeUtils.escapeHtml("" + (represented.getMID())) %>"><%= StringEscapeUtils.escapeHtml("" + (represented.getFullName())) %></option>
<% 
		} else {
%>
			<option value="<%= StringEscapeUtils.escapeHtml("" + (represented.getMID())) %>"><%= StringEscapeUtils.escapeHtml("" + (represented.getFullName())) %></option>
<% 
		}
	}
%>

		</select>
	</td>
	</tr>
	<tr class="subHeader">
		<td>Start Date:</td>
		<td>
			<input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>
		<td>End Date:</td>
		<td>
			<input name="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (endDate)) %>">
			<input type=button value="Select Date" onclick="displayDatePicker('endDate');">
		</td>
	</tr>
</table>
<br />
<input type="submit" name="submit" value="Filter Records">

</div>
</form>

<script type='text/javascript'>
function sortBy(dateOrRole) {
	document.getElementsByName('sortBy')[0].value = dateOrRole;
	document.forms[0].submit.click();
}

</script>

<%@include file="/footer.jsp"%>