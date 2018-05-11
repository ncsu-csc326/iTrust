<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.LabProcUAPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

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

	
	LabProcUAPAction action2 = new LabProcUAPAction(prodDAO, loggedInMID.longValue());
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
		loggingAction.logEvent(TransactionType.LAB_PROCEDURE_EDIT, loggedInMID.longValue(), bean.getPid() , "Procedure: " + lpid);
%>
	<br />
	<div align=center>
		<span class="iTrustMessage">Information Updated Successfully</span>
	</div>
	<br />
<%
	} catch(FormValidationException e){
%>
	<br />
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
	<br />
<%
		}
	}
	
%>

<%
if (request.getParameter("message") != null) {
	%><span class="iTrustMessage" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + (request.getParameter("message") )) %></span><%
}
%>
<br />
<div align=center>
<table class="fTable">
	<tr>
		<th colspan="11">Laboratory Procedures</th>
	</tr>

	<tr class="subHeader">

    		<th>PatientMID</th>
  			<th>Lab Code</th>
   			<th>Rights</th>
	 		<th>Status</th>
  			<th>Commentary</th>
   			<th>Results</th>
 			<th>OfficeVisitID</th>
   			<th>Updated Date</th>

  	</tr>
		<%LabProcedureBean bean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);
		loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW, loggedInMID.longValue(), bean.getPid() , "Procedure: " + lpid);
		%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getPid())) %></td>
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
<form action="updateLabProc.jsp?ID=<%= StringEscapeUtils.escapeHtml("" + (lpid)) %>"&message="Updated Laboratory Procedure" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<br />
	<%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %>
<br />
<table class="fTable">
	<tr>
		<th colspan=2>Update Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">Status:</td>
		<td>
		<select name="Status">
		<option value="<%= LabProcedureBean.In_Transit %>"><%= StringEscapeUtils.escapeHtml("" + (lbean.In_Transit )) %></option>
        <option value="<%= LabProcedureBean.Received %>"><%= StringEscapeUtils.escapeHtml("" + (lbean.Received )) %></option>
        <option value="<%= LabProcedureBean.Testing %>"><%= StringEscapeUtils.escapeHtml("" + (lbean.Testing )) %></option>
		<option value="<%= LabProcedureBean.Pending %>"><%= StringEscapeUtils.escapeHtml("" + (lbean.Pending )) %></option>
		<option value="<%= LabProcedureBean.Completed %>"><%= StringEscapeUtils.escapeHtml("" + (lbean.Completed )) %></option>
		</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Commentary:</td>
		<td><textarea name="Commentary"></textarea>
	</tr>
	<tr>
		<td class="subHeaderVertical">Results:</td>
		<td><textarea name="Results"></textarea></td>
	</tr>
</table>
<br />
<input type="submit" style="font-size: 14pt; font-weight: bold;" value="Update">
</div>
</form>
--%>
<br />

<%@include file="/footer.jsp" %>
