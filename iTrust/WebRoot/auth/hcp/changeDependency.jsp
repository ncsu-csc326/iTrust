<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Change Dependency";
%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Change Dependency" />
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/changeDependency.jsp");
		return;
	}
	
	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	EditPatientAction action = new EditPatientAction(prodDAO, loggedInMID.longValue(), pidString);

	//Get the patient's dependency status
	boolean isDependent = action.isDependent();
	/* Now take care of updating information */
	
	PatientBean p;
	if (request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true")) {
		try {	
			action.setDependent(!isDependent);
			session.removeAttribute("pid");
	
%>
	<br />
	<div align=center>
		<span class="iTrustMessage">Patient Dependency Status Successfully Changed</span>
	</div>
	<br />
<%
		} catch (Exception e) {
%>
	<br />
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
	<br />
<%
		}
	} else {
		p = action.getPatient();
%>

	<form id="dependencyForm" action="changeDependency.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"><br />
	<table cellspacing=0 align=center cellpadding=0>
		<tr>
			<td valign=top>
			<table class="fTable" align=center style="width: 350px;">
				<tr>
					<th colspan="4">Change Patient Dependency Status</th>
				</tr>		
				<tr>
			
					<td class="subHeaderVertical">First Name:</td>
					<td><%= StringEscapeUtils.escapeHtml("" + (p.getFirstName())) %></td>
					<td class="subHeaderVertical">Last Name:</td>
					<td><%= StringEscapeUtils.escapeHtml("" + (p.getLastName())) %></td>
				</tr>
				<tr>
		<%if (isDependent) { %>
					<td colspan="4">
					This patient is a dependent. Deactivating this patient's dependency status will allow him/her to log in as a full patient. 
					</td>
		<%} else { %>
					<td colspan="4">
					This patient has full patient status. Making this patient a dependent will prevent him/her from logging in as a full patient. 
					</td>
		<%} %>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<br />
	<div align=center>
		<%if (isDependent) {%>
		<input type="submit" name="action"
			style="font-size: 16pt; font-weight: bold;" value="Deactivate Dependency"><br /><br />
		<%} else { %>
		<input type="submit" name="action"
			style="font-size: 16pt; font-weight: bold;" value="Make Dependent"><br /><br />
		<%} %>
	</div>
	</form>
<br />
	<%} %>
<br />
<itrust:patientNav thisTitle="Change Dependency" />

<%@include file="/footer.jsp"%>
