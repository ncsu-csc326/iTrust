<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Diagnosis Statistics";

String view = request.getParameter("viewSelect");
%>

<%@include file="/header.jsp" %>

<%
if (view != null && !view.equalsIgnoreCase("")) {
	if (view.equalsIgnoreCase("trends")) {
%>		
	<form action="viewDiagnosisStatistics.jsp" method="post" id="formSelectFlow">
	<select name="viewSelect" style="font-size:10" >
		<option selected="selected" value="trends"> Trends </option>
		<option value="epidemics"> Epidemics </option>
	</select>
	<input type="submit" id="select_View" value="Go">
</form>
	<%@include file="viewDiagnosisTrends.jsp" %>
<%
	} else if(view.equalsIgnoreCase("epidemics")){
%>
	<form action="viewDiagnosisStatistics.jsp" method="post" id="formSelectFlow">
	<select name="viewSelect" style="font-size:10" >
		<option value="trends"> Trends </option>
		<option selected="selected" value="epidemics"> Epidemics </option>
	</select>
	<input type="submit" id="select_View" value="Go">
</form>
	<%@include file="viewEpidemics.jsp" %>
<% 		
	}
} else {
%>
<form action="viewDiagnosisStatistics.jsp" method="post" id="formSelectFlow">
	<select name="viewSelect" style="font-size:10" >
		<option value="">-- None Selected --</option>
		<option value="trends"> Trends </option>
		<option value="epidemics"> Epidemics </option>
	</select>
	<input type="submit" id="select_View" value="Go">
</form>
<% } %>

<%@include file="/footer.jsp" %>
