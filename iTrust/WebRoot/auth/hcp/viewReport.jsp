<%@page import="edu.ncsu.csc.itrust.report.PersonnelReportFilter.PersonnelReportFilterType"%>
<%@page import="edu.ncsu.csc.itrust.report.DemographicReportFilter.DemographicReportFilterType"%>
<%@page import="edu.ncsu.csc.itrust.report.MedicalReportFilter.MedicalReportFilterType"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.GroupReportBean"%>
<%@page import="edu.ncsu.csc.itrust.report.ReportFilter"%>
<%@page import="edu.ncsu.csc.itrust.action.GroupReportAction"%>
<%@page import="edu.ncsu.csc.itrust.action.GroupReportGeneratorAction"%>
<%@page import="edu.ncsu.csc.itrust.report.MedicalReportFilter"%>
<%@page import="edu.ncsu.csc.itrust.report.DemographicReportFilter"%>
<%@page import="edu.ncsu.csc.itrust.report.PersonnelReportFilter"%>
<%@page import="java.util.regex.Pattern" %>
<%@page import="java.io.ByteArrayInputStream" %>
<%@page import="java.io.InputStream" %>
<%@page import="java.io.IOException" %>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@include file="/global.jsp"%>

<%
pageTitle = "iTrust - View Group Report";
%>

<%@include file="/header.jsp"%>

<style type="text/css">
.content{
	height:40px;
	overflow-y:auto;
}
</style>
<h1>Group Report</h1>
<%
DAOFactory factory = DAOFactory.getProductionInstance();
List<ReportFilter> filters = new ArrayList<ReportFilter>();
GroupReportGeneratorAction gpga = null;
boolean validationError = false;
boolean noParameters = false;

if (request.getParameter("generate") != null) {
	gpga = new GroupReportGeneratorAction(factory, request);
} else {
	noParameters=true;
}
loggingAction.logEvent(TransactionType.GROUP_REPORT_VIEW, loggedInMID, 0L, "");

try{
	gpga.generateReport();
}catch(NumberFormatException e){
	validationError = true;
}

if(!noParameters){
	%>
	<p>Using filters:<br />
	<%
	for (int i=0 ; i < gpga.getReportFilterTypes().size();i++) {
		out.println("Filter by " + gpga.getReportFilterTypes().get(i) + " with value " + gpga.getReportFilterValues().get(i) + "<br />");
	}
	%>
	</p>
	<p>Patients matching these filters:</p>
	<%
	
	if(!validationError){
		%>
		<table border="1" cellpadding="5" cellspacing="0" class="fTable" id="report"><tr>
		<%
		for(int i = 0 ; i < gpga.getReportHeaders().size();i++) {
			out.print("<th>" + gpga.getReportHeaders().get(i) + "</th>");
		}

		for(int i = 0 ; i < gpga.getReportData().size(); i++) {
			out.print("<tr>");
			for(int j = 0 ; j < gpga.getReportData().get(i).size();j++) {
				out.print("<td><div class=\"content\">" + gpga.getReportData().get(i).get(j).replace("\n", "<br />") + "</div></td>");
			}
			out.print("</tr>");
		}
		
		%>	
		</table>
		<%
	} else {
		session.setAttribute("validationError",true);
		response.sendRedirect("groupReport.jsp");
	}
} else {
	%>
	<p>You have not selected any filters. Please <a href="groupReport.jsp">generate
	a group report</a> first.</p>
	<%
}
%>
<%@include file="/footer.jsp"%>
<%
%>