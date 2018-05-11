<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.charts.PatientLabResults" %>

<%@include file="/global.jsp" %>

<%
	// Retrieve type of data from URL
pageTitle = "Lab Procedure Results Chart";
%>

<%@include file="/header.jsp" %>

<%

String recType = (String)request.getParameter("dataType");

	//String pidString = (String)session.getAttribute("pid");
//if (null == pidString || "".equals(pidString)){ 
//	 	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=patient/home.jsp");
//	  	return;
//}

ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, loggedInMID.longValue());

long pid = action.getPatient().getMID();
String loincID = recType;

// Retrieve list of HealthRecord beans to access personalhealthinformation table in database.
List<LabProcedureBean> lpBeans = action.getSpecificLabs(pid, loincID);
%>

<!-- Use this tag to specify the location of the dataset for the chart -->
<jsp:useBean id="hwInfo" class="edu.ncsu.csc.itrust.charts.PatientLabResults"/>

<%

hwInfo.initializeLabProcedures(lpBeans, loincID);
String chartTitle = "Lab Procedures";

%>

<!-- The cewolf:chart tag defines attributes related to the chart you wish to generate -->
<cewolf:chart
     id="graph"
     title="<%= StringEscapeUtils.escapeHtml(chartTitle) %>"
     type="line"
     xaxislabel="Quarter"
     yaxislabel="<%= StringEscapeUtils.escapeHtml(recType) %>">
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
		<span class="iTrustMessage">Patient has no lab procedure data available from the past 3 years</span>
	</div>
<%
}
%>
<%@include file="/footer.jsp" %>
