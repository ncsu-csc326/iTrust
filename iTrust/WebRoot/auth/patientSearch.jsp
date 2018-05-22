
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.SearchUsersAction" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean" %>

<%@include file="/global.jsp" %>
<html><head><link href="/iTrust/css.jsp" type="text/css" rel="stylesheet" /> </head>
<body>
<%
	
	String query = (String)request.getParameter("q");
	SearchUsersAction searchAction = new SearchUsersAction(prodDAO,loggedInMID.longValue());
	List<PatientBean> patients;
	
	if(request.getParameter("allowDeactivated") != null && request.getParameter("allowDeactivated").equals("on")){
		patients=searchAction.fuzzySearchForPatients(query,true);
	}else{
		 
		patients=searchAction.fuzzySearchForPatients(query);
	}
	if(request.getParameter("forward") != null && !request.getParameter("forward").contains("hcp/auditPage.jsp")){
	%>
			<span class="searchResults">Found <%=StringEscapeUtils.escapeHtml("" + patients.size())%> Records</span>
			<table class="fTable" style="width:80%;">
				<tr>
					<th width="20%">MID</td>
					<th width="40%%">First Name</td>
					<th width="40%">Last Name</td>
				</tr>
	<%
		int index = 0;
		for(PatientBean p : patients){
			if(p==null) continue;
	%>
			<form id="selectPatient<%= StringEscapeUtils.escapeHtml("" + ( String.valueOf(patients.size()) )) %>" action="getPatientID.jsp?forward=<%= StringEscapeUtils.escapeHtml("" + ( request.getParameter("forward") )) %>" method="post">
				<tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
					<td>
						<input type='button' style='width:100px;' onclick="parent.location.href='getPatientID.jsp?UID_PATIENTID=<%=StringEscapeUtils.escapeHtml("" + p.getMID())%>&forward=<%= StringEscapeUtils.escapeHtml("" + ( request.getParameter("forward") )) %>';" value="<%=StringEscapeUtils.escapeHtml("" + p.getMID())%>" />
					</td>
					<td>
						<%=StringEscapeUtils.escapeHtml("" + p.getFirstName())%>
					</td>
					<td>
						<%=StringEscapeUtils.escapeHtml("" + p.getLastName())%>
					</td>
				</tr>
			</form>
	<%
			index++;
		}	
	
	%>
	</table>
	<% } else {
		if(request.getParameter("allowDeactivated") != null && request.getParameter("allowDeactivated").equals("on")){
			if(request.getParameter("q") == null || request.getParameter("q").isEmpty()){
				patients = searchAction.getDeactivated();
			}
		}
		%>
			<span class="searchResults">Found <%=StringEscapeUtils.escapeHtml("" + patients.size())%> Records</span>
			<table class="fTable" style="width:80%;">
				<tr>
					<th width="10%">MID</td>
					<th width="20%">First Name</td>
					<th width="20%">Last Name</td>
					<th width="30%">Status</td>
					<th width="20%">Action</td>
				</tr>
	<%
		int index = 0;
		for(PatientBean p : patients){
			if(p==null) continue;
	%>
			<form id="selectPatient<%= StringEscapeUtils.escapeHtml("" + ( String.valueOf(patients.size()) )) %>" action="getPatientID.jsp?forward=<%= StringEscapeUtils.escapeHtml("" + ( request.getParameter("forward") )) %>" method="post">
				<tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
					<td>
						<%=StringEscapeUtils.escapeHtml("" + p.getMID())%>
					</td>
					<td>
						<%=StringEscapeUtils.escapeHtml("" + p.getFirstName())%>
					</td>
					<td>
						<%=StringEscapeUtils.escapeHtml("" + p.getLastName())%>
					</td>
					
	
	<%
		boolean isActivated = p.getDateOfDeactivationStr() == null || p.getDateOfDeactivationStr().isEmpty();
		String change = isActivated ? "Deactivate" : "Activate";
	%>
					<td>
						<%
						if(isActivated){
						%>
							<%= StringEscapeUtils.escapeHtml("" + p.getFirstName() + " " + p.getLastName() + " is activated.") %>
						<%} else { %>
							<%= StringEscapeUtils.escapeHtml("" + p.getFirstName() + " " + p.getLastName() + " deactivated on: " + p.getDateOfDeactivationStr()) %>
						<%} %>
					</td>
					<td>
					<input type='button' style='width:100px;' onclick="parent.location.href='getPatientID.jsp?UID_PATIENTID=<%=StringEscapeUtils.escapeHtml("" + p.getMID())%>&forward=<%= StringEscapeUtils.escapeHtml("" + ( request.getParameter("forward") )) %>';" value="<%=StringEscapeUtils.escapeHtml("" + change)%>" />
					</td>
				</tr>
	<%
			index++;
		}	
	}
	%>
	</body></html>