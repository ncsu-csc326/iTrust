<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcUAPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>


<%

long pidTransit;
%>
<br />

<form action="allProcedures.jsp" method="post" id="inTransitForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="transitForm" />


<table class="fTable" align=center id="inTransitTable">
	<tr>
		<th colspan="11">Laboratory Procedures In Transit</th>
	</tr>
<%//lab procedure ID, lab procedure code, status, priority, HCP name, and timestamp %>
	<tr class="subHeader">

			<th>Lab Procedure ID</th>
  			<th>Lab Code</th>
	 		<th>Status</th>
	 		<th>Priority</th>
  			<th>HCP name</th>
   			<th>Updated Date</th>
   			<th>Action</th>

  	</tr>
  	<%if(lpInTransit != null && lpInTransit.size() > 0 ){ %>
		<%for(LabProcedureBean bean : lpInTransit){ 
		
		PatientBean patient = new PatientDAO(prodDAO).getPatient(bean.getPid());%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getProcedureID())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getLoinc())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getStatus())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getPriorityCode())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (action.getHCPName(bean.getOvID()))) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getTimestamp())) %></td>
				<td >
					<button type="submit" name="inTransitSubmitBtn" value="<%=bean.getProcedureID() %>">Set to Received</button>
				</td>
			</tr>
		<%} 
	} else {%>
	<tr>
		<td colspan=8 style="text-align: center;">There are no lab procedures in transit!</td>
	</tr>
	<%} %>
</table>
</form>
<br /><br />

