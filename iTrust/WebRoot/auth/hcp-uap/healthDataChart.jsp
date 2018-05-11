<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.action.EditHealthHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.charts.HealthData" %>

<%@include file="/global.jsp" %>

<%
	// Retrieve type of data from URL
String recType = (String)request.getParameter("dataType");

if (!recType.equals("Height") && !recType.equals("Weight") && !recType.equals("BMI")) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewBasicHealth.jsp");
	return;
}

pageTitle = "iTrust - " + recType + " Chart";
%>

<%@include file="/header.jsp" %>

<%
	String pidString = (String)session.getAttribute("pid");
if (null == pidString || "".equals(pidString)){ 
	 	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewBasicHealth.jsp");
	  	return;
}

// New instance of EditHealthHistoryAction class
EditHealthHistoryAction action = new EditHealthHistoryAction(prodDAO,loggedInMID.longValue(),pidString);
long pid = action.getPid();
String patientName = action.getPatientName();

// Retrieve list of HealthRecord beans to access personalhealthinformation table in database.
List<HealthRecord> hRecs = action.getAllHealthRecords(pid);
%>

<!-- Use this tag to specify the location of the dataset for the chart -->
<jsp:useBean id="hwInfo" class="edu.ncsu.csc.itrust.charts.HealthData"/>

<%
// This calls the class from the useBean tag and initializes the Adverse Event list and pres/immu name
hwInfo.initializeHealthRecords(hRecs, recType);
String chartTitle = recType + " chart for " + patientName;
loggingAction.logEvent(TransactionType.BASIC_HEALTH_CHARTS_VIEW, loggedInMID, pid, recType);

//if (hwInfo.hasData()) {
%>

<!-- The cewolf:chart tag defines attributes related to the chart you wish to generate -->
<cewolf:chart
     id="graph"
     title="<%= StringEscapeUtils.escapeHtml(chartTitle ) %>"
     type="line"
     xaxislabel="Quarter"
     yaxislabel="<%= StringEscapeUtils.escapeHtml(recType ) %>">
	<cewolf:data>
	       <cewolf:producer id="hwInfo"/>
	</cewolf:data>
</cewolf:chart>
<%
if (hwInfo.hasData()) {
%>
<!-- The cewolf:img tag defines the actual chart in your JSP -->
<cewolf:img chartid="graph" renderer="/charts/" width="1000" height="400"/>
<%
} else {
%>
	<div align=center>
		<span class="iTrustMessage">Patient has no data available from the past 3 years</span>
	</div>
<%
}
%>
<%@include file="/footer.jsp" %>
