<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View All Patients";
%>

<%@include file="/header.jsp" %>

<%
ViewPatientOfficeVisitHistoryAction action = new ViewPatientOfficeVisitHistoryAction(prodDAO, loggedInMID.longValue());
List<PatientVisitBean> patientVisits = action.getPatients();
loggingAction.logEvent(TransactionType.PATIENT_LIST_VIEW, loggedInMID, 0, "");

%>
			<script src="/iTrust/DataTables/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
			<script type="text/javascript">
				jQuery.fn.dataTableExt.oSort['lname-asc']  = function(x,y) {
					var a = x.split(" ");
					var b = y.split(" ");
					return ((a[1] < b[1]) ? -1 : ((a[1] > b[1]) ?  1 : 0));
				};
				
				jQuery.fn.dataTableExt.oSort['lname-desc']  = function(x,y) {
					var a = x.split(" ");
					var b = y.split(" ");
					return ((a[1] < b[1]) ? 1 : ((a[1] > b[1]) ?  -1 : 0));
				};
			</script>
			<script type="text/javascript">	
   				$(document).ready(function() {
       				$("#patientList").dataTable( {
       					"aaColumns": [ [2,'dsc'] ],
       					"aoColumns": [ { "sType": "lname" }, null, null],
       					"bStateSave": true,
       					"sPaginationType": "full_numbers"
       				});
   				});
			</script>
			<style type="text/css" title="currentStyle">
				@import "/iTrust/DataTables/media/css/demo_table.css";		
			</style>

<br />
	<h2>Past Patients</h2>
<form action="viewReport.jsp" method="post" name="myform">
<table class="display fTable" id="patientList" align="center">
	
	<thead>


	<tr class="">
		<th>Patient</th>
		<th>Address</th>
		<th>Last Visit</th>

	</tr>
	</thead>
	<tbody>
	<%
		List<PatientBean> patients = new ArrayList<PatientBean>();
		int index = 0;
		for (PatientVisitBean bean : patientVisits) {
			patients.add(bean.getPatient());
	%>
	<tr>
		<td >
			<a href="editPHR.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">
		
		
			<%= StringEscapeUtils.escapeHtml("" + (bean.getPatientName())) %>	
		
		
			</a>
			</td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getAddress1() +" " +bean.getAddress2())) %></td>
		<td >
			<%
				if(bean.getLastOVDate() != null) {
			%>
				<%= StringEscapeUtils.escapeHtml("" + (bean.getLastOVDateM() +"/" +bean.getLastOVDateD() +"/" +bean.getLastOVDateY())) %>
			<%
				}
			%>
		</td>
	</tr>
	<%
			index ++;
		}
		session.setAttribute("patients", patients);
	%>
	</tbody>
</table>
</form>
<br />
<br />

<%@include file="/footer.jsp" %>
