<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcUAPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>


<%
long pidTest;

String numResult = "";
String numResultUnit = "";
String upperNum = "";
String lowerNum = "";

%>



<form action="allProcedures.jsp" method="post" id="testingForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="testingForm" />

<br />
<table class="fTable" align=center>
	<tr>
		<th colspan="11">Testing Laboratory Procedure</th>
	</tr>
<%//lab procedure ID, lab procedure code, status, priority, HCP name, and timestamp %>
	<tr class="subHeader">

			<th>Lab Procedure ID</th>
  			<th>Lab Code</th>
	 		<th>Status</th>
	 		<th>Priority</th>
  			<th>HCP name</th>
   			<th>Updated Date</th>
   			<th>Numerical Result</th>
   			<th>Numerical Result Unit</th>
   			<th>Confidence Level</th>
   			<th>Action</th>

  	</tr>
  	<%if(request.getParameter("numericalResult") != null){
  		numResult = request.getParameter("numericalResult");
  	}
  	
  	if(request.getParameter("numericalResultUnit") != null) {
  		numResultUnit = request.getParameter("numericalResultUnit");
  	}
  	
  	if(request.getParameter("upperBound") != null){
  		upperNum = request.getParameter("upperBound");
  	}
  	
  	if(request.getParameter("lowerBound") != null){
  		lowerNum = request.getParameter("lowerBound");
  	}
  	
  	
  	%>
  	<%if(lpTesting != null && lpTesting.size() > 0 ){ %>
		<%for(LabProcedureBean bean : lpTesting){ 
		PatientBean patient = new PatientDAO(prodDAO).getPatient(bean.getPid());%>
			<tr>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getProcedureID())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getLoinc())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getStatus())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getPriorityCode())) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (action.getHCPName(bean.getOvID()))) %></td>
				<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getTimestamp())) %></td>
				<td ><input type="text" name="numericalResult" value="<%=numResult %>"/></td>
				<td ><input type="text" name="numericalResultUnit" value="<%=numResultUnit %>"/></td>
				<td >
					Upper Bound:<input type="text" name="upperBound" value="<%=upperNum %>"/><br />
					Lower Bound:<input type="text" name="lowerBound" value="<%=lowerNum %>"/>
				</td>
				<input style="display:none;" name="pidTest" value="<%=bean.getProcedureID() %>" />
				<td >
					<button type="submit" name="testingSubmitBtn">Set to Pending</button>
				</td>
				
			</tr>
		<%} 
	} else {%>
	<tr>
		<td colspan=9 style="text-align: center;">There are no testing lab procedures!</td>
	</tr>
	<%} %>
</table>
</form>
<br /><br />

