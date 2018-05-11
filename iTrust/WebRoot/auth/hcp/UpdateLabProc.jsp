<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Update Lab Procedure";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "";

LabProcedureBean lbean = null;
long requestID = 0L;
String lpid = request.getParameter("ID");

if (lpid != null && !lpid.equals("")) {
	try {
		
		requestID = Long.parseLong(lpid);
		lbean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);
	} catch (Exception e) {
		
	}
}

	
	LabProcHCPAction action2 = new LabProcHCPAction(prodDAO, loggedInMID.longValue());
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");


	if (formIsFilled) {
		//This page is not actually a "page", it just adds a user and forwards.
		lbean.setStatus(request.getParameter("Status"));
		lbean.setResults(request.getParameter("Results"));
		lbean.setCommentary(request.getParameter("Commentary"));

		try{
		action2.updateProcedure(lbean);
		LabProcedureBean bean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);
		loggingAction.logEvent(TransactionType.LAB_PROCEDURE_EDIT, loggedInMID.longValue(), bean.getPid(), "HCP updated procedure id: "+lpid);
		%>
		<span>Information Updated Successfully</span>
		<% } catch(FormValidationException e){
			e.printHTML(out);
		}
	} else {
		loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW, loggedInMID.longValue(), requestID, "HCP viewed procedure id: "+lpid);
	}
	
%>


<table  class="fTable">
	<tr>
		<th colspan="11">Lab Procedures</th>
	</tr>

	<tr>

    		<th>Patient</th>
  			<th>Lab Code</th>
   			<th>Rights</th>
	 		<th>Status</th>
  			<th>Commentary</th>
   			<th>Results</th>
 			<th>OfficeVisitID</th>
   			<th>Updated Date</th>

  	</tr>
		<%LabProcedureBean bean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);
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
				
			</tr>
</table>

<%-- 
<form action="UpdateLabProc.jsp?ID=<%= StringEscapeUtils.escapeHtml("" + (lpid)) %>"&message="Updated Laboratory Procedure" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<br />
<table align="center">
	<tr><td><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></td></tr>
</table>
<br />
<table>

	<tr>
		<td>Status:</td>
		<td>
		<select name="Status">
		<option value="In Transit"><%= StringEscapeUtils.escapeHtml("" + (lbean.In_Transit )) %></option>
		<option value="Received"><%= StringEscapeUtils.escapeHtml("" + (lbean.Received )) %></option>
		<option value="Testing"><%= StringEscapeUtils.escapeHtml("" + (lbean.Testing )) %></option>
		<option value="Pending"><%= StringEscapeUtils.escapeHtml("" + (lbean.Pending )) %></option>
		<option value="Completed"><%= StringEscapeUtils.escapeHtml("" + (lbean.Completed )) %></option>
		</select>
		</td>
	</tr>
	<tr>
		<td>Commentary:</td>
		<td><textarea name="Commentary"></textarea>
	</tr>
	<tr>
		<td>Results:</td>
		<td><textarea name="Results"></textarea></td>
	</tr>
	<tr>
		<td colspan=2 align=center><input type="submit"
			style="font-size: 16pt; font-weight: bold;" value="Update"></td>
	</tr>
</table>
</form>
 --%>
<br />

<a href="../hcp/LabProcHCP.jsp">Go to View Laboratory Procedures</a>

<%@include file="/footer.jsp" %>
