<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@include file="/global.jsp" %>

<%
PatientBean patient = new PatientDAO(DAOFactory.getProductionInstance()).getPatient(loggedInMID); 

%>

<div align="center" style="margin-bottom: 30px;">
	<img src="/iTrust/image/user/<%= StringEscapeUtils.escapeHtml("" + (loggedInMID.longValue() )) %>.png" alt="MID picture">
</div>

<div align="center">
<table>
	<tr>
		<td>Name: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
	</tr>
	<tr>
		<td>Gender: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (patient.getGender() )) %></td>
	</tr>
	<tr>
		<td>Location: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (patient.getCity() + ", " + patient.getState())) %></td>
	</tr>
	<tr>
		<td>DOB: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (patient.getDateOfBirthStr() )) %></td>
	</tr>
	<tr>
		<td>Blood Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><%= StringEscapeUtils.escapeHtml("" + (patient.getBloodType() )) %> </td>
	</tr>
</table>
</div>