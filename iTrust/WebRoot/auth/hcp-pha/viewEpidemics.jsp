<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<% 
	//log the page view
	loggingAction.logEvent(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, loggedInMID.longValue(), 0, "");

	ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
	ArrayList<DiagnosisStatisticsBean> dsList = new ArrayList<DiagnosisStatisticsBean>();
	DiagnosisStatisticsBean dsBean = null;
	DiagnosisStatisticsBean avgBean = null;
	
	
	//get form data
	String startDate = request.getParameter("startDate");
	
	String threshold = request.getParameter("threshold");
	
	String zipCode = request.getParameter("zipCode");
	if (zipCode == null)
		zipCode = "";
	
	String icdCode = request.getParameter("icdCode");
	
	//try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
	
	try{
		dsList = diagnoses.getEpidemicStatistics(startDate, icdCode, zipCode, threshold);
		
		if (dsList != null) {
			dsBean = dsList.get(0);
			avgBean = dsList.get(1);
		}
	} catch(FormValidationException e){
		e.printHTML(pageContext.getOut());
	}
	
	if (startDate == null)
		startDate = "";
	if (icdCode == null)
		icdCode = "";
	if (threshold == null)
		threshold = "";
	
%>
<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
<input type="hidden" name="viewSelect" value="epidemics" />
<table class="fTable" align="center" id="diagnosisStatisticsSelectionTable">
	<tr>
		<th colspan="4">Epidemic Evaluation</th>
	</tr>
	<tr class="subHeader">
		<td>Diagnosis:</td>
		<td>
			<select name="icdCode" style="font-size:10" onchange="hideThreshold(this.options[this.selectedIndex].value)">
				<option value="">-- None Selected --</option>
				<%if (icdCode.equals("84.50")) { %>
					<option selected="selected" value="84.50">84.50 - Malaria</option>
					<option value="487.00">487.00 - Influenza</option>
				<% } else if (icdCode.equals("487.00")) { %>
					<option value="84.50">84.50 - Malaria</option>
					<option selected="selected" value="487.00">487.00 - Influenza</option>
				<% } else { %>
					<option value="84.50">84.50 - Malaria</option>
					<option value="487.00">487.00 - Influenza</option>
				<% } %>
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
<% 
	String hide = "";
	if (icdCode.equals("487.00"))
		hide = "style='display:none;'";
%>
		<td><span id="thresh1" <%= hide %>>Threshold:</span></td>
		<td>
			<span id="thresh2" <%= hide %>><input name="threshold" value="<%= threshold %>" size="10"></span>
		</td>
	</tr>
	<tr>
		<td colspan="4" style="text-align: center;"><input type="submit" id="select_diagnosis" value="View Statistics"></td>
	</tr>
</table>	

</form>

<% if (dsBean != null && avgBean != null) { %>

<p style="display:block; margin-left:auto; margin-right:auto; width:600px;">
<%@include file="DiagnosisEpidemicsChart.jsp" %>
</p>

<%
	boolean isEp = false;
	if (icdCode.equals("84.50")) {
		isEp = diagnoses.isMalariaEpidemic(startDate, zipCode, threshold);
	} else if (icdCode.equals("487.00")) {
		isEp = diagnoses.isFluEpidemic(startDate, zipCode);
	}
	
%>

<% if ( isEp == true ) { %>

<h1><center><font color="#FF0000">THERE IS AN EPIDEMIC OCCURRING IN THIS REGION!</font></center></h1>

<% } else if ( isEp == false ) {%>

<h1><center>There is no epidemic occurring in the region.</center></h1>

<% }%>

<%} %>
<br />
<br />

<script type="text/javascript">
	function hideThreshold(selected) {
		if (selected == "487.00") {
			document.getElementById("thresh1").style.display = "none";
			document.getElementById("thresh2").style.display = "none";
		} else {
			document.getElementById("thresh1").style.display = "";
			document.getElementById("thresh2").style.display = "";
		}
	}
</script>