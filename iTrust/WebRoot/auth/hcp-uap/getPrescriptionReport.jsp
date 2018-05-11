<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPrescriptionRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Get Prescription Report";
%>

<%@include file="/header.jsp" %>
<itrust:patientNav thisTitle="Prescriptions"/>
<div align=center>
<h1>Prescription Report</h1>
<%
	String pidString = (String)session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/getPrescriptionReport.jsp");
  		return;
	}
	
	long pid = Long.parseLong(pidString);
	ViewPrescriptionRecordsAction action = new ViewPrescriptionRecordsAction(DAOFactory.getProductionInstance(), loggedInMID);
	PatientBean patient = action.getPatient(pid);
	List<PrescriptionBean> prescriptions = action.getPrescriptionsForPatient(pid);
	loggingAction.logEvent(TransactionType.PRESCRIPTION_REPORT_VIEW, loggedInMID, pid, "");

	if (prescriptions.size() == 0) { %>
	<i>No prescriptions found</i><br />
	<br />
	<br />
<%		} else { %>
	<table class="fTable">
		<tr>
			<th>ND Code</th>
			<th>Description</th>
			<th>Duration</th>
			<th>Prescribing HCP</th>
		</tr>
<%			for (PrescriptionBean prescription : prescriptions) { %>
		<tr>
			<td ><%= StringEscapeUtils.escapeHtml("" + (prescription.getMedication().getNDCodeFormatted() )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (prescription.getMedication().getDescription() )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (prescription.getStartDateStr() )) %> to <%= StringEscapeUtils.escapeHtml("" + (prescription.getEndDateStr() )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + ( action.getPrescribingDoctor(prescription).getFullName() )) %></td>
		</tr>
<%			} %>
	</table>
<%		} %>
</div>
<br />
<br />
<%@include file="/footer.jsp" %>
