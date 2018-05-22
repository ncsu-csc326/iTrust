<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.HtmlEncoder" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HospitalBean"%>
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
  	 	response.sendRedirect("../getPersonnelID.jsp?forward=admin/hospitalAssignments.jsp");
 	  	return;
	}
	else {
		if(pidString.charAt(0) == '5'){
			response.sendRedirect("../getPersonnelID.jsp?forward=admin/hospitalAssignments.jsp");
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
			%><span>LT has an assigned hospital already. Please remove assigned hospital before assigning to a new hospital.</span><%
		}else{
			hosAssignManager.assignHCPToHospital(pid + "", request.getParameter("id"));
			%><span >HCP has been assigned. <%= StringEscapeUtils.escapeHtml("" + (request.getParameter("id") )) %><br /></span><%
		}
	}
	else if (action.equals("unass"))
	{
		hosAssignManager.removeHCPAssignmentToHospital(pid + "", request.getParameter("id"));
		%><span >HCP has been unassigned. <%= StringEscapeUtils.escapeHtml("" + (request.getParameter("id") )) %><br /></span><%
	}
	
	
%>
<br />
<div align=center>
<table>
	<tr>
	<td valign=top>
	<table class="fTable">
		<tr>
			<th colspan=3>Assigned Hospitals</th>
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
			<td><a href="hospitalAssignments.jsp?pid=<%= StringEscapeUtils.escapeHtml("" + (pidString )) %>&id=<%= StringEscapeUtils.escapeHtml("" + (tempID )) %>&action=unass" >Unassign</a></td>
		</tr>
		<%} %>
	</table>
	</td>
	<td valign=top>
	<table class="fTable">
		<tr>
			<th colspan=3>Available Hospitals</th>
		</tr>
		<tr class="subHeader">
			<th>Hospital ID</th>
			<th>Hospital Name</th>
			<th>Assignment</th>
		</tr>
		<%
		List<HospitalBean> availableList = hosAssignManager.getAvailableHospitals(pidString);
		tempID = "";
		tempName = "";
		escapedName = "";
		
		for (HospitalBean assigned : availableList) {
			tempID = assigned.getHospitalID();
			tempName = assigned.getHospitalName();
			escapedName = URLEncoder.encode(tempName, "UTF-8").replaceAll("\\+", "%20");
		%><tr>
			<td><%= StringEscapeUtils.escapeHtml("" + (tempID )) %></td>
			<td><%=HtmlEncoder.encode(tempName) %></td>
			<td><a href="hospitalAssignments.jsp?pid=<%= StringEscapeUtils.escapeHtml("" + (pidString)) %>&id=<%= StringEscapeUtils.escapeHtml("" + (tempID)) %>&action=assgn">Assign</a></td>
		</tr>
		<%} %>
	</table>
    </td></tr>
</table>
</div>
<br />

<%@include file="/footer.jsp" %>
