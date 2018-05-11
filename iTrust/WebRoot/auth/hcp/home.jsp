<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>

<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - HCP Home";
%>

<%@include file="/header.jsp" %>

<%
ViewMyReportRequestsAction reportAction = new ViewMyReportRequestsAction(DAOFactory.getProductionInstance(), loggedInMID);
List<ReportRequestBean> reports = reportAction.getAllReportRequestsForRequester();
LabProcHCPAction lpAction = new LabProcHCPAction(DAOFactory.getProductionInstance(), loggedInMID);
List<LabProcedureBean> labProcedures = lpAction.getLabProcForNextMonth();

loggingAction.logEvent(TransactionType.HOME_VIEW, loggedInMID.longValue(), 0, "");

%>
<%@include file="/auth/hcp/notificationArea.jsp" %>

<div class="col-sm-12">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Comprehensive Report History</h3></div>
	<div class="panel-body">
	<ul>
<%
	if(reports.size() != 0) {
		for(ReportRequestBean report : reports) {
%>
			<li><%= StringEscapeUtils.escapeHtml("" + (reportAction.getLongStatus(report.getID()))) %></li>
<%
		} 
	} else {
%>
		<i>No Report Requests</i>
<%
	}
%>
		</ul>
</div>
</div>

	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Lab Procedures Completed in the Last Month</h3></div>
	<div class="panel-body">
		<a href="LabProcHCP.jsp">View All Lab Procedures here</a><br /><br />
		<ul>
			<%if(labProcedures.size() != 0) {
				for(LabProcedureBean bean : labProcedures) {
					PatientBean patient = new PatientDAO(DAOFactory.getProductionInstance()).getPatient(bean.getPid());%>
			<li>Lab Procedure <%= StringEscapeUtils.escapeHtml("" + (bean.getLoinc())) %> for patient <%= StringEscapeUtils.escapeHtml("" + (patient.getFullName() )) %>
			<br />
			Results: 
<%
					if(bean.getResults().equals("")) {
%> 
						<i>none</i> 
<%
					} else {
%>
						<%= StringEscapeUtils.escapeHtml("" + (bean.getResults())) %>
<%
					}
				}
			} else {
%>
				<i>No Recent Lab Procedures</i>
<%
			}
%>
			</li>
		</ul>
</div>
</div>
</div>

<%@include file="/footer.jsp" %>
