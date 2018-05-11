<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Laboratory Procedures";
%>

<%@include file="/header.jsp" %>

<%
loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW, loggedInMID.longValue(), 0, "");

ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, loggedInMID.longValue());
List<LabProcedureBean> proc = action.getLabs();
int id = 0;

if(request.getParameter("id") != null) {
	try {
		id = Integer.parseInt(request.getParameter("id"));
	} catch (NumberFormatException nfe) {
		response.sendRedirect("calendar.jsp");
	}
}

LabProcedureBean bean = null;

for(LabProcedureBean b : proc) {
	if(b.getPid() == id) {
		bean = b;
		break;
	}
}

if(bean == null) {
	response.sendRedirect("calendar.jsp");
}

String status = StringEscapeUtils.escapeHtml("" + (bean.getStatus()));

%>

<br />
<table  class="fTable" align=center>
	<tr>
		<th colspan="13">Lab Procedures</th>
	</tr>

	<tr class="subHeader">
  		<td>Lab Code</td>
		<td>Status</td>
		<td>Commentary</td>
		<td>Results</td>
		<td>Numerical<br/>Result</td>
        <td colspan="2">Confidence<br/>Interval</td>
		<td>Updated Date</td>
  	</tr>
	<tr>
		<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getLoinc())) %></td>
		<td ><%= status %></td>
		<td ><%= status.equals(LabProcedureBean.Completed) ? StringEscapeUtils.escapeHtml("" + (bean.getCommentary())) : ""%></td>
		<td ><%= status.equals(LabProcedureBean.Completed) ? StringEscapeUtils.escapeHtml("" + (bean.getResults())) : "" %></td>
        <td ><%= status.equals(LabProcedureBean.Completed) ? StringEscapeUtils.escapeHtml("" + (bean.getNumericalResult())) : "" %></td>
        <td ><%= status.equals(LabProcedureBean.Completed) ? StringEscapeUtils.escapeHtml("" + (bean.getLowerBound())) : "" %></td>
        <td ><%= status.equals(LabProcedureBean.Completed) ? StringEscapeUtils.escapeHtml("" + (bean.getUpperBound())) : "" %></td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getTimestamp())) %></td>
	</tr>
</table>
<br /><br />

<%@include file="/footer.jsp" %>
