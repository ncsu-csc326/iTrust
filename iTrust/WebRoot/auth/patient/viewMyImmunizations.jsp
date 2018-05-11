<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewImmunizationReportAction" %>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction" %>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.RequiredProceduresBean" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Immunization Records";
%>


<%@include file="/header.jsp" %>
<style type="text/css" media="print">
       #iTrustHeader2, #iTrustMenu, #iTrustFooter {
               display:none !important;
       }
       #container2 {
       
               background-image:none !important;
       }
       body {
			font-family: verdana, arial, sans-serif ;
			font-color: #0000;
			font-size: 12px ;
	   }
 
	   th,td {
	   		font-color: #0000;
			padding: 4px 4px 4px 4px ;
	   }
 
		th {
			font-color: #0000;
			border-bottom: 2px solid #333333 ;
		}
 
		td {
			border-bottom: 1px dotted #999999 ;
			font-color: #0000;
		}
 
		tfoot td {
			border-bottom-width: 0px ;
			font-color: #0000;
			border-top: 2px solid #333333 ;
			padding-top: 20px ;
		}

</style>
<%

String pidString = "" + loggedInMID;

final int KINDERGARTEN_AGE = 7;
final int SIXTH_GRADE_AGE = 16;
final int KINDERGARTEN = 0;
final int SIXTH_GRADE = 1;
final int COLLEGE = 2;
int ageGroup = KINDERGARTEN;

/* Get a list of all procedures obtained by patient */
long pid = Long.parseLong(pidString);
ViewImmunizationReportAction immAction = new ViewImmunizationReportAction(prodDAO, pid, loggedInMID);
List<ProcedureBean> procedures = immAction.getAllImmunizations(pid);

/* Get a list of all needed immunizations for the patient */
EditPatientAction patientAction = new EditPatientAction(prodDAO, loggedInMID, pidString);
PatientBean patient = patientAction.getPatient();
int patientAge = patient.getAge();
if(patientAge < KINDERGARTEN_AGE) {
	ageGroup = KINDERGARTEN;
} else if(patientAge < SIXTH_GRADE_AGE) {
	ageGroup = SIXTH_GRADE;
} else {
	ageGroup = COLLEGE;
}
List<RequiredProceduresBean> needed = immAction.getNeededImmunizations(pid, ageGroup);
loggingAction.logEvent(TransactionType.IMMUNIZATION_REPORT_PATIENT_VIEW, loggedInMID.longValue(), 0, "");
%>

<div id="content" align="center">
<br>

<table class="fTable" id="immunizationReport">
<tr>
    <th colspan="9">Immunizations Received</th>
</tr>
<tr class = "subHeader" colspan="10">
	<th>CPT Code</th>
	<th>Description</th>
	<th>Date Received</th>
	<th>HCP</th>
</tr>
<% if(procedures.size() == 0) { %>
<tr><td colspan="9">No immunizations received.</td></tr>
<% } else { for(ProcedureBean proc : procedures) { %>
<tr>
	<% //Get the HCP name from the ID.
    String HcpName = immAction.getHcpNameFromID(proc.getHcpid());
	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
   	%>
   	<td><%= proc.getCPTCode() %></td>
   	<td><%= proc.getDescription() %></td>
   	<td><%= df.format(proc.getDate()) %></td>
   	<td><%= HcpName %></td>
</tr>
<% } } %>

<tr>
	<th colspan = "9">Immunizations Needed</th>
</tr>
<tr class = "subHeader" colspan="10">
	<th colspan="2">CPT Code</th>
	<th colspan="2">Description</th>
</tr>
<% if(needed.size() == 0) { %>
<tr><td colspan="9">No further immunizations needed</td></tr>
<% } else { for(RequiredProceduresBean bean : needed) { %>
<tr>
	<td colspan="2"><%= bean.getCptCode() %></td>
	<td colspan="2"><%= bean.getDescription() %></td>
</tr>
<% } } %>
</table>
</div>
<%@include file="/footer.jsp"%>