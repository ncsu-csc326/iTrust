<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewExpiredPrescriptionsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Get My Expired Prescription Reports";
%>

<%@include file="/header.jsp"%>

<%
loggingAction.logEvent(TransactionType.EXPIRED_PRESCRIPTION_VIEW, loggedInMID.longValue(), loggedInMID.longValue(), "");

PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue()); 

ViewExpiredPrescriptionsAction expAction = new ViewExpiredPrescriptionsAction(prodDAO, loggedInMID.longValue());

List<PersonnelBean> personnelList = prodDAO.getPersonnelDAO().getAllPersonnel();

session.setAttribute("personnelList", personnelList);

%>
<div align="center">
	<br />
	<table class="fTable">
	
	
	
	
	
	<%
		List<PrescriptionBean> prescriptions = expAction.getPrescriptionsForPatient(loggedInMID.longValue());
		if (prescriptions.size() == 0) { 
%>
		<tr>
			<td colspan=4>
				<i>No prescriptions found</i>
			</td>
		</tr>
<%
		} else { 
%>
		<tr>
			<th colspan=4><%= StringEscapeUtils.escapeHtml("" + ( patient.getFullName() )) %></th>
		</tr>
		<tr class="subHeader">
			<td>ND Code</td>
			<td>Description</td>
			<td>Duration</td>
			<td>Prescribing HCP</td>
		</tr>
<%	
	for (PrescriptionBean prescription : prescriptions) { 
		PersonnelBean doctor = expAction.getPrescribingDoctor(prescription);
%>
		<tr>
			<td ><a href="viewPrescriptionInformation.jsp?visitID=<%= StringEscapeUtils.escapeHtml("" + (prescription.getVisitID())) %>&presID=<%= StringEscapeUtils.escapeHtml("" + (prescription.getId())) %>"><%= StringEscapeUtils.escapeHtml("" + (prescription.getMedication().getNDCodeFormatted() )) %></a></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (prescription.getMedication().getDescription() )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (prescription.getStartDateStr() )) %> to <%= StringEscapeUtils.escapeHtml("" + (prescription.getEndDateStr() )) %></td>
			<td ><a href=viewLHCP.jsp?index=<%= StringEscapeUtils.escapeHtml("" + (doctor.getIndexIn(personnelList) )) %> ><%= StringEscapeUtils.escapeHtml("" + (doctor.getFullName() )) %></a></td>
			
			
			
		</tr>
<%			
			}
		}
	
	
%>
	</table>	
	<br />
</div>

<%@include file="/footer.jsp"%>
