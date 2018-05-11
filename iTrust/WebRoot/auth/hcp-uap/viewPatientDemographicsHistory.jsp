<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientHistoryBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="java.util.List" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Demographics History";
%>

<%@include file="/header.jsp"%>

<itrust:patientNav thisTitle="Demographics" />
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");

	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	EditPatientAction action = new EditPatientAction(prodDAO,
		loggedInMID.longValue(), pidString);
	
	List<PatientHistoryBean> pList = action.getHistory();
%>

<br />

<form action="editPatient.jsp" method="post">
	Return to Demographics: <input type="submit" value="Back" />
</form>

<br />

<%
	if (pList != null && pList.size() > 0) {
		for (PatientHistoryBean p : pList) {
%>
			<table class="fTable">
				<tr>
					<th>Changed By: <%= StringEscapeUtils.escapeHtml("" + (action.getEmployeeName(p.getChangeMID()))) %></th>
					<th>On: <%= StringEscapeUtils.escapeHtml("" + (p.getChangeDateStr())) %></th>
					<th> TO: </th>
				</tr>
				<tr>
				<table class="fTable" align=center style="text-align:center;">
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
						<th>Address</th>
						<th>Phone</th>
						<th>Directions to Home</th>
						<th>Religion</th>
						<th>Language</th>
						<th>Spiritual Practices</th>
						<th>Alternate Name</th>
					</tr>		
					<tr>	
						<td> <%= StringEscapeUtils.escapeHtml("" + (p.getFirstName())) %> </td>				
						<td> <%= StringEscapeUtils.escapeHtml("" + (p.getLastName())) %> </td>
						<td> <%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %> </td>
						<td style="white-space:nowrap"> <%= StringEscapeUtils.escapeHtml("" + (p.getStreetAddress1())) %><br />
								<% if (!p.getStreetAddress2().equals(""))  { %>
									<%= StringEscapeUtils.escapeHtml("" + (p.getStreetAddress2())) %><br />
								<% } %>
								<%= StringEscapeUtils.escapeHtml("" + (p.getCity())) %>, <%= StringEscapeUtils.escapeHtml(p.getState()) %> 
								<%= StringEscapeUtils.escapeHtml("" + (p.getZip())) %>
						</td>
						<td> <%= StringEscapeUtils.escapeHtml("" + (p.getPhone())) %> </td>
						<td> <%= StringEscapeUtils.escapeHtml("" + (p.getDirectionsToHome())) %> </td>
						<td > <%= StringEscapeUtils.escapeHtml("" + (p.getReligion())) %> </td>
						<td > <%= StringEscapeUtils.escapeHtml("" + (p.getLanguage())) %> </td>
						<td > <%= StringEscapeUtils.escapeHtml("" + (p.getSpiritualPractices())) %> </td>
						<td > <%= StringEscapeUtils.escapeHtml("" + (p.getAlternateName())) %> </td>
					</tr>
				</table>
				</tr></table>	
			<br />
<% 		}
	} else { %>
	<h1>NO HISTORY FOUND</h1>
<% } %>

<br />

<%@include file="/footer.jsp"%>
