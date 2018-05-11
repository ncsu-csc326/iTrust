<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<% 
	//log the page view
	loggingAction.logEvent(TransactionType.DIAGNOSIS_TRENDS_VIEW, loggedInMID.longValue(), 0, "");

	ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
	DiagnosisStatisticsBean dsBean = null;

	//get form data
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	
	String zipCode = request.getParameter("zipCode");
	if (zipCode == null)
		zipCode = "";
	
	String icdCode = request.getParameter("icdCode");
	
	//try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
	try{
		dsBean = diagnoses.getDiagnosisStatistics(startDate, endDate, icdCode, zipCode);
	} catch(FormValidationException e){
		e.printHTML(pageContext.getOut());
	}
	
	if (startDate == null)
		startDate = "";
	if (endDate == null)
		endDate = "";
	if (icdCode == null)
		icdCode = "";
	
%>
<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
<input type="hidden" name="viewSelect" value="trends" />
<table class="fTable" align="center" id="diagnosisStatisticsSelectionTable">
	<tr>
		<th colspan="4">Diagnosis Statistics</th>
	</tr>
	<tr class="subHeader">
		<td>Diagnosis:</td>
		<td>
			<select name="icdCode" style="font-size:10" >
			<option value="">-- None Selected --</option>
			<%for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) { %>
				<%if (diag.getICDCode().equals(icdCode)) { %>
					<option selected="selected" value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
					- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
				<% } else { %>
					<option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
					- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
				<% } %>
			<%}%>
			</select>
		</td>
		<td>Zip Code:</td>
		<td ><input name="zipCode" value="<%= StringEscapeUtils.escapeHtml(zipCode) %>" /></td>
	</tr>
	<tr class="subHeader">
		<td>Start Date:</td>
		<td>
			<input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>
		<td>End Date:</td>
		<td>
			<input name="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (endDate)) %>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('endDate');">
		</td>
	</tr>
	<tr>
		<td colspan="4" style="text-align: center;"><input type="submit" id="select_diagnosis" value="View Statistics"></td>
	</tr>
</table>	

</form>

<br />

<% if (dsBean != null) { %>



<table class="fTable" align="center" id="diagnosisStatisticsTable">
<tr>
	<th>Diagnosis code</th>
	<th>Complete Zip</th>
	<th>Cases in Zip</th>
	<th>Cases in Region</th>
	<th>Start Date</th>
	<th>End Date</th>
</tr>
<tr style="text-align:center;">
	<td><%= icdCode %></td>
	<td><%= zipCode %></td>
	<td><%= dsBean.getZipStats() %></td>
	<td><%= dsBean.getRegionStats() %></td>
	<td><%= startDate %></td>
	<td><%= endDate %></td>
</tr>

</table>

<br />

<p style="display:block; margin-left:auto; margin-right:auto; width:600px;">
<%@include file="DiagnosisTrendChart.jsp" %>
</p>

<% } %>
<br />
<br />
