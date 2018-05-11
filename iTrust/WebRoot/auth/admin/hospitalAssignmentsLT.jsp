<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.HtmlEncoder" %>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ManageHospitalAssignmentsAction"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Hospital Staffing Assignments";
%>

<%@include file="/header.jsp" %>

<%
	String pidString = (String)session.getAttribute("mid");
	/* Require a Personnel ID first */

	if (null == pidString || "".equals(pidString)){ 
  	 	response.sendRedirect("../getPersonnelID.jsp?forward=admin/hospitalAssignmentsLT.jsp");
 	  	return;
	}
	else {
		if(pidString.charAt(0) != '5'){
			response.sendRedirect("../getPersonnelID.jsp?forward=admin/hospitalAssignmentsLT.jsp");
		}
	}


	
	/* Bad patient ID gets you booted to error page */
	ManageHospitalAssignmentsAction hosAssignManager =
		new ManageHospitalAssignmentsAction(prodDAO,loggedInMID.longValue());
	long pid  = hosAssignManager.checkHCPID(pidString);

	
	/* Now take care of updating information */
	String action = "";
	if (null != request.getParameter("action"))
	{
		action = request.getParameter("action");
	}
	if (action.equals("assgn"))
	{
		/* Checks to see if a HCP is a LT. If it is a LT check to see if the LT is assigned to any hospital */
		if(hosAssignManager.checkLTHospital(pidString)){
			%><span class="iTrustError">LT has an assigned hospital already. Please remove assigned hospital before assigning to a new hospital.<br /></span><%
		}else{
			hosAssignManager.assignHCPToHospital(pid + "", request.getParameter("hospital"));
			loggingAction.logEvent(TransactionType.LT_ASSIGN_HOSPITAL, loggedInMID.longValue(), pid, "");
			%><span class="iTrustMessage">The LT has been assigned a hospital<br /></span><%
		}
	}
	else if (action.equals("unass"))
	{
		hosAssignManager.removeHCPAssignmentToHospital(pid + "", request.getParameter("id"));
		loggingAction.logEvent(TransactionType.LT_REMOVE_HOSPITAL, loggedInMID.longValue(), pid, "");
		%><span class="iTrustMessage">LT's hospital has been unassigned<br /></span><%
	}
	
	
%>
<br />
<div align=center>
<table>
	<tr>
	<td valign=top>
	<table class="fTable">
		<tr>
			<th colspan=3>Assigned Hospital</th>
		</tr>
		<tr class="subHeader">
			<th>Hospital ID</th>
			<th>Hospital Name</th>
			<th>Assignment</th>
		</tr>
		<%
		List<HospitalBean> assignedList = hosAssignManager.getAssignedHospitals(pidString);
		String tempID = "";
		String tempName = "";
		String escapedName = "";
		for (HospitalBean assigned : assignedList) {
			tempID = assigned.getHospitalID();
			tempName = assigned.getHospitalName();
			escapedName = URLEncoder.encode(tempName, "UTF-8").replaceAll("\\+", "%20");
		%><tr>
			<td><%= StringEscapeUtils.escapeHtml("" + (tempID )) %></td>
			<td><%=HtmlEncoder.encode(tempName) %></td>
			<td><a href="hospitalAssignmentsLT.jsp?pid=<%= StringEscapeUtils.escapeHtml("" + (pidString )) %>&id=<%= StringEscapeUtils.escapeHtml("" + (tempID )) %>&action=unass" >Unassign</a></td>
		</tr>
		<%} %>
	</table>
	</td>
	<td valign=top>
	<form action="hospitalAssignmentsLT.jsp" method="post" id="LTHospitalForm">
	<input type="hidden" name="pid" value="<%=pidString %>" />
	<input type="hidden" name="action" value="assgn" />
	
	<table class="fTable">
		<tr>
			<th colspan=3>Available Hospitals</th>
		</tr>
		<tr class="subHeader">
			<th>Hospital Name</th>
			<th>Assignment</th>
		</tr>
		
		<%
		List<HospitalBean> availableList = hosAssignManager.getAvailableHospitals(pidString);
		tempID = "";
		tempName = "";
		escapedName = "";
		%><tr><td><select name="hospital"><%
		for (HospitalBean assigned : availableList) {
			tempID = assigned.getHospitalID();
			tempName = assigned.getHospitalName();
			escapedName = URLEncoder.encode(tempName, "UTF-8").replaceAll("\\+", "%20");
		%>
			<option value="<%= StringEscapeUtils.escapeHtml("" + (tempID )) %>"><%=HtmlEncoder.encode(tempName) %></option>
		
		
		
		
		<%} %>
		</td>
		<td><button type="submit" name="LTHospitalSubmitBtn">Assign</button></td>
		</tr>
	</table>
	</form>
    </td></tr>
</table>
</div>
<br />

<%@include file="/footer.jsp" %>
