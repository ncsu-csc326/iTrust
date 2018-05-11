<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ExpertBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateLOINCListAction"%> 
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@include file="/global.jsp" %>

<% pageTitle = "iTrust - View Expert Hospital"; %>

<%@include file="/header.jsp" %>

<%
String LOINCCode = request.getParameter("LOINC");
String specialty = request.getParameter("Specialty");
List<ExpertBean>  expertList = prodDAO.getExpertsDAO().getTopFiveExperts(LOINCCode);
PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());

if (expertList.size() == 0) {
	%>
	<span class= "iTrustMessage">There are no hospitals for this procedure</span>
<%}%>

<div align=center> 
<br />
<table class="fTable" id="htable">
	<tr>
		<th colspan="7">Expert Hospitals : <%= specialty %> (<%= LOINCCode %>)</th>
	</tr>
	<tr class="subHeader">
		<td>Name</td>
		<td>Frequency</td>
		<td>Address</td>
	</tr>
	<%
	for (ExpertBean expert : expertList) {		  
		%>
		<tr>
			<td align=center><a href="#" onClick="dropMarker('<%= expert.getName() %>',<%= expert.getLat() %>,<%= expert.getLng()%>);"><%= expert.getName() %></a></td>
			<td align=center><%= expert.getFrequency() %></td>
			<td align=center><%= expert.getAddress() + ", " + expert.getCity() + " " + expert.getState() + " " + expert.getZip()%></td>
		</tr>
	<%}%>
</table>
<br/>
</div>
<br />


<%@include file="/footer.jsp" %>
