<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Laboratory Procedures";
%>

<%@include file="/header.jsp" %>

<%
/* Patient ID is optional.  If no patient id is given, list all lab proces.  If 
one is given, list lab proces for only that patient. */

String pidString = (String)session.getAttribute("pid");
String filterPatients = request.getParameter("filterPatients");

boolean showAllPatients = false;

List<LabProcedureBean> procedures = null;
LabProcHCPAction lpaction = new LabProcHCPAction(prodDAO, loggedInMID.longValue());

if (filterPatients != null && "all".equals(filterPatients.toLowerCase())) {
	showAllPatients = true;
	procedures = lpaction.viewProceduresByHCP();
} else if (pidString == null || pidString.length() == 0) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/LabProcHCP.jsp");
   	return;
} else {
	procedures = lpaction.viewPatientProcedures(Long.parseLong(pidString));
}

if (showAllPatients == false) {
	loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW, loggedInMID.longValue(), Long.parseLong(pidString), "Viewed laboratory procedures");
	
	if(request.getParameter("priv")!=null && request.getParameter("priv").equals("yes")){
		lpaction.changePrivacy(Long.parseLong(request.getParameter("ID")));
		loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW, loggedInMID.longValue(), Long.parseLong(pidString), "Viewed laboratory procedures");
	}
}

/* If the patient id doesn't check out, then kick 'em out to the exception handler */
//EditPatientAction action = new EditPatientAction(prodDAO, loggedInMID.longValue(), pidString);
//long pid = action.getPid();

//List<LabProcedureBean> proc = action2.viewProcedures(pid);
%>

<br />
<table  class="fTable">
	<tr>
		<th colspan="11">Lab Procedures</th>
	</tr>

	<tr class="subHeader">
  		<td>Patient</td>
  		<td>Lab Code</td>
  		<td>Rights</td>
		<td>Status</td>
		<td>Commentary</td>
		<td>Results</td>
		<td>OfficeVisitID</td>
		<td>Updated Date</td>
		<td>Edit Office Visit</td>
		<td>Change Privacy</td>
		<td>Action</td>
  	</tr>
<%
	if(procedures.size() > 0) {
		for(LabProcedureBean bean : procedures){ 
			PatientBean patient = new PatientDAO(prodDAO).getPatient(bean.getPid());
%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getLoinc())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getRights())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getStatus())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getCommentary())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getResults())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getOvID())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getTimestamp())) %></td>
				<td >  <%if(lpaction.checkAccess(bean.getProcedureID())){%>
					<a href="/iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (bean.getOvID())) %>">Edit Office Visit</a><br />
				<%} %></td>
				<td >  <%if(lpaction.checkAccess(bean.getProcedureID())){%>
					<a href="/iTrust/auth/hcp/LabProcHCP.jsp?ID=<%= StringEscapeUtils.escapeHtml("" + (bean.getProcedureID())) %>&priv=yes">Allow/Disallow Viewing</a><br />
				<%} %></td>
				<td > 
					<a href="/iTrust/auth/hcp/UpdateLabProc.jsp?ID=<%= StringEscapeUtils.escapeHtml("" + (bean.getProcedureID())) %>">Update</a><br />
				</td>
				
			</tr>
<%
		}
	}
	else {
%>
	<tr>
		<td align=center colspan="11">
		    <%-- Note: The 'userName' comes from global.jsp. --%>
			No lab procedures found with <%= StringEscapeUtils.escapeHtml(userName) %>.
		</td>
	</tr>
<%		
	}
%>
</table>
<br /><br />

<%@include file="/footer.jsp" %>
