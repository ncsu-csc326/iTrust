<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@include file="/global.jsp" %>

<%PersonnelBean personnelb = new PersonnelDAO(prodDAO).getPersonnel(loggedInMID);
%>

<div align="center" style="margin-bottom: 30px;">
	<img src="/iTrust/image/user/<%= StringEscapeUtils.escapeHtml("" + (loggedInMID.longValue() )) %>.png" alt="MID picture">
</div>

<div align="center">
<table width="165">
	<tr>
		<td>Name: </td>
		<td><%= StringEscapeUtils.escapeHtml("" + (personnelb.getFullName())) %></td>
	</tr>
	<tr>
		<td>Location: </td>
		<td>Somewhere</td>
	</tr>
</table>
</div>