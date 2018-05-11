<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.beans.OperationalProfile"%>
<%@page import="java.text.NumberFormat"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Tester Home";
%>

<%@include file="/header.jsp" %>

<div style="text-align: center;">
<h1>Operational Profile</h1>
<%
	try {
		OperationalProfile op = prodDAO.getTransactionDAO().getOperationalProfile();
		loggingAction.logEvent(TransactionType.OPERATIONAL_PROFILE_VIEW, loggedInMID, 0, "");
		
		NumberFormat formatter = NumberFormat.getPercentInstance();
		
%>

<table class="fancyTable" align="center" border=1 cellpadding=2 cellspacing=2>
	<tr>
		<th>Operation</th>
		<th colspan=2 >Total</th>
		<th colspan=2 >Patients Only</th>
		<th colspan=2 >Personnel Only</th>
	</tr>
<%
	int i = 1;
	for (TransactionType type : TransactionType.values()) {
%>
	<tr <%= (i++%2 == 0)?" class=\"alt\"":"" %>>
		<td align=left><%= StringEscapeUtils.escapeHtml("" + (type.getDescription())) %></td>
		<td align=center><%= StringEscapeUtils.escapeHtml("" + (op.getTotalCount().get(type))) %></td>
		<td align=center><%= StringEscapeUtils.escapeHtml("" + (formatter.format((double)op.getTotalCount().get(type) / op.getNumTotalTransactions()))) %></td>
		<td align=center><%= StringEscapeUtils.escapeHtml("" + (op.getPatientCount().get(type))) %></td>
		<td align=center><%= StringEscapeUtils.escapeHtml("" + (formatter.format((double)op.getPatientCount().get(type) / op.getNumPatientTransactions()))) %></td>
		<td align=center><%= StringEscapeUtils.escapeHtml("" + (op.getPersonnelCount().get(type))) %></td>
		
<% 
	double personnelPercent = 0;
	if(op.getNumPersonnelTransactions() != 0)
		personnelPercent = (double)op.getPersonnelCount().get(type) / op.getNumPersonnelTransactions();
			
%>
		<td align=center><%= StringEscapeUtils.escapeHtml("" + (formatter.format(personnelPercent))) %></td>
	</tr>
<%
	}
%>
	<tr>
		<td><b>Totals</b></td>
		<td colspan=2 align=center><%= StringEscapeUtils.escapeHtml("" + (op.getNumTotalTransactions())) %></td>
		<td colspan=2 align=center><%= StringEscapeUtils.escapeHtml("" + (op.getNumPatientTransactions())) %></td>
		<td colspan=2 align=center><%= StringEscapeUtils.escapeHtml("" + (op.getNumPersonnelTransactions())) %></td>
	</tr>
</table>

</div>

<%
	} catch (Exception e) {
%>
	<span >Exception Occurred</span>
	<br />
	<%=StringEscapeUtils.escapeHtml(e.getMessage())%>
<%

}
%>

<%@include file="/footer.jsp" %>
