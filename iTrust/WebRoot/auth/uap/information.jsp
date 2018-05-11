<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@include file="/global.jsp" %>

<%
PersonnelBean personnelb = new PersonnelDAO(prodDAO).getPersonnel(loggedInMID);

%>

<div align="center" style="margin-bottom: 30px;">
	<img src="/iTrust/image/user/<%= StringEscapeUtils.escapeHtml("" + (loggedInMID.longValue() )) %>.png" alt="MID picture">
</div>

<div align="center">
<table>
	<tr>
		<td>Name: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (personnelb.getFullName())) %></td>
	</tr>
	<tr>
		<td>Location: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (personnelb.getCity() + ", " + personnelb.getState())) %></td>
	</tr>
</table>
</div>